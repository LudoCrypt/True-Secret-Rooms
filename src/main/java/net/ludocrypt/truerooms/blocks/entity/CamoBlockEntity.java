package net.ludocrypt.truerooms.blocks.entity;

import java.util.Random;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.ludocrypt.truerooms.SecretRooms;
import net.ludocrypt.truerooms.mixin.AccessibleBakedQuad;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

public class CamoBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

	public BlockState upState = Blocks.STONE.getDefaultState();
	public BlockState downState = Blocks.STONE.getDefaultState();
	public BlockState northState = Blocks.STONE.getDefaultState();
	public BlockState eastState = Blocks.STONE.getDefaultState();
	public BlockState southState = Blocks.STONE.getDefaultState();
	public BlockState westState = Blocks.STONE.getDefaultState();
	private Mesh mesh;

	public CamoBlockEntity() {
		super(SecretRooms.CAMO_BLOCK_ENTITY);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		return serialize(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		deserialize(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}

	@Override
	@SuppressWarnings("resource")
	public void fromClientTag(CompoundTag tag) {
		fromTag(null, tag);
		MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(),
				pos.getY(), pos.getZ());
	}

	public CompoundTag serialize(CompoundTag tag) {

		tag.put("upState", NbtHelper.fromBlockState(upState));
		tag.put("downState", NbtHelper.fromBlockState(downState));
		tag.put("northState", NbtHelper.fromBlockState(northState));
		tag.put("eastState", NbtHelper.fromBlockState(eastState));
		tag.put("southState", NbtHelper.fromBlockState(southState));
		tag.put("westState", NbtHelper.fromBlockState(westState));

		return tag;

	}

	public void deserialize(CompoundTag tag) {

		if (tag.contains("upState")) {
			this.upState = NbtHelper.toBlockState(tag.getCompound("upState"));
		}
		if (tag.contains("downState")) {
			this.downState = NbtHelper.toBlockState(tag.getCompound("downState"));
		}
		if (tag.contains("northState")) {
			this.northState = NbtHelper.toBlockState(tag.getCompound("northState"));
		}
		if (tag.contains("eastState")) {
			this.eastState = NbtHelper.toBlockState(tag.getCompound("eastState"));
		}
		if (tag.contains("southState")) {
			this.southState = NbtHelper.toBlockState(tag.getCompound("southState"));
		}
		if (tag.contains("westState")) {
			this.westState = NbtHelper.toBlockState(tag.getCompound("westState"));
		}

	}

	public void setState(Direction dir, BlockState newState) {
		switch (dir) {
		case UP:
			this.upState = newState;
			break;
		case DOWN:
			this.downState = newState;
			break;
		case NORTH:
			this.northState = newState;
			break;
		case EAST:
			this.eastState = newState;
			break;
		case SOUTH:
			this.southState = newState;
			break;
		case WEST:
			this.westState = newState;
			break;
		}
		if (!world.isClient) {
			sync();
		}
	}

	public void setState(BlockState newState) {
		this.upState = newState;
		this.downState = newState;
		this.northState = newState;
		this.eastState = newState;
		this.southState = newState;
		this.westState = newState;
		if (!world.isClient) {
			sync();
		}
	}

	public BlockState getState(Direction dir) {
		BlockState tempState;
		switch (dir) {
		case UP:
			tempState = upState;
			break;
		case DOWN:
			tempState = downState;
			break;
		case NORTH:
			tempState = northState;
			break;
		case EAST:
			tempState = eastState;
			break;
		case SOUTH:
			tempState = southState;
			break;
		case WEST:
			tempState = westState;
			break;
		default:
			tempState = upState;
			break;
		}
		return tempState;
	}

	public BlockState getState(Direction dir, CompoundTag tag) {
		BlockState tempState = Blocks.STONE.getDefaultState();
		switch (dir) {
		case UP:
			if (tag.contains("upState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("upState"));
			}
			break;
		case DOWN:
			if (tag.contains("downState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("downState"));
			}
			break;
		case NORTH:
			if (tag.contains("northState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("northState"));
			}
			break;
		case EAST:
			if (tag.contains("eastState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("eastState"));
			}
			break;
		case SOUTH:
			if (tag.contains("southState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("southState"));
			}
			break;
		case WEST:
			if (tag.contains("westState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("westState"));
			}
			break;
		default:
			if (tag.contains("upState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("upState"));
			}
			break;
		}
		return tempState;
	}

	public Mesh getMesh(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier,
			RenderContext context) {

		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();

		CompoundTag tag = serialize(new CompoundTag());

		for (Direction direction : Direction.values()) {

			BlockState tempState = Blocks.STONE.getDefaultState();

			if (tag.contains(direction.toString() + "State")) {
				tempState = NbtHelper.toBlockState(tag.getCompound(direction.toString() + "State"));
			}

			Sprite spr = MinecraftClient.getInstance().getBlockRenderManager().getModel(tempState).getSprite();

			BakedQuad abq = (MinecraftClient.getInstance().getBlockRenderManager().getModel(tempState)
					.getQuads(tempState, direction, randomSupplier.get())).get(0);

			if (abq != null) {
				spr = ((AccessibleBakedQuad) abq).getSprite();
			}

			BlockColors colors = MinecraftClient.getInstance().getBlockColors();
			int color = colors.getColor(tempState, blockView, pos, 0);

//			emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
//			emitter.spriteBake(0, spr, MutableQuadView.BAKE_LOCK_UV);

			emitter.fromVanilla(abq.getVertexData(), 0, false);

			if (abq.hasColor()) {
				emitter.spriteColor(0, color, color, color, color);
			} else {
				emitter.spriteColor(0, 0xFFFF_FFFF, 0xFFFF_FFFF, 0xFFFF_FFFF, 0xFFFF_FFFF);
			}

			emitter.emit();

		}

		this.mesh = builder.build();

		return this.mesh;
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(this.pos, -1, serialize(new CompoundTag()));
	}

}
