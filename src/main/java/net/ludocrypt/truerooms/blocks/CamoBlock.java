package net.ludocrypt.truerooms.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.ludocrypt.truerooms.SecretRoomsClient;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
		if (world.isClient) {
			MinecraftClient client = MinecraftClient.getInstance();
			SecretRoomsClient.sendHitSetter(pos, (BlockHitResult) client.crosshairTarget, false);
		}
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
