/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraftforge.client.event.GuiScreenEvent$BackgroundDrawnEvent
 *  net.minecraftforge.client.event.GuiScreenEvent$MouseInputEvent$Pre
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.input.Mouse
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.config.components.ClearButton;
import cheaters.get.banned.events.DrawSlotEvent;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.Utils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class AutoSalvage {
    private static final ClearButton button = new ClearButton(0, 0, 0, 71, 20, "\u00a7aStart");
    private boolean inSalvageGui = false;
    private boolean salvaging = false;
    private int tickCount = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (this.tickCount % 5 == 0 && Config.autoSalvage && this.inSalvageGui && this.salvaging && Shady.mc.currentScreen instanceof GuiChest) {
            List chestInventory = ((GuiChest)Shady.mc.currentScreen).inventorySlots.inventorySlots;
            if (((Slot)chestInventory.get(31)).getStack() != null && ((Slot)chestInventory.get(31)).getStack().getItem() == Items.skull) {
                if (chestInventory.get(22) != null && ((Slot)chestInventory.get(22)).getStack() != null & ((Slot)chestInventory.get(22)).getStack().getItem() == Item.getItemFromBlock((Block)Blocks.stained_hardened_clay)) {
                    Shady.mc.playerController.windowClick(Shady.mc.thePlayer.openContainer.windowId, 22, 2, 0, (EntityPlayer)Shady.mc.thePlayer);
                }
                if (chestInventory.get(13) != null && ((Slot)chestInventory.get(13)).getStack() != null && chestInventory.get(22) != null && ((Slot)chestInventory.get(22)).getStack() != null && ((Slot)chestInventory.get(22)).getStack().getItem() == Item.getItemFromBlock((Block)Blocks.anvil)) {
                    Shady.mc.playerController.windowClick(Shady.mc.thePlayer.openContainer.windowId, 22, 2, 0, (EntityPlayer)Shady.mc.thePlayer);
                }
            } else if (chestInventory.get(13) != null && ((Slot)chestInventory.get(13)).getStack() == null) {
                ArrayList<Slot> itemsToSalvage = new ArrayList<Slot>(Shady.mc.thePlayer.inventoryContainer.inventorySlots);
                itemsToSalvage.removeIf(slot -> !AutoSalvage.shouldSalvage(slot.getStack()));
                if (itemsToSalvage.isEmpty()) {
                    this.salvaging = false;
                } else {
                    Shady.mc.playerController.windowClick(Shady.mc.thePlayer.openContainer.windowId, 45 + ((Slot)itemsToSalvage.get((int)0)).slotNumber, 0, 1, (EntityPlayer)Shady.mc.thePlayer);
                }
            }
        }
        ++this.tickCount;
    }

    @SubscribeEvent
    public void onBackgroundRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        String chestName = Utils.getGuiName(event.gui);
        this.inSalvageGui = chestName.equals("Salvage Dungeon Item");
        if (Config.autoSalvage && this.inSalvageGui) {
            if (Config.automaticallyStartSalvaging) {
                this.salvaging = true;
            } else {
                ScaledResolution scaledResolution = new ScaledResolution(Shady.mc);
                AutoSalvage.button.xPosition = (scaledResolution.getScaledWidth() - AutoSalvage.button.width) / 2;
                AutoSalvage.button.yPosition = scaledResolution.getScaledHeight() / 2 - 145;
                AutoSalvage.button.displayString = this.salvaging ? "\u00a7cStop" : "\u00a7aStart";
                button.drawButton(event.gui.mc, event.getMouseX(), event.getMouseY());
            }
        } else {
            this.salvaging = false;
        }
    }

    @SubscribeEvent
    public void onDrawSlot(DrawSlotEvent event) {
        if (Config.autoSalvage && this.inSalvageGui && button.isMouseOver() && AutoSalvage.shouldSalvage(event.slot.getStack())) {
            int x = event.slot.xDisplayPosition;
            int y = event.slot.yDisplayPosition;
            Gui.drawRect((int)x, (int)y, (int)(x + 16), (int)(y + 16), (int)Utils.addAlpha(Color.RED, 128).getRGB());
        }
    }

    @SubscribeEvent
    public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (Utils.inSkyBlock && Config.autoSalvage && this.inSalvageGui && button.isMouseOver() && !Config.automaticallyStartSalvaging && Mouse.isButtonDown((int)0)) {
            this.salvaging = !this.salvaging;
        }
    }

    public static boolean shouldSalvage(ItemStack item) {
        if (item == null) {
            return false;
        }
        NBTTagCompound attributes = item.getSubCompound("ExtraAttributes", false);
        if (attributes == null) {
            return false;
        }
        if (!attributes.hasKey("baseStatBoostPercentage") || attributes.hasKey("dungeon_item_level")) {
            return false;
        }
        return !Utils.getSkyBlockID(item).equals("ICE_SPRAY_WAND");
    }
}

