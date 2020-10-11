package net.ludocrypt.truerooms.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GhostBlock extends BlockWithEntity {

	public GhostBlock() {
		super(FabricBlockSettings.of(Material.GOURD).sounds(BlockSoundGroup.WOOL).hardness(1).resistance(2));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new CamoBlockEntity();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {
			CamoBlockEntity blockEntity = (CamoBlockEntity) world.getBlockEntity(pos);
			if (world.getBlockState(pos.down()) != this.getDefaultState()
					&& world.getBlockState(pos.up()) != this.getDefaultState()) {
				if (world.getBlockState(pos.down()).isOpaque()) {
					blockEntity.setState(world.getBlockState(pos.down()));
				} else if (world.getBlockState(pos.up()).isOpaque()) {
					blockEntity.setState(world.getBlockState(pos.up()));
				} else {
					blockEntity.setState(Blocks.STONE.getDefaultState());
				}
			} else {
				if (world.getBlockEntity(pos.down()) instanceof CamoBlockEntity) {
					CamoBlockEntity blockEntityDown = (CamoBlockEntity) world.getBlockEntity(pos.down());
					blockEntity.setState(blockEntityDown.getState(Direction.UP));
				} else {
					if (world.getBlockEntity(pos.up()) instanceof CamoBlockEntity) {
						CamoBlockEntity blockEntityUp = (CamoBlockEntity) world.getBlockEntity(pos.up());
						blockEntity.setState(blockEntityUp.getState(Direction.UP));
					}
				}
			}
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return (stateFrom.getBlock() instanceof GhostBlock) ? true : super.isSideInvisible(state, stateFrom, direction);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

}
