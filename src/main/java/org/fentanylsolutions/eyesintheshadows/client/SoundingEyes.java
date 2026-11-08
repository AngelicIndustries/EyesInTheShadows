package org.fentanylsolutions.eyesintheshadows.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;

import org.fentanylsolutions.eyesintheshadows.Config;
import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.entity.entities.EntityEyes;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * The sounds the eyes are making right now, so glitched eyes glitch only while they whisper, giggle or go, and an
 * eye's whispers and giggles stop when it dies (only its death sound plays on). The server plays an eye's sounds where
 * the eye is, at full volume; each one the client starts is scaled by this player's volume settings and matched to the
 * eye nearest to it.
 */
public final class SoundingEyes {

    /** How far from an eye its sound may start (the eye moves a little before the sound arrives). */
    private static final double REACH = 2;

    private static final Map<EntityEyes, List<ISound>> SOUNDS = new WeakHashMap<>();

    /** Whether a sound the eyes started is still playing. */
    public static boolean isSounding(EntityEyes eyes) {
        List<ISound> sounds = SOUNDS.get(eyes);
        if (sounds == null) return false;
        SoundHandler handler = Minecraft.getMinecraft()
            .getSoundHandler();
        for (ISound sound : sounds) if (handler.isSoundPlaying(sound)) return true;
        return false;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlaySound(PlaySoundEvent17 event) {
        ISound sound = event.result;
        Minecraft mc = Minecraft.getMinecraft();
        if (sound == null || mc.theWorld == null
            || !EyesInTheShadows.MODID.equals(
                sound.getPositionedSoundLocation()
                    .getResourceDomain())) {
            return;
        }
        sound = event.result = scaled(sound);
        EntityEyes nearest = null;
        double best = REACH * REACH;
        for (Object entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityEyes)) continue;
            EntityEyes eyes = (EntityEyes) entity;
            double d = eyes.getDistanceSq(sound.getXPosF(), sound.getYPosF(), sound.getZPosF());
            if (d <= best) {
                best = d;
                nearest = eyes;
            }
        }
        if (nearest == null) return;
        List<ISound> sounds = SOUNDS.get(nearest);
        if (sounds == null) SOUNDS.put(nearest, sounds = new ArrayList<>());
        SoundHandler handler = mc.getSoundHandler();
        for (Iterator<ISound> it = sounds.iterator(); it.hasNext();) {
            if (!handler.isSoundPlaying(it.next())) it.remove();
        }
        sounds.add(sound);
    }

    /** A dying or vanished eye's whispers and giggles stop; its death sound plays on. */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || SOUNDS.isEmpty()) return;
        SoundHandler handler = Minecraft.getMinecraft()
            .getSoundHandler();
        for (Iterator<Map.Entry<EntityEyes, List<ISound>>> it = SOUNDS.entrySet()
            .iterator(); it.hasNext();) {
            Map.Entry<EntityEyes, List<ISound>> entry = it.next();
            EntityEyes eyes = entry.getKey();
            if (eyes.isEntityAlive()) continue;
            for (ISound sound : entry.getValue()) {
                if (!isDeathSound(sound)) handler.stopSound(sound);
            }
            entry.getValue()
                .removeIf(sound -> !isDeathSound(sound));
            if (eyes.isDead && entry.getValue()
                .isEmpty()) it.remove();
        }
    }

    /** The sound at this player's volume for its kind: vanishing or idle. The jumpscare sets its own. */
    private static ISound scaled(ISound sound) {
        String path = sound.getPositionedSoundLocation()
            .getResourcePath();
        float volume;
        if (path.endsWith(".disappear")) volume = Config.eyeDisappearVolume;
        else if (path.endsWith(".laugh") || path.endsWith(".whisper")) volume = Config.eyeIdleVolume;
        else return sound;
        if (volume == 1.0F) return sound;
        return new PositionedSoundRecord(
            sound.getPositionedSoundLocation(),
            sound.getVolume() * volume,
            sound.getPitch(),
            sound.getXPosF(),
            sound.getYPosF(),
            sound.getZPosF());
    }

    private static boolean isDeathSound(ISound sound) {
        return sound.getPositionedSoundLocation()
            .getResourcePath()
            .endsWith(".disappear");
    }
}
