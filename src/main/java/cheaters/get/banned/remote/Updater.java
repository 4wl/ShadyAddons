/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package cheaters.get.banned.remote;

import cheaters.get.banned.utils.HttpUtils;
import com.google.gson.Gson;

public class Updater {
    public static boolean shouldUpdate = false;
    public static Update update = null;

    public static void check() {
        new Thread(() -> {
            String url = "https://cheatersgetbanned.me/api/updates";
            String response = null;
            try {
                response = HttpUtils.fetch(url);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (response != null) {
                update = (Update)new Gson().fromJson(response, Update.class);
                shouldUpdate = !Updater.update.version.equals("2.2.3");
            } else {
                System.out.println("Error checking for updates");
            }
        }, "ShadyAddons-Updater").start();
    }

    public static class Update {
        public String version;
        public String download;
        public String description;
        public int users;

        public Update(String version, String download, String description, int users) {
            this.version = version;
            this.download = download;
            this.description = description;
            this.users = users;
        }
    }
}

