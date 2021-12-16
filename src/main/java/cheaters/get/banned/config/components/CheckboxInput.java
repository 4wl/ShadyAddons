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
        this.xPosition -= 9;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        CheckboxInput.drawRect((int)this.xPosition, (int)this.yPosition, (int)(this.xPosition + this.width), (int)(this.yPosition + this.height), (int)white.getRGB());
        if (this.setting.get(Boolean.class).booleanValue()) {
            FontUtils.drawString("\u00a70x", this.xPosition + 2, this.yPosition, false);
        } else if (this.hovered) {
            FontUtils.drawString("\u00a77x", this.xPosition + 2, this.yPosition, false);
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

