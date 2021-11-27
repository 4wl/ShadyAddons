/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.Loader
 *  net.minecraftforge.fml.common.ModContainer
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.apache.commons.io.IOUtils
 *  org.apache.http.HttpEntity
 *  org.apache.http.client.entity.UrlEncodedFormEntity
 *  org.apache.http.client.methods.HttpPost
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.impl.client.HttpClients
 *  org.apache.http.message.BasicNameValuePair
 */
package cheaters.get.banned.remote;

import cheaters.get.banned.Shady;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class CrashReporter {
    public static void send(File file, String reason) {
        try {
            String serverId = UUID.randomUUID().toString().replace("-", "");
            String hash = CrashReporter.hashMod();
            String report = IOUtils.toString((InputStream)new FileInputStream(file), (Charset)StandardCharsets.UTF_8).replace(Shady.mc.getSession().getProfile().getName(), "<USERNAME>");
            report = Base64.getEncoder().encodeToString(report.getBytes(StandardCharsets.UTF_8));
            reason = Base64.getEncoder().encodeToString(reason.getBytes(StandardCharsets.UTF_8));
            String note = "Good for you, checking out your mods! See cheaters.get.banned.remove.Analytics for information on how this works!";
            Shady.mc.getSessionService().joinServer(Shady.mc.getSession().getProfile(), Shady.mc.getSession().getToken(), serverId);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("https://cheatersgetbanned.me/api/crashes/");
            ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
            parameters.add(new BasicNameValuePair("username", Shady.mc.getSession().getProfile().getName()));
            parameters.add(new BasicNameValuePair("server_id", serverId));
            parameters.add(new BasicNameValuePair("hash", hash));
            parameters.add(new BasicNameValuePair("version", "2.2.3"));
            parameters.add(new BasicNameValuePair("report", report));
            parameters.add(new BasicNameValuePair("reason", reason));
            post.setEntity((HttpEntity)new UrlEncodedFormEntity(parameters));
            client.execute((HttpUriRequest)post);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static String hashMod() {
        File file;
        ModContainer mod = (ModContainer)Loader.instance().getIndexedModList().get("autogg");
        if (mod != null && (file = mod.getSource()) != null) {
            try {
                InputStream inputStream = Files.newInputStream(file.toPath(), new OpenOption[0]);
                return DigestUtils.sha256Hex((InputStream)inputStream);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return null;
    }
}

