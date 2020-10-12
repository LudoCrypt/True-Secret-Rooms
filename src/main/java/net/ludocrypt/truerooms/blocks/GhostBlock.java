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
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
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

			MinecraftClient client = MinecraftClient.getInstance();
			HitResult hit = client.crosshairTarget;

			switch (hit.getType()) {
			case MISS:
				blockEntity.setState(Blocks.STONE.getDefaultState());
				break;
			case BLOCK:
				BlockHitResult blockHit = (BlockHitResult) hit;
				BlockPos blockPos = blockHit.getBlockPos();
				BlockState blockState = client.world.getBlockState(blockPos);
				Block block = blockState.getBlock();

				if (block != Blocks.AIR && blockState.isFullCube(world, blockPos)) {
					blockEntity.setState(blockState);
				} else if (block == this) {
					if (world.getBlockEntity(blockPos) instanceof CamoBlockEntity) {
						CamoBlockEntity blockEntityAdjacent = (CamoBlockEntity) world.getBlockEntity(blockPos);
						blockEntity.setState(blockEntityAdjacent.getState(blockHit.getSide()));
					} else {
						blockEntity.setState(Blocks.STONE.getDefaultState());
					}
				} else {
					blockEntity.setState(Blocks.STONE.getDefaultState());
				}
				break;
			case ENTITY:
				blockEntity.setState(Blocks.STONE.getDefaultState());
				break;
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
