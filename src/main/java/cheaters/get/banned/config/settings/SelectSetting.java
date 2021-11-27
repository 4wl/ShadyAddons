/*
 * Decompiled with CFR 0.150.
 */
package cheaters.get.banned.config.settings;

import cheaters.get.banned.config.Property;
import cheaters.get.banned.config.settings.Setting;
import java.lang.reflect.Field;

public class SelectSetting
extends Setting {
    public String[] options;

    public SelectSetting(Property annotation, Field field) {
        super(annotation, field);
        this.options = annotation.options();
    }

    @Override
    public boolean set(Object value) {
        if (((Number)value).intValue() > this.options.length - 1) {
            return super.set(0);
        }
        if (((Number)value).intValue() < 0) {
            return super.set(this.options.length - 1);
        }
        return super.set(value);
    }
}

