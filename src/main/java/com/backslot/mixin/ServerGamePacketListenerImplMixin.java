package com.backslot.mixin;

import com.backslot.BackSlot;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
	// Creative writes slots straight over the network and the handler rejects anything past
	// the shield, so without this the back slot silently ignores creative clicks.
	@ModifyConstant(method = "handleSetCreativeModeSlot", constant = @Constant(intValue = 45))
	private int backslot$allowBackSlot(int lastVanillaSlot) {
		return BackSlot.MENU_SLOT;
	}
}
