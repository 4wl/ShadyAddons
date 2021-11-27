/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.util.BlockPos
 */
package cheaters.get.banned.features.dungeonmap;

import cheaters.get.banned.Shady;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class BlockList {
    public HashMap<Block, Integer> blocks;

    public BlockList(BlockPos position) {
        this.blocks = BlockList.getBlockFrequencyList(position.getX(), position.getY(), position.getZ());
    }

    public BlockList(int x, int y, int z) {
        this.blocks = BlockList.getBlockFrequencyList(x, y, z);
    }

    public HashMap<Block, Integer> getBlocks() {
        return this.blocks;
    }

    public static HashMap<Block, Integer> getBlockFrequencyList(int xPosition, int yPosition, int zPosition) {
        HashMap<Block, Integer> blockFrequency = new HashMap<Block, Integer>();
        for (int x = xPosition - 16; x < xPosition + 16; ++x) {
            for (int z = zPosition - 16; z < zPosition + 16; ++z) {
                Block block;
                blockFrequency.put(block, blockFrequency.containsKey((Object)(block = Shady.mc.world.getBlockState(new BlockPos(x, yPosition, z)).getBlock())) ? blockFrequency.get((Object)block) + 1 : 1);
            }
        }
        return blockFrequency;
    }
}

