package org.fentanylsolutions.eyesintheshadows.mixins.early.minecraft;

import java.util.List;

import net.minecraft.client.shader.ShaderGroup;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShaderGroup.class)
public interface AccessorShaderGroup {

    @Accessor("listShaders")
    List<?> getListShaders();
}
