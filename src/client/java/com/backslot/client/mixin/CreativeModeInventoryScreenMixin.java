package com.backslot.client.mixin;

import com.backslot.BackSlot;
import com.backslot.BackSlotSync;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/**
 * The creative screen re-lays the inventory menu out by slot index, and the arithmetic it
 * falls back on for anything past the shield would drop slot 46 on top of a hotbar slot.
 */
@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin
		extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
	@Unique
	private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");

	// Left of the shield slot at (35, 20). The player preview takes x 73..105, so this
	// corner is free.
	@Unique
	private static final int SLOT_X = 17;

	@Unique
	private static final int SLOT_Y = 20;

	@Shadow
	private static CreativeModeTab selectedTab;

	private CreativeModeInventoryScreenMixin() {
		super(null, null, null);
	}

	@ModifyArgs(
			method = "selectTab",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$SlotWrapper;<init>(Lnet/minecraft/world/inventory/Slot;III)V"))
	private void backslot$placeBackSlot(Args args) {
		int index = args.get(1);
		if (index == BackSlot.MENU_SLOT) {
			args.set(2, SLOT_X);
			args.set(3, SLOT_Y);
		}
	}

	@Inject(method = "extractBackground", at = @At("TAIL"))
	private void backslot$drawSlotFrame(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
			float partialTick, CallbackInfo ci) {
		if (BackSlotSync.serverHasMod() && selectedTab.getType() == CreativeModeTab.Type.INVENTORY) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_SPRITE,
					leftPos + SLOT_X - 1, topPos + SLOT_Y - 1, 18, 18);
		}
	}
}
