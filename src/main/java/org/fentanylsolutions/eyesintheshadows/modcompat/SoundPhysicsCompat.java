package org.fentanylsolutions.eyesintheshadows.modcompat;

import java.lang.reflect.Method;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;

/** Muffles the eyes in a jar with Sound Physics, as heard through the glass, wherever their sounds play. */
public final class SoundPhysicsCompat {

    /** About a wall's worth: the highs nearly gone, the rest at a little over half. */
    private static final float JAR_MUFFLE = 0.9F;

    private SoundPhysicsCompat() {}

    public static void muffleJars() {
        try {
            Method muffle = Class.forName("com.sonicether.soundphysics.SoundPhysics")
                .getMethod("muffleSound", String.class, float.class);
            for (String event : new String[] { "jar.laugh", "jar.glitched", "jar.glitched_short" }) {
                muffle.invoke(null, EyesInTheShadows.MODID + ":" + event, JAR_MUFFLE);
            }
        } catch (ReflectiveOperationException | LinkageError e) {
            EyesInTheShadows.LOG.warn("Couldn't muffle the eye jars with Sound Physics", e);
        }
    }
}
