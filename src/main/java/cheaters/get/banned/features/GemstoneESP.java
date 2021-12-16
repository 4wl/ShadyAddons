/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockStainedGlass
 *  net.minecraft.block.BlockStainedGlassPane
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.EnumDyeColor
 *  net.minecraft.util.BlockPos
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.config.settings.FolderSetting;
import cheaters.get.banned.events.BlockChangeEvent;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.LocationUtils;
import cheaters.get.banned.utils.RenderUtils;
import cheaters.get.banned.utils.Utils;
import java.awt.Color;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GemstoneESP {
    private ConcurrentHashMap<BlockPos, Gemstone> gemstones = new ConcurrentHashMap();
    private HashSet<BlockPos> checked = new HashSet();
    private BlockPos lastChecked = null;
    private boolean isScanning = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!(!GemstoneESP.isEnabled() || this.isScanning || this.lastChecked != null && this.lastChecked.equals((Object)Shady.mc.thePlayer.playerLocation))) {
            this.isScanning = true;
            new Thread(() -> {
                BlockPos playerPosition;
                this.lastChecked = playerPosition = Shady.mc.thePlayer.getPosition();
                for (int x = playerPosition.getX() - Config.gemstoneRadius; x < playerPosition.getX() + Config.gemstoneRadius; ++x) {
                    for (int y = playerPosition.getY() - Config.gemstoneRadius; y < playerPosition.getY() + Config.gemstoneRadius; ++y) {
                        for (int z = playerPosition.getZ() - Config.gemstoneRadius; z < playerPosition.getZ() + Config.gemstoneRadius; ++z) {
                            Gemstone gemstone;
                            BlockPos position = new BlockPos(x, y, z);
                            if (!this.checked.contains((Object)position) && !Shady.mc.theWorld.isAirBlock(position) && (gemstone = GemstoneESP.getGemstone(Shady.mc.theWorld.getBlockState(position))) != null) {
                                this.gemstones.put(position, gemstone);
                            }
                            this.checked.add(position);
                        }
                    }
                }
                this.isScanning = false;
            }, "ShadyAddons-GemstoneScanner").start();
        }
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (event.newBlock.getBlock() == Blocks.air) {
            this.gemstones.remove((Object)event.position);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (GemstoneESP.isEnabled()) {
            for (Map.Entry<BlockPos, Gemstone> gemstone : this.gemstones.entrySet()) {
                double distance;
                if (!GemstoneESP.isGemstoneEnabled(gemstone.getValue()) || (distance = Math.sqrt(gemstone.getKey().distanceSq(Shady.mc.thePlayer.posX, Shady.mc.thePlayer.posY, Shady.mc.thePlayer.posZ))) > (double)(Config.gemstoneRadius + 2)) continue;
                int alpha = (int)Math.abs(100.0 - distance / (double)Config.gemstoneRadius * 100.0);
                Color color = Utils.addAlpha(gemstone.getValue().color, alpha);
                RenderUtils.highlightBlock(gemstone.getKey(), color, event.partialTicks);
            }
        }
    }

    private static boolean isEnabled() {
        return Shady.mc.thePlayer != null && Shady.mc.theWorld != null && FolderSetting.isEnabled("Gemstone ESP") && Utils.inSkyBlock && LocationUtils.onIsland(LocationUtils.Island.CRYSTAL_HOLLOWS);
    }

    private static Gemstone getGemstone(IBlockState block) {
        if (block.getBlock() != Blocks.stained_glass) {
            return null;
        }
        if (Config.includeGlassPanes && block.getBlock() != Blocks.stained_glass_pane) {
            return null;
        }
        EnumDyeColor color = Utils.firstNotNull(new EnumDyeColor[]{(EnumDyeColor)block.getValue((IProperty)BlockStainedGlass.COLOR), (EnumDyeColor)block.getValue((IProperty)BlockStainedGlassPane.COLOR)});
        if (color == Gemstone.RUBY.dyeColor) {
            return Gemstone.RUBY;
        }
        if (color == Gemstone.AMETHYST.dyeColor) {
            return Gemstone.AMETHYST;
        }
        if (color == Gemstone.JADE.dyeColor) {
            return Gemstone.JADE;
        }
        if (color == Gemstone.SAPPHIRE.dyeColor) {
            return Gemstone.SAPPHIRE;
        }
        if (color == Gemstone.AMBER.dyeColor) {
            return Gemstone.AMBER;
        }
        if (color == Gemstone.TOPAZ.dyeColor) {
            return Gemstone.TOPAZ;
        }
        if (color == Gemstone.JASPER.dyeColor) {
            return Gemstone.JASPER;
        }
        return null;
    }

    private static boolean isGemstoneEnabled(Gemstone gemstone) {
        switch (gemstone) {
            case RUBY: {
                return Config.rubyEsp;
            }
            case AMETHYST: {
                return Config.amethystEsp;
            }
            case JADE: {
                return Config.jadeEsp;
            }
            case SAPPHIRE: {
                return Config.sapphireEsp;
            }
            case AMBER: {
                return Config.amberEsp;
            }
            case TOPAZ: {
                return Config.topazEsp;
            }
            case JASPER: {
                return Config.jasperEsp;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        this.gemstones.clear();
        this.checked.clear();
        this.lastChecked = null;
    }

    static enum Gemstone {
        RUBY(new Color(188, 3, 29), EnumDyeColor.RED),
        AMETHYST(new Color(137, 0, 201), EnumDyeColor.PURPLE),
        JADE(new Color(157, 249, 32), EnumDyeColor.LIME),
        SAPPHIRE(new Color(60, 121, 224), EnumDyeColor.LIGHT_BLUE),
        AMBER(new Color(237, 139, 35), EnumDyeColor.ORANGE),
        TOPAZ(new Color(249, 215, 36), EnumDyeColor.YELLOW),
        JASPER(new Color(214, 15, 150), EnumDyeColor.MAGENTA);

        public Color color;
        public EnumDyeColor dyeColor;

        private Gemstone(Color color, EnumDyeColor dyeColor) {
            this.color = color;
            this.dyeColor = dyeColor;
        }
    }
}

