/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiMainMenu
 */
package cheaters.get.banned.mixins;

import cheaters.get.banned.remote.Updater;
import cheaters.get.banned.utils.FontUtils;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiMainMenu.class})
public abstract class MixinGuiMainMenu {
    @Shadow
    private String splashText;

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    public void initMainMenu(CallbackInfo callbackInfo) {
        if (Updater.update != null && !Updater.update.version.equals("2.2.3")) {
            this.splashText = "Update to \u00a7" + FontUtils.getRainbowCode('e') + Updater.update.version + "\u00a7e!";
        }
    }
}

