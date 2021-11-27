/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.utils;

import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.TabUtils;
import cheaters.get.banned.utils.Utils;
import java.util.HashMap;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LocationUtils {
    private static String prevIsland = null;
    private static Island island = null;
    public static HashMap<String, Island> islandLookup = new HashMap<String, Island>(){
        {
            for (Island island : Island.values()) {
                this.put(island.getName(), island);
            }
        }
    };

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Utils.inSkyBlock) {
            for (String name : TabUtils.getTabList()) {
                if (name.equals(prevIsland) || !name.contains("Area:")) continue;
                prevIsland = name;
                name = Utils.removeFormatting(name).replace("Area: ", "");
                island = islandLookup.get(name);
            }
        } else {
            island = null;
        }
    }

    public static Island getIsland() {
        return island;
    }

    public static boolean onIsland(Island island) {
        return LocationUtils.getIsland() != null && LocationUtils.getIsland().equals((Object)island);
    }

    public static enum Island {
        PRIVATE_ISLAND("Private Island"),
        HUB("Hub"),
        SPIDERS_DEN("Spider's Den"),
        BLAZING_FORTRESS("Blazing Fortress"),
        THE_END("The End"),
        THE_PARK("The Park"),
        GOLD_MINE("Gold Mine"),
        DEEP_CAVERNS("Deep Caverns"),
        DWARVEN_MINES("Dwarven Mines"),
        FARMING_ISLANDS("The Farming Islands"),
        DUNGEON_HUB("Dungeon Hub"),
        CRYSTAL_HOLLOWS("Crystal Hollows"),
        JERRYS_WORKSHOP("Jerry's Workshop");

        private String tabName;

        private Island(String tabName) {
            this.tabName = tabName;
        }

        public String getName() {
            return this.tabName;
        }
    }
}

