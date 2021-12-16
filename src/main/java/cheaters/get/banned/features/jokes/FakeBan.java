/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientConnectedToServerEvent
 *  org.apache.commons.lang3.time.DurationFormatUtils
 */
package cheaters.get.banned.features.jokes;

import cheaters.get.banned.Shady;
import cheaters.get.banned.utils.Utils;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class FakeBan {
    private static boolean permBan = false;
    private static long banStart = 0L;
    private static boolean usernameBan = false;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type != 0) {
            return;
        }
        String message = event.message.getUnformattedText();
        if (message.contains("!BANNED!") || message.contains("!PERMBAN!") || message.contains("!BADNAME!")) {
            switch (message.hashCode()) {
                case -1778728484: 
                case 870570787: {
                    permBan = true;
                }
                case -1292812670: 
                case 561388713: {
                    banStart = System.currentTimeMillis() - 1000L;
                    event.setCanceled(true);
                    FakeBan.fakeGenericBan(Shady.mc.getNetHandler().getNetworkManager());
                    break;
                }
                case -1708887982: 
                case -63219957: {
                    usernameBan = true;
                    event.setCanceled(true);
                    FakeBan.fakeUsernameBan(Shady.mc.getNetHandler().getNetworkManager());
                    break;
                }
                default: {
                    Utils.sendMessageAsPlayer("/r Nice try bozo");
                }
            }
        }
    }

    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (Shady.mc.getCurrentServerData() != null && Shady.mc.getCurrentServerData().serverIP.contains("hypixel.net") && !Shady.mc.getCurrentServerData().serverIP.contains("letmein")) {
            if (usernameBan) {
                FakeBan.fakeUsernameBan(event.manager);
            }
            if (permBan) {
                FakeBan.fakeGenericBan(event.manager);
            }
        }
    }

    public static void fakeGenericBan(NetworkManager manager) {
        long banDuration = banStart + 2592000000L - System.currentTimeMillis();
        String formattedDuration = DurationFormatUtils.formatDuration((long)banDuration, (String)"d'd' H'h' m'm' s's'", (boolean)false);
        ChatComponentText component = new ChatComponentText("\u00a7cYou are temporarily banned for \u00a7f" + formattedDuration + " \u00a7cfrom this server!");
        component.appendText("\n");
        component.appendText("\n\u00a77Reason: \u00a7rCheating through the use of unfair game advantages.");
        component.appendText("\n\u00a77Find out more: \u00a7b\u00a7nhttps://www.hypixel.net/appeal");
        component.appendText("\n");
        component.appendText("\n\u00a77Ban ID: \u00a7r#" + Math.abs(Shady.mc.getSession().getProfile().getId().toString().hashCode()));
        component.appendText("\n\u00a77Sharing your Ban ID may affect the processing of your appeal!");
        manager.closeChannel((IChatComponent)component);
    }

    public static void fakeUsernameBan(NetworkManager manager) {
        ChatComponentText component = new ChatComponentText("\n\u00a7cYou are currently blocked from joining this server!");
        component.appendText("\n");
        component.appendText("\n\u00a77Reason: \u00a7fYour username, " + Shady.mc.getSession().getUsername() + ", is not allowed on the server and is breaking our rules.");
        component.appendText("\n\u00a77Find out more: \u00a7b\u00a7nhttps://www.hypixel.net/rules");
        component.appendText("\n");
        component.appendText("\n\u00a7cPlease change your Minecraft username before trying to join again.");
        manager.closeChannel((IChatComponent)component);
    }
}

