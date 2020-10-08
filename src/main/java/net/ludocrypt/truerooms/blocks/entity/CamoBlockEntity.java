package net.ludocrypt.truerooms.blocks.entity;

import net.ludocrypt.truerooms.SecretRooms;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class CamoBlockEntity extends BlockEntity {

	public Identifier idNorth;
	public Identifier idEast;
	public Identifier idSouth;
	public Identifier idWest;
	public Identifier idTop;
	public Identifier idBottom;

	public CamoBlockEntity() {
		super(SecretRooms.CAMO_BLOCK_ENTITY);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		return serializeCarving(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		deserializeCarving(tag);
	}

	public CompoundTag serializeCarving(CompoundTag tag) {
		tag.putString("idNorth", idNorth.toString());
		return tag;
	}

	public void deserializeCarving(CompoundTag tag) {
		if (tag.contains("idNorth", 8)) {
			idNorth = new Identifier(tag.getString("idNorth"));
		}
	}
	
	public void setId(Direction dir, Identifier newValue) {
		switch (dir) {
		case NORTH:
			idNorth = newValue;
			break;
		case EAST:
			idEast = newValue;
			break;
		case SOUTH:
			idSouth = newValue;
			break;
		case WEST:
			idWest = newValue;
			break;
		case UP:
			idTop = newValue;
			break;
		case DOWN:
			idBottom = newValue;
			break;
		}
	}
	
	public void setId(Identifier newValue) {
		idNorth = newValue;
		idEast = newValue;
		idSouth = newValue;
		idWest = newValue;
		idTop = newValue;
		idBottom = newValue;
	}

}
