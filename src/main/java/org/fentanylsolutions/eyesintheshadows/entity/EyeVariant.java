package org.fentanylsolutions.eyesintheshadows.entity;

import net.minecraft.world.World;

import org.fentanylsolutions.eyesintheshadows.Config;
import org.fentanylsolutions.eyesintheshadows.util.Util;

/** Which eyes these are, decided where they spawn and kept wherever they go. */
public enum EyeVariant {

    /** Amber, the eyes of the dark. */
    NORMAL,
    /** White and glitched (Config.glitchedDimensions). */
    LIMBO,
    /** Soul-fire cyan (Config.netherEyes, in Config.netherEyeDimensions). */
    NETHER;

    public static EyeVariant byId(int id) {
        return id >= 0 && id < values().length ? values()[id] : NORMAL;
    }

    /** The variant of eyes spawning in this world. */
    public static EyeVariant of(World world) {
        if (Config.netherEyes && Util.isDimensionListed(world, Config.netherEyeDimensions)) return NETHER;
        if (Util.isDimensionListed(world, Config.glitchedDimensions)) return LIMBO;
        return NORMAL;
    }
}
