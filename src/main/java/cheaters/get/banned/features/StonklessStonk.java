/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.properties.Property
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockChest
 *  net.minecraft.block.BlockLever
 *  net.minecraft.block.BlockSkull
 *  net.minecraft.tileentity.TileEntitySkull
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent
 *  net.minecraftforge.event.entity.player.PlayerInteractEvent$Action
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.BlockChangeEvent;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.ArrayUtils;
import cheaters.get.banned.utils.DungeonUtils;
import cheaters.get.banned.utils.RayCastUtils;
import cheaters.get.banned.utils.RenderUtils;
import cheaters.get.banned.utils.Utils;
import com.mojang.authlib.properties.Property;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StonklessStonk {
    private static HashMap<BlockPos, Block> blockList = new HashMap();
    private static BlockPos selectedBlock = null;
    private static BlockPos lastCheckedPosition = null;
    private static HashSet<BlockPos> usedBlocks = new HashSet();
    private static float range = 5.0f;
    private static final String witherEssenceSkin = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=";
    private static final int essenceSkinHash = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzRkYjRhZGZhOWJmNDhmZjVkNDE3MDdhZTM0ZWE3OGJkMjM3MTY1OWZjZDhjZDg5MzQ3NDlhZjRjY2U5YiJ9fX0=".hashCode();

    private static boolean isEnabled() {
        boolean isEnabled;
        boolean bl = isEnabled = Utils.inDungeon && Shady.mc.thePlayer != null;
        if (!Config.alwaysOn && isEnabled) {
            boolean bl2 = isEnabled = Config.stonklessStonk && Shady.mc.thePlayer.isSneaking();
        }
        if (Config.disableInBoss && isEnabled) {
            isEnabled = DungeonUtils.dungeonRun != null && !DungeonUtils.dungeonRun.inBoss;
        }
        return isEnabled;
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Shady.mc.thePlayer == null) {
            return;
        }
        BlockPos playerPosition = Shady.mc.thePlayer.getPosition();
        if (StonklessStonk.isEnabled() && (lastCheckedPosition == null || !lastCheckedPosition.equals((Object)playerPosition))) {
            blockList.clear();
            lastCheckedPosition = playerPosition;
            for (int x = playerPosition.getX() - 6; x < playerPosition.getX() + 6; ++x) {
                for (int y = playerPosition.getY() - 6; y < playerPosition.getY() + 6; ++y) {
                    for (int z = playerPosition.getZ() - 6; z < playerPosition.getZ() + 6; ++z) {
                        BlockPos position = new BlockPos(x, y, z);
                        Block block = Shady.mc.theWorld.getBlockState(position).getBlock();
                        if (!StonklessStonk.shouldEspBlock(block, position)) continue;
                        blockList.put(position, block);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (StonklessStonk.isEnabled()) {
            for (Map.Entry<BlockPos, Block> block : blockList.entrySet()) {
                if (usedBlocks.contains((Object)block.getKey())) continue;
                if (selectedBlock == null) {
                    if (RayCastUtils.isFacingBlock(block.getKey(), range)) {
                        selectedBlock = block.getKey();
                    }
                } else if (!RayCastUtils.isFacingBlock(selectedBlock, range)) {
                    selectedBlock = null;
                }
                Color color = Utils.addAlpha(Color.WHITE, 51);
                if (block.getValue() instanceof BlockSkull) {
                    color = Utils.addAlpha(Color.BLACK, 51);
                }
                if (block.getValue() instanceof BlockLever) {
                    color = Utils.addAlpha(Color.LIGHT_GRAY, 51);
                }
                if (block.getValue() instanceof BlockChest && ((BlockChest)block.getValue()).chestType == 1) {
                    color = Utils.addAlpha(Color.RED, 51);
                }
                if (block.getKey().equals((Object)selectedBlock)) {
                    color = Utils.addAlpha(Color.GREEN, 51);
                }
                RenderUtils.highlightBlock(block.getKey(), color, event.partialTicks);
            }
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (StonklessStonk.isEnabled() && selectedBlock != null && !usedBlocks.contains((Object)selectedBlock)) {
            if (Shady.mc.objectMouseOver != null && selectedBlock.equals((Object)Shady.mc.objectMouseOver.getBlockPos())) {
                return;
            }
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                usedBlocks.add(selectedBlock);
                Shady.mc.thePlayer.setSneaking(false);
                if (Shady.mc.playerController.onPlayerRightClick(Shady.mc.thePlayer, Shady.mc.theWorld, Shady.mc.thePlayer.inventory.getCurrentItem(), selectedBlock, EnumFacing.fromAngle((double)Shady.mc.thePlayer.rotationYaw), new Vec3(Math.random(), Math.random(), Math.random()))) {
                    Shady.mc.thePlayer.swingItem();
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (Shady.mc.theWorld == null || Shady.mc.thePlayer == null) {
            return;
        }
        if (event.position.distanceSq((Vec3i)Shady.mc.thePlayer.getPosition()) > (double)range) {
            return;
        }
        if (usedBlocks.contains((Object)event.position)) {
            return;
        }
        if (!StonklessStonk.shouldEspBlock(event.newBlock.getBlock(), event.position)) {
            return;
        }
        blockList.put(event.position, event.newBlock.getBlock());
    }

    public static boolean shouldEspBlock(Block block, BlockPos position) {
        TileEntitySkull tileEntity;
        if (block instanceof BlockChest || block instanceof BlockLever) {
            return true;
        }
        if (block instanceof BlockSkull && (tileEntity = (TileEntitySkull)Shady.mc.theWorld.getTileEntity(position)).getSkullType() == 3) {
            Property property = (Property)ArrayUtils.firstOrNull(tileEntity.getPlayerProfile().getProperties().get((String) "textures"));
            return property != null && property.getValue().hashCode() == essenceSkinHash;
        }
        return false;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        blockList.clear();
        usedBlocks.clear();
        selectedBlock = null;
        lastCheckedPosition = null;
    }
}

