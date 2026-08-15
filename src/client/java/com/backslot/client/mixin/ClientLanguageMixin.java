package com.backslot.client.mixin;

import com.backslot.client.BackSlotKeys;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Stands in for the lang file we can't ship. Without it the controls screen shows the raw
 * {@code key.backslot.swap_back_item}.
 */
@Mixin(ClientLanguage.class)
public abstract class ClientLanguageMixin {
	@Inject(method = "getOrDefault", at = @At("HEAD"), cancellable = true)
	private void backslot$translate(String key, String fallback, CallbackInfoReturnable<String> cir) {
		if (BackSlotKeys.SWAP_KEY.equals(key)) {
			cir.setReturnValue(BackSlotKeys.SWAP_LABEL);
		}
	}

	@Inject(method = "has", at = @At("HEAD"), cancellable = true)
	private void backslot$hasTranslation(String key, CallbackInfoReturnable<Boolean> cir) {
		if (BackSlotKeys.SWAP_KEY.equals(key)) {
			cir.setReturnValue(true);
		}
	}
}
