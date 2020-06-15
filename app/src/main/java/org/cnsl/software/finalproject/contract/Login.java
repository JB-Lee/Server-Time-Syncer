package org.cnsl.software.finalproject.contract;

public interface Login {

    interface View {
        void showForgotPassword(boolean show);

        void clearId();

        void clearPw();

        void setServerMsg(String msg);

        void startMainActivity(String id, String email);

        void startSignUpActivity();

        void startFindPasswordActivity();
    }

    interface Presenter {
        void onLogin(String id, String password);

        void onFindPassword();

        void onSignUp();

        void onCheckServer(String url);

        void onServerUrlChanged(CharSequence s, int start, int before, int count);
    }
}
