/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cheaters.get.banned.config.settings;

import cheaters.get.banned.config.Property;
import cheaters.get.banned.config.settings.ParentSetting;
import cheaters.get.banned.events.SettingChangeEvent;
import java.lang.reflect.Field;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class Setting {
    public String name;
    public ParentSetting parent = null;
    public String note;
    public boolean warning;
    public boolean beta;
    public Field field;
    public Property annotation;

    public Setting(Property annotation, Field field) {
        this.annotation = annotation;
        this.name = annotation.name();
        this.warning = annotation.warning();
        this.beta = annotation.beta();
        if (!annotation.note().equals("")) {
            this.note = annotation.note();
        }
        this.field = field;
    }

    public int getIndent(int startingIndent) {
        return this.getIndent(startingIndent, this);
    }

    public int getIndent(int startingIndent, Setting setting) {
        if (setting.parent != null) {
            return setting.getIndent(startingIndent += 10, setting.parent);
        }
        return startingIndent;
    }

    public <T> T get(Class<T> type) {
        try {
            return type.cast(this.field.get(Object.class));
        }
        catch (Exception exception) {
            return null;
        }
    }

    public boolean set(Object value) {
        try {
            Object oldValue = this.get(Object.class);
            this.field.set(value.getClass(), value);
            MinecraftForge.EVENT_BUS.post((Event)new SettingChangeEvent(this, oldValue, value));
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public boolean forceSet(Object value) {
        try {
            this.field.set(value.getClass(), value);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }
}

