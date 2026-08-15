package com.backslot.client.mixin;

import java.io.File;
import java.util.Arrays;

import com.backslot.client.BackSlotKeys;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public abstract class OptionsMixin {
	@Shadow
	@Final
	@Mutable
	public KeyMapping[] keyMappings;

	// Appending to this array is what puts the key in the controls screen and gets it saved
	// to options.txt. Same category as the offhand swap so the two sit together.
	@Inject(method = "<init>", at = @At("TAIL"))
	private void backslot$addSwapKey(Minecraft minecraft, File gameDirectory, CallbackInfo ci) {
		BackSlotKeys.swapBackItem =
				new KeyMapping(BackSlotKeys.SWAP_KEY, InputConstants.KEY_G, KeyMapping.Category.INVENTORY);

		KeyMapping[] extended = Arrays.copyOf(keyMappings, keyMappings.length + 1);
		extended[keyMappings.length] = BackSlotKeys.swapBackItem;
		keyMappings = extended;
	}
}
