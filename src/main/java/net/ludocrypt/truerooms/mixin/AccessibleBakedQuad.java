package net.ludocrypt.truerooms.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;

@Mixin(BakedQuad.class)
public interface AccessibleBakedQuad {
    /**
     * Get this {@link BakedQuad}'s sprite.
     * @return this quad's sprite
     */
    @Accessor("sprite")
    Sprite getSprite();
}