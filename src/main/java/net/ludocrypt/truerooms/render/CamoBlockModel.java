package net.ludocrypt.truerooms.render;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.ludocrypt.truerooms.mixin.AccessibleBakedQuad;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

public class CamoBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {

	private static final SpriteIdentifier[] SPRITE_IDS = new SpriteIdentifier[] {
			new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					new Identifier("minecraft:block/beehive_side")),
			new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					new Identifier("minecraft:block/birch_trapdoor")),
			new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					new Identifier("minecraft:block/gilded_blackstone")),
			new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					new Identifier("minecraft:block/hay_block_side")),
			new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					new Identifier("minecraft:block/barrel_bottom")),
			new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					new Identifier("minecraft:block/daylight_detector_inverted_top")) };
	private Sprite[] SPRITES = new Sprite[6];

	private Mesh mesh;

	private static final Identifier DEFAULT_BLOCK_MODEL = new Identifier("minecraft:block/block");

	private ModelTransformation transformation;

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Arrays.asList(DEFAULT_BLOCK_MODEL);
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter,
			Set<Pair<String, String>> unresolvedTextureReferences) {
		return Arrays.asList(SPRITE_IDS);
	}

	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter,
			ModelBakeSettings rotationContainer, Identifier modelId) {

		JsonUnbakedModel defaultBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL);

		transformation = defaultBlockModel.getTransformations();

		for (int i = 0; i < 6; ++i) {
			SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
		}

		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();

		for (Direction direction : Direction.values()) {
			int spriteIdx = direction == Direction.UP ? 5
					: direction == Direction.DOWN ? 4
							: direction == Direction.NORTH ? 0
									: direction == Direction.EAST ? 1
											: direction == Direction.SOUTH ? 2 : direction == Direction.WEST ? 3 : 0;

			emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);

			emitter.spriteBake(0, SPRITES[spriteIdx], MutableQuadView.BAKE_LOCK_UV);

			emitter.spriteColor(0, -1, -1, -1, -1);

			emitter.emit();
		}
		mesh = builder.build();

		return this;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
		return null;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return true;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return SPRITES[5];
	}

	@Override
	public ModelTransformation getTransformation() {
		return transformation;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos,
			Supplier<Random> supplier, RenderContext renderContext) {

		renderContext.pushTransform(mqv -> {

			Direction dir = mqv.lightFace();

			BlockState[] idList = (BlockState[]) ((RenderAttachedBlockView) blockRenderView)
					.getBlockEntityRenderAttachment(blockPos);

			BakedModel model = null;

			if (idList != null) {
				model = MinecraftClient.getInstance().getBlockRenderManager().getModel(idList[0]);
			}

			List<BakedQuad> bakedQuadList = null;

			if (model != null) {
				bakedQuadList = model.getQuads(idList[0], dir, supplier.get());
			}

			BakedQuad id0 = null;

			if (bakedQuadList != null) {
				if (0 < bakedQuadList.toArray().length) {
					id0 = bakedQuadList.get(0);
				}
			}

			Sprite id0Sprite = null;

			if (id0 != null) {
				id0Sprite = ((AccessibleBakedQuad) id0).getSprite();
			}

			if (id0Sprite != null) {
				mqv.spriteBake(0, id0Sprite, MutableQuadView.BAKE_LOCK_UV);
			}

			return true;
		});

		renderContext.meshConsumer().accept(mesh);

		renderContext.popTransform();
	}

	@Override
	public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
		renderContext.meshConsumer().accept(mesh);
	}

}
