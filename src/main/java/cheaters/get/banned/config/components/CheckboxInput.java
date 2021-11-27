/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package cheaters.get.banned.config.components;

import cheaters.get.banned.config.components.ConfigInput;
import cheaters.get.banned.config.settings.BooleanSetting;
import cheaters.get.banned.utils.FontUtils;
import net.minecraft.client.Minecraft;

public class CheckboxInput
extends ConfigInput {
    public BooleanSetting setting;

    public CheckboxInput(BooleanSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        this.width = 9;
        this.height = 9;
        this.x -= 9;
    }

    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        CheckboxInput.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), (int)white.getRGB());
        if (this.setting.get(Boolean.class).booleanValue()) {
            FontUtils.drawString("\u00a70x", this.x + 2, this.y, false);
        } else if (this.hovered) {
            FontUtils.drawString("\u00a77x", this.x + 2, this.y, false);
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.hovered) {
            this.setting.set(this.setting.get(Boolean.class) == false);
            return true;
        }
        return false;
    }
}

