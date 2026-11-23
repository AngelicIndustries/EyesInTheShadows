package org.fentanylsolutions.eyesintheshadows.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.entity.EyeVariant;

/** Eyes in a jar: use it to set the jar down, sneak and use it to let the eyes out. The damage is their variant. */
public class ItemEyeJar extends ItemBlock {

    public ItemEyeJar(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(16);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "."
            + EyeVariant.byId(stack.getItemDamage())
                .name()
                .toLowerCase();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        ForgeDirection face = ForgeDirection.getOrientation(side);
        JarHelper.release(
            stack,
            world,
            player,
            x + 0.5 + face.offsetX * 0.8,
            y + 0.2 + face.offsetY * 0.8,
            z + 0.5 + face.offsetZ * 0.8);
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            Vec3 look = player.getLookVec();
            double eyes = world.isRemote ? player.posY : player.posY + player.getEyeHeight();
            JarHelper.release(
                stack,
                world,
                player,
                player.posX + look.xCoord * 1.5,
                eyes + look.yCoord * 1.5 - 0.5,
                player.posZ + look.zCoord * 1.5);
            ItemStack held = player.inventory.getCurrentItem();
            return held != null ? held : stack;
        }
        return stack;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advanced) {
        lines.add(StatCollector.translateToLocal("tile." + EyesInTheShadows.MODID + ".eyes_jar.tooltip"));
    }
}
