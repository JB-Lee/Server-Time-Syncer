package org.cnsl.software.finalproject;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.cnsl.software.finalproject.contract.Login;
import org.cnsl.software.finalproject.models.LoginModel;
import org.cnsl.software.finalproject.utils.Async;
import org.cnsl.software.finalproject.utils.GlobalVariable;
import org.cnsl.software.finalproject.utils.RequestWrapper;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter implements Login.Presenter {

    Login.View loginView;
    LoginModel loginModel;

    boolean serverChecked = false;

    public LoginPresenter(Login.View loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel();
    }

    @Override
    public void onCheckServer(String url) {
        loginModel.doCheckServer(url, new RequestWrapper.ApiListener() {
            @Override
            public void onSuccess(JSONObject json) {
                try {
                    if (json.getString("msg").equals("ServerTimeSyncer")) {
                        serverChecked = true;
                        Async.syncRunTask(() -> {
                            GlobalVariable.setServerUrl(url);
                            loginView.setServerMsg("연동 성공");
                        });
                    } else {
                        serverChecked = false;
                        Async.syncRunTask(() -> {
                            loginView.setServerMsg("연동 실패");
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    serverChecked = false;
                    Async.syncRunTask(() -> {
                        loginView.setServerMsg("연동 실패.");
                    });
                }
            }

            @Override
            public void onFail(JSONObject json) {
                serverChecked = false;
                Async.syncRunTask(() -> {
                    loginView.setServerMsg("연동 실패.");
                });

            }

            @Override
            public void onError(String msg) {
                serverChecked = false;
                Async.syncRunTask(() -> {
                    loginView.setServerMsg("연동 실패.");
                });

            }
        });
    }

    @Override
    public void onServerUrlChanged(CharSequence s, int start, int before, int count) {
        serverChecked = false;
        loginView.setServerMsg("");
    }

    @Override
    public void onLogin(String id, String password) {
        if (!serverChecked) {
            loginView.setServerMsg("서버 연동이 필요함");
            return;
        }

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
                Async.syncRunTask(() -> loginView.showForgotPassword(true));
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(((AppCompatActivity) loginView).getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                Async.syncRunTask(() -> loginView.showForgotPassword(true));
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
