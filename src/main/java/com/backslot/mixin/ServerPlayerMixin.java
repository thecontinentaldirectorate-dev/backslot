package com.backslot.mixin;

import com.backslot.BackSlotHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
	// Vanilla only calls this when the inventory should survive — keepInventory, dying as a
	// spectator, or coming back through the end portal — so hooking it keeps the back slot
	// on the same terms as everything else without repeating those conditions.
	@Inject(method = "transferInventoryXpAndScore", at = @At("TAIL"))
	private void backslot$transfer(Player oldPlayer, CallbackInfo ci) {
		((BackSlotHolder) this).backslot$setItem(((BackSlotHolder) oldPlayer).backslot$getItem());
	}
}
