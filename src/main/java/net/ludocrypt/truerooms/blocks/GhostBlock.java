package net.ludocrypt.truerooms.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GhostBlock extends BlockWithEntity {

	public GhostBlock() {
		super(FabricBlockSettings.of(Material.GOURD).sounds(BlockSoundGroup.WOOL).hardness(1).resistance(2)
				.collidable(false));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new CamoBlockEntity();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {
			CamoBlockEntity blockEntity = (CamoBlockEntity) world.getBlockEntity(pos);
			blockEntity.setId(Registry.BLOCK.getId(world.getBlockState(pos.down()).getBlock()));
		}
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

}
