package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.RequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class PostModel {
    String username;
    String hostname;

    public PostModel(String username, String hostname) {
        this.username = username;
        this.hostname = hostname;
    }

    public void doPost(String content, RequestWrapper.ApiListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("writer", username);
        param.put("cat", hostname);
        param.put("content", content);

        RequestWrapper.doRequest(
                "http://192.168.0.18:5000/board/insert",
                param,
                listener
        );
    }

}
