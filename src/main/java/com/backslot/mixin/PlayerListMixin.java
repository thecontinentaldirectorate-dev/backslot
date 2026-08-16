package com.backslot.mixin;

import com.backslot.BackSlotHolder;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
	// The whole handshake. Re-setting the back item forces its synched data entry dirty even
	// when it's empty, so the joining client gets one ClientboundSetEntityDataPacket carrying
	// our id. A server without the mod never sends it, and the client hides the slot.
	@Inject(method = "placeNewPlayer", at = @At("TAIL"))
	private void backslot$announceToClient(Connection connection, ServerPlayer player,
			CommonListenerCookie cookie, CallbackInfo ci) {
		BackSlotHolder holder = (BackSlotHolder) player;
		holder.backslot$setItem(holder.backslot$getItem());
	}
}
