package org.cnsl.software.finalproject.contract;

public interface SignUp {

    interface View {
        void goPreviousActivity();
        void toggleButton(boolean enable);
        void setRiskMsg(String string);
        void setRiskIconColor(int color);
        void setPasswordMsg(String msg);
        void setUserMsg(String msg);
        void setEmailMsg(String msg);

        void setMsg(String msg);
    }

    interface Presenter {
        void onSignUp(String id, String email, String pw, String pw2);

        void onPasswordChanged(CharSequence s, int start, int before, int count);

        void onCheckId(String id);

        void onIdChanged(CharSequence s, int start, int before, int count);
    }
}
