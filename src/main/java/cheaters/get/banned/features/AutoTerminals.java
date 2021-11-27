/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.EnumDyeColor
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.StringUtils
 *  net.minecraftforge.client.event.GuiScreenEvent$BackgroundDrawnEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoTerminals {
    private static final ArrayList<Slot> clickQueue = new ArrayList(28);
    private static final int[] mazeDirection = new int[]{-9, -1, 1, 9};
    private static TerminalType currentTerminal = TerminalType.NONE;
    private static long lastClickTime = 0L;
    private static int windowId = 0;
    private static int windowClicks = 0;
    private static boolean recalculate = false;
    public static boolean testing = false;

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
        Container container;
        if (!Config.autoTerminals || !Utils.inDungeon) {
            return;
        }
        if (event.gui instanceof GuiChest && (container = ((GuiChest)event.gui).inventorySlots) instanceof ContainerChest) {
            List invSlots = container.inventorySlots;
            if (currentTerminal == TerminalType.NONE) {
                String chestName = ((ContainerChest)container).getLowerChestInventory().getDisplayName().getUnformattedText();
                if (chestName.equals("Navigate the maze!")) {
                    currentTerminal = TerminalType.MAZE;
                } else if (chestName.equals("Click in order!")) {
                    currentTerminal = TerminalType.NUMBERS;
                } else if (chestName.equals("Correct all the panes!")) {
                    currentTerminal = TerminalType.CORRECT_ALL;
                } else if (chestName.startsWith("What starts with: '")) {
                    currentTerminal = TerminalType.LETTER;
                } else if (chestName.startsWith("Select all the")) {
                    currentTerminal = TerminalType.COLOR;
                }
            }
            if (currentTerminal != TerminalType.NONE) {
                if (clickQueue.isEmpty() || recalculate) {
                    recalculate = this.getClicks((ContainerChest)container);
                } else {
                    switch (currentTerminal) {
                        case MAZE: 
                        case NUMBERS: 
                        case CORRECT_ALL: {
                            clickQueue.removeIf(slot -> ((Slot)invSlots.get(slot.slotNumber)).getHasStack() && ((Slot)invSlots.get(slot.slotNumber)).getStack().getItemDamage() == 5);
                            break;
                        }
                        case LETTER: 
                        case COLOR: {
                            clickQueue.removeIf(slot -> ((Slot)invSlots.get(slot.slotNumber)).getHasStack() && ((Slot)invSlots.get(slot.slotNumber)).getStack().isItemEnchanted());
                        }
                    }
                }
                if (!clickQueue.isEmpty() && Config.autoTerminals && System.currentTimeMillis() - lastClickTime > (long)Config.terminalClickDelay) {
                    this.clickSlot(clickQueue.get(0));
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Config.autoTerminals || !Utils.inDungeon) {
            return;
        }
        if (Shady.mc.player == null || Shady.mc.world == null) {
            return;
        }
        if (!(Shady.mc.currentScreen instanceof GuiChest)) {
            currentTerminal = TerminalType.NONE;
            clickQueue.clear();
            windowClicks = 0;
        }
    }

    private boolean getClicks(ContainerChest container) {
        List invSlots = container.inventorySlots;
        String chestName = container.getLowerChestInventory().getDisplayName().getUnformattedText();
        clickQueue.clear();
        switch (currentTerminal) {
            case MAZE: {
                int[] mazeDirection = new int[]{-9, -1, 1, 9};
                boolean[] isStartSlot = new boolean[54];
                int endSlot = -1;
                for (Slot slot : invSlots) {
                    ItemStack itemStack;
                    if (slot.inventory == Shady.mc.player.inventory || (itemStack = slot.getStack()) == null || itemStack.getItem() != Item.getItemFromBlock((Block)Blocks.STAINED_GLASS_PANE)) continue;
                    if (itemStack.getItemDamage() == 5) {
                        isStartSlot[slot.slotNumber] = true;
                        continue;
                    }
                    if (itemStack.getItemDamage() != 14) continue;
                    endSlot = slot.slotNumber;
                }
                for (int slot = 0; slot < 54; ++slot) {
                    if (!isStartSlot[slot]) continue;
                    boolean[] mazeVisited = new boolean[54];
                    int startSlot = slot;
                    while (startSlot != endSlot) {
                        boolean newSlotChosen = false;
                        for (int i : mazeDirection) {
                            ItemStack itemStack;
                            int nextSlot = startSlot + i;
                            if (nextSlot < 0 || nextSlot > 53 || i == -1 && startSlot % 9 == 0 || i == 1 && startSlot % 9 == 8) continue;
                            if (nextSlot == endSlot) {
                                return false;
                            }
                            if (mazeVisited[nextSlot] || (itemStack = ((Slot)invSlots.get(nextSlot)).getStack()) == null || itemStack.getItem() != Item.getItemFromBlock((Block)Blocks.STAINED_GLASS_PANE) || itemStack.getItemDamage() != 0) continue;
                            clickQueue.add((Slot)invSlots.get(nextSlot));
                            startSlot = nextSlot;
                            mazeVisited[nextSlot] = true;
                            newSlotChosen = true;
                            break;
                        }
                        if (newSlotChosen) continue;
                        System.out.println("Maze calculation aborted");
                        return true;
                    }
                }
                return true;
            }
            case NUMBERS: {
                int min = 0;
                Slot[] temp = new Slot[14];
                for (int i = 10; i <= 25; ++i) {
                    ItemStack itemStack;
                    if (i == 17 || i == 18 || (itemStack = ((Slot)invSlots.get(i)).getStack()) == null || itemStack.getItem() != Item.getItemFromBlock((Block)Blocks.STAINED_GLASS_PANE) || itemStack.stackSize >= 15) continue;
                    if (itemStack.getItemDamage() == 14) {
                        temp[itemStack.stackSize - 1] = (Slot)invSlots.get(i);
                        continue;
                    }
                    if (itemStack.getItemDamage() != 5 || min >= itemStack.stackSize) continue;
                    min = itemStack.stackSize;
                }
                clickQueue.addAll(Arrays.stream(temp).filter(Objects::nonNull).collect(Collectors.toList()));
                if (clickQueue.size() == 14 - min) break;
                return true;
            }
            case CORRECT_ALL: {
                for (Slot slot : invSlots) {
                    if (slot.inventory == Shady.mc.player.inventory || slot.slotNumber < 9 || slot.slotNumber > 35 || slot.slotNumber % 9 <= 1 || slot.slotNumber % 9 >= 7) continue;
                    ItemStack itemStack = slot.getStack();
                    if (itemStack == null) {
                        return true;
                    }
                    if (itemStack.getItem() != Item.getItemFromBlock((Block)Blocks.STAINED_GLASS_PANE) || itemStack.getItemDamage() != 14) continue;
                    clickQueue.add(slot);
                }
                break;
            }
            case LETTER: {
                if (chestName.length() <= chestName.indexOf("'") + 1) break;
                char letterNeeded = chestName.charAt(chestName.indexOf("'") + 1);
                for (Slot slot : invSlots) {
                    if (slot.inventory == Shady.mc.player.inventory || slot.slotNumber < 9 || slot.slotNumber > 44 || slot.slotNumber % 9 == 0 || slot.slotNumber % 9 == 8) continue;
                    ItemStack itemStack = slot.getStack();
                    if (itemStack == null) {
                        return true;
                    }
                    if (itemStack.isItemEnchanted() || StringUtils.stripControlCodes((String)itemStack.getDisplayName()).charAt(0) != letterNeeded) continue;
                    clickQueue.add(slot);
                }
                break;
            }
            case COLOR: {
                String colorNeeded = null;
                for (EnumDyeColor color : EnumDyeColor.values()) {
                    String colorName = color.getName().replaceAll("_", " ").toUpperCase();
                    if (!chestName.contains(colorName)) continue;
                    colorNeeded = color.getTranslationKey();
                    break;
                }
                if (colorNeeded == null) break;
                for (Slot slot : invSlots) {
                    if (slot.inventory == Shady.mc.player.inventory || slot.slotNumber < 9 || slot.slotNumber > 44 || slot.slotNumber % 9 == 0 || slot.slotNumber % 9 == 8) continue;
                    ItemStack itemStack = slot.getStack();
                    if (itemStack == null) {
                        return true;
                    }
                    if (itemStack.isItemEnchanted() || !itemStack.getTranslationKey().contains(colorNeeded)) continue;
                    clickQueue.add(slot);
                }
                break;
            }
        }
        return false;
    }

    private void clickSlot(Slot slot) {
        if (windowClicks == 0) {
            windowId = Shady.mc.player.openContainer.windowId;
        }
        if (testing) {
            Shady.mc.playerController.func_78753_a(windowId + windowClicks, slot.slotNumber, 0, 1, (EntityPlayer)Shady.mc.player);
        } else {
            Shady.mc.playerController.func_78753_a(windowId + windowClicks, slot.slotNumber, 2, 0, (EntityPlayer)Shady.mc.player);
        }
        lastClickTime = System.currentTimeMillis();
        if (Config.terminalPingless) {
            ++windowClicks;
            clickQueue.remove((Object)slot);
        }
    }

    private static enum TerminalType {
        MAZE,
        NUMBERS,
        CORRECT_ALL,
        LETTER,
        COLOR,
        NONE;

    }
}

