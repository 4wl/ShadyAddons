/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.Vec3
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.util.vector.Vector3f
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.ClickEvent;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.DungeonUtils;
import cheaters.get.banned.utils.RayCastUtils;
import cheaters.get.banned.utils.Utils;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.util.vector.Vector3f;

public class CrystalReach {
    public static EntityEnderCrystal crystal = null;

    public static boolean isEnabled() {
        return Config.crystalReach && Utils.inDungeon && Shady.mc.player != null && Shady.mc.player.getPosition().getY() > 215 && DungeonUtils.dungeonRun != null && DungeonUtils.dungeonRun.inBoss && DungeonUtils.inFloor(DungeonUtils.Floor.FLOOR_7);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        List<EntityEnderCrystal> crystals;
        crystal = CrystalReach.isEnabled() && Shady.mc.player.isSneaking() ? ((crystals = RayCastUtils.getFacedEntityOfType(EntityEnderCrystal.class, 32.0f)) != null ? crystals.get(0) : null) : null;
    }

    @SubscribeEvent
    public void onRightClick(ClickEvent.Right event) {
        List armorStand;
        if (CrystalReach.isEnabled() && Shady.mc.player.isSneaking() && crystal != null && !(armorStand = Shady.mc.world.getEntitiesInAABBexcluding((Entity)crystal, crystal.getEntityBoundingBox(), entity -> entity instanceof EntityArmorStand && entity.getCustomNameTag().contains("CLICK HERE"))).isEmpty() && armorStand.get(0) != null) {
            Shady.mc.playerController.func_78768_b((EntityPlayer)Shady.mc.player, (Entity)armorStand.get(0));
            event.setCanceled(true);
        }
    }

    private static EntityEnderCrystal lookingAtCrystal() {
        float range = 32.0f;
        float stepSize = 0.5f;
        if (Shady.mc.player != null && Shady.mc.world != null) {
            Vector3f position = new Vector3f((float)Shady.mc.player.posX, (float)Shady.mc.player.posY + Shady.mc.player.getEyeHeight(), (float)Shady.mc.player.posZ);
            Vec3 look = Shady.mc.player.getLook(0.0f);
            Vector3f step = new Vector3f((float)look.x, (float)look.y, (float)look.z);
            step.scale(stepSize / step.length());
            int i = 0;
            while ((double)i < Math.floor(range / stepSize) - 2.0) {
                List entities = Shady.mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB((double)position.x - 0.5, (double)position.y - 0.5, (double)position.z - 0.5, (double)position.x + 0.5, (double)position.y + 0.5, (double)position.z + 0.5));
                if (!entities.isEmpty()) {
                    return (EntityEnderCrystal)entities.get(0);
                }
                position.translate(step.x, step.y, step.z);
                ++i;
            }
        }
        return null;
    }
}

