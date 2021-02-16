package net.ludocrypt.truerooms.mixin;

import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Direction.class)
public interface DirectionAccessor {

    @Accessor("NAME_MAP")
	static Map<String, Direction> NAME_MAP() {
        throw new AssertionError();
    }

}
