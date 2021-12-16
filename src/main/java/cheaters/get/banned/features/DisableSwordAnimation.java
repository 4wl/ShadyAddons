/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.util.BlockPos
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$Action
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.NetworkUtils;
import cheaters.get.banned.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DisableSwordAnimation {
    private final ArrayList<String> swords = new ArrayList<String>(Arrays.asList("HYPERION", "VALKYRIE", "SCYLLA", "ASTRAEA", "ASPECT_OF_THE_END", "ROGUE_SWORD"));
    private static boolean isRightClickKeyDown = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        isRightClickKeyDown = Shady.mc.gameSettings.keyBindUseItem.isKeyDown();
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        String itemID;
        if (Config.disableBlockAnimation && event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR && Shady.mc.thePlayer.getHeldItem() != null && this.swords.contains(itemID = Utils.getSkyBlockID(Shady.mc.thePlayer.getHeldItem()))) {
            event.setCanceled(true);
            if (!isRightClickKeyDown) {
                NetworkUtils.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, Shady.mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
            }
        }
    }
}

