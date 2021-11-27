/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityCreeper
 *  net.minecraft.entity.monster.EntityEnderman
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.client.event.RenderLivingEvent$Pre
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.config.Config;
import cheaters.get.banned.utils.ScoreboardUtils;
import cheaters.get.banned.utils.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShowHiddenEntities {
    @SubscribeEvent
    public void onBeforeRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (event.entity.isInvisible()) {
            if (Config.showHiddenFels && event.entity instanceof EntityEnderman) {
                event.entity.setInvisible(false);
            }
            if (Utils.inDungeon && event.entity instanceof EntityPlayer) {
                if (Config.showHiddenShadowAssassins && event.entity.getName().contains("Shadow Assassin")) {
                    event.entity.setInvisible(false);
                }
                if (Config.showStealthyBloodMobs) {
                    for (String name : new String[]{"Revoker", "Psycho", "Reaper", "Cannibal", "Mute", "Ooze", "Putrid", "Freak", "Leech", "Tear", "Parasite", "Flamer", "Skull", "Mr. Dead", "Vader", "Frost", "Walker", "Wandering Soul", "Bonzo", "Scarf", "Livid"}) {
                        if (!event.entity.getName().contains(name)) continue;
                        event.entity.setInvisible(false);
                        break;
                    }
                }
            }
            if (Config.showGhosts && event.entity instanceof EntityCreeper && ScoreboardUtils.scoreboardContains("The Mist")) {
                event.entity.setInvisible(false);
            }
        }
    }
}

