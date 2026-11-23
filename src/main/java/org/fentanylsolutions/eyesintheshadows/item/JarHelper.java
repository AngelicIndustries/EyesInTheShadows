package org.fentanylsolutions.eyesintheshadows.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.entity.EyeVariant;
import org.fentanylsolutions.eyesintheshadows.entity.entities.EntityEyes;

/** Catching eyes in a jar and letting them out. */
public final class JarHelper {

    private static final double REACH = 4.5;

    private JarHelper() {}

    /**
     * The eyes the player is looking at within reach, with nothing solid between. Eyes can't be clicked as other mobs
     * can (they don't collide), so the look is traced here, a little forgiving around them.
     */
    public static EntityEyes eyesInSight(World world, EntityPlayer player) {
        // The client's player stands at eye level already; the server's at its feet
        Vec3 from = world.isRemote ? player.getPosition(1.0F)
            : Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 look = player.getLookVec();
        Vec3 to = from.addVector(look.xCoord * REACH, look.yCoord * REACH, look.zCoord * REACH);
        MovingObjectPosition block = world.rayTraceBlocks(
            Vec3.createVectorHelper(from.xCoord, from.yCoord, from.zCoord),
            Vec3.createVectorHelper(to.xCoord, to.yCoord, to.zCoord));
        double limit = block == null ? REACH : block.hitVec.distanceTo(from);
        AxisAlignedBB around = player.boundingBox
            .addCoord(look.xCoord * REACH, look.yCoord * REACH, look.zCoord * REACH)
            .expand(1, 1, 1);
        EntityEyes nearest = null;
        @SuppressWarnings("unchecked")
        List<EntityEyes> candidates = world.getEntitiesWithinAABB(EntityEyes.class, around);
        for (EntityEyes eyes : candidates) {
            if (eyes.isDead || eyes.deathTime > 0) continue;
            MovingObjectPosition hit = eyes.boundingBox.expand(0.3, 0.3, 0.3)
                .calculateIntercept(from, to);
            if (hit == null) continue;
            double distance = hit.hitVec.distanceTo(from);
            if (distance <= limit) {
                limit = distance;
                nearest = eyes;
            }
        }
        return nearest;
    }

    /**
     * Catches the eyes the player looks at into the empty jar, if the player is invisible: the eyes see anyone else
     * coming.
     */
    public static boolean capture(ItemStack emptyJar, World world, EntityPlayer player) {
        EntityEyes eyes = eyesInSight(world, player);
        if (eyes == null || !player.isPotionActive(Potion.invisibility)) return false;
        if (!world.isRemote) {
            EyeVariant variant = eyes.getVariant();
            if (variant == EyeVariant.NORMAL) variant = EyeVariant.of(world);
            world.playSoundEffect(
                eyes.posX,
                eyes.posY,
                eyes.posZ,
                EyesInTheShadows.MODID + (variant == EyeVariant.LIMBO ? ":jar.glitched_short" : ":jar.laugh"),
                0.5F,
                1.0F);
            eyes.setDead();
            swap(player, emptyJar, new ItemStack(EyesInTheShadows.eyesJarItem, 1, variant.ordinal()));
        }
        return true;
    }

    /** Lets the jar's eyes out at a spot, giving the empty jar back. */
    public static void release(ItemStack jar, World world, EntityPlayer player, double x, double y, double z) {
        if (world.isRemote) return;
        EntityEyes eyes = new EntityEyes(world);
        eyes.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);
        eyes.onSpawnWithEgg(null);
        eyes.setVariant(EyeVariant.byId(jar.getItemDamage()));
        if (jar.hasDisplayName()) eyes.setCustomNameTag(jar.getDisplayName());
        world.spawnEntityInWorld(eyes);
        // Uncorked, and the eyes laugh on their way out
        world.playSoundEffect(x, y, z, EyesInTheShadows.MODID + ":jar.release", 1.0F, 1.0F);
        world.playSoundAtEntity(
            eyes,
            eyes.isGlitched() ? EyesInTheShadows.varInstanceCommon.glitchedLaughSound
                : EyesInTheShadows.varInstanceCommon.laughSound,
            0.8F,
            1.0F);
        swap(player, jar, new ItemStack(EyesInTheShadows.emptyJar));
    }

    /** Turns one item of the held stack into another, as a bucket does when it's filled. */
    private static void swap(EntityPlayer player, ItemStack held, ItemStack result) {
        if (player.capabilities.isCreativeMode) {
            if (!player.inventory.addItemStackToInventory(result)) player.dropPlayerItemWithRandomChoice(result, false);
            return;
        }
        if (--held.stackSize <= 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, result);
        } else if (!player.inventory.addItemStackToInventory(result)) {
            player.dropPlayerItemWithRandomChoice(result, false);
        }
        player.inventoryContainer.detectAndSendChanges();
    }
}
