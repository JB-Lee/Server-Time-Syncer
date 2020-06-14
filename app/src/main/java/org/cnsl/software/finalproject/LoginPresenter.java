package org.cnsl.software.finalproject;

import android.os.Handler;
import android.os.Looper;

import org.cnsl.software.finalproject.contract.Login;
import org.cnsl.software.finalproject.models.LoginModel;
import org.cnsl.software.finalproject.utils.RequestWrapper;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter implements Login.Presenter {

    Login.View loginView;
    LoginModel loginModel;

    public LoginPresenter(Login.View loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel();
    }

    @Override
    public void onLogin(String id, String password) {
        loginModel.doLogin(id, password, new RequestWrapper.ApiListener() {
            @Override
            public void onSuccess(JSONObject json) {
                try {
                    loginView.startMainActivity(json.getString("id"), json.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(JSONObject json) {
                new Handler(Looper.getMainLooper()).post(() -> loginView.showForgotPassword(true));
            }

            @Override
            public void onError(String msg) {
                new Handler(Looper.getMainLooper()).post(() -> loginView.showForgotPassword(true));
            }
        });

    }

    @Override
    public void onFindPassword() {
        loginView.startFindPasswordActivity();
    }

    @Override
    public void onSignUp() {
        loginView.startSignUpActivity();
    }
}
