/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package cheaters.get.banned.remote;

import cheaters.get.banned.utils.HttpUtils;
import com.google.gson.Gson;

public class MayorAPI {
    private static Mayor mayor = null;

    public static void fetch() {
        new Thread(() -> {
            String response = null;
            try {
                response = HttpUtils.fetch("https://cheatersgetbanned.me/api/mayor/");
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (response != null) {
                mayor = (Mayor)new Gson().fromJson(response, Mayor.class);
            } else {
                System.out.println("Error fetching current mayor");
            }
        }, "ShadyAddons-MayorAPI").start();
    }

    public static boolean isPaulBonus() {
        return mayor != null && MayorAPI.mayor.name.equals("paul") && MayorAPI.mayor.ezpz;
    }

    public static Mayor getMayor() {
        return mayor;
    }

    public static void forcePaul() {
        mayor = new Mayor("paul", true);
    }

    public static class Mayor {
        public String name;
        public boolean ezpz;

        public Mayor(String name, boolean ezpz) {
            this.name = name;
            this.ezpz = ezpz;
        }
    }
}

