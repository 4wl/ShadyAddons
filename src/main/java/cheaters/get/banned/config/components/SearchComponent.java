/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.util.ChatAllowedCharacters
 *  net.minecraft.util.ResourceLocation
 */
package cheaters.get.banned.config.components;

import cheaters.get.banned.config.components.ConfigInput;
import cheaters.get.banned.utils.FontUtils;
import cheaters.get.banned.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;

public class SearchComponent
extends GuiButton {
    public String text;

    public SearchComponent(int x, int y, int width, String initText) {
        super(0, x, y, width, 18, "");
        this.text = initText;
    }

    public void func_146112_a(Minecraft mc, int mouseX, int mouseY) {
        RenderUtils.drawTexture(new ResourceLocation("shadyaddons:search.png"), this.x, this.y + 2, 14, 14);
        SearchComponent.drawRect((int)(this.x + 20), (int)this.y, (int)(this.x + this.width + 15), (int)(this.y + this.height), (int)ConfigInput.transparent.getRGB());
        FontUtils.drawScaledString(this.text, 1.25f, this.x + 26, this.y + 4, false);
        int textWidth = (int)((float)FontUtils.getStringWidth(this.text) * 1.25f);
        if (System.currentTimeMillis() / 500L % 2L == 0L) {
            SearchComponent.drawRect((int)(this.x + textWidth + 20 + 1), (int)(this.y + 2), (int)(this.x + textWidth + 26 + 2), (int)(this.y + this.height - 2), (int)ConfigInput.white.getRGB());
        }
    }

    public void onKeyTyped(char letter, int code) {
        if (code == 14) {
            this.text = "";
        } else if (ChatAllowedCharacters.isAllowedCharacter((char)letter)) {
            StringBuilder stringBuilder = new StringBuilder();
            if ((double)FontUtils.getStringWidth(stringBuilder.append(this.text).append(letter).toString()) * 1.25 <= (double)(this.width - 2)) {
                this.text = this.text + Character.toLowerCase(letter);
            }
        }
    }
}

