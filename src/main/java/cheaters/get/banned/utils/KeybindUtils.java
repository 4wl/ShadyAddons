/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 */
package cheaters.get.banned.utils;

import cheaters.get.banned.Shady;
import cheaters.get.banned.utils.ReflectionUtils;
import java.util.HashMap;
import net.minecraft.client.settings.KeyBinding;

public class KeybindUtils {
    public static HashMap<String, KeyBinding> keyBindings = new HashMap();

    public static void register(String name, int key) {
        keyBindings.put(name, new KeyBinding(name, key, "ShadyAddons"));
    }

    public static boolean isPressed(String name) {
        return KeybindUtils.get(name).isPressed();
    }

    public static KeyBinding get(String name) {
        return keyBindings.get(name);
    }

    public static void rightClick() {
        if (!ReflectionUtils.invoke(Shady.mc.getClass(), "func_147121_ag")) {
            ReflectionUtils.invoke(Shady.mc.getClass(), "rightClickMouse");
        }
    }

    public static void leftClick() {
        if (!ReflectionUtils.invoke(Shady.mc.getClass(), "func_147116_af")) {
            ReflectionUtils.invoke(Shady.mc.getClass(), "clickMouse");
        }
    }

    public static void middleClick() {
        if (!ReflectionUtils.invoke(Shady.mc.getClass(), "func_147112_ai")) {
            ReflectionUtils.invoke(Shady.mc.getClass(), "middleClickMouse");
        }
    }
}

