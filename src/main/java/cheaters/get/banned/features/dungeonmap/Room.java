/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 */
package cheaters.get.banned.features.dungeonmap;

import cheaters.get.banned.features.dungeonmap.BlockList;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;

public class Room {
    public String name;
    public Type type;
    public int secrets;
    public Integer crypts;
    public int yLevel;
    public HashMap<Block, String> blockConditions;
    public static final Room GENERIC_NORMAL_ROOM = new Room("", Type.NORMAL, 0, 0, 0, new HashMap<Block, String>());

    public Room() {
    }

    public Room(String name, Type type, int secrets, Integer crypts, int yLevel, HashMap<Block, String> blockConditions) {
        this.name = name;
        this.type = type;
        this.secrets = secrets;
        this.crypts = crypts;
        this.yLevel = yLevel;
        this.blockConditions = blockConditions;
    }

    public boolean matches(BlockList blockList) {
        int conditionsMet = 0;
        for (Map.Entry<Block, String> condition : this.blockConditions.entrySet()) {
            Block block = condition.getKey();
            char comparisonOperator = condition.getValue().charAt(0);
            int valueToCompareAgainst = Integer.parseInt(condition.getValue().substring(1));
            HashMap<Block, Integer> scannedBlocks = blockList.getBlocks();
            if (!scannedBlocks.containsKey((Object)block)) continue;
            switch (comparisonOperator) {
                case '=': {
                    if (valueToCompareAgainst != scannedBlocks.get((Object)block)) break;
                    ++conditionsMet;
                    break;
                }
                case '<': {
                    if (valueToCompareAgainst <= scannedBlocks.get((Object)block)) break;
                    ++conditionsMet;
                    break;
                }
                case '>': {
                    if (valueToCompareAgainst >= scannedBlocks.get((Object)block)) break;
                    ++conditionsMet;
                }
            }
        }
        return conditionsMet == this.blockConditions.size();
    }

    static enum Type {
        NORMAL(new Color(107, 58, 17)),
        ENTRANCE(new Color(20, 133, 0)),
        BLOOD(new Color(255, 0, 0)),
        PUZZLE(new Color(117, 0, 133)),
        MINIBOSS(new Color(254, 223, 0)),
        FAIRY(new Color(224, 0, 255)),
        TRAP(new Color(216, 127, 51)),
        UNKNOWN(new Color(83, 83, 83));

        Color color;

        private Type(Color color) {
            this.color = color;
        }
    }
}

