package org.cnsl.software.finalproject.contract;

public interface FindPassword {

    interface View {
        void goPreviousActivity();

        void setMsg(String msg);
    }

    interface Presenter {
        void onSubmit(String id, String email);
    }
}
