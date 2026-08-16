package com.backslot.client.mixin;

import com.backslot.BackSlotSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
	// One listener per connection, so this is where the handshake resets. Assume the worst
	// until the server proves otherwise.
	@Inject(method = "<init>", at = @At("TAIL"))
	private void backslot$resetHandshake(Minecraft minecraft, Connection connection,
			CommonListenerCookie cookie, CallbackInfo ci) {
		BackSlotSync.setServerHasMod(false);
	}

	@Inject(method = "handleSetEntityData", at = @At("TAIL"))
	private void backslot$watchForAnnouncement(ClientboundSetEntityDataPacket packet, CallbackInfo ci) {
		if (BackSlotSync.serverHasMod()) {
			return;
		}

		for (SynchedEntityData.DataValue<?> value : packet.packedItems()) {
			if (value.id() == BackSlotSync.dataId()) {
				BackSlotSync.setServerHasMod(true);
				break;
			}
		}
	}
}
