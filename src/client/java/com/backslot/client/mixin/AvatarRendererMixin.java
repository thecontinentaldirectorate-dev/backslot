package com.backslot.client.mixin;

import com.backslot.BackSlotHolder;
import com.backslot.client.BackItemLayer;
import com.backslot.client.BackSlotRenderState;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin extends LivingEntityRenderer<Avatar, AvatarRenderState, PlayerModel> {
	private AvatarRendererMixin() {
		super(null, null, 0.0f);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void backslot$addLayer(EntityRendererProvider.Context context, boolean slim, CallbackInfo ci) {
		addLayer(new BackItemLayer(this));
	}

	// Mannequins share this renderer and have no back slot, so they leave the render state
	// empty and the layer skips them.
	@Inject(
			method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
			at = @At("TAIL"))
	private void backslot$extractBackItem(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		ItemStackRenderState item = ((BackSlotRenderState) state).backslot$item();
		item.clear();

		if (!(entity instanceof Player player)) {
			return;
		}

		ItemStack stack = ((BackSlotHolder) player).backslot$getItem();
		if (!stack.isEmpty()) {
			itemModelResolver.updateForLiving(item, stack, ItemDisplayContext.FIXED, player);
		}
	}
}
