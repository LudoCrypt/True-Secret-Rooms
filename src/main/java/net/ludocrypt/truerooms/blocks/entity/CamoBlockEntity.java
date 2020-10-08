package net.ludocrypt.truerooms.blocks.entity;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.ludocrypt.truerooms.SecretRooms;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.Direction;

public class CamoBlockEntity extends BlockEntity {

	public BlockState upState = Blocks.MELON.getDefaultState();
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
		serialize(tag);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		deserialize(tag);
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

		upState = NbtHelper.toBlockState(tag.getCompound("upState"));
		downState = NbtHelper.toBlockState(tag.getCompound("downState"));
		northState = NbtHelper.toBlockState(tag.getCompound("northState"));
		eastState = NbtHelper.toBlockState(tag.getCompound("eastState"));
		southState = NbtHelper.toBlockState(tag.getCompound("southState"));
		westState = NbtHelper.toBlockState(tag.getCompound("westState"));

	}

	public void setState(Direction dir, BlockState newState) {
		switch (dir) {
		case UP:
			upState = newState;
			break;
		case DOWN:
			downState = newState;
			break;
		case NORTH:
			northState = newState;
			break;
		case EAST:
			eastState = newState;
			break;
		case SOUTH:
			southState = newState;
			break;
		case WEST:
			westState = newState;
			break;
		}
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 11);
	}

	public void setState(BlockState newState) {
		upState = newState;
		downState = newState;
		northState = newState;
		eastState = newState;
		southState = newState;
		westState = newState;
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 11);
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

	public Mesh getMesh() {
		if (this.mesh != null) {
			return this.mesh;
		}

		Renderer renderer = RendererAccess.INSTANCE.getRenderer();
		MeshBuilder builder = renderer.meshBuilder();
		QuadEmitter emitter = builder.getEmitter();

		for (Direction direction : Direction.values()) {

			BlockState tempState = direction == Direction.UP ? upState
					: direction == Direction.DOWN ? downState
							: direction == Direction.NORTH ? northState
									: direction == Direction.EAST ? eastState
											: direction == Direction.SOUTH ? southState
													: direction == Direction.WEST ? westState : null;

			if (tempState != null) {

				emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);

				emitter.spriteBake(0,
						MinecraftClient.getInstance().getBlockRenderManager().getModel(tempState).getSprite(),
						MutableQuadView.BAKE_LOCK_UV);

				emitter.spriteColor(0, -1, -1, -1, -1);

				emitter.emit();

			}

		}

		return this.mesh = builder.build();
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(this.pos, -1, this.serialize(new CompoundTag()));
	}

}
