/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.SoundHandler
 *  net.minecraft.client.gui.GuiButton
 */
package cheaters.get.banned.config.components;

import cheaters.get.banned.config.components.ConfigInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class Scrollbar
extends GuiButton {
    public Scrollbar(int y, int viewport, int contentHeight, int scrollOffset, int x, boolean hovered) {
        super(0, x, y, "");
        this.yPosition += Math.round((float)scrollOffset / (float)contentHeight * (float)viewport);
        this.width = 5;
        this.height = contentHeight > viewport ? Math.round((float)viewport / (float)contentHeight * (float)viewport) : 0;
        this.hovered = hovered;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = this.mousePressed(mc, mouseX, mouseY);
        Scrollbar.drawRect((int)this.xPosition, (int)this.yPosition, (int)(this.xPosition + this.width), (int)(this.yPosition + this.height), (int)(this.hovered ? ConfigInput.white.getRGB() : ConfigInput.transparent.getRGB()));
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
    }
}

