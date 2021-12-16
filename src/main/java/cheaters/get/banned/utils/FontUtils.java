/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 */
package cheaters.get.banned.utils;

import cheaters.get.banned.Shady;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;

public class FontUtils {
    public static char getRainbowCode(char fallback) {
        return (char)(Shady.usingSkyBlockAddons && (!Shady.usingPatcher || Shady.usingSkytils) ? 122 : fallback);
    }

    public static String enforceWidth(String text, int width) {
        String[] splitText = text.split(" ");
        int lineWidth = 0;
        StringBuilder result = new StringBuilder();
        for (String word : splitText) {
            int wordWidth = FontUtils.getStringWidth(word);
            if (wordWidth + lineWidth > width) {
                result.append(word).append("\n");
                lineWidth = 0;
                continue;
            }
            result.append(word).append(" ");
            lineWidth += wordWidth + FontUtils.getStringWidth(" ");
        }
        return result.toString();
    }

    public static void drawCenteredString(String text, int x, int y, boolean shadow) {
        String[] lines;
        y -= FontUtils.getStringHeight(text) / 2;
        for (String line : lines = text.split("\n")) {
            FontUtils.drawString(line, x - FontUtils.getStringWidth(line) / 2, y, shadow);
            y += FontUtils.getLineHeight() + 1;
        }
    }

    public static void drawCenteredString(String text, int x, int y) {
        FontUtils.drawCenteredString(text, x, y, true);
    }

    public static void drawString(String text, int x, int y, boolean shadow) {
        String[] lines;
        for (String line : lines = text.split("\n")) {
            Shady.mc.fontRendererObj.drawString(line, (float)x, (float)y, Color.WHITE.getRGB(), shadow);
            y += FontUtils.getLineHeight() + 1;
        }
    }

    public static int getStringHeight(String text) {
        int lines = text.split("\n").length;
        return lines > 1 ? lines * (FontUtils.getLineHeight() + 1) - 1 : FontUtils.getLineHeight();
    }

    public static int getStringWidth(String text) {
        String[] lines = text.split("\n");
        int longestLine = 0;
        for (String line : lines) {
            int lineWidth = Shady.mc.fontRendererObj.getStringWidth(line);
            if (lineWidth <= longestLine) continue;
            longestLine = lineWidth;
        }
        return longestLine;
    }

    public static int getLineHeight() {
        return Shady.mc.fontRendererObj.FONT_HEIGHT;
    }

    public static void drawScaledString(String string, float scale, int x, int y, boolean shadow) {
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)scale, (float)scale, (float)scale);
        FontUtils.drawString(string, (int)((float)x / scale), (int)((float)y / scale), shadow);
        GlStateManager.popMatrix();
    }

    public static void drawScaledCenteredString(String string, float scale, int x, int y, boolean shadow) {
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)scale, (float)scale, (float)scale);
        FontUtils.drawCenteredString(string, (int)((float)x / scale), (int)((float)y / scale), shadow);
        GlStateManager.popMatrix();
    }
}

