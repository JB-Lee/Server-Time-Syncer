package org.cnsl.software.finalproject.contract;

public interface ChangePassword {

    interface View {
        void goPreviousActivity();

        void toggleButton(boolean enable);

        void setRiskMsg(String string);

        void setRiskIconColor(int color);

        void setPasswordMsg(String msg);
    }

    interface Presenter {
        void onChangePassword(String pw, String pw2);

        void onPasswordChanged(CharSequence s, int start, int before, int count);
    }
}
