/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.ClickEvent;
import cheaters.get.banned.utils.KeybindUtils;
import cheaters.get.banned.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GhostBlocks {
    public GhostBlocks() {
        KeybindUtils.register("Create Ghost Block", 34);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Block lookingAtblock;
        BlockPos lookingAtPos;
        if (Config.ghostBlockKeybind && KeybindUtils.get("Create Ghost Block").isKeyDown() && (lookingAtPos = Shady.mc.player.rayTrace((double)Shady.mc.playerController.getBlockReachDistance(), 1.0f).getBlockPos()) != null && !Utils.isInteractable(lookingAtblock = Shady.mc.world.getBlockState(lookingAtPos).getBlock())) {
            Shady.mc.world.setBlockToAir(lookingAtPos);
        }
    }

    @SubscribeEvent
    public void onRightClick(ClickEvent.Right event) {
        String itemId;
        if (Utils.inSkyBlock && Config.stonkGhostBlock && Shady.mc.objectMouseOver != null && Shady.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !Utils.isInteractable(Shady.mc.world.getBlockState(Shady.mc.objectMouseOver.getBlockPos()).getBlock()) && ((itemId = Utils.getSkyBlockID(Shady.mc.player.func_70694_bm())).equals("STONK_PICKAXE") || itemId.equals("GOLD_PICKAXE"))) {
            Shady.mc.world.setBlockToAir(Shady.mc.objectMouseOver.getBlockPos());
            event.setCanceled(true);
        }
    }
}

