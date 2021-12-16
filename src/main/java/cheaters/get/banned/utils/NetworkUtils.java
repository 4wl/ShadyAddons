/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package cheaters.get.banned.utils;

import cheaters.get.banned.Shady;
import net.minecraft.network.Packet;

public class NetworkUtils {
    public static void sendPacket(Packet<?> packet) {
        Shady.mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
}

