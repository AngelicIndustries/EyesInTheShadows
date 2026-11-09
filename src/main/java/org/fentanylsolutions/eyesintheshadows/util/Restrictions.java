package org.fentanylsolutions.eyesintheshadows.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;

/**
 * What the eyes may not do to players in some places, from the config: place=what, the place a dimension (name or ID)
 * or biome: and a biome name (the biome the player stands in), what any of jumpscare, hurt and approach.
 */
public final class Restrictions {

    public static final String JUMPSCARE = "jumpscare", HURT = "hurt", APPROACH = "approach";
    private static final List<String> BEHAVIORS = Arrays.asList(JUMPSCARE, HURT, APPROACH);
    private static final String BIOME = "biome:";

    private static final List<Rule> RULES = new ArrayList<>();

    private Restrictions() {}

    /** Reads the rules; ones that don't parse are logged and left out. */
    public static void load(String[] rules) {
        RULES.clear();
        for (String line : rules) {
            int equals = line.indexOf('=');
            String place = equals < 0 ? ""
                : line.substring(0, equals)
                    .trim();
            List<String> blocked = new ArrayList<>();
            boolean ok = !place.isEmpty();
            if (ok) {
                for (String part : line.substring(equals + 1)
                    .split(",")) {
                    part = part.trim();
                    if (BEHAVIORS.contains(part)) blocked.add(part);
                    else if (!part.isEmpty()) ok = false;
                }
            }
            if (ok && !blocked.isEmpty()) RULES.add(new Rule(place, blocked));
            else EyesInTheShadows.LOG.warn("Ignoring place restriction '{}'", line);
        }
    }

    /** Whether the eyes may not do this (JUMPSCARE, HURT or APPROACH) to the player where they are. */
    public static boolean blocks(String behavior, Entity player) {
        if (RULES.isEmpty()) return false;
        World world = player.worldObj;
        BiomeGenBase biome = null;
        for (Rule rule : RULES) {
            if (!rule.blocked.contains(behavior)) continue;
            if (rule.place.startsWith(BIOME)) {
                if (biome == null) biome = world
                    .getBiomeGenForCoords(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posZ));
                if (biome != null && biome.biomeName.equals(
                    rule.place.substring(BIOME.length())
                        .trim()))
                    return true;
            } else if (Util.isDimension(world, rule.place)) {
                return true;
            }
        }
        return false;
    }

    private static final class Rule {

        final String place;
        final List<String> blocked;

        Rule(String place, List<String> blocked) {
            this.place = place;
            this.blocked = blocked;
        }
    }
}
