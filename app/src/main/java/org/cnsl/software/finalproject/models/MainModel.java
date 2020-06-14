package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.Async;
import org.cnsl.software.finalproject.utils.HttpHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class MainModel {

    public void doServerTimeLookup(String url, ServerTimeListener listener) {

        new Async.Executor<Void>()
                .setCallable(() -> {
                    HttpHelper.Connection conn = new HttpHelper.Connection(url);
                    listener.onSuccess(conn.getHost(), conn.getServerTime());
                    conn.close();
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

    public void doLookup(Map<String, Object> param, ApiListener listener) {
        HttpHelper.asyncRequest(
                "http://192.168.0.18:5000/board/lookup",
                HttpHelper.Method.GET,
                param,
                new HttpHelper.ResponseListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        try {
                            if (json.getString("status").equals("Success"))
                                listener.onSuccess(json.getJSONArray("content"));
                            else
                                listener.onError(json.getString("content"));
                        } catch (JSONException e) {
                            listener.onError(e.toString());
                        }
                    }

                    @Override
                    public void onError(int code, String message) {
                        listener.onError(message);
                    }
                }
        );
    }

    public interface ServerTimeListener {
        void onSuccess(String hostname, Date date);

        void onError(Exception e);
    }

    public interface ApiListener {
        void onSuccess(JSONArray json);

        void onError(String msg);
    }
}
