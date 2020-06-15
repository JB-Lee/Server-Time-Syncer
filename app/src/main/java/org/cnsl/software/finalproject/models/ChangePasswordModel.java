package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.GlobalVariable;
import org.cnsl.software.finalproject.utils.PasswordHelper;
import org.cnsl.software.finalproject.utils.RequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordModel {

    String id;
    String email;

    public ChangePasswordModel(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void doChangePassword(String pw, RequestWrapper.ApiListener listener) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("pw", pw);
        RequestWrapper.doRequest(
                GlobalVariable.getServerUrl() + "/user/password/change",
                param,
                listener
        );
    }

    public int getPasswordRisk(String pw) {
        return PasswordHelper.getPasswordRisk(pw);
    }

    public boolean passwordValid(int risk) {
        return risk / (60 * 60) > 1;
    }


    public String getCrackTime(int risk) {
        return PasswordHelper.getCrackTime(risk);
    }

    public int getWarningColor(int risk) {
        return PasswordHelper.getWarningColor(risk);
    }

}
