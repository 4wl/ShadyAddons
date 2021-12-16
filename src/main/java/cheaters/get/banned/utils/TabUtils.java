/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ComparisonChain
 *  com.google.common.collect.Ordering
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.world.WorldSettings$GameType
 */
package cheaters.get.banned.utils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;

public class TabUtils {
    public static final Ordering<NetworkPlayerInfo> playerInfoOrdering = new Ordering<NetworkPlayerInfo>(){

        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
            ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare((Comparable)((Object)(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "")), (Comparable)((Object)(scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : ""))).compare((Comparable)((Object)p_compare_1_.getGameProfile().getName()), (Comparable)((Object)p_compare_2_.getGameProfile().getName())).result();
        }
    };

    public static List<NetworkPlayerInfo> getTabEntries() {
        if (Minecraft.getMinecraft().thePlayer == null) {
            return Collections.emptyList();
        }
        return playerInfoOrdering.sortedCopy((Iterable)Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap());
    }

    public static List<String> getTabList() {
        return TabUtils.getTabEntries().stream().map(playerInfo -> Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(playerInfo)).collect(Collectors.toList());
    }
}

