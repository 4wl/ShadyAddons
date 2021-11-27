/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.CommandBase
 *  net.minecraft.command.CommandException
 *  net.minecraft.command.ICommandSender
 *  net.minecraft.util.ResourceLocation
 *  org.apache.commons.lang3.RandomStringUtils
 */
package cheaters.get.banned.config;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.ConfigGui;
import cheaters.get.banned.features.AutoTerminals;
import cheaters.get.banned.features.dungeonmap.DungeonMap;
import cheaters.get.banned.features.dungeonmap.Room;
import cheaters.get.banned.features.dungeonmap.RoomLoader;
import cheaters.get.banned.features.jokes.CatPeople;
import cheaters.get.banned.remote.MayorAPI;
import cheaters.get.banned.utils.DungeonUtils;
import cheaters.get.banned.utils.MathUtils;
import cheaters.get.banned.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.RandomStringUtils;

public class MainCommand
extends CommandBase {
    public String getName() {
        return "sh";
    }

    public List<String> getAliases() {
        return new ArrayList<String>(){
            {
                this.add("shady");
                this.add("shadyaddons");
            }
        };
    }

    public String getUsage(ICommandSender sender) {
        return "/" + this.getName();
    }

    public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
        block47: {
            block46: {
                if (!Shady.enabled) {
                    Utils.sendMessageAsPlayer("/" + RandomStringUtils.random((int)10, (boolean)true, (boolean)false));
                    return;
                }
                if (args.length <= 0) break block46;
                switch (args[0]) {
                    case "force_dungeon": {
                        Utils.forceDungeon = !Utils.forceDungeon;
                        Utils.sendModMessage("Toggled forcing dungeon to " + Utils.forceDungeon);
                        break;
                    }
                    case "force_paul": {
                        MayorAPI.forcePaul();
                        Utils.sendModMessage("Paul has committed a coup d'\u00e9tat and is now mayor of SkyBlock");
                        break;
                    }
                    case "force_skyblock": {
                        Utils.forceSkyBlock = !Utils.forceSkyBlock;
                        Utils.sendModMessage("Toggled forcing SkyBlock to " + Utils.forceSkyBlock);
                        break;
                    }
                    case "debug": {
                        if (args.length <= 1) break block47;
                        switch (args[1]) {
                            case "dungeon": {
                                if (Utils.inDungeon) {
                                    DungeonUtils.debug();
                                    break;
                                }
                                break block47;
                            }
                            case "crash": {
                                Shady.mc.renderEngine = null;
                            }
                            case "copylook": {
                                if (Shady.mc.objectMouseOver != null) {
                                    Utils.copyToClipboard(Shady.mc.objectMouseOver.getBlockPos().getX() + ", " + Shady.mc.objectMouseOver.getBlockPos().getY() + ", " + Shady.mc.objectMouseOver.getBlockPos().getZ());
                                    break;
                                }
                                break block47;
                            }
                            case "catperson": 
                            case "cat": {
                                CatPeople.addRandomCatPerson(MathUtils.random(0, 3));
                                Utils.sendModMessage("rawr");
                                break;
                            }
                            case "terminals": 
                            case "terms": {
                                AutoTerminals.testing = !AutoTerminals.testing;
                                Utils.sendModMessage("Toggled testing terminals to " + AutoTerminals.testing);
                                if (!Utils.forceDungeon) {
                                    Utils.sendMessageAsPlayer("sh force_dungeon");
                                    break;
                                }
                                break block47;
                            }
                            case "rooms": {
                                Utils.sendModMessage("There are currently " + RoomLoader.rooms.size() + " rooms loaded");
                                for (Room room : RoomLoader.rooms) {
                                    Utils.sendModMessage(room.name);
                                }
                                break block47;
                            }
                            case "scan": {
                                boolean bl = DungeonMap.debug = !DungeonMap.debug;
                                if (DungeonMap.debug) {
                                    if (DungeonMap.activeDungeonLayout != null) {
                                        Utils.sendModMessage("Rooms detected: " + DungeonMap.activeDungeonLayout.roomTiles.size());
                                        Utils.sendModMessage("Connectors detected: " + DungeonMap.activeDungeonLayout.connectorTiles.size());
                                        Utils.sendModMessage("Trap room type: " + (Object)((Object)DungeonMap.activeDungeonLayout.trapType));
                                        Utils.sendModMessage("Total secrets: " + DungeonMap.activeDungeonLayout.totalSecrets);
                                        Utils.sendModMessage("Total crypts: " + DungeonMap.activeDungeonLayout.totalCrypts);
                                        break;
                                    }
                                    Utils.sendModMessage("No scan exists");
                                } else {
                                    break;
                                }
                            }
                        }
                        break block47;
                    }
                    case "force_dungeon_floor": {
                        if (args.length > 1) {
                            for (DungeonUtils.Floor floor : DungeonUtils.Floor.values()) {
                                if (!floor.name.replaceAll("[()]", "").equalsIgnoreCase(args[1])) continue;
                                if (!Utils.forceDungeon) {
                                    Utils.sendMessageAsPlayer("sh force_dungeon");
                                }
                                DungeonUtils.dungeonRun.floor = floor;
                                Utils.sendModMessage("Set floor to " + (Object)((Object)DungeonUtils.dungeonRun.floor));
                                return;
                            }
                            Utils.sendModMessage("Unable to match \"" + args[1] + "\" to a dungeon floor");
                            break;
                        }
                        break block47;
                    }
                    case "disable": {
                        Shady.disable();
                        break;
                    }
                    default: {
                        Utils.sendModMessage("Unrecognized command");
                        break;
                    }
                }
                break block47;
            }
            Shady.guiToOpen = new ConfigGui(new ResourceLocation("shadyaddons:" + Utils.getLogo() + ".png"));
        }
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}

