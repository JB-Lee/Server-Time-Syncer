package org.cnsl.software.finalproject.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class HttpHelper {

    private static final String TAG = HttpHelper.class.getSimpleName();

    public enum Method {
        GET, POST
    }

    public interface ResponseListener {
        void onSuccess(JSONObject json);

        void onError(int code, String message);
    }

    private static void _request(String url, Method method, Map<String, Object> params, ResponseListener listener) throws IOException {
        String param = null;

        if (params != null) {
            StringBuilder sb = new StringBuilder();
            for (String key : params.keySet()) {
                if (sb.length() > 0)
                    sb.append("&");
                sb.append(URLEncoder.encode(key, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8"));

            }
            param = sb.toString();
        }

        if (Method.GET.equals(method)) {
            if (url.contains("?"))
                url += "&" + param;
            else
                url += "?" + param;
        }

        URL u = new URL(url);
        HttpURLConnection http = (HttpURLConnection) u.openConnection();
        http.setRequestMethod(method.toString());

        if (Method.POST.equals(method)) {
            http.setDoOutput(true);
            if (param != null) {
                http.getOutputStream().write(param.getBytes(StandardCharsets.UTF_8));
            }
        }

        int code = http.getResponseCode();

        if (code == 200) {
            StringBuilder data = new StringBuilder();
            String tmp;
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            while ((tmp = br.readLine()) != null)
                data.append(tmp);
            br.close();
            try {
                listener.onSuccess(new JSONObject(data.toString()));
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
        } else {
            listener.onError(code, http.getResponseMessage());
        }
        http.disconnect();
    }

    private static String _simpleRequest(String url, Method method, Map<String, Object> params) throws IOException {
        String param = null;

        if (params != null) {
            StringBuilder sb = new StringBuilder();
            for (String key : params.keySet()) {
                if (sb.length() > 0)
                    sb.append("&");
                sb.append(URLEncoder.encode(key, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(String.valueOf(params.get(key)), "UTF-8"));

            }
            param = sb.toString();
        }

        if (Method.GET.equals(method)) {
            if (url.contains("?"))
                url += "&" + param;
            else
                url += "?" + param;
        }

        URL u = new URL(url);
        HttpURLConnection http = (HttpURLConnection) u.openConnection();
        http.setRequestMethod(method.toString());

        if (Method.POST.equals(method)) {
            http.setDoOutput(true);
            if (param != null) {
                http.getOutputStream().write(param.getBytes(StandardCharsets.UTF_8));
            }
        }

        int code = http.getResponseCode();

        if (code == 200) {
            StringBuilder data = new StringBuilder();
            String tmp;
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            while ((tmp = br.readLine()) != null)
                data.append(tmp);
            br.close();

            http.disconnect();

            return data.toString();
        } else {
            http.disconnect();
            return http.getResponseMessage();
        }

    }

    public static void request(String url, Method method, Map<String, Object> params, ResponseListener listener) {
        new Thread() {
            public void run() {
                try {
                    _request(url, method, params, listener);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        }.start();
    }

    public static void asyncRequest(String url, Method method, Map<String, Object> params, ResponseListener listener) {
        new Async.Executor<Void>()
                .setCallable(() -> {
                    String msg = _simpleRequest(url, method, params);
                    listener.onSuccess(new JSONObject(msg));
                    return null;
                })
                .execute();

    }

    public static HttpURLConnection urlOpen(String url) throws IOException {
        URL u = new URL(url);
        return urlOpen(u);
    }

    public static HttpURLConnection urlOpen(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }

    public static class Connection {
        private HttpURLConnection conn;
        private URL url;

        public Connection(String url) {
            try {
                this.url = new URL(url);
                conn = urlOpen(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Date getServerTime(){
            try {
                String timeString = conn.getHeaderField("Date");
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
                return format.parse(timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getHost() {
            return url.getHost();
        }

        public void close() {
            conn.disconnect();
        }
    }
}
