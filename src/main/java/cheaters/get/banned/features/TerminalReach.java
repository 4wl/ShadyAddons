/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.BlockPos
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.ClickEvent;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.RayCastUtils;
import cheaters.get.banned.utils.RenderUtils;
import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TerminalReach {
    private BlockPos selectedTerminal = null;
    private EntityArmorStand selectedTerminalStand = null;
    private static List<BlockPos> terminals = Arrays.asList(new BlockPos[]{new BlockPos(310, 113, 272), new BlockPos(310, 119, 278), new BlockPos(288, 112, 291), new BlockPos(288, 122, 300), new BlockPos(267, 109, 320), new BlockPos(258, 120, 321), new BlockPos(246, 109, 320), new BlockPos(238, 108, 342), new BlockPos(196, 109, 311), new BlockPos(196, 119, 292), new BlockPos(218, 123, 292), new BlockPos(196, 109, 276), new BlockPos(240, 109, 228), new BlockPos(243, 121, 228), new BlockPos(266, 109, 228), new BlockPos(271, 115, 247)});
    private static final int[][][] sections = new int[][][]{new int[][]{{310, 251}, {288, 319}}, new int[][]{{287, 320}, {219, 342}}, new int[][]{{218, 251}, {196, 319}}, new int[][]{{287, 228}, {219, 250}}};

    private boolean isEnabled() {
        return Config.terminalReach && Shady.mc.thePlayer != null && Shady.mc.thePlayer.isSneaking() && Shady.mc.thePlayer.getPosition().getY() < 150 && Shady.mc.thePlayer.getPosition().getY() > 100;
    }

    @SubscribeEvent
    public void onRightClick(ClickEvent.Right event) {
        if (this.isEnabled() && this.selectedTerminalStand != null && this.selectedTerminal != null) {
            Shady.mc.playerController.interactWithEntitySendPacket((EntityPlayer)Shady.mc.thePlayer, (Entity)this.selectedTerminalStand);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (this.isEnabled()) {
            for (BlockPos terminal : terminals) {
                if (!Config.outsideTerms && !TerminalReach.isInSection(terminal)) continue;
                RenderUtils.highlightBlock(terminal, terminal.equals((Object)this.selectedTerminal) ? Color.MAGENTA : Color.WHITE, event.partialTicks);
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        List<EntityArmorStand> armorStands;
        if (this.isEnabled() && (armorStands = RayCastUtils.getFacedEntityOfType(EntityArmorStand.class, Config.terminalReachRange)) != null) {
            for (EntityArmorStand armorStand : armorStands) {
                if (!armorStand.getCustomNameTag().equals("\u00a7cInactive Terminal")) continue;
                if (!Config.outsideTerms && !TerminalReach.isInSection(armorStand.getPosition())) break;
                terminals.sort(Comparator.comparingDouble(((EntityArmorStand)armorStand)::getDistanceSq));
                this.selectedTerminal = terminals.get(0);
                this.selectedTerminalStand = armorStand;
            }
        }
        this.selectedTerminal = null;
    }

    private static boolean isInSection(BlockPos blockPos) {
        boolean inSection = false;
        int x = blockPos.getX();
        int z = blockPos.getZ();
        for (int[][] s : sections) {
            if (x >= s[0][0] || z <= s[0][1] || x <= s[1][0] || z >= s[1][1]) continue;
            inSection = true;
            break;
        }
        x = Shady.mc.thePlayer.getPosition().getX();
        z = Shady.mc.thePlayer.getPosition().getZ();
        for (int[][] s : sections) {
            if (x <= s[0][0] && z >= s[0][1] && x >= s[1][0] && z <= s[1][1]) continue;
            inSection = false;
            break;
        }
        return inSection;
    }
}

