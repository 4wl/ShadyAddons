/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$Action
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TeleportWithAnything {
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (Config.teleportWithAnything && Utils.inSkyBlock && Shady.mc.thePlayer.inventory.currentItem == 0 && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            for (int i = 0; i < 9; ++i) {
                ItemStack item = Shady.mc.thePlayer.inventory.getStackInSlot(i);
                String itemID = Utils.getSkyBlockID(item);
                if (!itemID.equals("ASPECT_OF_THE_END") && !itemID.equals("ASPECT_OF_THE_VOID")) continue;
                event.setCanceled(true);
                Shady.mc.thePlayer.inventory.currentItem = i;
                Shady.mc.playerController.sendUseItem((EntityPlayer)Shady.mc.thePlayer, (World)Shady.mc.theWorld, item);
                Shady.mc.thePlayer.inventory.currentItem = 0;
                break;
            }
        }
    }
}

