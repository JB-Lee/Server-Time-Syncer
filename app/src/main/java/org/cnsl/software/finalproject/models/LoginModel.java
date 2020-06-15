package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.GlobalVariable;
import org.cnsl.software.finalproject.utils.RequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class LoginModel {

    public void doLogin(String id, String pw, RequestWrapper.ApiListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("pw", pw);

        RequestWrapper.doRequest(
                GlobalVariable.getServerUrl() + "/user/login",
                param,
                listener
        );
    }

    public void doCheckServer(String url, RequestWrapper.ApiListener listener) {
        RequestWrapper.doRequest(
                url + "/hello",
                null,
                listener
        );
    }

}
