/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package cheaters.get.banned.config.components;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.components.ConfigInput;
import cheaters.get.banned.config.settings.SelectSetting;
import net.minecraft.client.Minecraft;

public class SelectInput
extends ConfigInput {
    private int leftWidth;
    private int rightWidth;
    private int gap;
    private boolean leftHovered;
    private boolean rightHovered;
    public SelectSetting setting;

    public SelectInput(SelectSetting setting, int x, int y) {
        super(setting, x, y);
        this.leftWidth = Shady.mc.fontRendererObj.getStringWidth("<");
        this.rightWidth = Shady.mc.fontRendererObj.getStringWidth(">");
        this.gap = 3;
        this.leftHovered = false;
        this.rightHovered = false;
        this.setting = setting;
        this.height = 10;
        this.updateText();
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.rightHovered = mouseX >= this.xPosition - this.rightWidth - this.gap && mouseY >= this.yPosition && mouseX < this.xPosition && mouseY < this.yPosition + this.height;
        this.leftHovered = mouseX >= this.xPosition - this.width && mouseY >= this.yPosition && mouseX < this.xPosition - this.width + this.leftWidth + this.gap && mouseY < this.yPosition + this.height;
        Shady.mc.fontRendererObj.drawString((this.leftHovered ? "\u00a7a" : "\u00a77") + "<", this.xPosition - this.width, this.yPosition, -1);
        Shady.mc.fontRendererObj.drawString(this.displayString, this.xPosition - this.width + this.leftWidth + this.gap, this.yPosition, -1);
        Shady.mc.fontRendererObj.drawString((this.rightHovered ? "\u00a7a" : "\u00a77") + ">", this.xPosition - this.rightWidth, this.yPosition, -1);
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.rightHovered || this.leftHovered) {
            if (this.rightHovered) {
                this.setting.set(this.setting.get(Integer.class) + 1);
            }
            if (this.leftHovered) {
                this.setting.set(this.setting.get(Integer.class) - 1);
            }
            this.updateText();
            return true;
        }
        return false;
    }

    public void updateText() {
        this.displayString = this.setting.options[this.setting.get(Integer.class)];
        this.width = Shady.mc.fontRendererObj.getStringWidth(this.displayString) + this.rightWidth + this.leftWidth + this.gap * 2;
    }
}

