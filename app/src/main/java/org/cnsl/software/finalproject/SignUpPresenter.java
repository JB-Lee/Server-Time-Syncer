package org.cnsl.software.finalproject;

import android.text.TextUtils;
import android.util.Patterns;

import org.cnsl.software.finalproject.contract.SignUp;
import org.cnsl.software.finalproject.models.SignUpModel;
import org.cnsl.software.finalproject.utils.Async;
import org.cnsl.software.finalproject.utils.RequestWrapper;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpPresenter implements SignUp.Presenter {

    SignUp.View signUpView;
    SignUpModel signUpModel;

    boolean idChecked = false;

    public SignUpPresenter(SignUp.View signUpView) {
        this.signUpView = signUpView;
        this.signUpModel = new SignUpModel();
    }

    @Override
    public void onIdChanged(CharSequence s, int start, int before, int count) {
        idChecked = false;
        signUpView.setUserMsg("");
    }

    @Override
    public void onCheckId(String id) {
        signUpModel.chkId(id, new RequestWrapper.ApiListener() {
            @Override
            public void onSuccess(JSONObject json) {
                try {
                    idChecked = !json.getBoolean("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!idChecked)
                    Async.syncRunTask(() -> signUpView.setUserMsg("이미 존재하는 ID 입니다."));
                else
                    Async.syncRunTask(() -> signUpView.setUserMsg("확인 완료"));
            }

            @Override
            public void onFail(JSONObject json) {

            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    @Override
    public void onSignUp(String id, String email, String pw, String pw2) {

        boolean pass = true;

        if (!idChecked) {
            signUpView.setUserMsg("ID 체크를 진행하세요.");
            pass = false;
        } else {
            signUpView.setUserMsg("");
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpView.setEmailMsg("올바른 Email이 아닙니다.");
            pass = false;
        } else {
            signUpView.setEmailMsg("");
        }

        if (!pw.equals(pw2)) {
            signUpView.setPasswordMsg("비밀번호가 동일하지 않습니다.");
            pass = false;
        } else {
            signUpView.setPasswordMsg("");
        }

        if (pass) {
            signUpModel.doSignUp(id, email, pw, new RequestWrapper.ApiListener() {
                @Override
                public void onSuccess(JSONObject json) {
                    signUpView.goPreviousActivity();
                }

                @Override
                public void onFail(JSONObject json) {
                    signUpView.setMsg("이미 존재하는 계정입니다.");
                }

                @Override
                public void onError(String msg) {
                    signUpView.setMsg(msg);
                }
            });
        }
    }

    @Override
    public void onPasswordChanged(CharSequence s, int start, int before, int count) {
        int risk = signUpModel.getPasswordRisk(s.toString());

        int color = signUpModel.getWarningColor(risk);
        String warnMsg = "크랙에 소요되는 시간 : " + signUpModel.getCrackTime(risk);

        signUpView.setRiskMsg(warnMsg);
        signUpView.setRiskIconColor(color);

        if (signUpModel.passwordValid(risk)) {
            signUpView.toggleButton(true);
        } else {
            signUpView.toggleButton(false);
        }

    }
}
