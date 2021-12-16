/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.Utils;
import java.util.ArrayList;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoMelody {
    private boolean inHarp = false;
    private ArrayList<Item> lastInventory = new ArrayList();

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest && Utils.inSkyBlock && Config.autoMelody && Utils.getGuiName(event.gui).startsWith("Harp -")) {
            this.lastInventory.clear();
            this.inHarp = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!this.inHarp || !Config.autoMelody || Shady.mc.thePlayer == null) {
            return;
        }
        String inventoryName = Utils.getInventoryName();
        if (inventoryName == null || !inventoryName.startsWith("Harp -")) {
            this.inHarp = false;
        }
        ArrayList<Item> thisInventory = new ArrayList<Item>();
        for (Slot slot : Shady.mc.thePlayer.openContainer.inventorySlots) {
            if (slot.getStack() == null) continue;
            thisInventory.add(slot.getStack().getItem());
        }
        if (!this.lastInventory.toString().equals(thisInventory.toString())) {
            for (Slot slot : Shady.mc.thePlayer.openContainer.inventorySlots) {
                if (slot.getStack() == null || !(slot.getStack().getItem() instanceof ItemBlock) || ((ItemBlock)slot.getStack().getItem()).getBlock() != Blocks.quartz_block) continue;
                Shady.mc.playerController.windowClick(Shady.mc.thePlayer.openContainer.windowId, slot.slotNumber, 2, 0, (EntityPlayer)Shady.mc.thePlayer);
                break;
            }
        }
        this.lastInventory.clear();
        this.lastInventory.addAll(thisInventory);
    }
}

