/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.ModContainer
 */
package cheaters.get.banned.mixins;

import java.util.List;
import net.minecraftforge.fml.common.ModContainer;

public abstract class MixinModList {
    private static List<ModContainer> modifyModList(List<ModContainer> modList) {
        System.out.println("Before: " + modList);
        modList.removeIf(mod -> mod.getName().equals("ShadyAddons"));
        System.out.println("After: " + modList);
        return modList;
    }
}

