/*
 * Decompiled with CFR 0.150.
 */
package cheaters.get.banned.utils;

public class ThreadUtils {
    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }
}

