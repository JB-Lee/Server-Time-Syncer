package org.cnsl.software.finalproject;

import org.cnsl.software.finalproject.contract.Post;
import org.cnsl.software.finalproject.models.PostModel;

import java.util.HashMap;
import java.util.Map;

public class PostPresenter implements Post.Presenter {

    Post.View postView;
    PostModel postModel;

    public PostPresenter(Post.View postView) {
        this.postView = postView;
        postModel = new PostModel();
    }

    @Override
    public void onPost(String writer, String hostname, String content) {
        Map<String, Object> param = new HashMap<>();
        param.put("writer", writer);
        param.put("cat", hostname.substring(1));
        param.put("content", content);
        postModel.doPost(param, new PostModel.PostListener() {
            @Override
            public void onSuccess() {
                postView.goPreviousActivity();
            }

            @Override
            public void onError(String msg) {

            }
        });

    }
}
