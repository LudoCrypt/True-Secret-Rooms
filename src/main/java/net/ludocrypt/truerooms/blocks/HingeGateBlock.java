package net.ludocrypt.truerooms.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class HingeGateBlock extends CamoBlock {
	public static final DirectionProperty FACING;
	public static final BooleanProperty OPEN;
	public static final EnumProperty<DoorHinge> HINGE;
	public static final BooleanProperty POWERED;
	public static final VoxelShape NORTH_SHAPE;
	public static final VoxelShape SOUTH_SHAPE;
	public static final VoxelShape EAST_SHAPE;
	public static final VoxelShape WEST_SHAPE;

	public HingeGateBlock(FabricBlockSettings settings) {
		super(settings);
		this.setDefaultState((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)).with(OPEN, false)).with(HINGE, DoorHinge.LEFT)).with(POWERED, false)));
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction direction = (Direction) state.get(FACING);
		boolean bl = !(Boolean) state.get(OPEN);
		boolean bl2 = state.get(HINGE) == DoorHinge.RIGHT;
		switch (direction) {
		case EAST:
		default:
			return bl ? WEST_SHAPE : (bl2 ? SOUTH_SHAPE : NORTH_SHAPE);
		case SOUTH:
			return bl ? NORTH_SHAPE : (bl2 ? WEST_SHAPE : EAST_SHAPE);
		case WEST:
			return bl ? EAST_SHAPE : (bl2 ? NORTH_SHAPE : SOUTH_SHAPE);
		case NORTH:
			return bl ? SOUTH_SHAPE : (bl2 ? EAST_SHAPE : WEST_SHAPE);
		}
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		switch (type) {
		case LAND:
			return (Boolean) state.get(OPEN);
		case WATER:
			return false;
		case AIR:
			return (Boolean) state.get(OPEN);
		default:
			return false;
		}
	}

	private int getOpenSoundEventId() {
		return this.material == Material.METAL ? 1011 : 1012;
	}

	private int getCloseSoundEventId() {
		return this.material == Material.METAL ? 1005 : 1006;
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		if (blockPos.getY() < 255 && ctx.getWorld().getBlockState(blockPos.up()).canReplace(ctx)) {
			World world = ctx.getWorld();
			boolean bl = world.isReceivingRedstonePower(blockPos) || world.isReceivingRedstonePower(blockPos.up());
			return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.getDefaultState().with(FACING, ctx.getPlayerFacing())).with(HINGE, this.getHinge(ctx))).with(POWERED, bl)).with(OPEN, bl));
		} else {
			return null;
		}
	}

	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
	}

	private DoorHinge getHinge(ItemPlacementContext ctx) {
		BlockView blockView = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		Direction direction = ctx.getPlayerFacing();
		BlockPos blockPos2 = blockPos.up();
		Direction direction2 = direction.rotateYCounterclockwise();
		BlockPos blockPos3 = blockPos.offset(direction2);
		BlockState blockState = blockView.getBlockState(blockPos3);
		BlockPos blockPos4 = blockPos2.offset(direction2);
		BlockState blockState2 = blockView.getBlockState(blockPos4);
		Direction direction3 = direction.rotateYClockwise();
		BlockPos blockPos5 = blockPos.offset(direction3);
		BlockState blockState3 = blockView.getBlockState(blockPos5);
		BlockPos blockPos6 = blockPos2.offset(direction3);
		BlockState blockState4 = blockView.getBlockState(blockPos6);
		int i = (blockState.isFullCube(blockView, blockPos3) ? -1 : 0) + (blockState2.isFullCube(blockView, blockPos4) ? -1 : 0) + (blockState3.isFullCube(blockView, blockPos5) ? 1 : 0) + (blockState4.isFullCube(blockView, blockPos6) ? 1 : 0);
		if (i <= 0) {
			if (i >= 0) {
				int j = direction.getOffsetX();
				int k = direction.getOffsetZ();
				Vec3d vec3d = ctx.getHitPos();
				double d = vec3d.x - (double) blockPos.getX();
				double e = vec3d.z - (double) blockPos.getZ();
				return (j >= 0 || e >= 0.5D) && (j <= 0 || e <= 0.5D) && (k >= 0 || d <= 0.5D) && (k <= 0 || d >= 0.5D) ? DoorHinge.LEFT : DoorHinge.RIGHT;
			} else {
				return DoorHinge.LEFT;
			}
		} else {
			return DoorHinge.RIGHT;
		}
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		super.onUse(state, world, pos, player, hand, hit);
		if (this.material == Material.METAL) {
			return ActionResult.PASS;
		} else {
			state = (BlockState) state.cycle(OPEN);
			world.setBlockState(pos, state, 10);
			world.syncWorldEvent(player, (Boolean) state.get(OPEN) ? this.getCloseSoundEventId() : this.getOpenSoundEventId(), pos, 0);
			return ActionResult.success(world.isClient);
		}
	}

	public boolean method_30841(BlockState blockState) {
		return (Boolean) blockState.get(OPEN);
	}

	public void setOpen(World world, BlockState blockState, BlockPos blockPos, boolean bl) {
		if (blockState.isOf(this) && (Boolean) blockState.get(OPEN) != bl) {
			world.setBlockState(blockPos, (BlockState) blockState.with(OPEN, bl), 10);
			this.playOpenCloseSound(world, blockPos, bl);
		}
	}

	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		boolean bl = world.isReceivingRedstonePower(pos);
		if (block != this && bl != (Boolean) state.get(POWERED)) {
			if (bl != (Boolean) state.get(OPEN)) {
				this.playOpenCloseSound(world, pos, bl);
			}

			world.setBlockState(pos, (BlockState) ((BlockState) state.with(POWERED, bl)).with(OPEN, bl), 2);
		}

	}

	private void playOpenCloseSound(World world, BlockPos pos, boolean open) {
		world.syncWorldEvent((PlayerEntity) null, open ? this.getCloseSoundEventId() : this.getOpenSoundEventId(), pos, 0);
	}

	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}

	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
	}

	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return mirror == BlockMirror.NONE ? state : (BlockState) state.rotate(mirror.getRotation((Direction) state.get(FACING))).cycle(HINGE);
	}

	@Environment(EnvType.CLIENT)
	public long getRenderingSeed(BlockState state, BlockPos pos) {
		return MathHelper.hashCode(pos.getX(), pos.getY(), pos.getZ());
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN, HINGE, POWERED);
	}

	public static boolean isWoodenDoor(World world, BlockPos pos) {
		return isWoodenDoor(world.getBlockState(pos));
	}

	public static boolean isWoodenDoor(BlockState state) {
		return state.getBlock() instanceof HingeGateBlock && (state.getMaterial() == Material.WOOD || state.getMaterial() == Material.NETHER_WOOD);
	}

	static {
		FACING = HorizontalFacingBlock.FACING;
		OPEN = Properties.OPEN;
		HINGE = Properties.DOOR_HINGE;
		POWERED = Properties.POWERED;
		NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
		SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
		EAST_SHAPE = Block.createCuboidShape(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
		WEST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
	}
}
