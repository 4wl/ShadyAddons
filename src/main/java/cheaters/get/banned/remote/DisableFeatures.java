/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package cheaters.get.banned.remote;

import cheaters.get.banned.utils.HttpUtils;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DisableFeatures {
    public static List<String> load() {
        String url = "https://cheatersgetbanned.me/api/disabled-features.json";
        String response = null;
        try {
            response = HttpUtils.fetch(url);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (response != null) {
      //      return Arrays.asList((Object[])new Gson().fromJson(response, String[].class));
        }
        return Collections.emptyList();
    }
}

