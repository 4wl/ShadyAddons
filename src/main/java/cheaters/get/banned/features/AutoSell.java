/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.client.event.GuiScreenEvent$BackgroundDrawnEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.features.AutoSalvage;
import cheaters.get.banned.utils.Utils;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSell {
    private boolean inTradeMenu = false;
    private int tickCount = 0;
    private static final String[] salable = new String[]{"Training Weight", "Healing Potion VIII", "Healing Potion 8", "Beating Heart", "Premium Flesh", "Mimic Fragment", "Enchanted Rotten Flesh", "Enchanted Bone", "Defuse Kit", "Enchanted Ice", "Optic Lense", "Tripwire Hook", "Button", "Carpet", "Lever", "Rune", "Journal Entry", "Sign"};

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        List chestInventory;
        if (this.tickCount % 3 == 0 && Utils.inSkyBlock && Config.autoSell && this.inTradeMenu && Shady.mc.currentScreen instanceof GuiChest && ((Slot)(chestInventory = ((GuiChest)Shady.mc.currentScreen).inventorySlots.inventorySlots).get(49)).getStack() != null && ((Slot)chestInventory.get(49)).getStack().getItem() != Item.getItemFromBlock((Block)Blocks.BARRIER)) {
            for (Slot slot : Shady.mc.player.inventoryContainer.inventorySlots) {
                if (!this.shouldSell(slot.getStack())) continue;
                Shady.mc.playerController.func_78753_a(Shady.mc.player.openContainer.windowId, 45 + slot.slotNumber, 2, 0, (EntityPlayer)Shady.mc.player);
                break;
            }
        }
        ++this.tickCount;
    }

    @SubscribeEvent
    public void onBackgroundRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        String chestName = Utils.getGuiName(event.gui);
        this.inTradeMenu = chestName.equals("Trades");
    }

    private boolean shouldSell(ItemStack item) {
        if (item != null) {
            if (Config.autoSellSalvageable && AutoSalvage.shouldSalvage(item)) {
                return true;
            }
            if (Config.autoSellSuperboom && Utils.getSkyBlockID(item).equals("SUPERBOOM_TNT")) {
                return true;
            }
            if (Config.autoSellPotions && item.getDisplayName().contains("Potion") && (item.getDisplayName().contains("Speed") || item.getDisplayName().contains("Weakness"))) {
                return true;
            }
            if (Config.autoSellDungeonsJunk) {
                for (String name : salable) {
                    if (!item.getDisplayName().contains(name)) continue;
                    return true;
                }
            }
        }
        return false;
    }
}

