/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 */
package cheaters.get.banned.config.components;

import cheaters.get.banned.config.components.CheckboxInput;
import cheaters.get.banned.config.components.FolderComponent;
import cheaters.get.banned.config.components.NumberInput;
import cheaters.get.banned.config.components.SelectInput;
import cheaters.get.banned.config.components.SwitchInput;
import cheaters.get.banned.config.settings.BooleanSetting;
import cheaters.get.banned.config.settings.FolderSetting;
import cheaters.get.banned.config.settings.NumberSetting;
import cheaters.get.banned.config.settings.SelectSetting;
import cheaters.get.banned.config.settings.Setting;
import java.awt.Color;
import net.minecraft.client.gui.GuiButton;

public abstract class ConfigInput
extends GuiButton {
    public static Color white = new Color(255, 255, 255);
    public static Color green = new Color(85, 255, 85);
    public static Color red = new Color(255, 85, 85);
    public static Color transparent = new Color(255, 255, 255, 64);
    public Setting setting;

    public ConfigInput(Setting setting, int x, int y) {
        super(0, x, y, "");
        this.setting = setting;
    }

    public static ConfigInput buttonFromSetting(Setting setting, int x, int y) {
        switch (setting.annotation.type()) {
            case BOOLEAN: {
                return new SwitchInput((BooleanSetting)setting, x, y);
            }
            case CHECKBOX: {
                return new CheckboxInput((BooleanSetting)setting, x, y);
            }
            case FOLDER: {
                return new FolderComponent((FolderSetting)setting, x, y);
            }
            case NUMBER: {
                return new NumberInput((NumberSetting)setting, x, y);
            }
            case SELECT: {
                return new SelectInput((SelectSetting)setting, x, y);
            }
        }
        return null;
    }
}

