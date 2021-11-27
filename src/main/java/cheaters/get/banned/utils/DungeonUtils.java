/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.monster.EntityZombie
 *  net.minecraft.util.MathHelper
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.utils;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.features.dungeonmap.DungeonMap;
import cheaters.get.banned.remote.MayorAPI;
import cheaters.get.banned.utils.ScoreboardUtils;
import cheaters.get.banned.utils.TabUtils;
import cheaters.get.banned.utils.Utils;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DungeonUtils {
    private static final Pattern namePattern = Pattern.compile("([A-Za-z0-9_]{0,16}) \\((Mage|Tank|Berserk|Healer|Archer) ([IVXL0]{1,7})\\)");
    private static final Pattern secretsPattern = Pattern.compile(" Secrets Found: (\\d*)");
    private static final Pattern cryptsPattern = Pattern.compile(" Crypts: (\\d*)");
    private static final Pattern deathsPattern = Pattern.compile("Deaths: \\((\\d*)\\)");
    private static final String[] mimicDeathMessages = new String[]{"Mimic Dead!", "$SKYTILS-DUNGEON-SCORE-MIMIC$", "Child Destroyed!", "Mimic Obliterated!", "Mimic Exorcised!", "Mimic Destroyed!", "Mimic Annhilated!"};
    public static DungeonRun dungeonRun = null;
    private int counter = 0;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (Utils.inDungeon && !DungeonUtils.dungeonRun.mimicFound && DungeonUtils.onFloorWithMimic()) {
            for (String mimicMessage : mimicDeathMessages) {
                if (!event.message.getFormattedText().contains(mimicMessage)) continue;
                DungeonUtils.dungeonRun.mimicFound = true;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (this.counter % 20 == 0) {
            if (Utils.inDungeon) {
                String cataLine;
                if (dungeonRun == null) {
                    dungeonRun = new DungeonRun();
                }
                if (DungeonUtils.dungeonRun.floor == null && (cataLine = ScoreboardUtils.getLineThatContains("The Catacombs")) != null) {
                    for (Floor floor : Floor.values()) {
                        if (!cataLine.contains(floor.name)) continue;
                        DungeonUtils.dungeonRun.floor = floor;
                    }
                }
                for (String name : TabUtils.getTabList()) {
                    Matcher nameMatcher;
                    if (name.contains("Crypts: ")) {
                        Matcher cryptsMatcher = cryptsPattern.matcher(name = Utils.removeFormatting(name));
                        if (!cryptsMatcher.matches()) continue;
                        DungeonUtils.dungeonRun.cryptsFound = Integer.parseInt(cryptsMatcher.group(1));
                        continue;
                    }
                    if (name.contains("Secrets Found: ")) {
                        Matcher secretsMatcher = secretsPattern.matcher(name = Utils.removeFormatting(name));
                        if (!secretsMatcher.matches()) continue;
                        DungeonUtils.dungeonRun.secretsFound = Integer.parseInt(secretsMatcher.group(1));
                        continue;
                    }
                    if (name.contains("Deaths: ")) {
                        Matcher deathsMatcher = deathsPattern.matcher(name = Utils.removeFormatting(name));
                        if (!deathsMatcher.matches()) continue;
                        DungeonUtils.dungeonRun.deaths = Integer.parseInt(deathsMatcher.group(1));
                        continue;
                    }
                    if (DungeonUtils.dungeonRun.team.size() >= 5 || !name.contains("Mage") && !name.contains("Berserker") && !name.contains("Archer") && !name.contains("Tank") && !name.contains("Healer") || !(nameMatcher = namePattern.matcher(name = Utils.removeFormatting(name))).matches()) continue;
                    DungeonUtils.dungeonRun.team.add(nameMatcher.group(1).trim());
                }
                if (Shady.mc.world != null && dungeonRun != null && (ScoreboardUtils.scoreboardContains("30,30") && (DungeonUtils.dungeonRun.floor == Floor.FLOOR_1 || DungeonUtils.dungeonRun.floor == Floor.MASTER_1) || ScoreboardUtils.scoreboardContains("30,125") && (DungeonUtils.dungeonRun.floor == Floor.FLOOR_2 || DungeonUtils.dungeonRun.floor == Floor.MASTER_2) || ScoreboardUtils.scoreboardContains("30,225") && (DungeonUtils.dungeonRun.floor == Floor.FLOOR_3 || DungeonUtils.dungeonRun.floor == Floor.MASTER_3) || ScoreboardUtils.scoreboardContains("- Healthy") && (DungeonUtils.dungeonRun.floor == Floor.FLOOR_3 || DungeonUtils.dungeonRun.floor == Floor.MASTER_3) || ScoreboardUtils.scoreboardContains("30,344") && (DungeonUtils.dungeonRun.floor == Floor.FLOOR_4 || DungeonUtils.dungeonRun.floor == Floor.MASTER_4) || ScoreboardUtils.scoreboardContains("livid") && (DungeonUtils.dungeonRun.floor == Floor.FLOOR_5 || DungeonUtils.dungeonRun.floor == Floor.MASTER_5) || ScoreboardUtils.scoreboardContains("sadan") && (DungeonUtils.dungeonRun.floor == Floor.FLOOR_6 || DungeonUtils.dungeonRun.floor == Floor.MASTER_6) || ScoreboardUtils.scoreboardContains("necron") && (DungeonUtils.dungeonRun.floor == Floor.FLOOR_7 || DungeonUtils.dungeonRun.floor == Floor.MASTER_7))) {
                    DungeonUtils.dungeonRun.inBoss = true;
                }
            } else {
                dungeonRun = null;
            }
            this.counter = 0;
        }
        ++this.counter;
    }

    public static void debug() {
        if (Utils.inDungeon && dungeonRun != null) {
            Utils.sendModMessage("Floor: " + DungeonUtils.dungeonRun.floor.name());
            Utils.sendModMessage("In Boss: " + DungeonUtils.dungeonRun.inBoss);
            Utils.sendModMessage("Secrets Found: " + DungeonUtils.dungeonRun.secretsFound);
            Utils.sendModMessage("Crypts Found: " + DungeonUtils.dungeonRun.cryptsFound);
        } else {
            Utils.sendMessage("You must be in a dungeon to debug a dungeon!");
        }
    }

    public static int calculateScore() {
        if (dungeonRun == null || DungeonMap.activeDungeonLayout == null) {
            return 0;
        }
        return DungeonUtils.calculateSkillScore() + DungeonUtils.calculateExploreScore() + DungeonUtils.calculateSpeedScore() + DungeonUtils.calculateBonusScore();
    }

    private static int calculateSkillScore() {
        return 100 - DungeonUtils.dungeonRun.deaths * 2 + (Config.assumeSpiritPet && DungeonUtils.dungeonRun.deaths > 0 ? 1 : 0);
    }

    private static int calculateExploreScore() {
        return 60 + 40 * DungeonUtils.dungeonRun.secretsFound / DungeonMap.activeDungeonLayout.totalSecrets;
    }

    private static int calculateSpeedScore() {
        return 100;
    }

    private static int calculateBonusScore() {
        return (MayorAPI.isPaulBonus() ? 10 : 0) + (DungeonUtils.dungeonRun.mimicFound ? 5 : 0) + MathHelper.clamp((int)DungeonUtils.dungeonRun.cryptsFound, (int)0, (int)5);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        EntityZombie entity;
        if (DungeonUtils.onFloorWithMimic() && !DungeonUtils.dungeonRun.mimicFound && event.entity.getClass() == EntityZombie.class && (entity = (EntityZombie)event.entity).isChild() && entity.func_82169_q(0) == null && entity.func_82169_q(1) == null && entity.func_82169_q(2) == null && entity.func_82169_q(3) == null) {
            DungeonUtils.dungeonRun.mimicFound = true;
        }
    }

    public static boolean inFloor(Floor floor) {
        return floor.equals((Object)DungeonUtils.dungeonRun.floor);
    }

    public static boolean onFloorWithMimic() {
        return dungeonRun != null && DungeonUtils.dungeonRun.floor != null && DungeonUtils.dungeonRun.floor != Floor.ENTERANCE && DungeonUtils.dungeonRun.floor != Floor.FLOOR_1 && DungeonUtils.dungeonRun.floor != Floor.FLOOR_2 && DungeonUtils.dungeonRun.floor != Floor.FLOOR_3 && DungeonUtils.dungeonRun.floor != Floor.MASTER_1 && DungeonUtils.dungeonRun.floor != Floor.MASTER_2 && DungeonUtils.dungeonRun.floor != Floor.MASTER_3;
    }

    public static class DungeonRun {
        public int secretsFound = 0;
        public int cryptsFound = 0;
        public Floor floor = null;
        public boolean inBoss = false;
        public boolean mimicFound = false;
        public int deaths;
        public HashSet<String> team = new HashSet();
        public long startTime = System.currentTimeMillis();

        public long getTimeMs() {
            return System.currentTimeMillis() - this.startTime;
        }
    }

    public static enum Floor {
        ENTERANCE("(E)"),
        FLOOR_1("(F1)"),
        FLOOR_2("(F2)"),
        FLOOR_3("(F3)"),
        FLOOR_4("(F4)"),
        FLOOR_5("(F5)"),
        FLOOR_6("(F6)"),
        FLOOR_7("(F7)"),
        MASTER_1("(M1)"),
        MASTER_2("(M2)"),
        MASTER_3("(M3)"),
        MASTER_4("(M4)"),
        MASTER_5("(M5)"),
        MASTER_6("(M6)"),
        MASTER_7("(M7)");

        public String name;

        private Floor(String name) {
            this.name = name;
        }
    }
}

