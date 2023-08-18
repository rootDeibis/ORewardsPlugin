package me.rootdeibis.orewards.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class RequestUtils
{
    public static String get(final String url, final String... params) {
        return requestContent(url, "get", params);
    }

    public static String post(final String url, final String... params) {
        return requestContent(url, "post", params);
    }

    public static String image(final String url, final String method, final String... params) {
        final HttpURLConnection http = requestInput(url, method, params);
        try {
            return Base64.encodeBase64String(IOUtils.toByteArray(http.getInputStream()));
        }
        catch (Exception e) {
            return null;
        }
    }

    public static HashMap<String, String> parseJson(final String jsonData) {
        final HashMap<String, String> jsonReturn = new HashMap<String, String>();
        final Pattern pattern = Pattern.compile("(\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\")|(\"([^\"]+)\"\\s*:\\s*(\\d+\\.?\\d*))|(\"([^\"]+)\"\\s*:\\s*(\\w+))");
        final Matcher matcher = pattern.matcher(jsonData);
        while (matcher.find()) {
            final String[] $split = matcher.group().replaceAll("\"", "").split(":");
            jsonReturn.put($split[0], $split[1]);
        }
        return jsonReturn;
    }

    private static String requestContent(final String $url, final String method, final String... params) {
        final HttpURLConnection http = requestInput($url, method, params);
        try {
            if (http.getResponseCode() == 422) {
                return null;
            }
            return IOUtils.toString(http.getInputStream(), http.getContentEncoding());
        }
        catch (IOException e) {
            return null;
        }
    }

    private static HttpURLConnection requestInput(String $url, final String method, final String... params) {
        try {
            final String data = String.join("&", (CharSequence[])params);
            if (method.equalsIgnoreCase("get")) {
                $url = $url + "?" + data;
            }
            final URL url = new URL($url);
            final HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod(method.toUpperCase());
            http.setDoOutput(true);
            if (method.equalsIgnoreCase("post")) {
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                final byte[] outData = data.getBytes(StandardCharsets.UTF_8);
                final OutputStream outputStream = http.getOutputStream();
                outputStream.write(outData);
            }
            http.disconnect();
            return http;
        }
        catch (Exception e) {
            return null;
        }
    }
}