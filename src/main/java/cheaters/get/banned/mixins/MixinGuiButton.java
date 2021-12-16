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
    public int xPosition;
    @Shadow
    public int yPosition;
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
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                Color color = this.hovered ? new Color(30, 30, 30, 64) : new Color(0, 0, 0, 64);
                Gui.drawRect((int)this.xPosition, (int)this.yPosition, (int)(this.xPosition + this.width), (int)(this.yPosition + this.height), (int)color.getRGB());
                FontUtils.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + this.height / 2);
            }
            callbackInfo.cancel();
        }
    }
}

