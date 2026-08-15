package com.backslot.client.mixin;

import com.backslot.client.BackSlotRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public abstract class AvatarRenderStateMixin implements BackSlotRenderState {
	@Unique
	private ItemStackRenderState backslot$item;

	@Override
	public ItemStackRenderState backslot$item() {
		if (backslot$item == null) {
			backslot$item = new ItemStackRenderState();
		}
		return backslot$item;
	}
}
