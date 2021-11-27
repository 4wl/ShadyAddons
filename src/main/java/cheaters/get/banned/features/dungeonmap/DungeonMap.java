/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features.dungeonmap;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.features.dungeonmap.DungeonLayout;
import cheaters.get.banned.features.dungeonmap.DungeonScanner;
import cheaters.get.banned.features.dungeonmap.Room;
import cheaters.get.banned.utils.DungeonUtils;
import cheaters.get.banned.utils.FontUtils;
import cheaters.get.banned.utils.RenderUtils;
import cheaters.get.banned.utils.Utils;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DungeonMap {
    private static int xCorner;
    private static int zCorner;
    public static DungeonLayout activeDungeonLayout;
    public static boolean scanning;
    public static boolean debug;
    public static final HashMap<String, String> shortNames;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        activeDungeonLayout = null;
        scanning = false;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Utils.inDungeon) {
            if (activeDungeonLayout == null) {
                if (DungeonUtils.dungeonRun != null && DungeonUtils.dungeonRun.floor != null && !scanning) {
                    switch (DungeonUtils.dungeonRun.floor) {
                        case FLOOR_1: 
                        case MASTER_1: {
                            xCorner = 127;
                            zCorner = 159;
                            break;
                        }
                        case FLOOR_2: 
                        case MASTER_2: {
                            xCorner = 159;
                            zCorner = 159;
                            break;
                        }
                        default: {
                            xCorner = 191;
                            zCorner = 191;
                        }
                    }
                    if (Shady.mc.world.getChunk(new BlockPos(xCorner, 70, zCorner)).isLoaded() && Shady.mc.world.getChunk(new BlockPos(0, 70, 0)).isLoaded()) {
                        scanning = true;
                        new Thread(() -> {
                            activeDungeonLayout = DungeonScanner.scan(xCorner, zCorner);
                            scanning = false;
                        }, "ShadyAddons-DungeonScanner").start();
                    }
                }
            } else if (Config.announceScore && !DungeonMap.activeDungeonLayout.sentScoreMessage && DungeonUtils.calculateScore() >= Config.announceScoreNumber) {
                DungeonMap.activeDungeonLayout.sentScoreMessage = true;
                String chatPrefix = (new String[]{"/pc", "/ac", "/gc", "/r"})[Config.announceScoreChat];
                Utils.sendMessageAsPlayer(chatPrefix + Config.announceScoreNumber + " score reached");
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (activeDungeonLayout != null) {
            if (debug) {
                for (DungeonLayout.ConnectorTile connectorTile : DungeonMap.activeDungeonLayout.connectorTiles) {
                    if (connectorTile.type == null) continue;
                    RenderUtils.highlightBlock(new BlockPos(connectorTile.x, 69, connectorTile.z), connectorTile.type == DungeonLayout.ConnectorTile.Type.CONNECTOR ? Color.BLUE : connectorTile.type.color, event.partialTicks);
                }
                for (DungeonLayout.RoomTile roomTile : DungeonMap.activeDungeonLayout.roomTiles) {
                    RenderUtils.highlightBlock(new BlockPos(roomTile.x, 99, roomTile.z), roomTile.room.type.color, event.partialTicks);
                }
            }
            if (Config.witherDoorESP && DungeonUtils.dungeonRun != null && !DungeonUtils.dungeonRun.inBoss) {
                for (DungeonLayout.ConnectorTile door : DungeonMap.activeDungeonLayout.connectorTiles) {
                    Color color;
                    if ((door.isOpen || door.type != DungeonLayout.ConnectorTile.Type.WITHER_DOOR) && door.type != DungeonLayout.ConnectorTile.Type.BLOOD_DOOR) continue;
                    Color color2 = color = Config.witherDoorColor == 0 ? Color.WHITE : Color.BLACK;
                    if (door.type == DungeonLayout.ConnectorTile.Type.BLOOD_DOOR) {
                        color = Color.RED;
                    }
                    Iterable positions = door.direction == EnumFacing.NORTH || door.direction == EnumFacing.SOUTH ? BlockPos.getAllInBox((BlockPos)door.getPosition(69).west(1), (BlockPos)door.getPosition(72).east(1)) : BlockPos.getAllInBox((BlockPos)door.getPosition(69).north(1), (BlockPos)door.getPosition(72).south(1));
                    for (BlockPos position : positions) {
                        RenderUtils.highlightBlock(position, color, event.partialTicks);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (!Config.dungeonMap || event.type != RenderGameOverlayEvent.ElementType.HOTBAR || !Utils.inDungeon || DungeonUtils.dungeonRun == null || DungeonUtils.dungeonRun.inBoss) {
            return;
        }
        float scale = (float)Config.mapScale / 100.0f;
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)scale, (float)scale, (float)scale);
        GlStateManager.translate((float)Config.mapXOffset, (float)Config.mapYOffset, (float)0.0f);
        if (!scanning && activeDungeonLayout != null && Config.showDungeonInformation) {
            Gui.drawRect((int)0, (int)0, (int)200, (int)230, (int)new Color(0, 0, 0, 255 * Config.mapBackgroundOpacity / 100).getRGB());
        } else {
            Gui.drawRect((int)0, (int)0, (int)200, (int)200, (int)new Color(0, 0, 0, 255 * Config.mapBackgroundOpacity / 100).getRGB());
        }
        if (scanning) {
            FontUtils.drawCenteredString("Scanning Dungeon...", 100, 100);
            GlStateManager.popMatrix();
            return;
        }
        if (activeDungeonLayout == null) {
            FontUtils.drawCenteredString("\u00a7cNot Scanned", 100, 100);
            GlStateManager.popMatrix();
            return;
        }
        GlStateManager.translate((float)((float)(200 - xCorner) / 2.0f), (float)((float)(200 - zCorner) / 2.0f), (float)0.0f);
        for (DungeonLayout.ConnectorTile connector : DungeonMap.activeDungeonLayout.connectorTiles) {
            if (connector.type == null) continue;
            int x1 = connector.x - 4;
            int y1 = connector.z - 2;
            int x2 = connector.x + 4;
            int y2 = connector.z + 2;
            if (connector.direction == EnumFacing.EAST || connector.direction == EnumFacing.WEST) {
                x1 = connector.x - 2;
                y1 = connector.z - 6;
                x2 = connector.x + 2;
                y2 = connector.z + 6;
            }
            if (connector.type == DungeonLayout.ConnectorTile.Type.CONNECTOR) {
                x1 = connector.x - 14;
                y1 = connector.z - 2;
                x2 = connector.x + 14;
                y2 = connector.z + 2;
                if (connector.direction == EnumFacing.EAST || connector.direction == EnumFacing.WEST) {
                    x1 = connector.x - 2;
                    y1 = connector.z - 14;
                    x2 = connector.x + 2;
                    y2 = connector.z + 14;
                }
            }
            Gui.drawRect((int)x1, (int)y1, (int)x2, (int)y2, (int)connector.type.color.getRGB());
        }
        for (DungeonLayout.RoomTile room : DungeonMap.activeDungeonLayout.roomTiles) {
            Gui.drawRect((int)(room.x - 14), (int)(room.z - 14), (int)(room.x + 14), (int)(room.z + 14), (int)room.room.type.color.getRGB());
        }
        for (DungeonLayout.RoomTile room : DungeonMap.activeDungeonLayout.roomTiles) {
            if (room.room.type != Room.Type.PUZZLE && (room.room.type != Room.Type.TRAP || Config.significantRoomNameStyle == 2)) continue;
            switch (Config.significantRoomNameStyle) {
                case 0: {
                    FontUtils.drawCenteredString(DungeonMap.getShortenedRoomName(room.room.name), room.x, room.z);
                    break;
                }
                case 1: {
                    FontUtils.drawCenteredString(room.room.name.replace(" ", "\n"), room.x, room.z);
                }
            }
        }
        if (Shady.mc.player != null) {
            int size = 14;
            HashSet<String> players = DungeonUtils.dungeonRun.team;
            players.add(Shady.mc.getSession().getUsername());
            try {
                for (String player : DungeonUtils.dungeonRun.team) {
                    EntityPlayer playerEntity = Shady.mc.world.getPlayerEntityByName(player);
                    if (playerEntity == null || playerEntity.getPosition() == null) continue;
                    int playerX = MathHelper.clamp((int)(playerEntity.getPosition().getX() - size / 2), (int)0, (int)(xCorner - 14));
                    int playerZ = MathHelper.clamp((int)(playerEntity.getPosition().getZ() - size / 2), (int)0, (int)(zCorner - 14));
                    float playerRotation = playerEntity.getRotationYawHead() - 180.0f;
                    DungeonMap.drawPlayerIcon(playerEntity, size, playerX, playerZ, (int)playerRotation);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        GlStateManager.translate((float)(-((float)(200 - xCorner) / 2.0f)), (float)(-((float)(200 - zCorner) / 2.0f)), (float)0.0f);
        if (Config.showDungeonInformation) {
            String dungeonStats = "\u00a77Secrets: \u00a7a" + DungeonUtils.dungeonRun.secretsFound + "\u00a77/" + DungeonMap.activeDungeonLayout.totalSecrets + "   Crypts: \u00a7a" + DungeonUtils.dungeonRun.cryptsFound + "\u00a77/" + (DungeonMap.activeDungeonLayout.uncertainCrypts ? "~" : "") + DungeonMap.activeDungeonLayout.totalCrypts + "\n";
            dungeonStats = dungeonStats + "\u00a77Puzzles: \u00a7a" + DungeonMap.activeDungeonLayout.totalPuzzles + "\u00a77   Deaths: \u00a7c" + DungeonUtils.dungeonRun.deaths + "\u00a77   Score: \u00a7e~" + DungeonUtils.calculateScore();
            FontUtils.drawCenteredString(dungeonStats, 100, 212);
        }
        GlStateManager.popMatrix();
    }

    private static void drawPlayerIcon(EntityPlayer player, int size, int x, int y, int angle) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)((float)x + (float)size / 2.0f), (float)((float)y + (float)size / 2.0f), (float)0.0f);
        GlStateManager.rotate((float)angle, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.translate((float)((float)(-x) - (float)size / 2.0f), (float)((float)(-y) - (float)size / 2.0f), (float)0.0f);
        Gui.drawRect((int)x, (int)y, (int)(x + size), (int)(y + size), (int)Color.BLACK.getRGB());
        GlStateManager.color((float)255.0f, (float)255.0f, (float)255.0f);
        RenderUtils.drawPlayerIcon(player, size - 2, x + 1, y + 1);
        GlStateManager.popMatrix();
    }

    private static String getShortenedRoomName(String longName) {
        String shortName = shortNames.get(longName);
        return shortName == null ? longName : shortName;
    }

    static {
        activeDungeonLayout = null;
        scanning = false;
        debug = false;
        shortNames = new HashMap<String, String>(){
            {
                this.put("Old Trap", "Old");
                this.put("New Trap", "New");
                this.put("Boulder", "Box");
                this.put("Creeper Beams", "Beams");
                this.put("Teleport Maze", "Maze");
                this.put("Ice Path", "S.Fish");
                this.put("Ice Fill", "Fill");
                this.put("Tic Tac Toe", "TTT");
                this.put("Water Board", "Water");
                this.put("Bomb Defuse", "Bomb");
            }
        };
    }
}

