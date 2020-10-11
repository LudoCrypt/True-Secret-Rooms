package net.ludocrypt.truerooms;

import java.util.Map;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.ludocrypt.truerooms.blocks.GhostBlock;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class SecretRooms implements ModInitializer {

	public static BlockEntityType<CamoBlockEntity> CAMO_BLOCK_ENTITY;

	public static Block GHOST_BLOCK = new GhostBlock();
	
	public static Map<Identifier, Map<Direction, Sprite>> mapIdentifier;

	public static final ItemGroup SECRET_BLOCKS_GROUP = FabricItemGroupBuilder.build(id("secret_blocks"),
			() -> new ItemStack(Items.KNOWLEDGE_BOOK));

	public static final Block[] camoBlocksList = { GHOST_BLOCK };

	@Override
	public void onInitialize() {
		CAMO_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("camo_block_entity_type"),
				BlockEntityType.Builder.create(CamoBlockEntity::new, camoBlocksList).build(null));

		Registry.register(Registry.BLOCK, id("ghost_block"), GHOST_BLOCK);

		Registry.register(Registry.ITEM, id("ghost_block"),
				new BlockItem(GHOST_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));
	}

	public static Identifier id(String id) {
		return new Identifier("truerooms", id);
	}
}
