package org.cnsl.software.finalproject.utils;

import android.util.Log;
import android.util.Pair;

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
        http.setConnectTimeout(2000);
        http.setReadTimeout(2000);
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
            listener.onFail(code, http.getResponseMessage());
        }
        http.disconnect();
    }

    private static Pair<Boolean, Object> _simpleRequest(String url, Method method, Map<String, Object> params) throws IOException {
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
        http.setConnectTimeout(2000);
        http.setReadTimeout(2000);
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

            return new Pair<Boolean, Object>(true, data.toString());
        } else {
            http.disconnect();
            return new Pair<Boolean, Object>(false, new Pair<Integer, String>(code, http.getResponseMessage()));
        }

    }

    public static void asyncRequest(String url, Method method, Map<String, Object> params, ResponseListener listener) {
        new Async.Executor<Void>()
                .setCallable(() -> {
                    Pair<Boolean, Object> result = _simpleRequest(url, method, params);
                    if (result.first)
                        listener.onSuccess(new JSONObject((String) result.second));
                    else {
                        Pair<Integer, String> failResult = (Pair<Integer, String>) result.second;
                        listener.onFail(failResult.first, failResult.second);
                    }
                    return null;
                })
                .setCallback(new Async.Callback<Void>() {
                    @Override
                    public void onResult(Void result) {

                    }

                    @Override
                    public void onError(Exception e) {
                        listener.onError(e);
                    }
                })
                .execute();

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

    public interface ResponseListener {
        void onSuccess(JSONObject json);

        void onFail(int code, String message);

        void onError(Exception e);
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
