package net.ludocrypt.truerooms.blocks;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.ludocrypt.truerooms.SecretRooms;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
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
		if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {

			CamoBlockEntity blockEntity = (CamoBlockEntity) world.getBlockEntity(pos);

			if (world.isClient) {

				MinecraftClient.getInstance();

				BlockHitResult hit = (BlockHitResult) MinecraftClient.getInstance().crosshairTarget;

				// Client side, send packet
				PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
				data.writeBlockHitResult(hit);
				data.writeBlockPos(pos);

				ClientSidePacketRegistry.INSTANCE.sendToServer(SecretRooms.id("blockhitresult"), data);

				BlockPos blockPos = hit.getBlockPos();
				BlockState blockState = world.getBlockState(blockPos);
				Block block = blockState.getBlock();

				if (block instanceof CamoBlock) {
					if (world.getBlockEntity(blockPos) instanceof CamoBlockEntity) {
						CamoBlockEntity blockEntityAdjacent = (CamoBlockEntity) world.getBlockEntity(blockPos);
						blockEntity.setState(blockEntityAdjacent.getState(hit.getSide()));
					} else {
						blockEntity.setState(Blocks.STONE.getDefaultState());
					}
				} else if (block != Blocks.AIR && blockState.isFullCube(world, blockPos)) {
					blockEntity.setState(blockState);
				} else {
					blockEntity.setState(Blocks.STONE.getDefaultState());
				}
			}
			super.onPlaced(world, pos, state, placer, itemStack);
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
