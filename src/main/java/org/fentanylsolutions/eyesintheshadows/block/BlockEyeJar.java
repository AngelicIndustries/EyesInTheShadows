package org.fentanylsolutions.eyesintheshadows.block;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.fentanylsolutions.eyesintheshadows.Config;
import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.entity.EyeVariant;

/**
 * Eyes in a jar, set down: Twilight Forest's firefly jar with captured eyes inside. The metadata is the eyes' variant.
 */
public class BlockEyeJar extends BlockContainer {

    /** The jar's block renderer, set by the client proxy. */
    public static int renderId = -1;

    private IIcon side;
    private IIcon top;
    public IIcon cork;

    public BlockEyeJar() {
        super(Material.glass);
        setBlockName(EyesInTheShadows.MODID + ".eyes_jar");
        setHardness(0.3F);
        setStepSound(soundTypeWood);
        setBlockBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityEyeJar();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return renderId;
    }

    /** The eyes glow faintly (Config.jarLightLevel); Supernova colors the light by variant. */
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return Config.jarLightLevel;
    }

    @Override
    public int getLightValue() {
        return Config.jarLightLevel;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (EyeVariant variant : EyeVariant.values()) list.add(new ItemStack(item, 1, variant.ordinal()));
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        side = register.registerIcon(EyesInTheShadows.MODID + ":jar_side");
        top = register.registerIcon(EyesInTheShadows.MODID + ":jar_top");
        cork = register.registerIcon(EyesInTheShadows.MODID + ":jar_cork");
    }

    @Override
    public IIcon getIcon(int face, int meta) {
        return face < 2 ? top : side;
    }
}
