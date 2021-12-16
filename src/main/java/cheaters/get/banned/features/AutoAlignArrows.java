/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityItemFrame
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.utils.KeybindUtils;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoAlignArrows {
    private static HashMap<BlockPos, ItemFrame> itemFrames = new HashMap();
    private static boolean calculatedPath = false;
    private int counter = 0;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (this.counter % 5 == 0 && AutoAlignArrows.isEnabled()) {
            this.counter = 0;
            if (itemFrames.isEmpty()) {
                AutoAlignArrows.addItemFrames();
            } else if (calculatedPath) {
                if (Shady.mc.objectMouseOver.entityHit instanceof EntityItemFrame && itemFrames.containsKey((Object)Shady.mc.objectMouseOver.entityHit.getPosition()) && itemFrames.get((Object)Shady.mc.objectMouseOver.entityHit.getPosition()).shouldClick()) {
                    KeybindUtils.rightClick();
                }
            } else {
                AutoAlignArrows.calculatePath();
            }
        }
        ++this.counter;
    }

    private static void calculatePath() {
        block0: for (Map.Entry<BlockPos, ItemFrame> frame : itemFrames.entrySet()) {
            BlockPos position = frame.getKey();
            ItemFrame itemFrame = frame.getValue();
            if (itemFrame.finalRotation != null) continue;
            for (ItemFrame.Rotation rotation : ItemFrame.Rotation.values()) {
                if (!itemFrames.containsKey((Object)position.offset(rotation.facing))) continue;
                itemFrame.finalRotation = rotation;
                continue block0;
            }
        }
        calculatedPath = true;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        itemFrames.clear();
        calculatedPath = false;
    }

    private static boolean isEnabled() {
        return Config.autoArrowAlign && Shady.mc.thePlayer != null && Shady.mc.thePlayer.getPosition().getY() > 100 && Shady.mc.thePlayer.getPosition().getY() < 150 && AutoAlignArrows.isInSection3(Shady.mc.thePlayer.getPosition());
    }

    private static void addItemFrames() {
        for (Entity entity : Shady.mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityItemFrame) || !AutoAlignArrows.isInSection3(entity.getPosition())) continue;
            itemFrames.put(entity.getPosition(), new ItemFrame((EntityItemFrame)entity));
        }
    }

    private static boolean isInSection3(BlockPos blockPos) {
        int x = Shady.mc.thePlayer.getPosition().getX();
        int z = Shady.mc.thePlayer.getPosition().getZ();
        return x < 218 && z > 251 && x > 196 && z < 319;
    }

    private static class ItemFrame {
        public BlockPos position;
        private Rotation finalRotation = null;
        private EntityItemFrame entity;
        public Type type;

        public ItemFrame(EntityItemFrame entity) {
            this.entity = entity;
            if (entity.getDisplayedItem().getItem() == Items.arrow) {
                this.type = Type.NORMAL;
            }
            if (entity.getDisplayedItem().getItem() == Item.getItemFromBlock((Block)Blocks.wool)) {
                if (entity.getDisplayedItem().getItemDamage() == 5) {
                    this.type = Type.START;
                }
                if (entity.getDisplayedItem().getItemDamage() == 14) {
                    this.type = Type.END;
                }
            }
        }

        public boolean shouldClick() {
            if (this.finalRotation == null || this.type != Type.NORMAL) {
                return false;
            }
            return this.entity.getRotation() != this.finalRotation.number;
        }

        static enum Type {
            NORMAL,
            START,
            END;

        }

        static enum Rotation {
            NORTH(7, EnumFacing.UP),
            SOUTH(3, EnumFacing.DOWN),
            EAST(1, EnumFacing.NORTH),
            WEST(5, EnumFacing.SOUTH);

            public int number;
            public EnumFacing facing;

            private Rotation(int number, EnumFacing facing) {
                this.number = number;
                this.facing = facing;
            }
        }
    }
}

