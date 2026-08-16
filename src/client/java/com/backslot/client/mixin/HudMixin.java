package com.backslot.client.mixin;

import com.backslot.BackSlotHolder;
import com.backslot.BackSlotSync;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class HudMixin {
	@Unique
	private static final Identifier SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_right");

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	private void extractSlot(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker,
			Player player, ItemStack itemStack, int seed) {
		throw new AssertionError();
	}

	@Shadow
	private Player getCameraPlayer() {
		throw new AssertionError();
	}

	@Inject(method = "extractItemHotbar", at = @At("TAIL"))
	private void backslot$drawBackSlot(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		if (!BackSlotSync.serverHasMod()) {
			return;
		}

		Player player = getCameraPlayer();
		if (player == null) {
			return;
		}

		ItemStack stack = ((BackSlotHolder) player).backslot$getItem();
		if (stack.isEmpty()) {
			return;
		}

		int x = graphics.guiWidth() / 2 + 91;

		// Step outside whatever vanilla already drew on this side. The offhand lands here for
		// left-handed players, and the attack indicator does when it's set to hotbar mode.
		if (player.getMainArm().getOpposite() == HumanoidArm.RIGHT) {
			if (!player.getOffhandItem().isEmpty()) {
				x += 29;
			}
		} else if (minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
			x += 24;
		}

		int y = graphics.guiHeight();
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE, x, y - 23, 29, 24);
		// Seed 11 keeps the item model out of the hotbar's own 1..10.
		extractSlot(graphics, x + 10, y - 19, deltaTracker, player, stack, 11);
	}
}
