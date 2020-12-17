package net.ludocrypt.truerooms.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.ludocrypt.truerooms.SecretRooms;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CamoBlock extends BlockWithEntity {

	public CamoBlock(FabricBlockSettings fabricBlockSettings) {
		super(fabricBlockSettings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new CamoBlockEntity();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			state.scheduledTick((ServerWorld) world, pos, world.getRandom());
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {

			CamoBlockEntity blockEntity = (CamoBlockEntity) world.getBlockEntity(pos);

			for (Direction dir : Direction.values()) {
				BlockPos blockPos = pos.mutableCopy().move(dir);
				BlockState blockState = world.getBlockState(blockPos);
				Block block = blockState.getBlock();
				Direction faceDir = dir.getOpposite();
				if (block instanceof CamoBlock) {
					if (world.getBlockEntity(blockPos) instanceof CamoBlockEntity) {
						CamoBlockEntity blockEntityAdjacent = (CamoBlockEntity) world.getBlockEntity(blockPos);
						SecretRooms.updateSide(blockEntityAdjacent.getState(faceDir), dir, pos, blockEntity);
						SecretRooms.updateDirection(faceDir, dir, pos, blockEntity);
					} else {
						SecretRooms.updateSide(Blocks.STONE.getDefaultState(), dir, pos, blockEntity);
						SecretRooms.updateDirection(faceDir, dir, pos, blockEntity);
					}
				} else if (block != Blocks.AIR) {
					SecretRooms.updateSide(blockState, dir, pos, blockEntity);
					SecretRooms.updateDirection(faceDir, dir, pos, blockEntity);
				} else {
					SecretRooms.updateSide(Blocks.STONE.getDefaultState(), dir, pos, blockEntity);
					SecretRooms.updateDirection(faceDir, dir, pos, blockEntity);
				}
			}
			blockEntity.refresh();
		}
		world.scheduleBlockRerenderIfNeeded(pos, state, state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {
			CamoBlockEntity blockEntity = (CamoBlockEntity) world.getBlockEntity(pos);
			ItemStack itemStack = player.getStackInHand(hand);
			if (itemStack.getItem() == Items.HONEYCOMB) {
				if (!blockEntity.waxed) {
					blockEntity.waxed = true;
					player.playSound(SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
					return ActionResult.SUCCESS;
				}
			} else if (itemStack.getItem() instanceof BlockItem) {
				BlockItem blockItem = (BlockItem) itemStack.getItem();
				Block block = blockItem.getBlock();
				if (!(block instanceof CamoBlock)) {
					BlockState blockState = block.getDefaultState();
					if (block != Blocks.AIR) {
						blockEntity.setState(hit.getSide(), blockState);
					} else {
						blockEntity.setState(hit.getSide(), Blocks.STONE.getDefaultState());
					}
				}
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return (stateFrom.getBlock() instanceof CamoBlock) ? true : super.isSideInvisible(state, stateFrom, direction);
	}

}
