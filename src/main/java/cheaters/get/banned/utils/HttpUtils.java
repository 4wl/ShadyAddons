/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.http.HttpEntity
 *  org.apache.http.client.methods.HttpGet
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.impl.client.HttpClientBuilder
 *  org.apache.http.impl.client.HttpClients
 *  org.apache.http.util.EntityUtils
 */
package cheaters.get.banned.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
    public static String fetch(String url) {
        return HttpUtils.fetch(url, true);
    }

    public static String fetch(String url, boolean includeUserAgent) {
        String response = null;
        /*HttpClientBuilder client = HttpClients.custom().addInterceptorFirst((request, context) -> {
            if (!request.containsHeader("Pragma")) {
                request.addHeader("Pragma", "no-cache");
            }
            if (!request.containsHeader("Cache-Control")) {
                request.addHeader("Cache-Control", "no-cache");
            }
        });*/
        if (includeUserAgent) {
          //  client.setUserAgent("ShadyAddons/2.2.3");
        }
        try {
            HttpGet request2 = new HttpGet(url);
            //response = EntityUtils.toString((HttpEntity)client.build().execute((HttpUriRequest)request2).getEntity(), (String)"UTF-8");
        }
        catch (Exception exception) {
            // empty catch block
        }
        return response;
    }
}

