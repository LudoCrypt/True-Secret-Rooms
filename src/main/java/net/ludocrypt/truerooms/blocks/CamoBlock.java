package net.ludocrypt.truerooms.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
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
		if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {
			CamoBlockEntity blockEntity = (CamoBlockEntity) world.getBlockEntity(pos);

			Direction dir = placer.getHorizontalFacing();

			switch (dir) {
			case UP:
				pos = pos.up();
				break;
			case DOWN:
				pos = pos.down();
				break;
			case NORTH:
				pos = pos.north();
				break;
			case EAST:
				pos = pos.east();
				break;
			case SOUTH:
				pos = pos.south();
				break;
			case WEST:
				pos = pos.west();
				break;
			}

			Direction hitDir = dir.getOpposite();
			BlockState blockState = world.getBlockState(pos);
			Block block = blockState.getBlock();

			if (block instanceof CamoBlock) {
				if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {
					CamoBlockEntity blockEntityAdjacent = (CamoBlockEntity) world.getBlockEntity(pos);
					blockEntity.setState(blockEntityAdjacent.getState(hitDir));
				} else {
					blockEntity.setState(Blocks.STONE.getDefaultState());
				}
			} else if (block != Blocks.AIR && blockState.isFullCube(world, pos)) {
				blockEntity.setState(blockState);
			} else {
				blockEntity.setState(Blocks.STONE.getDefaultState());
			}

		}
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
