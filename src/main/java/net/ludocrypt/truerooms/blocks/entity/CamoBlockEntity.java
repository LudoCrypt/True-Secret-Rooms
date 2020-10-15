package net.ludocrypt.truerooms.blocks.entity;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.ludocrypt.truerooms.SecretRooms;
import net.ludocrypt.truerooms.blocks.DoorBlock;
import net.ludocrypt.truerooms.blocks.TrapdoorBlock;
import net.ludocrypt.truerooms.mixin.AccessibleBakedQuad;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.DoorHinge;
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

	public int upRotation = 180;
	public int downRotation = 0;
	public int northRotation = 0;
	public int eastRotation = 0;
	public int southRotation = 0;
	public int westRotation = 0;

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

		// Separation

		tag.putInt("upRotation", upRotation);
		tag.putInt("downRotation", downRotation);
		tag.putInt("northRotation", northRotation);
		tag.putInt("eastRotation", eastRotation);
		tag.putInt("southRotation", southRotation);
		tag.putInt("westRotation", westRotation);

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

		// Separation

		if (tag.contains("upRotation")) {
			upRotation = tag.getInt("upRotation");
		}
		if (tag.contains("downRotation")) {
			downRotation = tag.getInt("downRotation");
		}
		if (tag.contains("northRotation")) {
			northRotation = tag.getInt("northRotation");
		}
		if (tag.contains("eastRotation")) {
			eastRotation = tag.getInt("eastRotation");
		}
		if (tag.contains("southRotation")) {
			southRotation = tag.getInt("southRotation");
		}
		if (tag.contains("southRotation")) {
			westRotation = tag.getInt("southRotation");
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

	@SuppressWarnings("resource")
	public void setRotation(Direction dir, int rotation) {
		switch (dir) {
		case UP:
			this.upRotation = rotation;
			break;
		case DOWN:
			this.downRotation = rotation;
			break;
		case NORTH:
			this.northRotation = rotation;
			break;
		case EAST:
			this.eastRotation = rotation;
			break;
		case SOUTH:
			this.southRotation = rotation;
			break;
		case WEST:
			this.westRotation = rotation;
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
	public void setRotation(int rotation) {
		this.upRotation = rotation;
		this.downRotation = rotation;
		this.northRotation = rotation;
		this.eastRotation = rotation;
		this.southRotation = rotation;
		this.westRotation = rotation;
		if (!world.isClient) {
			sync();
		} else {
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(),
					pos.getX(), pos.getY(), pos.getZ());
		}
	}

	public int getRotation(Direction dir) {
		int tempInt;
		switch (dir) {
		case UP:
			tempInt = upRotation;
			break;
		case DOWN:
			tempInt = downRotation;
			break;
		case NORTH:
			tempInt = northRotation;
			break;
		case EAST:
			tempInt = eastRotation;
			break;
		case SOUTH:
			tempInt = southRotation;
			break;
		case WEST:
			tempInt = westRotation;
			break;
		default:
			tempInt = upRotation;
			break;
		}
		return tempInt;
	}

	public static int getRotation(Direction dir, CompoundTag tag) {
		int tempInt;
		switch (dir) {
		case UP:
			tempInt = tag.getInt("upRotation");
			break;
		case DOWN:
			tempInt = tag.getInt("downRotation");
			break;
		case NORTH:
			tempInt = tag.getInt("northRotation");
			break;
		case EAST:
			tempInt = tag.getInt("eastRotation");
			break;
		case SOUTH:
			tempInt = tag.getInt("southRotation");
			break;
		case WEST:
			tempInt = tag.getInt("westRotation");
			break;
		default:
			tempInt = tag.getInt("upRotation");
			break;
		}
		return tempInt;
	}

	public void renderBlock(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier,
			RenderContext context) {

		QuadEmitter emitter = context.getEmitter();

		CompoundTag tag = serialize(new CompoundTag());

		for (Direction direction : Direction.values()) {

			BlockState tempState = Blocks.STONE.getDefaultState();
			Direction tempDirection = Direction.NORTH;
			int tempRotation = 0;

			if (tag.contains(direction.name().toLowerCase() + "State")) {
				tempState = NbtHelper.toBlockState(tag.getCompound(direction.name().toLowerCase() + "State"));
			}

			if (tag.contains(direction.name().toLowerCase() + "Direction")) {
				tempDirection = Direction.byName(tag.getString(direction.name().toLowerCase() + "Direction"));
			}

			if (tag.contains(direction.name().toLowerCase() + "Rotation")) {
				tempRotation = tag.getInt(direction.name().toLowerCase() + "Rotation");
			}

			BakedModel fullModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(tempState);
			List<BakedQuad> abqList = fullModel.getQuads(tempState, tempDirection, randomSupplier.get());

			BlockColors colors = MinecraftClient.getInstance().getBlockColors();
			int color = 0xFF00_0000 | colors.getColor(tempState, blockView, pos, 0xFF);

			for (BakedQuad quad : abqList) {

				Sprite quadSprite = ((AccessibleBakedQuad) quad).getSprite();

				if (state.getBlock() instanceof DoorBlock) {
					renderDoor(emitter, state, direction);
				} else if (state.getBlock() instanceof TrapdoorBlock) {
					renderTrapdoor(emitter, state, direction);
				} else {
					emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0);
				}

				emitter.spriteBake(0, quadSprite, 4);

				rotateAndFlip(emitter, tempRotation, direction);

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

	private void renderDoorCuboid(QuadEmitter emitter, Direction faceDirection, Direction cuboidDirection) {
		switch (cuboidDirection) {

		case NORTH:

			if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0f);
			} else if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			}
			break;

		case EAST:

			if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			}
			break;

		case SOUTH:

			if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0f);
			} else if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			}
			break;

		case WEST:

			if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			}
			break;

		case UP:

			if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			}
			break;

		case DOWN:

			if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			} else if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			}
			break;

		}
	}

	private void renderDoor(QuadEmitter emitter, BlockState state, Direction direction) {

		Direction renderDirection = Direction.EAST;

		if ((state.get(DoorBlock.FACING) == Direction.NORTH && !state.get(DoorBlock.OPEN))
				|| (state.get(DoorBlock.FACING) == Direction.EAST && state.get(DoorBlock.HINGE) == DoorHinge.RIGHT
						&& state.get(DoorBlock.OPEN))
				|| (state.get(DoorBlock.FACING) == Direction.WEST && state.get(DoorBlock.HINGE) == DoorHinge.LEFT
						&& state.get(DoorBlock.OPEN))) {
			renderDirection = Direction.SOUTH;
		} else if ((state.get(DoorBlock.FACING) == Direction.EAST && !state.get(DoorBlock.OPEN))
				|| (state.get(DoorBlock.FACING) == Direction.SOUTH && state.get(DoorBlock.HINGE) == DoorHinge.RIGHT
						&& state.get(DoorBlock.OPEN))
				|| (state.get(DoorBlock.FACING) == Direction.NORTH && state.get(DoorBlock.HINGE) == DoorHinge.LEFT
						&& state.get(DoorBlock.OPEN))) {
			renderDirection = Direction.WEST;
		} else if ((state.get(DoorBlock.FACING) == Direction.SOUTH && !state.get(DoorBlock.OPEN))
				|| (state.get(DoorBlock.FACING) == Direction.WEST && state.get(DoorBlock.HINGE) == DoorHinge.RIGHT
						&& state.get(DoorBlock.OPEN))
				|| (state.get(DoorBlock.FACING) == Direction.EAST && state.get(DoorBlock.HINGE) == DoorHinge.LEFT
						&& state.get(DoorBlock.OPEN))) {
			renderDirection = Direction.NORTH;
		} else if ((state.get(DoorBlock.FACING) == Direction.WEST && !state.get(DoorBlock.OPEN))
				|| (state.get(DoorBlock.FACING) == Direction.NORTH && state.get(DoorBlock.HINGE) == DoorHinge.RIGHT
						&& state.get(DoorBlock.OPEN))
				|| (state.get(DoorBlock.FACING) == Direction.SOUTH && state.get(DoorBlock.HINGE) == DoorHinge.LEFT
						&& state.get(DoorBlock.OPEN))) {
			renderDirection = Direction.EAST;
		} else {
			renderDirection = Direction.NORTH;
		}

		renderDoorCuboid(emitter, direction, renderDirection);

	}

	private void renderTrapdoor(QuadEmitter emitter, BlockState state, Direction direction) {
		if (state.get(TrapdoorBlock.OPEN)) {
			renderDoorCuboid(emitter, direction, state.get(TrapdoorBlock.FACING).getOpposite());
		} else {
			if (state.get(TrapdoorBlock.HALF) == BlockHalf.TOP) {
				renderDoorCuboid(emitter, direction, Direction.UP);
			} else if (state.get(TrapdoorBlock.HALF) == BlockHalf.BOTTOM) {
				renderDoorCuboid(emitter, direction, Direction.DOWN);
			}
		}
	}

	private void rotateAndFlip(QuadEmitter emitter, int rotation, Direction dir) {

		float u0 = emitter.spriteU(0, 0);
		float v0 = emitter.spriteV(0, 0);

		float u1 = emitter.spriteU(1, 0);
		float v1 = emitter.spriteV(1, 0);

		float u2 = emitter.spriteU(2, 0);
		float v2 = emitter.spriteV(2, 0);

		float u3 = emitter.spriteU(3, 0);
		float v3 = emitter.spriteV(3, 0);

		switch (rotation) {
		case 0:
			if (dir.equals(Direction.UP)) {
				emitter.sprite(0, 0, u3, v3);
				emitter.sprite(1, 0, u2, v2);
				emitter.sprite(2, 0, u1, v1);
				emitter.sprite(3, 0, u0, v0);
			} else {
				emitter.sprite(0, 0, u0, v0);
				emitter.sprite(1, 0, u1, v1);
				emitter.sprite(2, 0, u2, v2);
				emitter.sprite(3, 0, u3, v3);
			}
			break;
		case 90:
			if (dir.equals(Direction.UP)) {
				emitter.sprite(0, 0, u0, v0);
				emitter.sprite(1, 0, u3, v3);
				emitter.sprite(2, 0, u2, v2);
				emitter.sprite(3, 0, u1, v1);
			} else {
				emitter.sprite(0, 0, u1, v1);
				emitter.sprite(1, 0, u2, v2);
				emitter.sprite(2, 0, u3, v3);
				emitter.sprite(3, 0, u0, v0);
			}
			break;
		case 180:
			if (dir.equals(Direction.UP)) {
				emitter.sprite(0, 0, u1, v1);
				emitter.sprite(1, 0, u0, v0);
				emitter.sprite(2, 0, u3, v3);
				emitter.sprite(3, 0, u2, v2);
			} else {
				emitter.sprite(0, 0, u2, v2);
				emitter.sprite(1, 0, u3, v3);
				emitter.sprite(2, 0, u0, v0);
				emitter.sprite(3, 0, u1, v1);
			}

			break;
		case 270:
			if (dir.equals(Direction.UP)) {
				emitter.sprite(0, 0, u2, v2);
				emitter.sprite(1, 0, u1, v1);
				emitter.sprite(2, 0, u0, v0);
				emitter.sprite(3, 0, u3, v3);
			} else {
				emitter.sprite(0, 0, u3, v3);
				emitter.sprite(1, 0, u0, v0);
				emitter.sprite(2, 0, u1, v1);
				emitter.sprite(3, 0, u2, v2);
			}

			break;
		}

	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(this.pos, -1, serialize(new CompoundTag()));
	}

}
