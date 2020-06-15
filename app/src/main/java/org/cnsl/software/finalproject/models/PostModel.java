package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.GlobalVariable;
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

    public String getUsername() {
        return username;
    }

    public String getHostname() {
        return hostname;
    }

    public void doPost(String content, RequestWrapper.ApiListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("writer", username);
        param.put("cat", hostname);
        param.put("content", content);

        RequestWrapper.doRequest(
                GlobalVariable.getServerUrl() + "/board/insert",
                param,
                listener
        );
    }

}
