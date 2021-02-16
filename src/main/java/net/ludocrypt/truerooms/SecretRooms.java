package net.ludocrypt.truerooms;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.ludocrypt.truerooms.blocks.*;
import net.ludocrypt.truerooms.blocks.entity.CamoBlockEntity;
import net.ludocrypt.truerooms.items.StaffOfCamo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Collection;

@SuppressWarnings("resource")
public class SecretRooms implements ModInitializer {

    public static BlockEntityType<CamoBlockEntity> CAMO_BLOCK_ENTITY;

    public static Block GHOST_BLOCK = new GhostBlock();
    public static Block SOLID_BLOCK = new SolidBlock();

    public static Block ONE_WAY_GLASS = new OneWayGlassBlock();

    public static Block STAIR_BLOCK = new StairBlock(SOLID_BLOCK.getDefaultState());
    public static Block SLAB_BLOCK = new SlabBlock();

    public static Block DOOR_BLOCK = new DoorBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).hardness(1).resistance(2));
    public static Block IRON_DOOR_BLOCK = new DoorBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.STONE).hardness(1).resistance(2));

    public static Block HINGE_GATE_BLOCK = new HingeGateBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).hardness(1).resistance(2));
    public static Block IRON_HINGE_GATE_BLOCK = new HingeGateBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.STONE).hardness(1).resistance(2));

    public static Block TRAPDOOR_BLOCK = new TrapdoorBlock(FabricBlockSettings.of(Material.STONE).sounds(BlockSoundGroup.STONE).hardness(1).resistance(2));
    public static Block IRON_TRAPDOOR_BLOCK = new TrapdoorBlock(FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.STONE).hardness(1).resistance(2));

    public static final Block[] camoBlocksList = {GHOST_BLOCK, SOLID_BLOCK, STAIR_BLOCK, SLAB_BLOCK, DOOR_BLOCK, IRON_DOOR_BLOCK, TRAPDOOR_BLOCK, IRON_TRAPDOOR_BLOCK, HINGE_GATE_BLOCK, IRON_HINGE_GATE_BLOCK, ONE_WAY_GLASS};

    public static final ItemGroup SECRET_BLOCKS_GROUP = FabricItemGroupBuilder.create(id("secret_blocks")).icon(() -> new ItemStack(SecretRooms.CAMOUFLAGE_PASTE)).build();

    public static Item CAMOLOCK_EYE = new Item(new Item.Settings().group(SECRET_BLOCKS_GROUP).rarity(Rarity.UNCOMMON));

    public static Item STAFF_OF_CAMO = new StaffOfCamo(new Item.Settings().group(SECRET_BLOCKS_GROUP).rarity(Rarity.RARE).maxCount(1));

    public static Item STAFF_OF_CAMO_ROTATION_MODE = new StaffOfCamo(new Item.Settings().group(SECRET_BLOCKS_GROUP).rarity(Rarity.RARE).maxCount(1));

    public static Item CAMOUFLAGE_PASTE = new Item(new Item.Settings().group(SECRET_BLOCKS_GROUP));

    @Override
    public void onInitialize() {
        CAMO_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("camo_block_entity_type"), BlockEntityType.Builder.create(CamoBlockEntity::new, camoBlocksList).build(null));

        Registry.register(Registry.BLOCK, id("ghost_block"), GHOST_BLOCK);
        Registry.register(Registry.BLOCK, id("solid_block"), SOLID_BLOCK);

        Registry.register(Registry.BLOCK, id("one_way_glass"), ONE_WAY_GLASS);

        Registry.register(Registry.BLOCK, id("stair_block"), STAIR_BLOCK);
        Registry.register(Registry.BLOCK, id("slab_block"), SLAB_BLOCK);

        Registry.register(Registry.BLOCK, id("door_block"), DOOR_BLOCK);
        Registry.register(Registry.BLOCK, id("iron_door_block"), IRON_DOOR_BLOCK);

        Registry.register(Registry.BLOCK, id("hinge_gate_block"), HINGE_GATE_BLOCK);
        Registry.register(Registry.BLOCK, id("iron_hinge_gate_block"), IRON_HINGE_GATE_BLOCK);

        Registry.register(Registry.BLOCK, id("trapdoor_block"), TRAPDOOR_BLOCK);
        Registry.register(Registry.BLOCK, id("iron_trapdoor_block"), IRON_TRAPDOOR_BLOCK);

        Registry.register(Registry.ITEM, id("ghost_block"), new BlockItem(GHOST_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));
        Registry.register(Registry.ITEM, id("solid_block"), new BlockItem(SOLID_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));

        Registry.register(Registry.ITEM, id("one_way_glass"), new BlockItem(ONE_WAY_GLASS, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));

        Registry.register(Registry.ITEM, id("stair_block"), new BlockItem(STAIR_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));
        Registry.register(Registry.ITEM, id("slab_block"), new BlockItem(SLAB_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));

        Registry.register(Registry.ITEM, id("door_block"), new BlockItem(DOOR_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));
        Registry.register(Registry.ITEM, id("iron_door_block"), new BlockItem(IRON_DOOR_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));

        Registry.register(Registry.ITEM, id("hinge_gate_block"), new BlockItem(HINGE_GATE_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));
        Registry.register(Registry.ITEM, id("iron_hinge_gate_block"), new BlockItem(IRON_HINGE_GATE_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));

        Registry.register(Registry.ITEM, id("trapdoor_block"), new BlockItem(TRAPDOOR_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));
        Registry.register(Registry.ITEM, id("iron_trapdoor_block"), new BlockItem(IRON_TRAPDOOR_BLOCK, new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));

        Registry.register(Registry.ITEM, id("camouflage_paste"), CAMOUFLAGE_PASTE);

        Registry.register(Registry.ITEM, id("camolock_eye"), CAMOLOCK_EYE);

        Registry.register(Registry.ITEM, id("staff_of_camo"), STAFF_OF_CAMO);

        Registry.register(Registry.ITEM, id("staff_of_camo_rotation_mode"), STAFF_OF_CAMO_ROTATION_MODE);

        ServerPlayNetworking.registerGlobalReceiver(SecretRooms.id("hit_setter"), (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            BlockHitResult hit = buf.readBlockHitResult();
            boolean glass = buf.readBoolean();
            World world = player.world;
            Direction facing = player.getHorizontalFacing().getOpposite();
            server.execute(() -> {

                if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {

                    CamoBlockEntity blockEntity = (CamoBlockEntity) world.getBlockEntity(pos);

                    if (hit.getType() != HitResult.Type.MISS) {

                        BlockPos blockPos = hit.getBlockPos();
                        BlockState blockState = world.getBlockState(blockPos);
                        Block block = blockState.getBlock();

                        if (block instanceof CamoBlock) {
                            if (world.getBlockEntity(blockPos) instanceof CamoBlockEntity) {
                                CamoBlockEntity blockEntityAdjacent = (CamoBlockEntity) world.getBlockEntity(blockPos);
                                for (Direction dir : Direction.values()) {
                                    if (glass) {
                                        SecretRooms.updateSide(facing != dir ? blockEntityAdjacent.getState(hit.getSide()) : Blocks.GLASS.getDefaultState(), dir, pos, blockEntity);
                                    } else {
                                        SecretRooms.updateSide(blockEntityAdjacent.getState(hit.getSide()), dir, pos, blockEntity);
                                    }
                                }
                            } else {
                                for (Direction dir : Direction.values()) {
                                    if (glass) {
                                        SecretRooms.updateSide(facing != dir ? Blocks.STONE.getDefaultState() : Blocks.GLASS.getDefaultState(), dir, pos, blockEntity);
                                    } else {
                                        SecretRooms.updateSide(Blocks.STONE.getDefaultState(), dir, pos, blockEntity);
                                    }
                                }
                            }
                        } else if (block != Blocks.AIR && blockState.isFullCube(world, blockPos)) {
                            for (Direction dir : Direction.values()) {
                                if (glass) {
                                    SecretRooms.updateSide(facing != dir ? blockState : Blocks.GLASS.getDefaultState(), dir, pos, blockEntity);
                                } else {
                                    SecretRooms.updateSide(blockState, dir, pos, blockEntity);
                                }
                            }
                        } else {
                            for (Direction dir : Direction.values()) {
                                if (glass) {
                                    SecretRooms.updateSide(facing != dir ? Blocks.STONE.getDefaultState() : Blocks.GLASS.getDefaultState(), dir, pos, blockEntity);
                                } else {
                                    SecretRooms.updateSide(Blocks.STONE.getDefaultState(), dir, pos, blockEntity);
                                }
                            }
                        }
                    }

                    blockEntity.refresh();
                }
            });
        });

    }

    public static void updateSide(BlockState state, Direction dir, BlockPos pos, CamoBlockEntity entity) {
        if (!entity.getWorld().isClient) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            CompoundTag tag = new CompoundTag();
            tag.put("state", NbtHelper.fromBlockState(state));
            passedData.writeCompoundTag(tag);
            passedData.writeEnumConstant(dir);
            passedData.writeBlockPos(pos);
            Collection<ServerPlayerEntity> watchingPlayers = PlayerLookup.world((ServerWorld) entity.getWorld());
            watchingPlayers.forEach(player -> ServerPlayNetworking.send(player, SecretRooms.id("update_side"), passedData));
        }
        entity.setState(dir, state);
    }

    public static void updateDirection(Direction faceDir, Direction dir, BlockPos pos, CamoBlockEntity entity) {
        if (!entity.getWorld().isClient) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeEnumConstant(faceDir);
            passedData.writeEnumConstant(dir);
            passedData.writeBlockPos(pos);
            Collection<ServerPlayerEntity> watchingPlayers = PlayerLookup.world((ServerWorld) entity.getWorld());
            watchingPlayers.forEach(player -> ServerPlayNetworking.send(player, SecretRooms.id("update_direction"), passedData));
        }
        entity.setDirection(dir, faceDir);
    }

    public static void updateRotation(int rotation, Direction dir, BlockPos pos, CamoBlockEntity entity) {
        if (!entity.getWorld().isClient) {
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeInt(rotation);
            passedData.writeEnumConstant(dir);
            passedData.writeBlockPos(pos);
            Collection<ServerPlayerEntity> watchingPlayers = PlayerLookup.world((ServerWorld) entity.getWorld());
            watchingPlayers.forEach(player -> ServerPlayNetworking.send(player, SecretRooms.id("update_rotation"), passedData));
        }
        entity.setRotation(dir, rotation);
    }

    public static Identifier id(String id) {
        return new Identifier("truerooms", id);
    }
}
