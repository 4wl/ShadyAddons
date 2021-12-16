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
        this.xPosition -= this.width;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        SwitchInput.drawRect((int)this.xPosition, (int)(this.yPosition + 3), (int)(this.xPosition + this.width), (int)(this.yPosition + 6), (int)white.getRGB());
        SwitchInput.drawRect((int)(this.setting.get(Boolean.class) != false ? this.xPosition + this.width - this.height : this.xPosition), (int)this.yPosition, (int)(this.setting.get(Boolean.class) != false ? this.xPosition + this.width : this.xPosition + this.height), (int)(this.yPosition + this.height), (int)(this.setting.get(Boolean.class) != false ? green.getRGB() : red.getRGB()));
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
            this.setting.set(this.setting.get(Boolean.class) == false);
            return true;
        }
        return false;
    }
}

