package net.ludocrypt.truerooms;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.ludocrypt.truerooms.render.CamoBlockResourceProvider;

@Environment(EnvType.CLIENT)
public class SecretRoomsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new CamoBlockResourceProvider());

	}

}
