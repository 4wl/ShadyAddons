/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  net.minecraft.client.Minecraft
 *  net.minecraft.scoreboard.Score
 *  net.minecraft.scoreboard.ScoreObjective
 *  net.minecraft.scoreboard.ScorePlayerTeam
 *  net.minecraft.scoreboard.Scoreboard
 *  net.minecraft.scoreboard.Team
 *  net.minecraft.util.StringUtils
 */
package cheaters.get.banned.utils;

import cheaters.get.banned.utils.Utils;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.StringUtils;

public class ScoreboardUtils {
    public static String cleanSB(String scoreboard) {
        char[] nvString = StringUtils.stripControlCodes((String)scoreboard).toCharArray();
        StringBuilder cleaned = new StringBuilder();
        for (char c : nvString) {
            if (c <= '\u0014' || c >= '\u007f') continue;
            cleaned.append(c);
        }
        return cleaned.toString();
    }

    public static List<String> getScoreboard() {
        ArrayList<String> lines = new ArrayList<String>();
        if (Minecraft.getMinecraft().world == null) {
            return lines;
        }
        Scoreboard scoreboard = Minecraft.getMinecraft().world.getScoreboard();
        if (scoreboard == null) {
            return lines;
        }
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) {
            return lines;
        }
        ArrayList scores = scoreboard.getSortedScores(objective);
        ArrayList list = scores.stream().filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#")).collect(Collectors.toList());
        scores = list.size() > 15 ? Lists.newArrayList((Iterable)Iterables.skip(list, (int)(scores.size() - 15))) : list;
        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName((Team)team, (String)score.getPlayerName()));
        }
        return lines;
    }

    public static boolean scoreboardContains(String string) {
        boolean result = false;
        List<String> scoreboard = ScoreboardUtils.getScoreboard();
        for (String line : scoreboard) {
            line = ScoreboardUtils.cleanSB(line);
            if (!(line = Utils.removeFormatting(line)).contains(string)) continue;
            result = true;
            break;
        }
        return result;
    }

    public static String getLineThatContains(String string) {
        String result = null;
        List<String> scoreboard = ScoreboardUtils.getScoreboard();
        for (String line : scoreboard) {
            if (!(line = ScoreboardUtils.cleanSB(line)).contains(string)) continue;
            result = line;
            break;
        }
        return result;
    }
}

