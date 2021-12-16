/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.ResourceLocation
 */
package cheaters.get.banned.remote;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.components.ClearButton;
import cheaters.get.banned.remote.Updater;
import cheaters.get.banned.utils.FontUtils;
import cheaters.get.banned.utils.RenderUtils;
import cheaters.get.banned.utils.Utils;
import java.awt.Color;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class UpdateGui
extends GuiScreen {
    public void initGui() {
        this.buttonList.add(new ClearButton(1, this.width / 2 - 105, this.height / 2 + 10, 100, 20, "Dismiss"));
        this.buttonList.add(new ClearButton(0, this.width / 2 + 5, this.height / 2 + 10, 100, 20, "Download"));
    }

    public void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            Utils.openUrl("https://cheatersgetbanned.me/");
        }
        Shady.guiToOpen = new GuiMainMenu();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        float ratio = 1.7727273f;
        float bgWidth = this.width;
        float bgHeight = this.height;
        if ((float)this.width / ratio < (float)this.height) {
            bgHeight = this.height;
            bgWidth = (float)this.height * ratio;
        } else {
            bgHeight = (float)this.width / ratio;
            bgWidth = this.width;
        }
        RenderUtils.drawTexture(new ResourceLocation("shadyaddons:background.jpg"), 0, 0, Math.round(bgWidth), Math.round(bgHeight));
        String title = "ShadyAddons \u00a7" + FontUtils.getRainbowCode('c') + Updater.update.version + "\u00a7f is available!";
        FontUtils.drawScaledCenteredString(title, 1.5f, this.width / 2, this.height / 2 - 15 - 5, true);
        if (Updater.update.description == null) {
            Updater.update.description = "\u00a77\u00a7oNo update description";
        }
        int descriptionWidth = this.mc.fontRendererObj.getStringWidth(Updater.update.description);
        this.mc.fontRendererObj.drawStringWithShadow(Updater.update.description, (float)(this.width - descriptionWidth) / 2.0f, (float)this.height / 2.0f - 7.0f, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

