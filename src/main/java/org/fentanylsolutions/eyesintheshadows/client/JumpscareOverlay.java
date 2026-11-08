package org.fentanylsolutions.eyesintheshadows.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import org.fentanylsolutions.eyesintheshadows.Config;
import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.util.Util;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * The jumpscare: the screen darkens as the eyes open on it (textures/jumpscare/eyes_0.png on, as many frames as there
 * are), stare, then blink with the creature behind them (textures/jumpscare/creature.png), and after a moment of black
 * the creature fades. Each image is drawn at its own size, so a resource pack can swap them, and either can be turned
 * off (Config.jumpscareEyes, Config.jumpscareCreature). Config.jumpscareSeconds stretches the stare and the blinking.
 */
public class JumpscareOverlay extends Gui implements IResourceManagerReloadListener {

    private static final String FOLDER = EyesInTheShadows.MODID + ":textures/jumpscare/";
    private static final ResourceLocation CREATURE = new ResourceLocation(FOLDER + "creature.png");
    private static final int MAX_FRAMES = 64;

    public static JumpscareOverlay INSTANCE = new JumpscareOverlay();

    private boolean visible = false;
    /** Whether this jumpscare happens where the eyes are glitched (Config.glitchedDimensions). */
    private boolean glitched;
    private float progress = 0;

    private Minecraft mc;

    /** The eyes opening, the black and the fade, in ticks; the stare and blinking share the rest of the length. */
    private static final int ANIMATION_APPEAR = 10;
    private static final int ANIMATION_SCARE = 20;
    private static final int ANIMATION_FADE = 20;
    private static final float LINGER_SHARE = 0.6F;
    private float blinkStart, scareStart, total;

    /** The eyes' frames, and each image's size, read when first drawn after the resources load. */
    private List<ResourceLocation> frames;
    private final Map<ResourceLocation, int[]> sizes = new HashMap<>();

    private JumpscareOverlay() {
        mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
        ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(this);
    }

    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        frames = null;
        sizes.clear();
    }

    public void show(double ex, double ey, double ez) {
        visible = true;
        progress = 0;
        float length = (float) (Config.jumpscareSeconds * 20);
        float rest = Math.max(0, length - ANIMATION_APPEAR - ANIMATION_SCARE - ANIMATION_FADE);
        blinkStart = ANIMATION_APPEAR + rest * LINGER_SHARE;
        scareStart = ANIMATION_APPEAR + rest;
        total = scareStart + ANIMATION_SCARE + ANIMATION_FADE;
        glitched = mc.theWorld != null && Util.isDimensionListed(mc.theWorld, Config.glitchedDimensions);
        mc.getSoundHandler()
            .playSound(
                new JumpscareSound(
                    new ResourceLocation(
                        glitched ? EyesInTheShadows.varInstanceCommon.glitchedJumpScareSound
                            : EyesInTheShadows.varInstanceCommon.jumpScareSound),
                    Config.eyeJumpscareVolume));
    }

    /**
     * The jumpscare at the player's ears, at the configured volume. (PositionedSoundRecord.func_147674_a, the button
     * click's, plays at a quarter volume and takes the second argument as the pitch.)
     */
    private static final class JumpscareSound extends PositionedSound {

        JumpscareSound(ResourceLocation sound, float volume) {
            super(sound);
            this.volume = volume;
            this.field_147666_i = ISound.AttenuationType.NONE;
        }
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (visible) {
            progress++;
            if (progress >= total) {
                visible = false;
                progress = 0;
            }
        }
    }

    /**
     * No HUD during the jumpscare: cancelling the whole of it first skips vanilla's and every overlay drawn with it,
     * such
     * as Better Sleeping's.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void hideHud(RenderGameOverlayEvent.Pre event) {
        if (visible && event.type == RenderGameOverlayEvent.ElementType.ALL) {
            event.setCanceled(true);
        }
    }

    /** Drawn last in the frame, over anything else a mod draws on the screen. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void render(TickEvent.RenderTickEvent event) {
        if (!visible || event.phase != TickEvent.Phase.END || mc.theWorld == null) {
            return;
        }

        float time = progress + event.renderTickTime;
        if (time >= total) {
            visible = false;
            progress = 0;
            return;
        }

        drawOverlay(time);
        // Where the eyes are glitched, the whole screen breaks up, heaviest from the scare on
        if (glitched) {
            float envelope = MathHelper
                .clamp_float(Math.min(time / ANIMATION_APPEAR, (total - time) / ANIMATION_FADE), 0, 1);
            JumpscareGlitch.render(envelope * (time >= scareStart ? 1.0F : 0.75F), event.renderTickTime);
        }
    }

    private void drawOverlay(float time) {
        int screenWidth = mc.displayWidth;
        int screenHeight = mc.displayHeight;

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, screenWidth, screenHeight, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        try {
            float darkening = MathHelper
                .clamp_float(Math.min(time / ANIMATION_APPEAR, (total - time) / ANIMATION_FADE), 0, 1);

            boolean showCreature = false;
            boolean blink = false;
            if (time >= blinkStart) {
                if (time >= scareStart) {
                    // A moment of black, then the creature fading
                    blink = true;
                    showCreature = time - scareStart > ANIMATION_SCARE;
                } else {
                    // The eyes and the creature take turns, ever faster
                    float fade = Math.max(0, (time - blinkStart) / (scareStart - blinkStart));
                    float blinkspeed = (float) (1 + Math.pow(fade, 3));
                    blink = (MathHelper.floor_float(20 * blinkspeed) & 1) == 1;
                    showCreature = blink;
                }
            }

            int alpha = MathHelper.floor_double(darkening * 255);
            int[] creatureSize = showCreature && Config.jumpscareCreature ? size(CREATURE) : null;
            if (creatureSize != null) {
                // As tall as the screen
                float drawW = creatureSize[0] * screenHeight / (float) creatureSize[1];
                GL11.glEnable(GL11.GL_BLEND);
                drawTexture(CREATURE, (screenWidth - drawW) / 2, 0, drawW, screenHeight, (alpha << 24) | 0xFFFFFF);
            } else {
                drawRect(0, 0, screenWidth, screenHeight, alpha << 24);
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glEnable(GL11.GL_BLEND);
            }

            List<ResourceLocation> frames = frames();
            if (blink || !Config.jumpscareEyes || frames.isEmpty()) {
                return;
            }

            // One whole-pixel scale for every frame, the biggest that fits all within most of the screen, growing in
            float scale = Float.MAX_VALUE;
            for (ResourceLocation frame : frames) {
                int[] size = size(frame);
                scale = Math.min(
                    scale,
                    Math.min(
                        MathHelper.floor_double(screenWidth * 0.8 / size[0]),
                        MathHelper.floor_double(screenHeight * 0.8 / size[1])));
            }
            scale = Math.min(1, (1 + time) / (1 + ANIMATION_APPEAR)) * scale;

            ResourceLocation frame = frames
                .get(Math.min(frames.size() - 1, MathHelper.floor_float(frames.size() * time / ANIMATION_APPEAR)));
            int[] size = size(frame);
            float drawW = size[0] * scale;
            float drawH = size[1] * scale;
            drawTexture(frame, (screenWidth - drawW) / 2, (screenHeight - drawH) / 2, drawW, drawH, 0xFFFFFFFF);

            GL11.glDisable(GL11.GL_ALPHA_TEST);

        } finally {
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    /** The eyes' frames there are: eyes_0.png, eyes_1.png and on, until one is missing. */
    private List<ResourceLocation> frames() {
        if (frames == null) {
            frames = new ArrayList<>();
            for (int i = 0; i < MAX_FRAMES; i++) {
                ResourceLocation frame = new ResourceLocation(FOLDER + "eyes_" + i + ".png");
                if (size(frame) == null) break;
                frames.add(frame);
            }
        }
        return frames;
    }

    /** An image's width and height, or null when it is missing. */
    private int[] size(ResourceLocation image) {
        if (!sizes.containsKey(image)) {
            int[] size = null;
            try (InputStream in = mc.getResourceManager()
                .getResource(image)
                .getInputStream()) {
                BufferedImage read = ImageIO.read(in);
                if (read != null) size = new int[] { read.getWidth(), read.getHeight() };
            } catch (IOException e) {
                // Missing: not drawn
            }
            sizes.put(image, size);
        }
        return sizes.get(image);
    }

    /** A whole image, tinted and faded by the color (ARGB). */
    private void drawTexture(ResourceLocation image, float x, float y, float width, float height, int color) {
        mc.renderEngine.bindTexture(image);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(color >> 16 & 255, color >> 8 & 255, color & 255, color >>> 24);
        tessellator.addVertexWithUV(x, y, 0, 0, 0);
        tessellator.addVertexWithUV(x, y + height, 0, 0, 1);
        tessellator.addVertexWithUV(x + width, y + height, 0, 1, 1);
        tessellator.addVertexWithUV(x + width, y, 0, 1, 0);
        tessellator.draw();
        GL11.glPopAttrib();
    }
}
