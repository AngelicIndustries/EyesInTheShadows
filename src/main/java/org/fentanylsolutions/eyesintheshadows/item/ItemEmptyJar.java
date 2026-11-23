package org.fentanylsolutions.eyesintheshadows.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;

/** An empty jar, to catch eyes in while invisible. */
public class ItemEmptyJar extends Item {

    public ItemEmptyJar() {
        setUnlocalizedName(EyesInTheShadows.MODID + ".empty_jar");
        setTextureName(EyesInTheShadows.MODID + ":empty_jar");
        setCreativeTab(CreativeTabs.tabMisc);
        setMaxStackSize(16);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        JarHelper.capture(stack, world, player);
        // The catch may have turned the held jar into a full one
        ItemStack held = player.inventory.getCurrentItem();
        return held != null ? held : stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        return JarHelper.capture(stack, world, player);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advanced) {
        lines.add(StatCollector.translateToLocal("item." + EyesInTheShadows.MODID + ".empty_jar.tooltip"));
    }
}
