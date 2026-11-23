package org.fentanylsolutions.eyesintheshadows.modcompat;

import java.lang.reflect.Method;

import net.minecraft.block.Block;

import org.fentanylsolutions.eyesintheshadows.Config;
import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.entity.EyeVariant;

/** Colors a jar's light by its eyes with Supernova: amber, white for Limbo's, soul-fire cyan for the Nether's. */
public final class SupernovaCompat {

    private SupernovaCompat() {}

    public static void registerJarColors(Block jar) {
        try {
            Method register = Class.forName("com.mitchej123.supernova.api.LightColorRegistry")
                .getMethod("register", Block.class, int.class, int.class, int.class, int.class);
            // Supernova only scales a color up to the block's light, never down, so it's given at the jar's light
            register(register, jar, EyeVariant.NORMAL, 15, 11, 2);
            register(register, jar, EyeVariant.LIMBO, 12, 12, 15);
            register(register, jar, EyeVariant.NETHER, 4, 13, 15);
        } catch (ReflectiveOperationException | LinkageError e) {
            EyesInTheShadows.LOG.warn("Couldn't color the eye jars' light with Supernova", e);
        }
    }

    /** A color at full brightness (0 to 15 a channel), dimmed to the jar's light level. */
    private static void register(Method register, Block jar, EyeVariant variant, int r, int g, int b)
        throws ReflectiveOperationException {
        int level = Config.jarLightLevel;
        register.invoke(
            null,
            jar,
            variant.ordinal(),
            Math.round(r * level / 15.0F),
            Math.round(g * level / 15.0F),
            Math.round(b * level / 15.0F));
    }
}
