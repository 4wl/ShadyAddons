/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.crash.CrashReport
 */
package cheaters.get.banned.mixins;

import cheaters.get.banned.config.Config;
import cheaters.get.banned.remote.CrashReporter;
import java.io.File;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={CrashReport.class})
public abstract class MixinCrashReport {
    @Shadow
    public abstract String getCauseStackTraceOrString();

    @Inject(method={"saveToFile"}, at={@At(value="RETURN")})
    public void onCrash(File file, CallbackInfoReturnable<Boolean> cir) {
        String reason = this.getCauseStackTraceOrString().split("\n")[0];
        if (Config.sendCrashReports && file.exists()) {
            CrashReporter.send(file, reason);
        }
    }
}

