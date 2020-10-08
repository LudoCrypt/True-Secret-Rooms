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
import net.minecraft.block.BlockState;
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
					new Identifier("minecraft:block/gilded_blackstone")),
			new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					new Identifier("minecraft:block/jukebox_side")) };
	private Sprite[] SPRITES = new Sprite[2];

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
		return Arrays.asList(SPRITE_IDS); // The textures this model (and all its model dependencies, and their
											// dependencies, etc...!) depends on.
	}

	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter,
			ModelBakeSettings rotationContainer, Identifier modelId) {

		// Load the default block model
		JsonUnbakedModel defaultBlockModel = (JsonUnbakedModel) loader.getOrLoadModel(DEFAULT_BLOCK_MODEL);
		// Get its ModelTransformation
		transformation = defaultBlockModel.getTransformations();

		// Get the sprites
		for (int i = 0; i < 2; ++i) {
			SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
		}
		// Build the mesh using the Renderer API
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();

		for (Direction direction : Direction.values()) {
			int spriteIdx = direction == Direction.UP || direction == Direction.DOWN ? 1 : 0;
			// Add a new face to the mesh
			emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
			// Set the sprite of the face, must be called after .square()
			// We haven't specified any UV coordinates, so we want to use the whole texture.
			// BAKE_LOCK_UV does exactly that.
			emitter.spriteBake(0, SPRITES[spriteIdx], MutableQuadView.BAKE_LOCK_UV);
			// Enable texture usage
			emitter.spriteColor(0, -1, -1, -1, -1);
			// Add the quad to the mesh
			emitter.emit();
		}
		mesh = builder.build();

		return this;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
		return null; // Don't need because we use FabricBakedModel instead
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false; // Again, we don't really care, etc...
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
		return SPRITES[1]; // Block break particle, let's use furnace_top
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
		return false; // False to trigger FabricBakedModel rendering
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos,
			Supplier<Random> supplier, RenderContext renderContext) {
		// Render function

		// We just render the mesh
		renderContext.meshConsumer().accept(mesh);
	}

	@Override
	public void emitItemQuads(ItemStack itemStack, Supplier<Random> supplier, RenderContext renderContext) {
		renderContext.meshConsumer().accept(mesh);
	}

}
