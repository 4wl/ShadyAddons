/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.inventory.ContainerChest
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.utils.Utils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RoyalPigeonMacro {
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        String chestName;
        if (Utils.inSkyBlock && Config.royalPigeonMacro && event.gui instanceof GuiChest && (chestName = ((ContainerChest)((GuiChest)event.gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText()).contains("Commissions") && Shady.mc.thePlayer.getHeldItem().getDisplayName().contains("Royal Pigeon")) {
            for (int i = 0; i < 9; ++i) {
                ItemStack item = Shady.mc.thePlayer.inventory.getStackInSlot(i);
                if (item == null || !item.getDisplayName().contains("Refined")) continue;
                Shady.mc.thePlayer.inventory.currentItem = i;
                break;
            }
        }
    }
}

