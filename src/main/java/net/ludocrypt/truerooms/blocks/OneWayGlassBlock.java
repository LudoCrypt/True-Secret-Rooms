package net.ludocrypt.truerooms.blocks;

import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OneWayGlassBlock extends SolidBlock {

	public OneWayGlassBlock() {
		super();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {
			CamoBlockEntity blockEntity = (CamoBlockEntity) world.getBlockEntity(pos);
			blockEntity.setState(placer.getHorizontalFacing().getOpposite(), Blocks.GLASS.getDefaultState());
		}
	}

}
