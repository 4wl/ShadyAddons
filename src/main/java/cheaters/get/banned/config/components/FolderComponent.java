/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.ResourceLocation
 */
package cheaters.get.banned.config.components;

import cheaters.get.banned.config.components.ConfigInput;
import cheaters.get.banned.config.settings.FolderSetting;
import cheaters.get.banned.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FolderComponent
extends ConfigInput {
    public FolderSetting setting;

    public FolderComponent(FolderSetting setting, int x, int y) {
        super(setting, x, y);
        this.setting = setting;
        this.width = 300;
        this.height = 9;
        this.xPosition -= this.width;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        RenderUtils.drawRotatedTexture(new ResourceLocation("shadyaddons:chevron.png"), this.xPosition + this.width - this.height, this.yPosition, this.height, this.height, this.setting.get(Boolean.class) != false ? 180 : 0);
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.hovered) {
            this.setting.set(this.setting.get(Boolean.class) == false);
            return true;
        }
        return false;
    }
}

