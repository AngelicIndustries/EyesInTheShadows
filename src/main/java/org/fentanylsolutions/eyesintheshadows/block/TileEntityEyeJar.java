package org.fentanylsolutions.eyesintheshadows.block;

import java.util.Random;

import net.minecraft.tileentity.TileEntity;

import org.fentanylsolutions.eyesintheshadows.Config;
import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.entity.EyeVariant;
import org.fentanylsolutions.eyesintheshadows.util.Util;

/**
 * The eyes inside a jar. All of it is the client's: they blink, and in the dark they giggle now and then, muffled by
 * the glass (glitched eyes whisper and glitch while they do).
 */
public class TileEntityEyeJar extends TileEntity {

    private final Random random = new Random();

    public int age;
    public boolean blinking;
    public float blinkProgress;
    /** The age until which the eyes are making a sound. */
    public int soundingUntil;
    private int nextSound = 40 + random.nextInt(200);

    public EyeVariant getVariant() {
        return EyeVariant.byId(getBlockMetadata());
    }

    public boolean isSounding() {
        return age < soundingUntil;
    }

    /** How visible the eyes are, by the light around the jar but not its own glow. */
    public float getVisibility() {
        return Util.getJarEyesAlpha(worldObj, xCoord, yCoord, zCoord, Config.jarLightLevel);
    }

    @Override
    public void updateEntity() {
        if (worldObj == null || !worldObj.isRemote) return;
        age++;
        if (Config.blinkDuration > 1) {
            if (!blinking) {
                if (random.nextFloat() < Config.blinkChance / 10) {
                    blinking = true;
                    blinkProgress = 0;
                }
            } else if (++blinkProgress >= Config.blinkDuration) {
                blinking = false;
            }
        }
        if (age >= nextSound) {
            nextSound = age + Config.idleSoundInterval + random.nextInt(Config.idleSoundInterval * 2);
            if (getVisibility() > 0) {
                boolean glitched = getVariant() == EyeVariant.LIMBO;
                worldObj.playSound(
                    xCoord + 0.5,
                    yCoord + 0.5,
                    zCoord + 0.5,
                    EyesInTheShadows.MODID + (glitched ? ":jar.glitched" : ":jar.laugh"),
                    0.3F,
                    0.9F + random.nextFloat() * 0.2F,
                    false);
                soundingUntil = age + (glitched ? 50 : 25);
            }
        }
    }
}
