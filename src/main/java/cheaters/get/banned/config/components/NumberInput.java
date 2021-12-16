/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package cheaters.get.banned.config.components;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.components.ConfigInput;
import cheaters.get.banned.config.settings.NumberSetting;
import net.minecraft.client.Minecraft;

public class NumberInput
extends ConfigInput {
    private int minusWidth;
    private int plusWidth;
    private int gap;
    private boolean minusHovered;
    private boolean plusHovered;
    public NumberSetting setting;

    public NumberInput(NumberSetting setting, int x, int y) {
        super(setting, x, y);
        this.minusWidth = Shady.mc.fontRendererObj.getStringWidth("-");
        this.plusWidth = Shady.mc.fontRendererObj.getStringWidth("+");
        this.gap = 3;
        this.minusHovered = false;
        this.plusHovered = false;
        this.setting = setting;
        this.height = 10;
        this.updateText();
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.plusHovered = mouseX >= this.xPosition - this.plusWidth - this.gap && mouseY >= this.yPosition && mouseX < this.xPosition && mouseY < this.yPosition + this.height;
        this.minusHovered = mouseX >= this.xPosition - this.width && mouseY >= this.yPosition && mouseX < this.xPosition - this.width + this.minusWidth + this.gap && mouseY < this.yPosition + this.height;
        Shady.mc.fontRendererObj.drawString((this.minusHovered ? "\u00a7c" : "\u00a77") + "-", this.xPosition - this.width, this.yPosition, -1);
        Shady.mc.fontRendererObj.drawString(this.displayString, this.xPosition - this.width + this.minusWidth + this.gap, this.yPosition, -1);
        Shady.mc.fontRendererObj.drawString((this.plusHovered ? "\u00a7a" : "\u00a77") + "+", this.xPosition - this.plusWidth, this.yPosition, -1);
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.plusHovered || this.minusHovered) {
            if (this.plusHovered) {
                this.setting.set(this.setting.get(Integer.class) + this.setting.step);
            }
            if (this.minusHovered) {
                this.setting.set(this.setting.get(Integer.class) - this.setting.step);
            }
            this.updateText();
            return true;
        }
        return false;
    }

    public void updateText() {
        this.displayString = (this.setting.prefix == null ? "" : this.setting.prefix) + this.setting.get(Integer.class) + (this.setting.suffix == null ? "" : this.setting.suffix);
        this.width = Shady.mc.fontRendererObj.getStringWidth(this.displayString) + this.plusWidth + this.minusWidth + this.gap * 2;
    }
}

