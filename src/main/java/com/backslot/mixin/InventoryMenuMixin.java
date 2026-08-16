package com.backslot.mixin;

import com.backslot.BackItemSlot;
import com.backslot.BackSlot;
import com.backslot.BackSlotHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu {
	private InventoryMenuMixin() {
		super(null, 0);
	}

	// Appended after the shield, so we land on index 46. quickMoveStack keys off indices
	// below that and needs no changes — shift-clicking out of 46 falls through to its
	// "move into the inventory" branch.
	//
	// No empty-slot icon: that would mean shipping a texture, and without Fabric API a mod
	// can't get its assets in front of the resource manager. The frame comes from a vanilla
	// sprite drawn by InventoryScreenMixin instead.
	@Inject(method = "<init>", at = @At("TAIL"))
	private void backslot$addSlot(Inventory inventory, boolean active, Player owner, CallbackInfo ci) {
		addSlot(new BackItemSlot(((BackSlotHolder) owner).backslot$container(), owner, BackSlot.SLOT_X, BackSlot.SLOT_Y));
	}
}
