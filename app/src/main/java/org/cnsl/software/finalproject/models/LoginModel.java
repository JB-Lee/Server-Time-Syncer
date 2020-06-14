package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.RequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class LoginModel {

    public void doLogin(String id, String pw, RequestWrapper.ApiListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("pw", pw);

        RequestWrapper.doRequest(
                "http://192.168.0.18:5000/user/login",
                param,
                listener
        );
    }

}
