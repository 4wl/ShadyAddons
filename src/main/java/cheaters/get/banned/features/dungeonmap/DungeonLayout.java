/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 */
package cheaters.get.banned.features.dungeonmap;

import cheaters.get.banned.features.dungeonmap.Room;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class DungeonLayout {
    public ArrayList<RoomTile> roomTiles = new ArrayList();
    public ArrayList<ConnectorTile> connectorTiles = new ArrayList();
    public int totalSecrets;
    public int totalCrypts;
    public int totalPuzzles;
    public boolean uncertainCrypts = false;
    public TrapType trapType;
    public boolean sentScoreMessage = false;

    public static class RoomTile {
        public int x;
        public int z;
        public Room room;
        public boolean isLarge;
        public boolean explored;
        public boolean greenCheck;

        public RoomTile(int x, int z, Room room) {
            this.x = x;
            this.z = z;
            this.room = room;
        }
    }

    public static class ConnectorTile {
        public int x;
        public int z;
        public Type type;
        public boolean isOpen = false;
        public EnumFacing direction;

        public ConnectorTile(int x, int z, Type type, EnumFacing direction) {
            this.x = x;
            this.z = z;
            this.type = type;
            this.direction = direction;
        }

        public BlockPos getPosition(int y) {
            return new BlockPos(this.x, y, this.z);
        }

        static enum Type {
            WITHER_DOOR(Color.BLACK),
            BLOOD_DOOR(Room.Type.BLOOD.color),
            ENTRANCE_DOOR(Room.Type.ENTRANCE.color),
            NORMAL_DOOR(Room.Type.NORMAL.color),
            CONNECTOR(Room.Type.NORMAL.color),
            DEBUG(Color.MAGENTA);

            Color color;

            private Type(Color color) {
                this.color = color;
            }
        }
    }

    static enum TrapType {
        OLD,
        NEW;

    }
}

