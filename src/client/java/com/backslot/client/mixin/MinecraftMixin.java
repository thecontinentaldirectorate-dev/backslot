package com.backslot.client.mixin;

import com.backslot.BackSlot;
import com.backslot.BackSlotSync;
import com.backslot.client.BackSlotKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.ContainerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow
	public LocalPlayer player;

	@Shadow
	public MultiPlayerGameMode gameMode;

	// Runs next to vanilla's offhand swap, which needs a dedicated action packet. This one
	// doesn't: the back slot is a real slot in the player's own menu, and a plain SWAP click
	// already means "trade this slot with that hotbar index". The server ends up in the same
	// code a number-key swap uses, so nothing custom goes over the wire.
	@Inject(method = "handleKeybinds", at = @At("TAIL"))
	private void backslot$handleSwapKey(CallbackInfo ci) {
		while (BackSlotKeys.swapBackItem.consumeClick()) {
			if (player == null || gameMode == null || player.isSpectator()) {
				continue;
			}

			// Slot 46 isn't there on a server without the mod, and the click would be
			// rejected server-side and leave the client out of sync.
			if (!BackSlotSync.serverHasMod()) {
				continue;
			}

			// Slot 46 only exists in the player's own menu, so sit it out while a chest or
			// anything else is the menu the server is tracking.
			if (player.containerMenu != player.inventoryMenu) {
				continue;
			}

			gameMode.handleContainerInput(
					player.inventoryMenu.containerId,
					BackSlot.MENU_SLOT,
					player.getInventory().getSelectedSlot(),
					ContainerInput.SWAP,
					player);
		}
	}
}
