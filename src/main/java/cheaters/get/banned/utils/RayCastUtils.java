/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 *  org.lwjgl.util.vector.Vector3f
 */
package cheaters.get.banned.utils;

import cheaters.get.banned.Shady;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector3f;

public class RayCastUtils {
    public static boolean isFacingBlock(BlockPos block, float range) {
        float stepSize = 0.15f;
        if (Shady.mc.thePlayer != null && Shady.mc.theWorld != null) {
            Vector3f position = new Vector3f((float)Shady.mc.thePlayer.posX, (float)Shady.mc.thePlayer.posY + Shady.mc.thePlayer.getEyeHeight(), (float)Shady.mc.thePlayer.posZ);
            Vec3 look = Shady.mc.thePlayer.getLook(0.0f);
            Vector3f step = new Vector3f((float)look.xCoord, (float)look.yCoord, (float)look.zCoord);
            step.scale(stepSize / step.length());
            int i = 0;
            while ((double)i < Math.floor(range / stepSize) - 2.0) {
                BlockPos blockAtPos = new BlockPos((double)position.x, (double)position.y, (double)position.z);
                if (blockAtPos.equals((Object)block)) {
                    return true;
                }
                position.translate(step.x, step.y, step.z);
                ++i;
            }
        }
        return false;
    }

    public static <T extends Entity> List<T> getFacedEntityOfType(Class<T> _class, float range) {
        float stepSize = 0.5f;
        if (Shady.mc.thePlayer != null && Shady.mc.theWorld != null) {
            Vector3f position = new Vector3f((float)Shady.mc.thePlayer.posX, (float)Shady.mc.thePlayer.posY + Shady.mc.thePlayer.getEyeHeight(), (float)Shady.mc.thePlayer.posZ);
            Vec3 look = Shady.mc.thePlayer.getLook(0.0f);
            Vector3f step = new Vector3f((float)look.xCoord, (float)look.yCoord, (float)look.zCoord);
            step.scale(stepSize / step.length());
            int i = 0;
            while ((double)i < Math.floor(range / stepSize) - 2.0) {
                List entities = Shady.mc.theWorld.getEntitiesWithinAABB(_class, new AxisAlignedBB((double)position.x - 0.5, (double)position.y - 0.5, (double)position.z - 0.5, (double)position.x + 0.5, (double)position.y + 0.5, (double)position.z + 0.5));
                if (!entities.isEmpty()) {
                    return entities;
                }
                position.translate(step.x, step.y, step.z);
                ++i;
            }
        }
        return null;
    }
}

