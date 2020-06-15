package org.cnsl.software.finalproject.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RequestWrapper {
    public static void doRequest(String url, Map<String, Object> param, ApiListener listener) {
        HttpHelper.asyncRequest(
                url,
                HttpHelper.Method.GET,
                param,
                new HttpHelper.ResponseListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        try {
                            if (json.getString("status").equals("Success"))
                                listener.onSuccess(json.getJSONObject("content"));
                            else
                                listener.onFail(json.getJSONObject("content"));
                        } catch (JSONException e) {
                            listener.onError(e.toString());
                        }
                    }

                    @Override
                    public void onFail(int code, String message) {
                        listener.onError(message);
                    }

                    @Override
                    public void onError(Exception e) {
                        listener.onError(e.toString());
                    }
                }
        );

    }

    public static void doRequest(String url, Map<String, Object> param, ListApiListener listener) {
        HttpHelper.asyncRequest(
                url,
                HttpHelper.Method.GET,
                param,
                new HttpHelper.ResponseListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        try {
                            if (json.getString("status").equals("Success"))
                                listener.onSuccess(json.getJSONArray("content"));
                            else
                                listener.onFail(json.getJSONObject("content"));
                        } catch (JSONException e) {
                            listener.onError(e.toString());
                        }
                    }

                    @Override
                    public void onFail(int code, String message) {
                        listener.onError(message);

                    }

                    @Override
                    public void onError(Exception e) {
                        listener.onError(e.toString());
                    }
                }
        );
    }

    public interface ApiListener {
        void onSuccess(JSONObject json);

        void onFail(JSONObject json);

        void onError(String msg);
    }

    public interface ListApiListener {
        void onSuccess(JSONArray json);

        void onFail(JSONObject json);

        void onError(String msg);
    }
}
