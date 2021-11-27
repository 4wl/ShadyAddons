/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.input.Mouse
 */
package cheaters.get.banned.config;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.ConfigLogic;
import cheaters.get.banned.config.components.ConfigInput;
import cheaters.get.banned.config.components.Scrollbar;
import cheaters.get.banned.config.settings.BooleanSetting;
import cheaters.get.banned.config.settings.FolderSetting;
import cheaters.get.banned.config.settings.Setting;
import cheaters.get.banned.features.jokes.CatPeople;
import cheaters.get.banned.utils.FontUtils;
import cheaters.get.banned.utils.RenderUtils;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class ConfigGui
extends GuiScreen {
    public static ArrayList<Setting> settings = new ArrayList();
    private int prevMouseY;
    private int scrollOffset = 0;
    private boolean scrolling = false;
    private ResourceLocation logo;
    private Scrollbar scrollbar;
    private final int columnWidth = 300;
    private final int headerHeight = 109;
    private Integer prevWidth = null;
    private Integer prevHeight = null;

    public ConfigGui(ResourceLocation logo) {
        this.logo = logo;
        settings = this.getFilteredSettings();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.mouseMoved(mouseY);
        GlStateManager.color((float)255.0f, (float)255.0f, (float)255.0f);
        Shady.mc.getTextureManager().bindTexture(this.logo);
        ConfigGui.drawModalRectWithCustomSizedTexture((int)(this.width / 2 - 143), (int)(24 - this.scrollOffset), (float)0.0f, (float)0.0f, (int)286, (int)40, (float)286.0f, (float)40.0f);
        this.drawCenteredString(Shady.mc.fontRenderer, (Shady.BETA ? "Beta \u2726 " : "Stable \u2726 ") + "2.2.3", this.width / 2, 67 - this.scrollOffset, -1);
        for (int i = 0; i < settings.size(); ++i) {
            Setting setting = settings.get(i);
            int x = this.getOffset();
            int y = 109 + i * 15 - this.scrollOffset;
            x += setting.getIndent(0);
            if (setting.parent == null && i > 0) {
                ConfigGui.drawRect((int)x, (int)(y - 3), (int)(this.getOffset() + 300), (int)(y - 2), (int)ConfigInput.transparent.getRGB());
            }
            if (setting.warning || setting.beta) {
                int textureX;
                if (setting.warning) {
                    textureX = (int)(System.currentTimeMillis() / 1000L % 2L * 9L);
                    RenderUtils.drawTexture(new ResourceLocation("shadyaddons:warning.png"), x, y, 9, 9, 18, 9, textureX, 0);
                } else {
                    textureX = (int)(System.currentTimeMillis() / 100L % 10L * 9L);
                    RenderUtils.drawTexture(new ResourceLocation("shadyaddons:beta.png"), x, y, 9, 9, 90, 9, textureX, 0);
                }
                x += 13;
            }
            char color = 'f';
            if (setting instanceof BooleanSetting && setting.get(Boolean.class).booleanValue()) {
                color = 'a';
            }
            if (setting instanceof FolderSetting && ((FolderSetting)setting).isChildEnabled()) {
                color = 'a';
            }
            Shady.mc.fontRenderer.drawString("\u00a7" + color + setting.name, x, y + 1, -1);
            if (setting.note == null) continue;
            int settingNameWidth = Shady.mc.fontRenderer.getStringWidth(setting.name + " ");
            GlStateManager.translate((double)0.0, (double)1.8, (double)0.0);
            FontUtils.drawScaledString("\u00a77" + setting.note, 0.8f, x + settingNameWidth, y + 1, false);
            GlStateManager.translate((double)0.0, (double)-1.8, (double)0.0);
        }
        if (this.prevHeight != null && this.prevWidth != null && (this.prevWidth != this.width || this.prevHeight != this.height)) {
            Shady.mc.displayGuiScreen((GuiScreen)new ConfigGui(this.logo));
        }
        this.prevWidth = this.width;
        this.prevHeight = this.height;
    }

    public void initGui() {
        this.buttonList.clear();
        int x = this.getOffset() + 300;
        int y = 109 - this.scrollOffset;
        for (int i = 0; i < settings.size(); ++i) {
            Setting setting = settings.get(i);
            this.buttonList.add(ConfigInput.buttonFromSetting(setting, x, y + i * 15));
        }
        int viewport = this.height - 109 - 9;
        int contentHeight = settings.size() * 15;
        int scrollbarX = this.getOffset() + 300 + 10;
        this.scrollbar = new Scrollbar(109, viewport, contentHeight, this.scrollOffset, scrollbarX, this.scrolling);
        this.buttonList.add(this.scrollbar);
    }

    protected void actionPerformed(GuiButton button) {
        if (button instanceof Scrollbar) {
            this.scrolling = true;
        } else {
            settings.clear();
            settings = this.getFilteredSettings();
        }
        this.initGui();
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.scrolling = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    private void scrollScreen(int scrollAmount, boolean pixels) {
        int viewport = this.height - 109 - 9;
        int contentHeight = settings.size() * 15;
        if (!pixels) {
            scrollAmount = (int)((float)scrollAmount / (float)viewport * (float)contentHeight);
        }
        if (contentHeight > viewport) {
            this.scrollOffset = MathHelper.clamp((int)(this.scrollOffset + scrollAmount), (int)0, (int)(contentHeight - viewport));
            this.initGui();
        }
    }

    private void mouseMoved(int mouseY) {
        if (this.scrolling) {
            this.scrollScreen(mouseY - this.prevMouseY, false);
        }
        this.prevMouseY = mouseY;
    }

    private ArrayList<Setting> getFilteredSettings() {
        ArrayList<Setting> newSettings = new ArrayList<Setting>();
        for (Setting setting : Shady.settings) {
            if (!CatPeople.usingPack && (setting.name.equals("Catgirls") || setting.parent != null && setting.parent.name.equals("Catgirls"))) continue;
            if (setting.parent == null) {
                newSettings.add(setting);
                continue;
            }
            if (!newSettings.contains(setting.parent) || !setting.parent.get(Boolean.class).booleanValue()) continue;
            newSettings.add(setting);
        }
        return newSettings;
    }

    public void handleMouseInput() throws IOException {
        if (Mouse.getEventDWheel() != 0) {
            this.scrollScreen(Integer.signum(Mouse.getEventDWheel()) * -10, true);
        }
        super.handleMouseInput();
    }

    public void onGuiClosed() {
        ConfigLogic.save();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    private int getOffset() {
        return (this.width - 300) / 2;
    }
}

