/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.monster.EntityMagmaCube
 *  net.minecraft.entity.monster.EntitySlime
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.event.entity.EntityJoinWorldEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.config.Config;
import cheaters.get.banned.config.settings.FolderSetting;
import cheaters.get.banned.events.RenderEntityModelEvent;
import cheaters.get.banned.utils.LocationUtils;
import cheaters.get.banned.utils.OutlineUtils;
import cheaters.get.banned.utils.Utils;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobESP {
    private static HashMap<Entity, Color> highlightedEntities = new HashMap();
    private static HashSet<Entity> checkedStarNameTags = new HashSet();

    private static void highlightEntity(Entity entity, Color color) {
        highlightedEntities.put(entity, color);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (Utils.inDungeon) {
            if (Config.minibossEsp && event.entity instanceof EntityPlayer) {
                String name;
                switch (name = event.entity.getName()) {
                    case "Shadow Assassin": {
                        event.entity.setInvisible(false);
                        MobESP.highlightEntity(event.entity, Color.MAGENTA);
                        break;
                    }
                    case "Lost Adventurer": {
                        MobESP.highlightEntity(event.entity, Color.BLUE);
                        break;
                    }
                    case "Diamond Guy": {
                        MobESP.highlightEntity(event.entity, Color.CYAN);
                    }
                }
            }
            if (Config.secretBatEsp && event.entity instanceof EntityBat) {
                MobESP.highlightEntity(event.entity, Color.RED);
            }
        }
        if (Utils.inSkyBlock && LocationUtils.onIsland(LocationUtils.Island.CRYSTAL_HOLLOWS)) {
            float health;
            if (Config.sludgeEsp && event.entity instanceof EntitySlime && !(event.entity instanceof EntityMagmaCube)) {
                MobESP.highlightEntity(event.entity, Color.GREEN);
            }
            if (Config.yogEsp && event.entity instanceof EntityMagmaCube) {
                MobESP.highlightEntity(event.entity, Color.RED);
            }
            if (Config.corleoneEsp && event.entity instanceof EntityOtherPlayerMP && event.entity.getName().equals("Team Treasurite") && ((health = ((EntityOtherPlayerMP)event.entity).getMaxHealth()) == 1000000.0f || health == 2000000.0f)) {
                MobESP.highlightEntity(event.entity, Color.PINK);
            }
        }
    }

    @SubscribeEvent
    public void onRenderEntityModel(RenderEntityModelEvent event) {
        if (Utils.inDungeon && !checkedStarNameTags.contains((Object)event.entity) && Config.starredMobEsp && event.entity instanceof EntityArmorStand && event.entity.hasCustomName() && event.entity.getCustomNameTag().contains("\u272f")) {
            List possibleEntities = event.entity.getEntityWorld().getEntitiesInAABBexcluding((Entity)event.entity, event.entity.getEntityBoundingBox().expand(0.0, 3.0, 0.0), entity -> !(entity instanceof EntityArmorStand));
            if (!possibleEntities.isEmpty()) {
                MobESP.highlightEntity((Entity)possibleEntities.get(0), Color.ORANGE);
            }
            checkedStarNameTags.add((Entity)event.entity);
        }
        if (FolderSetting.isEnabled("Mob ESP") && !highlightedEntities.isEmpty() && highlightedEntities.containsKey((Object)event.entity)) {
            OutlineUtils.outlineEntity(event, highlightedEntities.get((Object)event.entity));
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        highlightedEntities.clear();
        checkedStarNameTags.clear();
    }
}

