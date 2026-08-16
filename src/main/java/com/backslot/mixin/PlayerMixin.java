package com.backslot.mixin;

import com.backslot.BackContainer;
import com.backslot.BackSlotHolder;
import com.backslot.BackSlotSync;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements BackSlotHolder {
	// Synched rather than a plain field: the item gets drawn on other people's backs, so
	// every client tracking this player needs it, not just the owner.
	@Unique
	private static final EntityDataAccessor<ItemStack> BACK_ITEM =
			SynchedEntityData.defineId(Player.class, EntityDataSerializers.ITEM_STACK);

	@Unique
	private static final String SAVE_KEY = "backslot:item";

	@Unique
	private Container backslot$container;

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void backslot$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(BACK_ITEM, ItemStack.EMPTY);
		// The id is only known once defineId has run, and the client needs it to spot the
		// server's join announcement. This fires well before any entity data arrives.
		BackSlotSync.rememberDataId(BACK_ITEM.id());
	}

	@Override
	public ItemStack backslot$getItem() {
		return backslot$self().getEntityData().get(BACK_ITEM);
	}

	@Override
	public void backslot$setItem(ItemStack stack) {
		// Forced. ItemStack compares by identity, so editing the stack in place and setting
		// it back reads as unchanged and never reaches a client.
		backslot$self().getEntityData().set(BACK_ITEM, stack.copy(), true);
	}

	@Override
	public Container backslot$container() {
		if (backslot$container == null) {
			backslot$container = new BackContainer(backslot$self());
		}
		return backslot$container;
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void backslot$save(ValueOutput output, CallbackInfo ci) {
		ItemStack stack = backslot$getItem();
		if (!stack.isEmpty()) {
			output.store(SAVE_KEY, ItemStack.CODEC, stack);
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void backslot$load(ValueInput input, CallbackInfo ci) {
		backslot$setItem(input.read(SAVE_KEY, ItemStack.CODEC).orElse(ItemStack.EMPTY));
	}

	@Inject(method = "dropEquipment", at = @At("TAIL"))
	private void backslot$dropOnDeath(ServerLevel level, CallbackInfo ci) {
		// dropEquipment is called either way and does its own keepInventory check partway
		// through, so tailing it means repeating the check here.
		if (level.getGameRules().get(GameRules.KEEP_INVENTORY)) {
			return;
		}

		ItemStack stack = backslot$getItem();
		if (stack.isEmpty()) {
			return;
		}

		if (!EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
			backslot$self().drop(stack, true, false);
		}
		backslot$setItem(ItemStack.EMPTY);
	}

	@Unique
	private Player backslot$self() {
		return (Player) (Object) this;
	}
}
