/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cheaters.get.banned.events;

import cheaters.get.banned.config.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SettingChangeEvent
extends Event {
    public Setting setting;
    public Object oldValue;
    public Object newValue;

    public SettingChangeEvent(Setting setting, Object oldValue, Object newValue) {
        this.setting = setting;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}

