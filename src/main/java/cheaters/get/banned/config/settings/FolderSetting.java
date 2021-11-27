/*
 * Decompiled with CFR 0.150.
 */
package cheaters.get.banned.config.settings;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.ConfigLogic;
import cheaters.get.banned.config.Property;
import cheaters.get.banned.config.settings.BooleanSetting;
import cheaters.get.banned.config.settings.ParentSetting;
import cheaters.get.banned.config.settings.Setting;
import java.lang.reflect.Field;

public class FolderSetting
extends ParentSetting {
    public FolderSetting(Property annotation, Field field) {
        super(annotation, field);
    }

    public boolean isChildEnabled() {
        for (Setting child : this.children) {
            if (child instanceof BooleanSetting && child.get(Boolean.class).booleanValue()) {
                return true;
            }
            if (!(child instanceof FolderSetting) || !((FolderSetting)child).isChildEnabled()) continue;
            return true;
        }
        return false;
    }

    public static boolean isEnabled(String name) {
        Setting setting = ConfigLogic.getSettingByName(name, Shady.settings);
        if (setting == null) {
            return false;
        }
        return ((FolderSetting)setting).isChildEnabled();
    }
}

