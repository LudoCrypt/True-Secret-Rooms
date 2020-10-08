package net.ludocrypt.truerooms.render;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class CamoBlockModelProvider implements ModelResourceProvider {

	public static final CamoBlockModel CAMO_BLOCK_MODEL = new CamoBlockModel();
	public static final Identifier CAMO_BLOCK_MODEL_BLOCK = new Identifier("truerooms:block/camo_block");
	public static final Identifier CAMO_BLOCK_MODEL_ITEM  = new Identifier("truerooms:item/camo_block");

	@Override
	public UnbakedModel loadModelResource(Identifier identifier, ModelProviderContext modelProviderContext)
			throws ModelProviderException {
		if (identifier.equals(CAMO_BLOCK_MODEL_BLOCK) || identifier.equals(CAMO_BLOCK_MODEL_ITEM)) {
			return CAMO_BLOCK_MODEL;
		} else {
			return null;
		}
	}

}
