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
        this.leftWidth = Shady.mc.fontRenderer.getStringWidth("<");
        this.rightWidth = Shady.mc.fontRenderer.getStringWidth(">");
        this.gap = 3;
        this.leftHovered = false;
        this.rightHovered = false;
        this.setting = setting;
        this.height = 10;
        this.updateText();
    }

    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        this.rightHovered = mouseX >= this.x - this.rightWidth - this.gap && mouseY >= this.y && mouseX < this.x && mouseY < this.y + this.height;
        this.leftHovered = mouseX >= this.x - this.width && mouseY >= this.y && mouseX < this.x - this.width + this.leftWidth + this.gap && mouseY < this.y + this.height;
        Shady.mc.fontRenderer.drawString((this.leftHovered ? "\u00a7a" : "\u00a77") + "<", this.x - this.width, this.y, -1);
        Shady.mc.fontRenderer.drawString(this.displayString, this.x - this.width + this.leftWidth + this.gap, this.y, -1);
        Shady.mc.fontRenderer.drawString((this.rightHovered ? "\u00a7a" : "\u00a77") + ">", this.x - this.rightWidth, this.y, -1);
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
        this.width = Shady.mc.fontRenderer.getStringWidth(this.displayString) + this.rightWidth + this.leftWidth + this.gap * 2;
    }
}

