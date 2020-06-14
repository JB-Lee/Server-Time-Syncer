package org.cnsl.software.finalproject.contract;

public interface Post {

    interface View {
        void goPreviousActivity();
    }

    interface Presenter {
        void onPost(String writer, String hostname, String content);
    }
}
