package org.cnsl.software.finalproject;

import org.cnsl.software.finalproject.contract.Login;
import org.cnsl.software.finalproject.models.LoginModel;

public class LoginPresenter implements Login.Presenter {

    Login.View loginView;
    LoginModel loginModel;

    public LoginPresenter(Login.View loginView){
        this.loginView = loginView;
        this.loginModel = new LoginModel();
    }

    @Override
    public void onLogin(String id, String password) {
        if (loginModel.doLogin(id, password))
            loginView.startMainActivity();
        else
            loginView.showForgotPassword(true);
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
