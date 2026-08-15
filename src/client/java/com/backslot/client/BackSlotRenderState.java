package com.backslot.client;

import net.minecraft.client.renderer.item.ItemStackRenderState;

/** Mixed into {@code AvatarRenderState} to carry the back item from extract to render. */
public interface BackSlotRenderState {
	ItemStackRenderState backslot$item();
}
