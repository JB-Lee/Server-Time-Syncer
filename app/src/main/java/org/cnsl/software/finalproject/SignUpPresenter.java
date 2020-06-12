package org.cnsl.software.finalproject;

import android.text.TextUtils;
import android.util.Patterns;

import org.cnsl.software.finalproject.contract.SignUp;
import org.cnsl.software.finalproject.models.SignUpModel;

public class SignUpPresenter implements SignUp.Presenter {

    SignUp.View signUpView;
    SignUpModel signUpModel;

    public SignUpPresenter(SignUp.View signUpView) {
        this.signUpView = signUpView;
        this.signUpModel = new SignUpModel();
    }

    @Override
    public void onSignUp(String id, String email, String pw, String pw2) {

        boolean pass = true;

        if (!signUpModel.chkId(id)) {
            signUpView.setUserMsg("이미 존재하는 ID 입니다.");
            pass = false;
        } else {
            signUpView.setUserMsg("");
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpView.setEmailMsg("올바른 Email이 아닙니다.");
            pass = false;
        } else if (!signUpModel.chkEmail(email)) {
            signUpView.setEmailMsg("이미 등록된 Email 입니다.");
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
            if (signUpModel.doSignUp(id, pw)) {
                signUpView.goPreviousActivity();
            }
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
