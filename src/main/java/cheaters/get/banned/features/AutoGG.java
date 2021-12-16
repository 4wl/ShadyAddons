/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.config.Config;
import cheaters.get.banned.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoGG {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message;
        if (Config.autoGg && !Utils.inSkyBlock && event.type == 0 && (message = event.message.getFormattedText()).contains("Reward Summary") && !message.contains(":") && !message.contains("]")) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/ac gg");
        }
    }
}

