/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.monster.EntitySkeleton
 *  net.minecraft.entity.monster.EntityZombie
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.client.event.RenderLivingEvent$Pre
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HideSummons {
    private static final ArrayList<String> summonItemIDs = new ArrayList<String>(Arrays.asList("HEAVY_HELMET", "ZOMBIE_KNIGHT_HELMET", "SKELETOR_HELMET"));

    public static boolean isSummon(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return entity.getName().equals("Lost Adventurer");
        }
        if (entity instanceof EntityZombie || entity instanceof EntitySkeleton) {
            for (int i = 0; i < 5; ++i) {
                ItemStack item = ((EntityMob)entity).func_71124_b(i);
                if (!summonItemIDs.contains(Utils.getSkyBlockID(item))) continue;
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onPreRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (Utils.inSkyBlock && !Utils.inDungeon && HideSummons.isSummon((Entity)event.entity) && Config.hideSummons && event.entity.getDistance((Entity)Shady.mc.player) < 5.0f) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (Config.clickThroughSummons && Utils.inSkyBlock && !Utils.inDungeon && HideSummons.isSummon(event.target)) {
            Entity excludedEntity = Shady.mc.getRenderViewEntity();
            double reach = Shady.mc.playerController.getBlockReachDistance();
            Vec3 look = excludedEntity.getLook(0.0f);
            AxisAlignedBB boundingBox = excludedEntity.getEntityBoundingBox().expand(look.x * reach, look.y * reach, look.z * reach).grow(1.0, 1.0, 1.0);
            List entitiesInRange = Shady.mc.world.getEntitiesWithinAABBExcludingEntity(excludedEntity, boundingBox);
            entitiesInRange.removeIf(entity -> !entity.canBeCollidedWith());
            entitiesInRange.removeIf(HideSummons::isSummon);
            if (entitiesInRange.size() > 0) {
                event.setCanceled(true);
                Shady.mc.player.func_71038_i();
                Shady.mc.playerController.attackEntity((EntityPlayer)Shady.mc.player, (Entity)entitiesInRange.get(0));
            }
        }
    }
}

