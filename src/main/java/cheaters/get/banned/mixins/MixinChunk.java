/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.BlockPos
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cheaters.get.banned.mixins;

import cheaters.get.banned.Shady;
import cheaters.get.banned.events.BlockChangeEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Chunk.class})
public abstract class MixinChunk {
    @Shadow
    public abstract IBlockState getBlockState(BlockPos var1);

    @Inject(method={"setBlockState"}, at={@At(value="HEAD")})
    private void onBlockChange(BlockPos position, IBlockState newBlock, CallbackInfoReturnable<IBlockState> callbackInfoReturnable) {
        IBlockState oldBlock = this.getBlockState(position);
        if (oldBlock != newBlock && Shady.mc.theWorld != null) {
            try {
                MinecraftForge.EVENT_BUS.post((Event)new BlockChangeEvent(position, oldBlock, newBlock));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}

