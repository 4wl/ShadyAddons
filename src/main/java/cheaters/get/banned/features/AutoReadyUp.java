/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.Slot
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.Utils;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoReadyUp {
    private static boolean readyUp = false;
    private static boolean dungeonStarted = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Config.autoReadyUp && Utils.inDungeon && !dungeonStarted) {
            if (!readyUp) {
                for (Entity entity : Shady.mc.theWorld.getLoadedEntityList()) {
                    List possibleEntities;
                    if (!(entity instanceof EntityArmorStand) || !entity.hasCustomName() || !entity.getCustomNameTag().equals("\u00a7bMort") || (possibleEntities = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(0.0, 3.0, 0.0), e -> e instanceof EntityPlayer)).isEmpty()) continue;
                    Shady.mc.playerController.interactWithEntitySendPacket((EntityPlayer)Shady.mc.thePlayer, (Entity)possibleEntities.get(0));
                    readyUp = true;
                }
            }
            String chestName = Utils.getInventoryName();
            if (readyUp && chestName != null) {
                if (chestName.equals("Start Dungeon?")) {
                    Shady.mc.playerController.windowClick(Shady.mc.thePlayer.openContainer.windowId, 13, 2, 0, (EntityPlayer)Shady.mc.thePlayer);
                    return;
                }
                if (chestName.startsWith("Catacombs - ")) {
                    for (Slot slot : Shady.mc.thePlayer.openContainer.inventorySlots) {
                        if (slot.getStack() == null || !slot.getStack().getDisplayName().contains(Shady.mc.thePlayer.getName())) continue;
                        Shady.mc.playerController.windowClick(Shady.mc.thePlayer.openContainer.windowId, slot.slotNumber, 2, 0, (EntityPlayer)Shady.mc.thePlayer);
                        Shady.mc.thePlayer.closeScreen();
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (!dungeonStarted && event.message.getUnformattedText().contains("Dungeon starts in 4 seconds.")) {
            dungeonStarted = true;
        }
    }

    @SubscribeEvent
    public void onJoinWorld(WorldEvent.Load event) {
        readyUp = false;
        dungeonStarted = false;
    }
}

