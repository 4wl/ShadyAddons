/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 */
package cheaters.get.banned.config.components;

import cheaters.get.banned.utils.FontUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ClearButton
extends GuiButton {
    public ClearButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public ClearButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            Color color = this.hovered ? new Color(15, 15, 15, 64) : new Color(0, 0, 0, 64);
            ClearButton.drawRect((int)this.xPosition, (int)this.yPosition, (int)(this.xPosition + this.width), (int)(this.yPosition + this.height), (int)color.getRGB());
            FontUtils.drawCenteredString(this.displayString, this.xPosition + this.width / 2, this.yPosition + this.height / 2);
        }
    }
}

