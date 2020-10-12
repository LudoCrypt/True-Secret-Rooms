package net.ludocrypt.truerooms.blocks.entity;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
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
import net.minecraft.client.render.model.BakedModel;
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

	public Direction upDirection = Direction.UP;
	public Direction downDirection = Direction.DOWN;
	public Direction northDirection = Direction.NORTH;
	public Direction eastDirection = Direction.EAST;
	public Direction southDirection = Direction.SOUTH;
	public Direction westDirection = Direction.WEST;

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

		// Separation

		tag.putString("upDirection", upDirection.getName());
		tag.putString("downDirection", downDirection.getName());
		tag.putString("northDirection", northDirection.getName());
		tag.putString("eastDirection", eastDirection.getName());
		tag.putString("southDirection", southDirection.getName());
		tag.putString("westDirection", westDirection.getName());

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

		// Separation

		if (tag.contains("upDirection")) {
			upDirection = Direction.byName(tag.getString("upDirection"));
		}
		if (tag.contains("downDirection")) {
			downDirection = Direction.byName(tag.getString("downDirection"));
		}
		if (tag.contains("northDirection")) {
			northDirection = Direction.byName(tag.getString("northDirection"));
		}
		if (tag.contains("eastDirection")) {
			eastDirection = Direction.byName(tag.getString("eastDirection"));
		}
		if (tag.contains("southDirection")) {
			southDirection = Direction.byName(tag.getString("southDirection"));
		}
		if (tag.contains("westDirection")) {
			westDirection = Direction.byName(tag.getString("westDirection"));
		}

	}

	@SuppressWarnings("resource")
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
		} else {
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(),
					pos.getX(), pos.getY(), pos.getZ());
		}
	}

	@SuppressWarnings("resource")
	public void setState(BlockState newState) {
		this.upState = newState;
		this.downState = newState;
		this.northState = newState;
		this.eastState = newState;
		this.southState = newState;
		this.westState = newState;
		if (!world.isClient) {
			sync();
		} else {
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(),
					pos.getX(), pos.getY(), pos.getZ());
		}
	}

	@SuppressWarnings("resource")
	public void setDirection(Direction dir, Direction newDir) {
		switch (dir) {
		case UP:
			this.upDirection = newDir;
			break;
		case DOWN:
			this.downDirection = newDir;
			break;
		case NORTH:
			this.northDirection = newDir;
			break;
		case EAST:
			this.eastDirection = newDir;
			break;
		case SOUTH:
			this.southDirection = newDir;
			break;
		case WEST:
			this.westDirection = newDir;
			break;
		}
		if (!world.isClient) {
			sync();
		} else {
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(),
					pos.getX(), pos.getY(), pos.getZ());
		}
	}

	@SuppressWarnings("resource")
	public void setDirection(Direction newDir) {
		this.upDirection = newDir;
		this.downDirection = newDir;
		this.northDirection = newDir;
		this.eastDirection = newDir;
		this.southDirection = newDir;
		this.westDirection = newDir;
		if (!world.isClient) {
			sync();
		} else {
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(),
					pos.getX(), pos.getY(), pos.getZ());
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

	public static BlockState getState(Direction dir, CompoundTag tag) {
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

	public Direction getDir(Direction dir) {
		Direction tempDir;
		switch (dir) {
		case UP:
			tempDir = upDirection;
			break;
		case DOWN:
			tempDir = downDirection;
			break;
		case NORTH:
			tempDir = northDirection;
			break;
		case EAST:
			tempDir = eastDirection;
			break;
		case SOUTH:
			tempDir = southDirection;
			break;
		case WEST:
			tempDir = westDirection;
			break;
		default:
			tempDir = upDirection;
			break;
		}
		return tempDir;
	}

	public static Direction getDir(Direction dir, CompoundTag tag) {
		Direction tempDir;
		switch (dir) {
		case UP:
			tempDir = Direction.byName(tag.getString("upDirection"));
			break;
		case DOWN:
			tempDir = Direction.byName(tag.getString("downDirection"));
			break;
		case NORTH:
			tempDir = Direction.byName(tag.getString("northDirection"));
			break;
		case EAST:
			tempDir = Direction.byName(tag.getString("eastDirection"));
			break;
		case SOUTH:
			tempDir = Direction.byName(tag.getString("southDirection"));
			break;
		case WEST:
			tempDir = Direction.byName(tag.getString("westDirection"));
			break;
		default:
			tempDir = Direction.byName(tag.getString("upDirection"));
			break;
		}
		return tempDir;
	}

	public void renderBlock(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier,
			RenderContext context) {

		QuadEmitter emitter = context.getEmitter();

		CompoundTag tag = serialize(new CompoundTag());

		for (Direction direction : Direction.values()) {

			BlockState tempState = Blocks.STONE.getDefaultState();
			Direction tempDirection = Direction.NORTH;

			if (tag.contains(direction.toString() + "State")) {
				tempState = NbtHelper.toBlockState(tag.getCompound(direction.name().toLowerCase() + "State"));
			}

			if (tag.contains(direction.toString() + "Direction")) {
				tempDirection = Direction.byName(tag.getString(direction.name().toLowerCase() + "Direction"));
			}

			BakedModel fullModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(tempState);
			List<BakedQuad> abqList = fullModel.getQuads(tempState, tempDirection, randomSupplier.get());

			BlockColors colors = MinecraftClient.getInstance().getBlockColors();
			int color = 0xFF00_0000 | colors.getColor(tempState, blockView, pos, 0xFF);

			for (BakedQuad quad : abqList) {

				Sprite quadSprite = ((AccessibleBakedQuad) quad).getSprite();

				emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
				emitter.spriteBake(0, quadSprite, MutableQuadView.BAKE_LOCK_UV);

				if (quad.hasColor()) {
					emitter.colorIndex(0);
					emitter.spriteColor(0, color, color, color, color);
				} else {
					emitter.colorIndex(-1);
					emitter.spriteColor(0, 0xFFFF_FFFF, 0xFFFF_FFFF, 0xFFFF_FFFF, 0xFFFF_FFFF);
				}
				emitter.emit();
			}
		}
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(this.pos, -1, serialize(new CompoundTag()));
	}

}
