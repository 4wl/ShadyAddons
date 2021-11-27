/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.config.Config;
import cheaters.get.banned.utils.ExpressionParser;
import cheaters.get.banned.utils.LocationUtils;
import cheaters.get.banned.utils.ThreadUtils;
import cheaters.get.banned.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SocialCommandSolver {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message;
        if (Config.socialQuickMathsSolver && event.type == 0 && (Utils.inSkyBlock && LocationUtils.onIsland(LocationUtils.Island.PRIVATE_ISLAND) || Config.enableMathsOutsideSkyBlock) && (message = event.message.getUnformattedText()).startsWith("QUICK MATHS! Solve: ")) {
            message = message.replace("QUICK MATHS! Solve: ", "").replace("x", "*");
            int answer = (int)ExpressionParser.eval(message);
            new Thread(() -> {
                ThreadUtils.sleep(Config.quickMathsAnswerDelay);
                Utils.sendModMessage("The answer is " + answer);
                Utils.sendMessageAsPlayer("/ac " + answer);
            }).start();
        }
    }
}

