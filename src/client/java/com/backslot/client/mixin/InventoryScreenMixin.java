package com.backslot.client.mixin;

import com.backslot.BackSlot;
import com.backslot.BackSlotSync;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractContainerScreen<InventoryMenu> {
	@Unique
	private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");

	private InventoryScreenMixin() {
		super(null, null, null);
	}

	// Slot frames are painted into the background texture, not drawn per slot, so ours has
	// to go on top by hand.
	@Inject(method = "extractBackground", at = @At("TAIL"))
	private void backslot$drawSlotFrame(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
			float partialTick, CallbackInfo ci) {
		if (!BackSlotSync.serverHasMod()) {
			return;
		}

		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_SPRITE,
				leftPos + BackSlot.SLOT_X - 1, topPos + BackSlot.SLOT_Y - 1, 18, 18);
	}
}
