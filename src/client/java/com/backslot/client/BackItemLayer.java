package com.backslot.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

// Offsets are in blocks from the torso pivot, which sits on the shoulder line. The torso is
// addBox(-4, 0, -2, 8, 12, 4), so it runs 12px down and its back face is at z = +2px. What
// covers that back face varies, and one depth can't clear all of it: the jacket overlay is
// 0.25px out, but armour is a whole extra shell at OUTER_ARMOR_DEFORMATION = 1.0px, so a
// chestplate sits at +3px and buries anything mounted for a bare back. Hence two depths.
// Slim skins only change the arms, so this holds for both models.
public class BackItemLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
	private static final float VERTICAL_OFFSET = 0.4f;
	private static final float DEPTH_OFFSET = 0.15f;
	private static final float ARMOR_DEPTH_OFFSET = 0.2f;
	private static final float SCALE = 0.8f;

	public BackItemLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords,
			AvatarRenderState state, float yRot, float xRot) {
		ItemStackRenderState item = ((BackSlotRenderState) state).backslot$item();
		if (item.isEmpty()) {
			return;
		}

		PlayerModel model = getParentModel();

		// Anything in the chest slot brings its own shell, elytra included.
		float depth = state.chestEquipment.isEmpty() ? DEPTH_OFFSET : ARMOR_DEPTH_OFFSET;

		poseStack.pushPose();
		model.root().translateAndRotate(poseStack);
		model.body.translateAndRotate(poseStack);
		poseStack.translate(0.0f, VERTICAL_OFFSET, depth);
		// Body space has Y pointing down and Z pointing behind the player, so flipping X and
		// Y leaves the item upright with Z still facing out of the back. That's the basis an
		// item frame hands to a FIXED item.
		poseStack.scale(-SCALE, -SCALE, SCALE);
		item.submit(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		poseStack.popPose();
	}
}
