package net.ludocrypt.truerooms.items;

import net.ludocrypt.truerooms.blocks.GhostBlock;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StaffOfCamo extends Item {

	public StaffOfCamo(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {

		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		ItemStack itemStack = context.getPlayer().getActiveItem();

		if (block != Blocks.AIR && blockState.isFullCube(world, blockPos)) {
			CompoundTag tag = new CompoundTag();
			tag.put("chosenState", NbtHelper.fromBlockState(blockState));
			if (!tag.isEmpty()) {
				itemStack.putSubTag("BlockEntityTag", tag);
			}
			return ActionResult.SUCCESS;
		} else if (block instanceof GhostBlock) {
			if (world.getBlockEntity(blockPos) instanceof CamoBlockEntity) {
				CamoBlockEntity blockEntityAdjacent = (CamoBlockEntity) world.getBlockEntity(blockPos);
				CompoundTag tag = new CompoundTag();
				tag.put("chosenState", NbtHelper.fromBlockState(blockEntityAdjacent.getState(context.getSide())));
				if (!tag.isEmpty()) {
					itemStack.putSubTag("BlockEntityTag", tag);
				}
				return ActionResult.SUCCESS;
			} else {
				return ActionResult.FAIL;
			}
		} else {
			return ActionResult.FAIL;
		}

	}

}
