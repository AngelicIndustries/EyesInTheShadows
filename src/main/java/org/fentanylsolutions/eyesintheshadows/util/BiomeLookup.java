package org.fentanylsolutions.eyesintheshadows.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;

/**
 * The biome at a block. Biomic stacks biomes in a column (caves under the surface, islands in the sky), so with it the
 * biome is read at the block itself; without it, it's the column's.
 */
public final class BiomeLookup {

    private static final MethodHandle BIOMIC = find();

    private BiomeLookup() {}

    private static MethodHandle find() {
        try {
            Class<?> api = Class.forName("industries.angelic.biomic.api.BiomicBiomeApi");
            MethodHandle handle = MethodHandles.publicLookup()
                .findStatic(
                    api,
                    "getBiome",
                    MethodType.methodType(BiomeGenBase.class, World.class, int.class, int.class, int.class));
            EyesInTheShadows.LOG.info("Biome lists read Biomic's biomes where the eyes are, cave biomes included");
            return handle;
        } catch (ReflectiveOperationException | LinkageError e) {
            return null;
        }
    }

    public static BiomeGenBase at(World world, int x, int y, int z) {
        if (BIOMIC != null) {
            try {
                BiomeGenBase biome = (BiomeGenBase) BIOMIC.invokeExact(world, x, y, z);
                if (biome != null) return biome;
            } catch (Throwable ignored) {
                // Fall back to the column's biome
            }
        }
        return world.getBiomeGenForCoords(x, z);
    }
}
