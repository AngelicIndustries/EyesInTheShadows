package org.fentanylsolutions.eyesintheshadows.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;

public class DimensionUtil {

    public static class SimpleDimensionObj {

        private final int id;
        private final String name;
        private final String leaveMessage;

        public SimpleDimensionObj(int id, String name, String leaveMessage) {
            this.id = id;
            this.name = name;
            this.leaveMessage = leaveMessage;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getLeaveMessage() {
            return leaveMessage;
        }
    }

    /**
     * Every registered dimension with its real ID and name, as the config's dimension entries take them. A provider is
     * made per ID, since some (such as Dimensional Doors' pockets) name themselves by their ID.
     */
    public static List<SimpleDimensionObj> registeredDimensions() {
        List<SimpleDimensionObj> dimensions = new ArrayList<>();
        for (Integer id : DimensionManager.getStaticDimensionIDs()) {
            try {
                WorldProvider provider = DimensionManager.createProviderFor(id);
                dimensions.add(new SimpleDimensionObj(id, provider.getDimensionName(), provider.getDepartMessage()));
            } catch (RuntimeException e) {
                dimensions.add(new SimpleDimensionObj(id, "?", null));
            }
        }
        dimensions.sort(Comparator.comparingInt(SimpleDimensionObj::getId));
        return dimensions;
    }

    /** The registered dimension a config entry stands for, by name or ID, or null. */
    public static SimpleDimensionObj getSimpleDimensionObj(String entry) {
        String key = entry.trim();
        for (SimpleDimensionObj dimension : registeredDimensions()) {
            if (key.equals(dimension.getName()) || key.equals(String.valueOf(dimension.getId()))) return dimension;
        }
        return null;
    }

    public static void printDimensionNames() {
        EyesInTheShadows.LOG.info("=========Dimension List=========");
        for (SimpleDimensionObj dimension : registeredDimensions()) {
            EyesInTheShadows.LOG.info("{} ({})", dimension.getName(), dimension.getId());
        }
        EyesInTheShadows.LOG.info("=============================");
    }
}
