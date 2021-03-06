package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.GlobalVariable;
import org.cnsl.software.finalproject.utils.PasswordHelper;
import org.cnsl.software.finalproject.utils.RequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class SignUpModel {

    public int getPasswordRisk(String pw) {
        return PasswordHelper.getPasswordRisk(pw);
    }

    public boolean passwordValid(int risk) {
        return risk / (60 * 60) > 1;
    }

    public void doSignUp(String id, String email, String pw, RequestWrapper.ApiListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("email", email);
        param.put("pw", pw);
        RequestWrapper.doRequest(
                GlobalVariable.getServerUrl() + "/user/signup",
                param,
                listener
        );
    }

    public void chkId(String id, RequestWrapper.ApiListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        RequestWrapper.doRequest(
                GlobalVariable.getServerUrl() + "/user/check",
                param,
                listener
        );
    }

    public String getCrackTime(int risk) {
        return PasswordHelper.getCrackTime(risk);
    }

    public int getWarningColor(int risk) {
        return PasswordHelper.getWarningColor(risk);
    }

}
