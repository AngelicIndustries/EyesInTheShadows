package org.fentanylsolutions.eyesintheshadows.client;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderManager;
import net.minecraft.util.ResourceLocation;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.mixins.early.minecraft.AccessorShaderGroup;
import org.lwjgl.opengl.GL11;

/**
 * A jumpscare where the eyes are glitched breaks the whole screen up in white glitches: torn bands, jumping blocks,
 * split colors, negatives, static and flickering white edges (shaders/post/jumpscare_glitch.json). Drawn over
 * everything, the jumpscare included, so over any shader pack too.
 */
public final class JumpscareGlitch {

    private static final ResourceLocation SHADER = new ResourceLocation(
        EyesInTheShadows.MODID,
        "shaders/post/jumpscare_glitch.json");
    private static final long STARTED = System.nanoTime();

    private static ShaderGroup group;
    private static int width, height;
    private static boolean failed;

    private JumpscareGlitch() {}

    /** Draws the glitch at an intensity from 0 to 1 over what is on the screen now. */
    static void render(float intensity, float partialTicks) {
        if (intensity <= 0.001F || failed || !OpenGlHelper.shadersSupported) return;
        Minecraft mc = Minecraft.getMinecraft();
        try {
            if (group == null) {
                group = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), SHADER);
                width = -1;
            }
            if (width != mc.displayWidth || height != mc.displayHeight) {
                width = mc.displayWidth;
                height = mc.displayHeight;
                group.createBindFramebuffers(width, height);
            }
        } catch (IOException | RuntimeException e) {
            failed = true;
            EyesInTheShadows.LOG.warn("Couldn't load the jumpscare glitch: {}", e.toString());
            return;
        }
        float clock = (float) ((System.nanoTime() - STARTED) / 1e9 % 1000);
        for (Object shader : ((AccessorShaderGroup) group).getListShaders()) {
            ShaderManager manager = ((Shader) shader).getShaderManager();
            manager.func_147984_b("Intensity")
                .func_148090_a(intensity);
            manager.func_147984_b("Clock")
                .func_148090_a(clock);
        }
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        group.loadShaderGroup(partialTicks);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        mc.getFramebuffer()
            .bindFramebuffer(true);
    }
}
