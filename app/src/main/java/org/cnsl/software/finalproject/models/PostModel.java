package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.HttpHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PostModel {

    public void doPost(Map<String, Object> param, PostListener listener) {

        HttpHelper.request(
                "http://192.168.0.18:5000/board/insert",
                HttpHelper.Method.GET,
                param,
                new HttpHelper.ResponseListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        try {
                            if (json.getString("status").equals("Success"))
                                listener.onSuccess();
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

    public interface PostListener {
        void onSuccess();

        void onError(String msg);
    }
}
