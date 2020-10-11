package net.ludocrypt.truerooms.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.truerooms.blocks.GhostBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public class DrawSideMixin {

	@Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
	private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction facing,
			CallbackInfoReturnable<Boolean> ci) {
		BlockPos blockPos = pos.offset(facing);
		BlockState blockState = world.getBlockState(blockPos);
		if (!(state.getBlock() instanceof GhostBlock) && blockState.getBlock() instanceof GhostBlock) {
			ci.setReturnValue(true);
		}
	}

}
