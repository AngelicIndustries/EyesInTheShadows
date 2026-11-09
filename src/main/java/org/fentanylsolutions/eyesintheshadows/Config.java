package org.fentanylsolutions.eyesintheshadows;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.fentanylsolutions.eyesintheshadows.util.PotionPools;
import org.fentanylsolutions.eyesintheshadows.util.PotionUtil;
import org.fentanylsolutions.eyesintheshadows.util.Restrictions;
import org.fentanylsolutions.eyesintheshadows.util.Util;

import cpw.mods.fml.client.config.GuiConfigEntries;

public class Config {

    public static Configuration config = new Configuration(EyesInTheShadows.confFile);
    static boolean loaded = false;

    private static class Defaults {

        /* general */
        public static final boolean passiveEyes = false;
        public static final boolean debugMode = false;
        public static final boolean jumpscare = true;
        public static final String[] placeRestrictions = new String[0];
        public static final boolean eyesCanAttackWhileLit = false;
        public static final double speedNoAggro = 0.1;
        public static final double speedFullAggro = 0.5;

        /* potion */
        public static final Potion[] potionNames = { Potion.confusion, Potion.poison };
        public static final int potionDuration = 10;
        public static final int potionAmplifier = 0;
        public static final Potion[] potionCollisionNames = {};
        public static final int potionCollisionDuration = 10;
        public static final int potionCollisionAmplifier = 0;
        public static final Potion[] potionLookNames = {};
        public static final int potionLookDuration = 10;
        public static final int potionLookAmplifier = 0;
        public static final Potion[] potionIgnoreDisappearNames = { Potion.blindness, Potion.invisibility };
        public static final String[] potionPoolRules = { "attack@Limbo+=potion.wither,potion.blindness" };

        /* eye aggression */
        public static final boolean enableEyeAggressionEscalation = true;
        public static final boolean eyeAggressionDependsOnLocalDifficulty = true;
        public static final boolean eyeAggressionDependsOnLightLevel = true;
        public static final boolean changeColorWithAggro = true;
        public static final double eyeBaseAttackDamage = 1;
        public static final boolean fly = true;

        /* spawning */
        public static final int maxSpawnedInChunk = 8;
        public static final int despawnAfterAmountOfTicks = 2400;
        public static final boolean enableNaturalSpawn = true;
        public static final String[] biomeSpawnNames = new String[0];
        public static final boolean biomeListIsWhitelist = false;
        public static final String[] dimensionSpawnNames = new String[0];
        public static final String[] dimensionEyeMultipliers = { "Limbo=10" };
        public static final boolean dimensionListIsWhitelist = false;

        public static final int spawnCycleIntervalNormal = 150;
        public static final int maxEyesAroundPlayerNormal = 2;
        public static final int maxTotalEyesPerDimensionNormal = 15;

        public static final int spawnCycleIntervalMidnight = 50;
        public static final int maxEyesAroundPlayerMidnight = 3;
        public static final int maxTotalEyesPerDimensionMidnight = 15;

        public static final int spawnCycleIntervalHalloween = 50;
        public static final int maxEyesAroundPlayerHalloween = 5;
        public static final int maxTotalEyesPerDimensionHalloween = 25;
        public static final float maxEyesSpawnDistance = 64;
        public static final boolean spawnUnderCover = false;
        public static final int maxSpawnLightLevel = 15;

        /* misc */
        public static final float aggroEscalationPerTick = 1f / (20 * 60 * 5);
        public static final String[] dimensionAggroMultipliers = { "Limbo=60" };
        public static final int blinkDuration = 5;
        public static final float blinkChance = 0.02F;
        public static final int health = 1;
        public static final float scaleFactor = 1F;
        public static final float eyeGlowStrength = 1.3F;
        public static final double jumpscareSeconds = 10;
        public static final boolean jumpscareEyes = true;
        public static final boolean jumpscareCreature = true;
        public static final int eyeGlowPasses = 1;
        public static final String[] whiteEyeDimensions = { "Limbo" };
        public static final String[] glitchedDimensions = { "Limbo" };
        public static final String[] alwaysDarkDimensions = { "Limbo" };
        public static final int idleSoundInterval = 130;
        public static final boolean enableDynamicLightApproximation = true;
        public static final float eyeArtificialLightSensitivityOpenSky = 1.25F;
        public static final float eyeArtificialLightSensitivityCovered = 1.45F;
        public static final boolean eyesAttackTamedWolves = false;
        public static final boolean wolvesAttackEyes = true;
        public static final boolean mobsAttackDormantEyes = false;
        public static final float watchDistance = 16F;
        public static final boolean eyesWander = true;
        public static final boolean printPotions = false;
        public static final boolean printMobs = false;
        public static final boolean printDimensions = false;
        public static final boolean printBiomes = false;
        public static final String[] mobStringsAttackingEyes = {};
        public static final String[] mobStringsThatEyesAttack = {};
        public static final String[] mobStringsFleeingEyes = { "SnowMan", "Villager" };
        public static final String[] mobStringsThatEyesFlee = { "Ozelot" };
        public static final boolean mobsFleeDormantEyes = false;
        public static final float damageFromWet = 0;
        public static final int spawnCycleSpawnWarningTime = 200;
        public static final float waveMotionMinAmplitude = 0.03f;
        public static final float waveMotionMaxAmplitude = 0.1f;
        public static final float waveMotionMinSpeed = 0.05f;
        public static final float waveMotionMaxSpeed = 0.2f;

        /* sound_volumes */
        /* Each sound's share of its file's loudness, bringing the eyes in line with vanilla's mobs */
        public static final float laughVolume = 0.6F;
        public static final float disappearVolume = 0.6F;
        public static final float jumpscareVolume = 1;
        public static final float glitchedLaughVolume = 0.5F;
        public static final float glitchedWhisperVolume = 0.5F;
        public static final float glitchedDisappearVolume = 0.5F;
        public static final float glitchedJumpscareVolume = 0.6F;
    }

    public static class Categories {

        public static final String general = "general";
        public static final String eye_aggression = "eye_aggression";
        public static final String sound_volumes = "sound_volumes";
        public static final String spawning = "spawning";
        public static final String misc = "misc";
        public static final String potion = "potion";
        public static final String mob_interactions = "mob_interactions";
        public static final String visual = "visual";
    }

    /* general */
    public static boolean passiveEyes = Defaults.passiveEyes;
    public static boolean debugMode = Defaults.debugMode;
    public static boolean jumpscare = Defaults.jumpscare;
    public static String[] placeRestrictions = Defaults.placeRestrictions;
    public static boolean eyesCanAttackWhileLit = Defaults.eyesCanAttackWhileLit;

    /* potion */
    public static Potion[] potionNames = Defaults.potionNames;
    public static int potionDuration = Defaults.potionDuration;
    public static int potionAmplifier = Defaults.potionAmplifier;
    public static Potion[] potionCollisionNames = Defaults.potionCollisionNames;
    public static int potionCollisionDuration = Defaults.potionCollisionDuration;
    public static int potionCollisionAmplifier = Defaults.potionCollisionAmplifier;
    public static Potion[] potionLookNames = Defaults.potionLookNames;
    public static int potionLookDuration = Defaults.potionLookDuration;
    public static int potionLookAmplifier = Defaults.potionLookAmplifier;
    public static Potion[] potionIgnoreDisappearNames = Defaults.potionIgnoreDisappearNames;
    public static String[] potionPoolRules = Defaults.potionPoolRules;

    /* eye aggression */
    public static boolean enableEyeAggressionEscalation = Defaults.enableEyeAggressionEscalation;
    public static boolean eyeAggressionDependsOnLocalDifficulty = Defaults.eyeAggressionDependsOnLocalDifficulty;
    public static boolean eyeAggressionDependsOnLightLevel = Defaults.eyeAggressionDependsOnLightLevel;
    public static boolean changeColorWithAggro = Defaults.changeColorWithAggro;
    public static double speedNoAggro = Defaults.speedNoAggro;
    public static double speedFullAggro = Defaults.speedFullAggro;
    public static float aggroEscalationPerTick = Defaults.aggroEscalationPerTick;
    public static String[] dimensionAggroMultipliers = Defaults.dimensionAggroMultipliers;
    public static float watchDistance = Defaults.watchDistance;
    public static double eyeBaseAttackDamage = Defaults.eyeBaseAttackDamage;
    public static boolean fly = true;

    /* spawning */
    public static int maxSpawnedInChunk = Defaults.maxSpawnedInChunk;
    public static float maxSpawnDistance;
    public static int despawnAfterAmountOfTicks = Defaults.despawnAfterAmountOfTicks;
    public static boolean enableNaturalSpawn = Defaults.enableNaturalSpawn;
    public static String[] biomeSpawnNames = Defaults.biomeSpawnNames;
    public static boolean biomeListIsWhitelist = Defaults.biomeListIsWhitelist;
    public static String[] dimensionSpawnNames = Defaults.dimensionSpawnNames;
    public static String[] dimensionEyeMultipliers = Defaults.dimensionEyeMultipliers;
    public static boolean dimensionListIsWhitelist = Defaults.dimensionListIsWhitelist;
    public static int spawnCycleIntervalNormal = Defaults.spawnCycleIntervalNormal;
    public static int maxEyesAroundPlayerNormal = Defaults.maxEyesAroundPlayerNormal;
    public static int maxTotalEyesPerDimensionNormal = Defaults.maxTotalEyesPerDimensionNormal;
    public static int spawnCycleIntervalMidnight = Defaults.spawnCycleIntervalMidnight;
    public static int maxEyesAroundPlayerMidnight = Defaults.maxEyesAroundPlayerMidnight;
    public static int maxTotalEyesPerDimensionMidnight = Defaults.maxTotalEyesPerDimensionMidnight;
    public static int spawnCycleIntervalHalloween = Defaults.spawnCycleIntervalHalloween;
    public static int maxEyesAroundPlayerHalloween = Defaults.maxEyesAroundPlayerHalloween;
    public static int maxTotalEyesPerDimensionHalloween = Defaults.maxTotalEyesPerDimensionHalloween;
    public static float maxEyesSpawnDistance = 64;
    public static boolean spawnUnderCover = Defaults.spawnUnderCover;
    public static int maxSpawnLightLevel = Defaults.maxSpawnLightLevel;

    /* sounds */
    public static float laughVolume = Defaults.laughVolume;
    public static float disappearVolume = Defaults.disappearVolume;
    public static float jumpscareVolume = Defaults.jumpscareVolume;
    public static float glitchedLaughVolume = Defaults.glitchedLaughVolume;
    public static float glitchedWhisperVolume = Defaults.glitchedWhisperVolume;
    public static float glitchedDisappearVolume = Defaults.glitchedDisappearVolume;
    public static float glitchedJumpscareVolume = Defaults.glitchedJumpscareVolume;

    /* misc */
    public static int blinkDuration = Defaults.blinkDuration;
    public static float blinkChance = Defaults.blinkChance;
    public static int health = Defaults.health;
    public static float scaleFactor = Defaults.scaleFactor;
    public static float eyeGlowStrength = Defaults.eyeGlowStrength;
    public static double jumpscareSeconds = Defaults.jumpscareSeconds;
    public static boolean jumpscareEyes = Defaults.jumpscareEyes;
    public static boolean jumpscareCreature = Defaults.jumpscareCreature;
    public static int eyeGlowPasses = Defaults.eyeGlowPasses;
    public static String[] whiteEyeDimensions = Defaults.whiteEyeDimensions;
    public static String[] glitchedDimensions = Defaults.glitchedDimensions;
    public static String[] alwaysDarkDimensions = Defaults.alwaysDarkDimensions;
    public static int idleSoundInterval = Defaults.idleSoundInterval;
    public static boolean enableDynamicLightApproximation = Defaults.enableDynamicLightApproximation;
    public static float eyeArtificialLightSensitivityOpenSky = Defaults.eyeArtificialLightSensitivityOpenSky;
    public static float eyeArtificialLightSensitivityCovered = Defaults.eyeArtificialLightSensitivityCovered;
    public static boolean eyesAttackTamedWolves = Defaults.eyesAttackTamedWolves;
    public static boolean wolvesAttackEyes = Defaults.wolvesAttackEyes;
    public static boolean mobsAttackDormantEyes = Defaults.mobsAttackDormantEyes;
    public static boolean eyesWander = Defaults.eyesWander;
    public static boolean printPotions = Defaults.printPotions;
    public static boolean printMobs = Defaults.printMobs;
    public static boolean printDimensions = Defaults.printDimensions;
    public static boolean printBiomes = Defaults.printBiomes;
    public static String[] mobStringsAttackingEyes = Defaults.mobStringsAttackingEyes;
    public static String[] mobStringsThatEyesAttack = Defaults.mobStringsThatEyesAttack;
    public static String[] mobStringsFleeingEyes = Defaults.mobStringsFleeingEyes;
    public static String[] mobStringsThatEyesFlee = Defaults.mobStringsThatEyesFlee;
    public static boolean mobsFleeDormantEyes = Defaults.mobsFleeDormantEyes;
    public static float damageFromWet = Defaults.damageFromWet;
    public static int spawnCycleSpawnWarningTime = Defaults.spawnCycleSpawnWarningTime;
    public static float waveMotionMinAmplitude = Defaults.waveMotionMinAmplitude;
    public static float waveMotionMaxAmplitude = Defaults.waveMotionMaxAmplitude;
    public static float waveMotionMinSpeed = Defaults.waveMotionMinSpeed;
    public static float waveMotionMaxSpeed = Defaults.waveMotionMaxSpeed;

    public static void synchronizeConfigurationClient(File configFile, boolean force, boolean load) {
        if (!loaded || force) {
            if (load) {
                config.load();
            }
            loaded = true;

            synchronizeConfigurationCommon();

            /* Visual */
            Property scaleFactorProperty = config.get(
                Categories.visual,
                "scale_factor",
                Defaults.scaleFactor,
                "Eye size scale factor (purely visual).",
                0.01F,
                Float.MAX_VALUE);
            scaleFactor = (float) scaleFactorProperty.getDouble();

            Property eyeGlowStrengthProperty = config.get(
                Categories.visual,
                "eye_glow_strength",
                Defaults.eyeGlowStrength,
                "Emissive glow intensity multiplier for eye rendering.",
                0F,
                4F);
            eyeGlowStrength = (float) eyeGlowStrengthProperty.getDouble();

            Property eyeGlowPassesProperty = config.get(
                Categories.visual,
                "eye_glow_passes",
                Defaults.eyeGlowPasses,
                "How many additive emissive passes are rendered for eyes.",
                1,
                4);
            eyeGlowPasses = eyeGlowPassesProperty.getInt();

            Property jumpscareSecondsProperty = config.get(
                Categories.visual,
                "jumpscareSeconds",
                Defaults.jumpscareSeconds,
                "How long the jumpscare lasts, in seconds. The eyes opening, the moment of black and the creature fading take 2.5 seconds; the eyes stare and then blink with the creature for the rest.",
                2.5,
                60);
            jumpscareSeconds = jumpscareSecondsProperty.getDouble();

            Property jumpscareEyesProperty = config.get(
                Categories.visual,
                "jumpscareEyes",
                Defaults.jumpscareEyes,
                "Whether the jumpscare shows the eyes (textures/jumpscare/eyes_0.png on, as many frames as a resource pack gives).");
            jumpscareEyes = jumpscareEyesProperty.getBoolean();

            Property jumpscareCreatureProperty = config.get(
                Categories.visual,
                "jumpscareCreature",
                Defaults.jumpscareCreature,
                "Whether the jumpscare shows the creature behind the eyes (textures/jumpscare/creature.png).");
            jumpscareCreature = jumpscareCreatureProperty.getBoolean();

            Property whiteEyeDimensionsProperty = config.get(
                Categories.visual,
                "white_eye_dimensions",
                Defaults.whiteEyeDimensions,
                "Dimensions (by name or ID, as printDimensions lists them) where the eyes are white instead of yellow. They still turn red as they grow aggressive. Dimensional Doors' Limbo has them.");
            whiteEyeDimensions = whiteEyeDimensionsProperty.getStringList();

            /* Sound: each player's own volumes */
            laughVolume = volume("laughVolume", Defaults.laughVolume, "The eyes' giggling.");
            disappearVolume = volume("disappearVolume", Defaults.disappearVolume, "The screech of an eye vanishing.");
            jumpscareVolume = volume("jumpscareVolume", Defaults.jumpscareVolume, "The jumpscare's screech.");
            glitchedLaughVolume = volume(
                "glitchedLaughVolume",
                Defaults.glitchedLaughVolume,
                "The laughs and giggles of glitched eyes (glitchedDimensions).");
            glitchedWhisperVolume = volume(
                "glitchedWhisperVolume",
                Defaults.glitchedWhisperVolume,
                "The whispers of glitched eyes.");
            glitchedDisappearVolume = volume(
                "glitchedDisappearVolume",
                Defaults.glitchedDisappearVolume,
                "The sighs and gasps of glitched eyes vanishing.");
            glitchedJumpscareVolume = volume(
                "glitchedJumpscareVolume",
                Defaults.glitchedJumpscareVolume,
                "The jumpscare's sound where the eyes are glitched.");
            // Replaced by the above, under new names so that their quieter defaults reach old configs
            for (String old : new String[] { "eyeIdleVolume", "eyeDisappearVolume", "eyeJumpscareVolume" }) {
                config.getCategory(Categories.sound_volumes)
                    .remove(old);
            }
        }
        if (config.hasChanged()) {
            config.save();
        }
    }

    /**
     * A sound's volume slider: its share of the sound file's full loudness, from 0 to 1. The defaults bring the eyes in
     * line with vanilla's mobs; the sounds also follow the Hostile Creatures volume.
     */
    private static float volume(String name, float fallback, String comment) {
        Property property = config.get(
            Categories.sound_volumes,
            name,
            fallback,
            comment + " From 0 (silent) to 1 (the sound's full loudness). Each player sets their own.",
            0,
            1);
        property.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        return (float) property.getDouble();
    }

    public static void synchronizeConfigurationServer(File configFile, boolean force) {
        if (!loaded || force) {
            if (loaded) {
                config.load();
            }
            loaded = true;

            synchronizeConfigurationCommon();
        }
        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void synchronizeConfigurationCommon() {
        /* general */
        Property passiveEyesProperty = config.get(
            Categories.general,
            "passiveEyes",
            Defaults.passiveEyes,
            "Whether eye attack behavior is completely disabled.");
        passiveEyes = passiveEyesProperty.getBoolean();

        Property debugModeProperty = config
            .get(Categories.general, "debugMode", Defaults.debugMode, "Enable/disable debug logs.");
        debugMode = debugModeProperty.getBoolean();

        Property jumpscareProperty = config
            .get(Categories.general, "jumpscare", Defaults.jumpscare, "Set to false to disable the jumpscare system.");
        jumpscare = jumpscareProperty.getBoolean();

        Property placeRestrictionsProperty = config.get(
            Categories.general,
            "placeRestrictions",
            Defaults.placeRestrictions,
            "What the eyes may not do to players in some places, one place per line as place=what. The place is a dimension, by name or ID (printDimensions lists both), or biome: and a biome name, for the biome the player stands in. What is any of jumpscare (no jumpscare screen, sound or its potion effect), hurt (no damage) and approach (they never creep up on the player, only watch), separated by commas, like Overworld=jumpscare,hurt. An eye that reaches a player it may neither jumpscare nor hurt just vanishes. Potion effects on touching and looking at eyes follow potionPoolRules.");
        placeRestrictions = placeRestrictionsProperty.getStringList();
        Restrictions.load(placeRestrictions);

        Property eyesCanAttackWhileLitProperty = config.get(
            Categories.general,
            "eyesCanAttackWhileLit",
            Defaults.eyesCanAttackWhileLit,
            "While set to true, the eyes entity will ignore the artificial light level and will jumpscare even if it's lit. Daylight will still disable it's AI.");
        eyesCanAttackWhileLit = eyesCanAttackWhileLitProperty.getBoolean();

        Property healthProperty = config
            .get(Categories.general, "health", Defaults.health, "Eye health", 1, Integer.MAX_VALUE);
        health = healthProperty.getInt();

        Property eyesWanderProperty = config
            .get(Categories.general, "eyesWander", Defaults.eyesWander, "Controls whether Eyes wander around.");
        eyesWander = eyesWanderProperty.getBoolean();

        /* potion */
        Property potionNamesProperty = config.get(
            Categories.potion,
            "potionNames",
            potionToStrArray(Defaults.potionNames),
            "List of potion effect names that should be applied to a player attacked by Eyes.");
        potionNames = stringToPotionArr(potionNamesProperty.getStringList());

        Property potionDurationProperty = config.get(
            Categories.potion,
            "potionDuration",
            Defaults.potionDuration,
            "Attack inflicted potion effect duration.",
            0,
            Integer.MAX_VALUE);
        potionDuration = potionDurationProperty.getInt();

        Property potionAmplifierProperty = config.get(
            Categories.potion,
            "potionAmplifier",
            Defaults.potionAmplifier,
            "Attack inflicted potion effect amplifier.",
            0,
            Integer.MAX_VALUE);
        potionAmplifier = potionAmplifierProperty.getInt();

        Property potionCollisionNamesProperty = config.get(
            Categories.potion,
            "potionCollisionNames",
            potionToStrArray(Defaults.potionCollisionNames),
            "List of potion effect names that should be applied to a player colliding with the eyes.");
        potionCollisionNames = stringToPotionArr(potionCollisionNamesProperty.getStringList());

        Property potionCollisionDurationProperty = config.get(
            Categories.potion,
            "potionCollisionDuration",
            Defaults.potionCollisionDuration,
            "Collision inflicted potion effect duration.",
            0,
            Integer.MAX_VALUE);
        potionCollisionDuration = potionCollisionDurationProperty.getInt();

        Property potionCollisionAmplifierProperty = config.get(
            Categories.potion,
            "potionCollisionAmplifier",
            Defaults.potionCollisionAmplifier,
            "Collision inflicted potion effect amplifier.",
            0,
            Integer.MAX_VALUE);
        potionCollisionAmplifier = potionCollisionAmplifierProperty.getInt();

        Property potionLookNamesProperty = config.get(
            Categories.potion,
            "potionLookNames",
            potionToStrArray(Defaults.potionLookNames),
            "List of potion effect names that should be applied to a player looking at the eyes.");
        potionLookNames = stringToPotionArr(potionLookNamesProperty.getStringList());

        Property potionLookDurationProperty = config.get(
            Categories.potion,
            "potionLookDuration",
            Defaults.potionLookDuration,
            "Look inflicted potion effect duration.",
            0,
            Integer.MAX_VALUE);
        potionLookDuration = potionLookDurationProperty.getInt();

        Property potionLookAmplifierProperty = config.get(
            Categories.potion,
            "potionLookAmplifier",
            Defaults.potionLookAmplifier,
            "Look inflicted potion effect amplifier.",
            0,
            Integer.MAX_VALUE);
        potionLookAmplifier = potionLookAmplifierProperty.getInt();

        Property potionIgnoreDisappearNamesProperty = config.get(
            Categories.potion,
            "potionIgnoreDisappearNames",
            potionToStrArray(Defaults.potionIgnoreDisappearNames),
            "List of potion effect names that prevent the eyes from disappearing while being looked at.");
        potionIgnoreDisappearNames = stringToPotionArr(potionIgnoreDisappearNamesProperty.getStringList());

        Property potionPoolRulesProperty = config.get(
            Categories.potion,
            "potionPoolRules",
            Defaults.potionPoolRules,
            "Changes to the potion effect pools by dimension and biome, one rule per line: event@place=effects replaces the pool there, event@place+=effects adds to it. The event is attack (the jumpscare, potionNames), collision (potionCollisionNames) or look (potionLookNames). The place is a dimension, by name or ID (printDimensions lists both), or biome: and a biome name; biome rules apply after dimension rules. Effects are potion names separated by commas, each optionally with its own duration in seconds and amplifier (potion.wither:8:1); none draws no effect, which lowers the odds of the others. One effect is drawn from the pool each time. By default the jumpscare in Dimensional Doors' Limbo can also wither or blind.");
        potionPoolRules = potionPoolRulesProperty.getStringList();
        PotionPools.load(potionPoolRules);

        /* eye aggro */
        Property eyeBaseAttackDamageProperty = config.get(
            Categories.eye_aggression,
            "eyeBaseAttackDamage",
            Defaults.eyeBaseAttackDamage,
            "Base damage dealt by Eyes.",
            0,
            Double.MAX_VALUE);
        eyeBaseAttackDamage = eyeBaseAttackDamageProperty.getDouble();

        Property enableEyeAggressionEscalationProperty = config.get(
            Categories.eye_aggression,
            "enableEyeAggressionEscalation",
            Defaults.enableEyeAggressionEscalation,
            "While set to true, the eyes entities will progressively get more bold, and move faster, the longer they live.");
        enableEyeAggressionEscalation = enableEyeAggressionEscalationProperty.getBoolean();

        Property eyeAggressionDependsOnLocalDifficultyProperty = config.get(
            Categories.eye_aggression,
            "eyeAggressionDependsOnLocalDifficulty",
            Defaults.eyeAggressionDependsOnLocalDifficulty,
            "While set to true, the eyes entities will spawn with higher aggression levels in higher local difficulties.");
        eyeAggressionDependsOnLocalDifficulty = eyeAggressionDependsOnLocalDifficultyProperty.getBoolean();

        Property eyeAggressionDependsOnLightLevelProperty = config.get(
            Categories.eye_aggression,
            "eyeAggressionDependsOnLightLevel",
            Defaults.eyeAggressionDependsOnLightLevel,
            "While set to true, the eyes entities will have higher aggression values on lower light levels.");
        eyeAggressionDependsOnLightLevel = eyeAggressionDependsOnLightLevelProperty.getBoolean();

        Property changeColorWithAggroProperty = config.get(
            Categories.eye_aggression,
            "changeColorWithAggro",
            Defaults.changeColorWithAggro,
            "While set to true, the eyes entities will become more and more red as their aggression level grows.");
        changeColorWithAggro = changeColorWithAggroProperty.getBoolean();

        Property speedNoAggroProperty = config.get(
            Categories.eye_aggression,
            "speedNoAggro",
            Defaults.speedNoAggro,
            "Eyes speed at minimal aggro.",
            0,
            Double.MAX_VALUE);
        speedNoAggro = speedNoAggroProperty.getDouble();

        Property speedFullAggroProperty = config.get(
            Categories.eye_aggression,
            "speedFullAggro",
            Defaults.speedFullAggro,
            "Eyes speed at full aggro.",
            0,
            Double.MAX_VALUE);
        speedFullAggro = speedFullAggroProperty.getDouble();

        Property aggroEscalationPerTickProperty = config.get(
            Categories.eye_aggression,
            "aggroEscalationPerTick",
            Defaults.aggroEscalationPerTick,
            "Eyes aggro escalation per tick.",
            0,
            Float.MAX_VALUE);
        aggroEscalationPerTick = (float) aggroEscalationPerTickProperty.getDouble();

        Property dimensionAggroMultipliersProperty = config.get(
            Categories.eye_aggression,
            "dimensionAggroMultipliers",
            Defaults.dimensionAggroMultipliers,
            "Dimensions where the eyes grow aggressive faster, as dimension=multiplier (by name or ID) on aggroEscalationPerTick. In Dimensional Doors' Limbo they turn red within seconds.");
        dimensionAggroMultipliers = dimensionAggroMultipliersProperty.getStringList();

        // Gone: an eye vanishes after attacking, so there was never a next attack to wait for
        config.getCategory(Categories.eye_aggression)
            .remove("tickBetweenAttacks");

        Property watchDistanceProperty = config.get(
            Categories.eye_aggression,
            "watchDistance",
            Defaults.watchDistance,
            "Distance at which Eyes watch the player.",
            0,
            Float.MAX_VALUE);
        watchDistance = (float) watchDistanceProperty.getDouble();

        Property flyProperty = config.get(
            Categories.eye_aggression,
            "fly",
            Defaults.fly,
            "Allow Eyes to change altitude while pursuing targets.");
        fly = flyProperty.getBoolean();

        /* spawning */
        Property maxSpawnedInChunkProperty = config.get(
            Categories.spawning,
            "maxSpawnedInChunk",
            Defaults.maxSpawnedInChunk,
            "Max Eyes spawning per chunk.",
            1,
            Integer.MAX_VALUE);
        maxSpawnedInChunk = maxSpawnedInChunkProperty.getInt();

        Property despawnAfterAmountOfTicksProperty = config.get(
            Categories.spawning,
            "despawnAfterAmountOfTicks",
            Defaults.despawnAfterAmountOfTicks,
            "Ticks after which Eyes can despawn.",
            1,
            Integer.MAX_VALUE);
        despawnAfterAmountOfTicks = despawnAfterAmountOfTicksProperty.getInt();

        Property enableNaturalSpawnProperty = config.get(
            Categories.spawning,
            "enableNaturalSpawn",
            Defaults.enableNaturalSpawn,
            "Enable or disable natural Eye spawning. Useful if using another spawning manager.");
        enableNaturalSpawn = enableNaturalSpawnProperty.getBoolean();

        Property biomeSpawnNamesProperty = config
            .get(Categories.spawning, "biomeSpawnNames", Defaults.biomeSpawnNames, "List of biomes.");
        biomeSpawnNames = biomeSpawnNamesProperty.getStringList();

        Property biomeListIsWhitelistProperty = config.get(
            Categories.spawning,
            "biomeListIsWhitelist",
            Defaults.biomeListIsWhitelist,
            "Whether the biome list acts as a white or blacklist.");
        biomeListIsWhitelist = biomeListIsWhitelistProperty.getBoolean();

        Property dimensionSpawnNamesProperty = config.get(
            Categories.spawning,
            "dimensionSpawnNames",
            Defaults.dimensionSpawnNames,
            "List of dimensions, by name or ID, as printDimensions lists them.");
        dimensionSpawnNames = dimensionSpawnNamesProperty.getStringList();

        Property dimensionEyeMultipliersProperty = config.get(
            Categories.spawning,
            "dimensionEyeMultipliers",
            Defaults.dimensionEyeMultipliers,
            "Dimensions with more (or fewer) eyes, as dimension=multiplier (by name or ID): it multiplies how often eyes spawn there and how many there are around each player and in the whole dimension. Dimensional Doors' Limbo has ten times as many.");
        dimensionEyeMultipliers = dimensionEyeMultipliersProperty.getStringList();

        Property glitchedDimensionsProperty = config.get(
            Categories.general,
            "glitchedDimensions",
            Defaults.glitchedDimensions,
            "Dimensions (by name or ID) where the eyes are glitched: they tear sideways and flicker for as long as they make a sound, go out in a glitch, mostly whisper, giggle now and then, have their own disappearing and jumpscare sounds, and their jumpscare glitches the screen. Dimensional Doors' Limbo has them.");
        glitchedDimensions = glitchedDimensionsProperty.getStringList();

        Property alwaysDarkDimensionsProperty = config.get(
            Categories.general,
            "alwaysDarkDimensions",
            Defaults.alwaysDarkDimensions,
            "Dimensions (by name or ID) where daylight never shows: the eyes there are out, spawn and can attack at any hour, only lights keep them away. Dimensional Doors' Limbo keeps the Overworld's clock under a black sky.");
        alwaysDarkDimensions = alwaysDarkDimensionsProperty.getStringList();

        Property idleSoundIntervalProperty = config.get(
            Categories.general,
            "idleSoundInterval",
            Defaults.idleSoundInterval,
            "Ticks an eye waits after an idle sound before the chance of the next starts to grow (vanilla mobs wait 80).",
            1,
            Integer.MAX_VALUE);
        idleSoundInterval = idleSoundIntervalProperty.getInt();

        Property dimensionListIsWhitelistProperty = config.get(
            Categories.spawning,
            "dimensionListIsWhitelist",
            Defaults.dimensionListIsWhitelist,
            "Whether the dimension list acts as a white or blacklist.");
        dimensionListIsWhitelist = dimensionListIsWhitelistProperty.getBoolean();

        Property spawnCycleIntervalNormalProperty = config.get(
            Categories.spawning,
            "spawnCycleIntervalNormal",
            Defaults.spawnCycleIntervalNormal,
            "Number of ticks between spawn cycles.",
            1,
            Integer.MAX_VALUE);
        spawnCycleIntervalNormal = spawnCycleIntervalNormalProperty.getInt();

        Property maxEyesAroundPlayerNormalProperty = config.get(
            Categories.spawning,
            "maxEyesAroundPlayerNormal",
            Defaults.maxEyesAroundPlayerNormal,
            "Max amount of eyes that will spawn around one player.",
            1,
            Integer.MAX_VALUE);
        maxEyesAroundPlayerNormal = maxEyesAroundPlayerNormalProperty.getInt();

        Property maxTotalEyesPerDimensionNormalProperty = config.get(
            Categories.spawning,
            "maxTotalEyesPerDimensionNormal",
            Defaults.maxTotalEyesPerDimensionNormal,
            "Max number of eyes entities that will spawn in each dimension.",
            1,
            Integer.MAX_VALUE);
        maxTotalEyesPerDimensionNormal = maxTotalEyesPerDimensionNormalProperty.getInt();

        Property spawnCycleIntervalMidnightProperty = config.get(
            Categories.spawning,
            "spawnCycleIntervalMidnight",
            Defaults.spawnCycleIntervalMidnight,
            "Number of ticks between spawn cycles.",
            1,
            Integer.MAX_VALUE);
        spawnCycleIntervalMidnight = spawnCycleIntervalMidnightProperty.getInt();

        Property maxEyesAroundPlayerMidnightProperty = config.get(
            Categories.spawning,
            "maxEyesAroundPlayerMidnight",
            Defaults.maxEyesAroundPlayerMidnight,
            "Max amount of eyes that will spawn around one player.",
            1,
            Integer.MAX_VALUE);
        maxEyesAroundPlayerMidnight = maxEyesAroundPlayerMidnightProperty.getInt();

        Property maxTotalEyesPerDimensionMidnightProperty = config.get(
            Categories.spawning,
            "maxTotalEyesPerDimensionMidnight",
            Defaults.maxTotalEyesPerDimensionMidnight,
            "Max number of eyes entities that will spawn in each dimension.",
            1,
            Integer.MAX_VALUE);
        maxTotalEyesPerDimensionMidnight = maxTotalEyesPerDimensionMidnightProperty.getInt();

        Property spawnCycleIntervalHalloweenProperty = config.get(
            Categories.spawning,
            "spawnCycleIntervalHalloween",
            Defaults.spawnCycleIntervalHalloween,
            "Number of ticks between spawn cycles.",
            1,
            Integer.MAX_VALUE);
        spawnCycleIntervalHalloween = spawnCycleIntervalHalloweenProperty.getInt();

        Property maxEyesAroundPlayerHalloweenProperty = config.get(
            Categories.spawning,
            "maxEyesAroundPlayerHalloween",
            Defaults.maxEyesAroundPlayerHalloween,
            "Max amount of Eyes that will spawn around one player.",
            1,
            Integer.MAX_VALUE);
        maxEyesAroundPlayerHalloween = maxEyesAroundPlayerHalloweenProperty.getInt();

        Property maxTotalEyesPerDimensionHalloweenProperty = config.get(
            Categories.spawning,
            "maxTotalEyesPerDimensionHalloween",
            Defaults.maxTotalEyesPerDimensionHalloween,
            "Max number of Eyes entities that will spawn in each dimension.",
            1,
            Integer.MAX_VALUE);
        maxTotalEyesPerDimensionHalloween = maxTotalEyesPerDimensionHalloweenProperty.getInt();

        Property maxEyesSpawnDistanceProperty = config.get(
            Categories.spawning,
            "maxEyesSpawnDistance",
            Defaults.maxEyesSpawnDistance,
            "Max distance from a player at which Eyes will spawn.",
            1,
            Integer.MAX_VALUE);
        maxEyesSpawnDistance = (float) maxEyesSpawnDistanceProperty.getDouble();

        Property spawnUnderCoverProperty = config
            .get(Categories.spawning, "spawnUnderCover", Defaults.spawnUnderCover, "Spawn only when cannot see sky.");
        spawnUnderCover = spawnUnderCoverProperty.getBoolean();

        Property maxSpawnLightLevelProperty = config.get(
            Categories.spawning,
            "maxSpawnLightLevel",
            Defaults.maxSpawnLightLevel,
            "Max light level at which eyes will spawn.",
            0,
            15);
        maxSpawnLightLevel = maxSpawnLightLevelProperty.getInt();

        /* misc */
        Property printPotionsProperty = config.get(
            Categories.misc,
            "printPotions",
            Defaults.printPotions,
            "If set to true, print a list of potions in the logs on game post init.");
        printPotions = printPotionsProperty.getBoolean();

        Property printMobsProperty = config.get(
            Categories.misc,
            "printMobs",
            Defaults.printMobs,
            "If set to true, print a list of mob names on game post init.");
        printMobs = printMobsProperty.getBoolean();

        Property printDimensionsProperty = config.get(
            Categories.misc,
            "printDimensions",
            Defaults.printDimensions,
            "If set to true, print every registered dimension's name and ID on game post init. Dimension lists take either.");
        printDimensions = printDimensionsProperty.getBoolean();

        Property printBiomesProperty = config.get(
            Categories.misc,
            "printBiomes",
            Defaults.printBiomes,
            "If set to true, print a list of biome names on game post init.");
        printBiomes = printBiomesProperty.getBoolean();

        Property damageFromWetProperty = config.get(
            Categories.misc,
            "damageFromWet",
            Defaults.damageFromWet,
            "Amount of damage eyes get from water.",
            0,
            Float.MAX_VALUE);
        damageFromWet = (float) damageFromWetProperty.getDouble();

        Property spawnCycleSpawnWarningTimeProperty = config.get(
            Categories.misc,
            "spawnCycleSpawnWarningTime",
            Defaults.spawnCycleSpawnWarningTime,
            "Warning if a spawn cycle takes longer than this amount of milliseconds.",
            0,
            Float.MAX_VALUE);
        spawnCycleSpawnWarningTime = (int) spawnCycleSpawnWarningTimeProperty.getDouble();

        Property waveMotionMinAmplitudeProperty = config
            .get(Categories.misc, "waveMotionMinAmplitude", Defaults.waveMotionMinAmplitude, "", 0, Float.MAX_VALUE);
        waveMotionMinAmplitude = (float) waveMotionMinAmplitudeProperty.getDouble();

        Property waveMotionMaxAmplitudeProperty = config
            .get(Categories.misc, "waveMotionMaxAmplitude", Defaults.waveMotionMaxAmplitude, "", 0, Float.MAX_VALUE);
        waveMotionMaxAmplitude = (float) waveMotionMaxAmplitudeProperty.getDouble();

        Property waveMotionMinSpeedProperty = config
            .get(Categories.misc, "waveMotionMinSpeed", Defaults.waveMotionMinSpeed, "", 0, Float.MAX_VALUE);
        waveMotionMinSpeed = (float) waveMotionMinSpeedProperty.getDouble();

        Property waveMotionMaxSpeedProperty = config
            .get(Categories.misc, "waveMotionMaxSpeed", Defaults.waveMotionMaxSpeed, "", 0, Float.MAX_VALUE);
        waveMotionMaxSpeed = (float) waveMotionMaxSpeedProperty.getDouble();

        /* Mob interactions */
        Property eyesAttackTamedWolvesProperty = config.get(
            Categories.mob_interactions,
            "eyesAttackTamedWolves",
            Defaults.eyesAttackTamedWolves,
            "Controls whether Eyes attack tamed Wolves.");
        eyesAttackTamedWolves = eyesAttackTamedWolvesProperty.getBoolean();

        Property wolvesAttackEyesProperty = config.get(
            Categories.mob_interactions,
            "wolvesAttackEyes",
            Defaults.wolvesAttackEyes,
            "Controls whether tamed wolves attack Eyes.");
        wolvesAttackEyes = wolvesAttackEyesProperty.getBoolean();

        Property mobsAttackDormantEyesProperty = config.get(
            Categories.mob_interactions,
            "mobsAttackDormantEyes",
            Defaults.mobsAttackDormantEyes,
            "Controls whether tamed wolves attack dormant Eyes (wolvesAttackEyes must be true for the attack to happen).");
        mobsAttackDormantEyes = mobsAttackDormantEyesProperty.getBoolean();

        Property mobStringsAttackingProperty = config.get(
            Categories.mob_interactions,
            "mobStringsAttackingEyes",
            Defaults.mobStringsAttackingEyes,
            "List of mobs that should attack Eyes.");
        mobStringsAttackingEyes = mobStringsAttackingProperty.getStringList();

        Property mobStringsThatEyesAttackProperty = config.get(
            Categories.mob_interactions,
            "mobStringsThatEyesAttack",
            Defaults.mobStringsThatEyesAttack,
            "List of mobs that should get attacked by Eyes.");
        mobStringsThatEyesAttack = mobStringsThatEyesAttackProperty.getStringList();

        Property mobStringsFleeingEyesProperty = config.get(
            Categories.mob_interactions,
            "mobStringsFleeingEyes",
            Defaults.mobStringsFleeingEyes,
            "List of mobs that should flee Eyes.");
        mobStringsFleeingEyes = mobStringsFleeingEyesProperty.getStringList();

        Property mobStringsThatEyesFleeProperty = config.get(
            Categories.mob_interactions,
            "mobStringsThatEyesFlee",
            Defaults.mobStringsThatEyesFlee,
            "List of mobs that Eyes should flee.");
        mobStringsThatEyesFlee = mobStringsThatEyesFleeProperty.getStringList();

        /*
         * Property mobsFleeDormantEyesProperty = config.get(
         * Categories.mob_interactions,
         * "mobsFleeDormantEyes",
         * Defaults.mobsFleeDormantEyes,
         * "Controls whether mobs flee dormant eyes.");
         * mobsFleeDormantEyes = mobsFleeDormantEyesProperty.getBoolean();
         */

        /* Visual */
        Property blinkDurationProperty = config.get(
            Categories.visual,
            "blinkDuration",
            Defaults.blinkDuration,
            "Eye blink duration. Set to -1 to disable blinking",
            -1,
            Integer.MAX_VALUE);
        blinkDuration = blinkDurationProperty.getInt();

        Property blinkChanceProp = config
            .get(Categories.visual, "blinkChance", Defaults.blinkChance, "How often Eyes should blink.", 0, 1);
        if (!Util.isServer()) {
            blinkChanceProp.setConfigEntryClass(GuiConfigEntries.NumberSliderEntry.class);
        }
        blinkChance = (float) blinkChanceProp.getDouble();

        Property enableDynamicLightApproximationProperty = config.get(
            Categories.visual,
            "enableDynamicLightApproximation",
            Defaults.enableDynamicLightApproximation,
            "Enable or disable Eyes dynamic-light approximation (handheld or dropped emissive items) on both client and server. When false, Eyes only react to vanilla block light.");
        enableDynamicLightApproximation = enableDynamicLightApproximationProperty.getBoolean();

        Property eyeArtificialLightSensitivityOpenSkyProperty = config.get(
            Categories.visual,
            "eyeArtificialLightSensitivityOpenSky",
            Defaults.eyeArtificialLightSensitivityOpenSky,
            "Multiplier for how strongly artificial light fades Eyes while they are exposed to the sky. Higher values make nearby lights hide Eyes more aggressively.",
            0,
            4);
        eyeArtificialLightSensitivityOpenSky = (float) eyeArtificialLightSensitivityOpenSkyProperty.getDouble();

        Property eyeArtificialLightSensitivityCoveredProperty = config.get(
            Categories.visual,
            "eyeArtificialLightSensitivityCovered",
            Defaults.eyeArtificialLightSensitivityCovered,
            "Multiplier for how strongly artificial light fades Eyes while under cover. Higher values make torches and dynamic lights suppress Eyes from farther away.",
            0,
            4);
        eyeArtificialLightSensitivityCovered = (float) eyeArtificialLightSensitivityCoveredProperty.getDouble();

        EyesInTheShadows.varInstanceCommon.buildPotionList();
        EyesInTheShadows.varInstanceCommon.buildDimensionList();
        EyesInTheShadows.varInstanceCommon.buildMobLists();
    }

    protected static String[] potionToStrArray(Potion[] potionArr) {
        if (potionArr == null) {
            return new String[0];
        }

        String[] result = new String[potionArr.length];
        for (int i = 0; i < potionArr.length; i++) {
            result[i] = potionArr[i].getName();
        }
        return result;
    }

    protected static Potion[] stringToPotionArr(String[] names) {
        if (names == null) {
            return new Potion[0];
        }

        List<Potion> result = new ArrayList<>();
        for (String name : names) {
            Potion potion = PotionUtil.getPotionByName(name);
            if (potion != null) {
                result.add(potion);
            } else {
                EyesInTheShadows.LOG.warn("Unknown potion name: {}", name);
            }
        }
        return result.toArray(new Potion[0]);
    }
}
