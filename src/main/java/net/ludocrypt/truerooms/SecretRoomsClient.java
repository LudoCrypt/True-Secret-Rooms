package net.ludocrypt.truerooms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.ludocrypt.truerooms.render.CamoBlockResourceProvider;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class SecretRoomsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new CamoBlockResourceProvider());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), SecretRooms.camoBlocksList);

	}

}
