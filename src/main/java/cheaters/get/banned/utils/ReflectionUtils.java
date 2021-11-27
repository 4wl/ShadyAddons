/*
 * Decompiled with CFR 0.150.
 */
package cheaters.get.banned.utils;

import cheaters.get.banned.Shady;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static boolean invoke(Class<?> _class, String methodName) {
        try {
            Method method = _class.getDeclaredMethod(methodName, new Class[0]);
            method.setAccessible(true);
            method.invoke((Object)Shady.mc, new Object[0]);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static Object field(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        }
        catch (Exception exception) {
            return null;
        }
    }
}

