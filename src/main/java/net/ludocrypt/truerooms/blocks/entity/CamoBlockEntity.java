package net.ludocrypt.truerooms.blocks.entity;

import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.ludocrypt.truerooms.SecretRooms;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;

public class CamoBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity {

	public BlockState upState = Blocks.STONE.getDefaultState();
	public BlockState downState = Blocks.STONE.getDefaultState();
	public BlockState northState = Blocks.STONE.getDefaultState();
	public BlockState eastState = Blocks.STONE.getDefaultState();
	public BlockState southState = Blocks.STONE.getDefaultState();
	public BlockState westState = Blocks.STONE.getDefaultState();

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
		deserializeCarving(state, tag);
	}

	public CompoundTag serializeCarving(CompoundTag tag) {
		for (int i = 0; i < 6; i++) {
			tag.put("blockState_" + i, NbtHelper.fromBlockState(getStates()[i]));
		}
		return tag;
	}

	public void deserializeCarving(BlockState state, CompoundTag tag) {
		for (int i = 0; i < 6; i++) {
			getStates()[i] = NbtHelper.toBlockState(tag.getCompound("blockState_" + i));
		}
	}

	public void setState(int space, BlockState newValue) {
		getStates()[space] = newValue;
	}

	public void setState(BlockState newValue) {
		for (int i = 0; i < 6; i++) {
			getStates()[i] = newValue;
		}
	}

	public BlockState getState(int space) {
		return getStates()[space];
	}

	public BlockState[] getStates() {
		BlockState[] states = { upState, downState, northState, eastState, southState, westState };
		return states;
	}

	@Override
	public Object getRenderAttachmentData() {
		return getStates();
	}

}
