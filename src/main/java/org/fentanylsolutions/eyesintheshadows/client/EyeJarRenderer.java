package org.fentanylsolutions.eyesintheshadows.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.fentanylsolutions.eyesintheshadows.Config;
import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.block.BlockEyeJar;
import org.fentanylsolutions.eyesintheshadows.block.TileEntityEyeJar;
import org.fentanylsolutions.eyesintheshadows.entity.EyeVariant;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * Draws jars of eyes: set down (the jar with its chunk, the eyes as a tile entity), and as items, empty or full. Set
 * down, the eyes follow the light rules of free eyes; as items they always show.
 */
public class EyeJarRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    private static final float EYES_SCALE = 1.1F;
    /** In an inventory slot the jar is a few pixels across: larger eyes, or they're specks. */
    private static final float EYES_SCALE_GUI = 1.8F;

    /* The jar, set down */

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        JarModel.renderInWorld(renderer, (BlockEyeJar) block, x, y, z);
        return true;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        JarModel.renderAlone(renderer, (BlockEyeJar) block);
        GL11.glPopMatrix();
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return BlockEyeJar.renderId;
    }

    /* The eyes in a jar that's set down */

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityEyeJar jar)) return;
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.42, z + 0.5);
        ModelEyes.renderJarEyes(
            jar.getVariant(),
            jar.getVisibility(),
            jar.age + partialTicks,
            jar.blinking,
            jar.blinkProgress,
            jar.isSounding(),
            (long) jar.xCoord * 31 + jar.yCoord * 17L + jar.zCoord,
            EYES_SCALE,
            false,
            true);
        GL11.glPopMatrix();
    }

    /* Jars as items */

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY
            || type == ItemRenderType.EQUIPPED
            || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
        return helper == ItemRendererHelper.INVENTORY_BLOCK || helper == ItemRendererHelper.ENTITY_ROTATION
            || helper == ItemRendererHelper.ENTITY_BOBBING;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        Minecraft mc = Minecraft.getMinecraft();
        RenderBlocks renderer = (RenderBlocks) data[0];
        boolean centered = type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY;
        GL11.glPushMatrix();
        if (centered) GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        mc.getTextureManager()
            .bindTexture(TextureMap.locationBlocksTexture);
        JarModel.renderAlone(renderer, EyesInTheShadows.eyesJar);
        if (stack.getItem() == EyesInTheShadows.eyesJarItem && mc.theWorld != null) {
            GL11.glTranslatef(0.5F, 0.42F, 0.5F);
            float age = mc.theWorld.getTotalWorldTime();
            long seed = System.identityHashCode(stack);
            int blinkPhase = (int) ((age + seed % 400) % 400);
            boolean blinking = Config.blinkDuration > 1 && blinkPhase < Config.blinkDuration;
            boolean sounding = MathHelper.abs((float) Math.sin((age / 30 + seed) * 1.7)) > 0.85F;
            ModelEyes.renderJarEyes(
                EyeVariant.byId(stack.getItemDamage()),
                1,
                age,
                blinking,
                blinkPhase,
                sounding,
                seed,
                type == ItemRenderType.INVENTORY ? EYES_SCALE_GUI : EYES_SCALE,
                type == ItemRenderType.INVENTORY,
                false);
        }
        GL11.glPopMatrix();
    }

}
