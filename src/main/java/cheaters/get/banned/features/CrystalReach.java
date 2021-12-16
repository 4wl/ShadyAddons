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
        return Config.crystalReach && Utils.inDungeon && Shady.mc.thePlayer != null && Shady.mc.thePlayer.getPosition().getY() > 215 && DungeonUtils.dungeonRun != null && DungeonUtils.dungeonRun.inBoss && DungeonUtils.inFloor(DungeonUtils.Floor.FLOOR_7);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        List<EntityEnderCrystal> crystals;
        crystal = CrystalReach.isEnabled() && Shady.mc.thePlayer.isSneaking() ? ((crystals = RayCastUtils.getFacedEntityOfType(EntityEnderCrystal.class, 32.0f)) != null ? crystals.get(0) : null) : null;
    }

    @SubscribeEvent
    public void onRightClick(ClickEvent.Right event) {
        List armorStand;
        if (CrystalReach.isEnabled() && Shady.mc.thePlayer.isSneaking() && crystal != null && !(armorStand = Shady.mc.theWorld.getEntitiesInAABBexcluding((Entity)crystal, crystal.getEntityBoundingBox(), entity -> entity instanceof EntityArmorStand && entity.getCustomNameTag().contains("CLICK HERE"))).isEmpty() && armorStand.get(0) != null) {
            Shady.mc.playerController.interactWithEntitySendPacket((EntityPlayer)Shady.mc.thePlayer, (Entity)armorStand.get(0));
            event.setCanceled(true);
        }
    }

    private static EntityEnderCrystal lookingAtCrystal() {
        float range = 32.0f;
        float stepSize = 0.5f;
        if (Shady.mc.thePlayer != null && Shady.mc.theWorld != null) {
            Vector3f position = new Vector3f((float)Shady.mc.thePlayer.posX, (float)Shady.mc.thePlayer.posY + Shady.mc.thePlayer.getEyeHeight(), (float)Shady.mc.thePlayer.posZ);
            Vec3 look = Shady.mc.thePlayer.getLook(0.0f);
            Vector3f step = new Vector3f((float)look.xCoord, (float)look.yCoord, (float)look.zCoord);
            step.scale(stepSize / step.length());
            int i = 0;
            while ((double)i < Math.floor(range / stepSize) - 2.0) {
                List entities = Shady.mc.theWorld.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB((double)position.x - 0.5, (double)position.y - 0.5, (double)position.z - 0.5, (double)position.x + 0.5, (double)position.y + 0.5, (double)position.z + 0.5));
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

