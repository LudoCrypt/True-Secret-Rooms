package net.ludocrypt.truerooms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.ludocrypt.truerooms.render.CamoBlockUnbakedModel;
import net.minecraft.client.render.block.BlockModels;

@Environment(EnvType.CLIENT)
public class SecretRoomsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		ModelLoadingRegistry.INSTANCE.registerVariantProvider(rm -> (modelId, context) -> {
			if (modelId.equals(BlockModels.getModelId(SecretRooms.GHOST_BLOCK.getDefaultState()))
					|| modelId.equals(BlockModels.getModelId(SecretRooms.GHOST_BLOCK.getDefaultState()))) {
				return new CamoBlockUnbakedModel();
			}
			return null;
		});

	}

}
