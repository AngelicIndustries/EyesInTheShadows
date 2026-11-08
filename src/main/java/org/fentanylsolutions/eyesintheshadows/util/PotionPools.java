package org.fentanylsolutions.eyesintheshadows.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;

/**
 * The potion effects the eyes can inflict, one drawn at random from a pool: the pool set for the event (attack,
 * collision, look), changed by rules for the dimension and then the biome the player is in.
 *
 * <p>
 * A rule reads {@code event@place=effects} to replace the pool there, or {@code event@place+=effects} to add to it.
 * The place is a dimension name, or {@code biome:} and a biome name. Effects are potion names, each optionally with
 * its own duration in seconds and amplifier ({@code potion.wither:8:1}); {@code none} draws no effect, which lowers the
 * odds of the others.
 */
public final class PotionPools {

    private static final String BIOME = "biome:";

    private static final List<Rule> RULES = new ArrayList<>();

    private PotionPools() {}

    /** Reads the rules; ones that don't parse are logged and left out. */
    public static void load(String[] rules) {
        RULES.clear();
        for (String line : rules) {
            Rule rule = Rule.parse(line);
            if (rule != null) RULES.add(rule);
            else EyesInTheShadows.LOG.warn("Ignoring potion pool rule '{}'", line);
        }
    }

    /**
     * Inflicts one effect drawn from the event's pool for where the target is, if the pool isn't empty and the draw
     * isn't none.
     */
    public static void inflict(String event, EntityLivingBase target, Potion[] pool, int seconds, int amplifier,
        Random random) {
        List<Entry> entries = new ArrayList<>();
        for (Potion potion : pool) entries.add(new Entry(potion.getName(), -1, -1));
        World world = target.worldObj;
        String dimension = world.provider != null ? world.provider.getDimensionName() : "";
        BiomeGenBase biome = world
            .getBiomeGenForCoords(MathHelper.floor_double(target.posX), MathHelper.floor_double(target.posZ));
        for (Rule rule : RULES) if (rule.matches(event, false, dimension)) rule.applyTo(entries);
        if (biome != null)
            for (Rule rule : RULES) if (rule.matches(event, true, biome.biomeName)) rule.applyTo(entries);
        if (entries.isEmpty()) return;
        Entry entry = entries.get(random.nextInt(entries.size()));
        Potion potion = entry.potion();
        if (potion == null) return;
        target.addPotionEffect(
            new PotionEffect(
                potion.getId(),
                (entry.seconds >= 0 ? entry.seconds : seconds) * 20,
                entry.amplifier >= 0 ? entry.amplifier : amplifier));
    }

    private static final class Rule {

        final String event, place;
        final boolean biome, add;
        final List<Entry> entries;

        Rule(String event, String place, boolean biome, boolean add, List<Entry> entries) {
            this.event = event;
            this.place = place;
            this.biome = biome;
            this.add = add;
            this.entries = entries;
        }

        static Rule parse(String line) {
            int at = line.indexOf('@'), equals = line.indexOf('=');
            if (at <= 0 || equals <= at) return null;
            boolean add = line.charAt(equals - 1) == '+';
            String event = line.substring(0, at)
                .trim();
            String place = line.substring(at + 1, add ? equals - 1 : equals)
                .trim();
            boolean biome = place.startsWith(BIOME);
            if (biome) place = place.substring(BIOME.length())
                .trim();
            List<Entry> entries = new ArrayList<>();
            for (String part : line.substring(equals + 1)
                .split(",")) {
                part = part.trim();
                if (part.isEmpty()) continue;
                Entry entry = Entry.parse(part);
                if (entry == null) return null;
                entries.add(entry);
            }
            return event.isEmpty() || place.isEmpty() ? null : new Rule(event, place, biome, add, entries);
        }

        boolean matches(String event, boolean biome, String place) {
            return this.biome == biome && this.event.equals(event) && this.place.equals(place);
        }

        void applyTo(List<Entry> pool) {
            if (!add) pool.clear();
            pool.addAll(entries);
        }
    }

    private static final class Entry {

        /** The potion's name, or null for none. */
        final String name;
        final int seconds, amplifier;
        private Potion potion;
        private boolean looked;

        Entry(String name, int seconds, int amplifier) {
            this.name = name;
            this.seconds = seconds;
            this.amplifier = amplifier;
        }

        static Entry parse(String text) {
            String[] parts = text.split(":");
            if (parts.length > 3) return null;
            String name = parts[0].trim();
            if (name.equals("none")) return new Entry(null, -1, -1);
            if (!name.startsWith("potion.")) name = "potion." + name;
            try {
                int seconds = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : -1;
                int amplifier = parts.length > 2 ? Integer.parseInt(parts[2].trim()) : -1;
                return new Entry(name, seconds, amplifier);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        /** Looked up when first drawn, as other mods' potions may not exist yet when the config loads. */
        Potion potion() {
            if (name == null) return null;
            if (!looked) {
                looked = true;
                potion = PotionUtil.getPotionByName(name);
                if (potion == null) EyesInTheShadows.LOG.warn("Unknown potion in a potion pool rule: {}", name);
            }
            return potion;
        }
    }
}
