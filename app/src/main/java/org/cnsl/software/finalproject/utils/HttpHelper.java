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
            StringBuffer sb = new StringBuffer();
            for (String key : params.keySet()) {
                if (sb.length() > 0)
                    sb.append("&");
                sb.append(URLEncoder.encode(key, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(params.get(key).toString(), "UTF-8"));

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
            http.getOutputStream().write(param.getBytes("UTF-8"));
        }

        int code = http.getResponseCode();

        if (code == 200) {
            StringBuffer data = new StringBuffer();
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

    public static HttpURLConnection urlOpen(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestMethod("GET");
        return conn;
    }

    public static Date getServerTime(String url) {
        try {
            String timeString = urlOpen(url).getHeaderField("Date");
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            return format.parse(timeString);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Conn {
        private HttpURLConnection conn;

        public Conn(String url){
            try {
                conn = urlOpen(url);
                conn.setInstanceFollowRedirects(true);
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

        public String getTitle() {
            String title = "";
            try{
                String urlLine;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((urlLine = br.readLine()) != null) {
                    if ( urlLine.indexOf("<title>") >= 0 ) {
                        int indexStart = urlLine.indexOf("<title>")+"<title>".length();
                        int indexEnd = urlLine.indexOf("</title>");
                        title = urlLine.substring(indexStart,indexEnd);
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }


    }
}
