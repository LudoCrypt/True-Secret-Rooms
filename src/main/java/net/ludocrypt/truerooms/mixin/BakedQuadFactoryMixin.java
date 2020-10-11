package net.ludocrypt.truerooms.mixin;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.truerooms.SecretRooms;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.json.ModelElementFace;
import net.minecraft.client.render.model.json.ModelRotation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
@Mixin(BakedQuadFactory.class)
public class BakedQuadFactoryMixin {

	@Inject(method = "bake", at = @At("HEAD"), cancellable = true)
	public void bake(Vector3f from, Vector3f to, ModelElementFace face, Sprite texture, Direction side,
			ModelBakeSettings settings, ModelRotation rotation, boolean shade, Identifier modelId,
			CallbackInfoReturnable<BakedQuad> ci) {
//		SecretRooms.mapIdentifier.computeIfAbsent(modelId, i -> new HashMap<>()).put(side, texture);
	}

}
