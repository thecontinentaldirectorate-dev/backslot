package com.backslot.client;

import net.minecraft.client.KeyMapping;

public final class BackSlotKeys {
	public static final String SWAP_KEY = "key.backslot.swap_back_item";

	// Fed straight to the language lookup by ClientLanguageMixin. No Fabric API means no
	// lang file, so this is English regardless of the game's language.
	public static final String SWAP_LABEL = "Swap Item With Back Item";

	/** Built in the {@code Options} constructor, so it registers when the vanilla ones do. */
	public static KeyMapping swapBackItem;

	private BackSlotKeys() {
	}
}
