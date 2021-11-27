/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.SplashProgress
 */
package cheaters.get.banned.mixins;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.SplashProgress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={SplashProgress.class})
public abstract class MixinSlashProgress {
    @ModifyVariable(method={"start"}, at=@At(value="STORE"), ordinal=1, remap=false)
    private static ResourceLocation setSplash(ResourceLocation resourceLocation) {
        return new ResourceLocation("shadyaddons:splash.png");
    }
}

