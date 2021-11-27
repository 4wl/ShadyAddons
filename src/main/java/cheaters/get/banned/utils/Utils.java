/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.utils;

import cheaters.get.banned.Shady;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.ArrayUtils;
import cheaters.get.banned.utils.FontUtils;
import cheaters.get.banned.utils.ScoreboardUtils;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Utils {
    public static boolean inSkyBlock = false;
    public static boolean inDungeon = false;
    public static boolean forceSkyBlock = false;
    public static boolean forceDungeon = false;
    private int ticks = 0;

    public static int romanToInt(String s) {
        HashMap<Character, Integer> numerals = new HashMap<Character, Integer>();
        numerals.put(Character.valueOf('I'), 1);
        numerals.put(Character.valueOf('V'), 5);
        numerals.put(Character.valueOf('X'), 10);
        numerals.put(Character.valueOf('L'), 50);
        numerals.put(Character.valueOf('C'), 100);
        numerals.put(Character.valueOf('D'), 500);
        numerals.put(Character.valueOf('M'), 1000);
        int result = 0;
        for (int i = 0; i < s.length(); ++i) {
            int next;
            int add = (Integer)numerals.get(Character.valueOf(s.charAt(i)));
            if (i < s.length() - 1 && ((next = ((Integer)numerals.get(Character.valueOf(s.charAt(i + 1)))).intValue()) / add == 5 || next / add == 10)) {
                add = next - add;
                ++i;
            }
            result += add;
        }
        return result;
    }

    public static String getLogo() {
        ArrayList<String> logos = new ArrayList<String>(Arrays.asList("logo-fsr", "logo-sbe", "logo-pride"));
        int month = Calendar.getInstance().get(2);
        int day = Calendar.getInstance().get(5);
        if (month == 11 && day > 20) {
            return "logo-christmas";
        }
        if (month == 9 && day > 25) {
            return "logo-halloween";
        }
        if (month == 9 && day == 11) {
            return "logo-pride";
        }
        if (Math.random() < 0.8) {
            return "logo";
        }
        return (String)ArrayUtils.getRandomItem(logos);
    }

    public static void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static String removeFormatting(String input) {
        return input.replaceAll("\u00a7[0-9a-fk-or]", "");
    }

    public static String getSkyBlockID(ItemStack item) {
        NBTTagCompound extraAttributes;
        if (item != null && (extraAttributes = item.getSubCompound("ExtraAttributes", false)) != null && extraAttributes.hasKey("id")) {
            return extraAttributes.getString("id");
        }
        return "";
    }

    public static List<String> getLore(ItemStack item) {
        if (item != null) {
            return item.getTooltip((EntityPlayer)Shady.mc.player, false);
        }
        return null;
    }

    public static String getInventoryName() {
        if (Shady.mc.player == null || Shady.mc.world == null) {
            return "null";
        }
        return ((Slot)Shady.mc.player.openContainer.inventorySlots.get((int)0)).inventory.getName();
    }

    public static String getGuiName(GuiScreen gui) {
        if (gui instanceof GuiChest) {
            return ((ContainerChest)((GuiChest)gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }

    public static void sendMessage(String message) {
        if (Shady.mc.player != null && Shady.mc.world != null) {
            if (!message.contains("\u00a7")) {
                message = message.replace("&", "\u00a7");
            }
            Shady.mc.player.sendMessage((IChatComponent)new ChatComponentText(message));
        }
    }

    public static void sendMessageAsPlayer(String message) {
        Shady.mc.player.sendChatMessage(message);
    }

    public static void sendModMessage(String message) {
        if (message.contains("\u00a7")) {
            Utils.sendMessage("\u00a7" + FontUtils.getRainbowCode('c') + "ShadyAddons > \u00a7f" + message);
        } else {
            Utils.sendMessage("&" + FontUtils.getRainbowCode('c') + "ShadyAddons > &f" + message);
        }
    }

    public static Color addAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static boolean isInteractable(Block block) {
        return new ArrayList<Block>(Arrays.asList(new Block[]{Blocks.CHEST, Blocks.LEVER, Blocks.TRAPPED_CHEST, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, Blocks.SKULL})).contains((Object)block);
    }

    public static void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    @SafeVarargs
    public static <T> T firstNotNull(T ... args) {
        for (T arg : args) {
            if (arg == null) continue;
            return arg;
        }
        return null;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (forceDungeon || forceSkyBlock) {
            if (forceSkyBlock) {
                inSkyBlock = true;
            }
            if (forceDungeon) {
                inSkyBlock = true;
            }
            inDungeon = true;
            return;
        }
        if (this.ticks % 20 == 0) {
            if (Shady.mc.player != null && Shady.mc.world != null) {
                ScoreObjective scoreboardObj = Shady.mc.world.getScoreboard().getObjectiveInDisplaySlot(1);
                if (scoreboardObj != null) {
                    inSkyBlock = Utils.removeFormatting(scoreboardObj.getDisplayName()).contains("SKYBLOCK");
                }
                inDungeon = inSkyBlock && ScoreboardUtils.scoreboardContains("The Catacombs");
            }
            this.ticks = 0;
        }
        ++this.ticks;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        forceDungeon = false;
        forceSkyBlock = false;
    }
}

