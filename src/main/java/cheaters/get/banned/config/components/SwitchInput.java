/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package cheaters.get.banned.config.components;

import cheaters.get.banned.config.components.ConfigInput;
import cheaters.get.banned.config.settings.BooleanSetting;
import net.minecraft.client.Minecraft;

public class SwitchInput
extends ConfigInput {
    public BooleanSetting setting;

    public SwitchInput(BooleanSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        this.width = 25;
        this.height = 9;
        this.x -= this.width;
    }

    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        SwitchInput.drawRect((int)this.x, (int)(this.y + 3), (int)(this.x + this.width), (int)(this.y + 6), (int)white.getRGB());
        SwitchInput.drawRect((int)(this.setting.get(Boolean.class) != false ? this.x + this.width - this.height : this.x), (int)this.y, (int)(this.setting.get(Boolean.class) != false ? this.x + this.width : this.x + this.height), (int)(this.y + this.height), (int)(this.setting.get(Boolean.class) != false ? green.getRGB() : red.getRGB()));
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
            this.setting.set(this.setting.get(Boolean.class) == false);
            return true;
        }
        return false;
    }
}

