/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MathHelper
 */
package cheaters.get.banned.config.settings;

import cheaters.get.banned.config.Property;
import cheaters.get.banned.config.settings.Setting;
import java.lang.reflect.Field;
import net.minecraft.util.MathHelper;

public class NumberSetting
extends Setting
implements Comparable<Integer> {
    public int step;
    public int min;
    public int max;
    public String prefix;
    public String suffix;

    public NumberSetting(Property annotation, Field field) {
        super(annotation, field);
        this.step = annotation.step();
        this.min = annotation.min();
        this.max = annotation.max();
        this.prefix = annotation.prefix();
        this.suffix = annotation.suffix();
    }

    @Override
    public boolean set(Object value) {
        return super.set(MathHelper.clamp_int((int)((Integer)value), (int)this.min, (int)this.max));
    }

    @Override
    public int compareTo(Integer other) {
        try {
            return Integer.compare(this.get(Integer.TYPE), other);
        }
        catch (Exception exception) {
            return 0;
        }
    }
}

