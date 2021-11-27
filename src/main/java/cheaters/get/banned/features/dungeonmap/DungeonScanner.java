/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockColored
 *  net.minecraft.block.BlockSilverfish
 *  net.minecraft.block.BlockSilverfish$EnumType
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.EnumDyeColor
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features.dungeonmap;

import cheaters.get.banned.Shady;
import cheaters.get.banned.events.BlockChangeEvent;
import cheaters.get.banned.features.dungeonmap.BlockList;
import cheaters.get.banned.features.dungeonmap.DungeonLayout;
import cheaters.get.banned.features.dungeonmap.DungeonMap;
import cheaters.get.banned.features.dungeonmap.Room;
import cheaters.get.banned.features.dungeonmap.RoomLoader;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DungeonScanner {
    public static DungeonLayout scan(int xCorner, int zCorner) {
        DungeonLayout newDungeonLayout = new DungeonLayout();
        for (int x = 15; x < xCorner + 1; x += 32) {
            for (int z = 15; z < zCorner + 1; z += 32) {
                boolean isColumnEmpty = DungeonScanner.isColumnNotEmpty(x, z);
                if (isColumnEmpty) {
                    block2: for (int y : RoomLoader.yLevels) {
                        BlockList blockList = new BlockList(x, y, z);
                        for (Room room : RoomLoader.rooms) {
                            if (!room.matches(blockList)) continue;
                            newDungeonLayout.totalSecrets += room.secrets;
                            if (room.crypts == null) {
                                newDungeonLayout.uncertainCrypts = true;
                            } else {
                                newDungeonLayout.totalCrypts += room.crypts.intValue();
                            }
                            if (room.type == Room.Type.PUZZLE) {
                                ++newDungeonLayout.totalPuzzles;
                            }
                            if (room.type == Room.Type.TRAP) {
                                if (room.name.contains("Old")) {
                                    newDungeonLayout.trapType = DungeonLayout.TrapType.OLD;
                                } else if (room.name.contains("New")) {
                                    newDungeonLayout.trapType = DungeonLayout.TrapType.NEW;
                                }
                            }
                            newDungeonLayout.roomTiles.add(new DungeonLayout.RoomTile(x, z, room));
                            continue block2;
                        }
                    }
                }
                newDungeonLayout.connectorTiles.add(DungeonScanner.getConnectorAt(x, z - 16, EnumFacing.NORTH));
                newDungeonLayout.connectorTiles.add(DungeonScanner.getConnectorAt(x + 16, z, EnumFacing.EAST));
                newDungeonLayout.connectorTiles.add(DungeonScanner.getConnectorAt(x, z + 16, EnumFacing.SOUTH));
                newDungeonLayout.connectorTiles.add(DungeonScanner.getConnectorAt(x - 16, z, EnumFacing.WEST));
            }
        }
        ArrayList<DungeonLayout.RoomTile> roomTilesToAdd = new ArrayList<DungeonLayout.RoomTile>();
        for (int x = 15; x < xCorner + 1; x += 32) {
            for (int z = 15; z < zCorner + 1; z += 32) {
                if (DungeonScanner.layoutContainsRoomAtPosition(newDungeonLayout, x, z) || !DungeonScanner.isColumnNotEmpty(x, z)) continue;
                roomTilesToAdd.add(new DungeonLayout.RoomTile(x, z, Room.GENERIC_NORMAL_ROOM));
            }
        }
        newDungeonLayout.roomTiles.addAll(roomTilesToAdd);
        return newDungeonLayout;
    }

    private static DungeonLayout.ConnectorTile getConnectorAt(int x, int z, EnumFacing direction) {
        BlockPos doorBlock = new BlockPos(x, 69, z);
        IBlockState blockState = Shady.mc.world.getBlockState(doorBlock);
        Block block = blockState.getBlock();
        DungeonLayout.ConnectorTile.Type type = DungeonLayout.ConnectorTile.Type.CONNECTOR;
        if (block == Blocks.COAL_BLOCK) {
            type = DungeonLayout.ConnectorTile.Type.WITHER_DOOR;
        }
        if (block == Blocks.STAINED_HARDENED_CLAY && blockState.getValue((IProperty)BlockColored.COLOR) == EnumDyeColor.RED) {
            type = DungeonLayout.ConnectorTile.Type.BLOOD_DOOR;
        }
        if (block == Blocks.MONSTER_EGG && blockState.getValue((IProperty)BlockSilverfish.VARIANT) == BlockSilverfish.EnumType.CHISELED_STONEBRICK) {
            type = DungeonLayout.ConnectorTile.Type.ENTRANCE_DOOR;
        }
        if (block == Blocks.AIR) {
            type = Shady.mc.world.getBlockState(doorBlock.down(2)).getBlock() == Blocks.BEDROCK && Shady.mc.world.getBlockState(doorBlock.up(4)).getBlock() != Blocks.AIR ? DungeonLayout.ConnectorTile.Type.NORMAL_DOOR : (DungeonScanner.isColumnNotEmpty(x, z) ? DungeonLayout.ConnectorTile.Type.CONNECTOR : null);
        }
        return new DungeonLayout.ConnectorTile(x, z, type, direction);
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (DungeonMap.activeDungeonLayout != null && (event.oldBlock.getBlock() == Blocks.COAL_BLOCK || event.oldBlock.getBlock() == Blocks.STAINED_HARDENED_CLAY) && event.newBlock.getBlock() == Blocks.AIR) {
            for (DungeonLayout.ConnectorTile door : DungeonMap.activeDungeonLayout.connectorTiles) {
                if ((door.isOpen || event.oldBlock.getBlock() != Blocks.COAL_BLOCK || door.type != DungeonLayout.ConnectorTile.Type.WITHER_DOOR) && (event.oldBlock.getBlock() != Blocks.STAINED_HARDENED_CLAY || door.type != DungeonLayout.ConnectorTile.Type.BLOOD_DOOR) || !event.position.equals((Object)door.getPosition(69))) continue;
                door.isOpen = true;
            }
        }
    }

    private static boolean isColumnNotEmpty(int x, int z) {
        for (int y = 256; y > 0; --y) {
            if (Shady.mc.world == null || Shady.mc.world.isAirBlock(new BlockPos(x, y, z))) continue;
            return true;
        }
        return false;
    }

    private static boolean layoutContainsRoomAtPosition(DungeonLayout layout, int x, int z) {
        for (DungeonLayout.RoomTile room : layout.roomTiles) {
            if (room.x != x || room.z != z) continue;
            return true;
        }
        return false;
    }
}

