package org.cnsl.software.finalproject;

import org.cnsl.software.finalproject.contract.ChangePassword;
import org.cnsl.software.finalproject.models.ChangePasswordModel;
import org.cnsl.software.finalproject.utils.RequestWrapper;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordPresenter implements ChangePassword.Presenter {

    ChangePassword.View changePasswordView;
    ChangePasswordModel changePasswordModel;

    public ChangePasswordPresenter(ChangePassword.View changePasswordView, String id, String email) {
        this.changePasswordView = changePasswordView;
        changePasswordModel = new ChangePasswordModel(id, email);
    }

    @Override
    public void onChangePassword(String pw, String pw2) {
        boolean pass = true;

        if (!pw.equals(pw2)) {
            changePasswordView.setPasswordMsg("비밀번호가 동일하지 않습니다.");
            pass = false;
        }

        if (pass) {
            changePasswordModel.doChangePassword(pw, new RequestWrapper.ApiListener() {
                @Override
                public void onSuccess(JSONObject json) {
                    changePasswordView.goPreviousActivity();
                }

                @Override
                public void onFail(JSONObject json) {
                    try {
                        if (json.has("reason"))
                            changePasswordView.setPasswordMsg(json.getString("reason"));
                        if (json.has("msg"))
                            changePasswordView.setPasswordMsg(json.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String msg) {
                    changePasswordView.setPasswordMsg(msg);
                }
            });
        }
    }

    @Override
    public void onPasswordChanged(CharSequence s, int start, int before, int count) {
        int risk = changePasswordModel.getPasswordRisk(s.toString());

        int color = changePasswordModel.getWarningColor(risk);
        String warnMsg = "크랙에 소요되는 시간 : " + changePasswordModel.getCrackTime(risk);

        changePasswordView.setRiskMsg(warnMsg);
        changePasswordView.setRiskIconColor(color);

        if (changePasswordModel.passwordValid(risk)) {
            changePasswordView.toggleButton(true);
        } else {
            changePasswordView.toggleButton(false);
        }
    }
}
