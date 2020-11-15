package net.ludocrypt.truerooms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.ludocrypt.truerooms.render.CamoBlockResourceProvider;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public class SecretRoomsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new CamoBlockResourceProvider());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), SecretRooms.camoBlocksList);

		ClientSidePacketRegistry.INSTANCE.register(SecretRooms.id("update_side"), (packetContext, attachedData) -> {
			CompoundTag tag = attachedData.readCompoundTag();
			Direction dir = attachedData.readEnumConstant(Direction.class);
			BlockPos pos = attachedData.readBlockPos();
			BlockState state = NbtHelper.toBlockState(tag.getCompound("state"));
			packetContext.getTaskQueue().execute(() -> {
				((CamoBlockEntity) packetContext.getPlayer().world.getBlockEntity(pos)).setState(dir, state);
			});
		});

		ClientSidePacketRegistry.INSTANCE.register(SecretRooms.id("update_direction"), (packetContext, attachedData) -> {
			Direction faceDir = attachedData.readEnumConstant(Direction.class);
			Direction dir = CamoBlockEntity.byName(attachedData.readString());
			BlockPos pos = attachedData.readBlockPos();
			packetContext.getTaskQueue().execute(() -> {
				((CamoBlockEntity) packetContext.getPlayer().world.getBlockEntity(pos)).setDirection(faceDir, dir);
			});
		});

		ClientSidePacketRegistry.INSTANCE.register(SecretRooms.id("update_rotation"), (packetContext, attachedData) -> {
			int rotation = attachedData.readInt();
			Direction dir = attachedData.readEnumConstant(Direction.class);
			BlockPos pos = attachedData.readBlockPos();
			packetContext.getTaskQueue().execute(() -> {
				((CamoBlockEntity) packetContext.getPlayer().world.getBlockEntity(pos)).setRotation(dir, rotation);
			});
		});

	}

}
