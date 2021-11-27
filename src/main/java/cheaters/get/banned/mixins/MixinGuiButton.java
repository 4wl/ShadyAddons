/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 */
package cheaters.get.banned.mixins;

import cheaters.get.banned.config.Config;
import cheaters.get.banned.utils.FontUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiButton.class})
public abstract class MixinGuiButton {
    @Shadow
    public int id;
    @Shadow
    public int x;
    @Shadow
    public int y;
    @Shadow
    public String displayString;
    @Shadow
    public int width;
    @Shadow
    public int height;
    @Shadow
    public boolean visible;
    @Shadow
    protected boolean hovered;

    @Inject(method={"drawButton"}, at={@At(value="HEAD")}, cancellable=true)
    public void drawCleanButton(Minecraft mc, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        if (Config.useCleanButtons) {
            if (this.visible) {
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                Color color = this.hovered ? new Color(30, 30, 30, 64) : new Color(0, 0, 0, 64);
                Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), (int)color.getRGB());
                FontUtils.drawCenteredString(this.displayString, this.x + this.width / 2, this.y + this.height / 2);
            }
            callbackInfo.cancel();
        }
    }
}

