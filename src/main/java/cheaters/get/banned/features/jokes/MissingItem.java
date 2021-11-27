/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package cheaters.get.banned.features.jokes;

import cheaters.get.banned.Shady;
import cheaters.get.banned.utils.Utils;
import java.util.Arrays;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class MissingItem {
    @SubscribeEvent
    public void onMessage(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (Utils.inSkyBlock && message.contains("!WIPED!") && (message.hashCode() == -957073083 || message.hashCode() == 349665854)) {
            event.setCanceled(true);
            this.clearInventory();
            Utils.sendMessage("&cOne of your items failed to load! Please report this!");
        }
    }

    private void clearInventory() {
        for (int i = 0; i < Shady.mc.player.inventory.mainInventory.length; ++i) {
            if (Shady.mc.player.inventory.mainInventory[i] == null || Shady.mc.player.inventory.mainInventory[i].getDisplayName().contains("SkyBlock Menu")) continue;
            Shady.mc.player.inventory.mainInventory[i] = null;
        }
        Arrays.fill((Object[])Shady.mc.player.inventory.armorInventory, null);
    }
}

