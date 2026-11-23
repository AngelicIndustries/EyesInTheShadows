package org.fentanylsolutions.eyesintheshadows.client;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import org.fentanylsolutions.eyesintheshadows.block.BlockEyeJar;
import org.lwjgl.opengl.GL11;

/** Twilight Forest's firefly jar: a glass body and a cork, drawn as two boxes. */
public final class JarModel {

    private JarModel() {}

    /** In the world, as part of its chunk. */
    public static void renderInWorld(RenderBlocks renderer, BlockEyeJar block, int x, int y, int z) {
        renderer.setRenderBounds(0.1875, 0.0, 0.1875, 0.8125, 0.875, 0.8125);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.setOverrideBlockTexture(block.cork);
        renderer.setRenderBounds(0.25, 0.75, 0.25, 0.75, 1.0, 0.75);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.clearOverrideBlockTexture();
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
    }

    /** On its own, filling the unit cube from the origin, lit as items are. */
    public static void renderAlone(RenderBlocks renderer, BlockEyeJar block) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        renderer.setRenderBounds(0.1875, 0.0, 0.1875, 0.8125, 0.875, 0.8125);
        box(renderer, block, false);
        renderer.setRenderBounds(0.25, 0.75, 0.25, 0.75, 1.0, 0.75);
        box(renderer, block, true);
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        GL11.glPopAttrib();
    }

    private static void box(RenderBlocks renderer, BlockEyeJar block, boolean cork) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, -1, 0);
        renderer.renderFaceYNeg(block, 0, 0, 0, cork ? block.cork : block.getIcon(0, 0));
        tessellator.setNormal(0, 1, 0);
        renderer.renderFaceYPos(block, 0, 0, 0, cork ? block.cork : block.getIcon(1, 0));
        tessellator.setNormal(0, 0, -1);
        renderer.renderFaceZNeg(block, 0, 0, 0, cork ? block.cork : block.getIcon(2, 0));
        tessellator.setNormal(0, 0, 1);
        renderer.renderFaceZPos(block, 0, 0, 0, cork ? block.cork : block.getIcon(3, 0));
        tessellator.setNormal(-1, 0, 0);
        renderer.renderFaceXNeg(block, 0, 0, 0, cork ? block.cork : block.getIcon(4, 0));
        tessellator.setNormal(1, 0, 0);
        renderer.renderFaceXPos(block, 0, 0, 0, cork ? block.cork : block.getIcon(5, 0));
        tessellator.draw();
    }
}
