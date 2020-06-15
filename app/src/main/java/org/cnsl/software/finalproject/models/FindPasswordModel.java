package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.GlobalVariable;
import org.cnsl.software.finalproject.utils.RequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class FindPasswordModel {

    public void doSubmit(String id, String email, RequestWrapper.ApiListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("email", email);

        RequestWrapper.doRequest(
                GlobalVariable.getServerUrl() + "/user/password/find",
                param,
                listener
        );
    }
}
