/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterables
 */
package cheaters.get.banned.utils;

import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ArrayUtils {
    public static Object getRandomItem(List<?> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static <T> T firstOrNull(Iterable<T> iterable) {
        return (T)Iterables.getFirst(iterable, null);
    }

    public static <T> T getFirstMatch(List<T> list, Predicate<? super T> predicate) {
        return list.stream().filter(predicate).findFirst().orElse(null);
    }
}

