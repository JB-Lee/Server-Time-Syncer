package org.cnsl.software.finalproject;

import org.cnsl.software.finalproject.contract.Post;
import org.cnsl.software.finalproject.models.PostModel;
import org.cnsl.software.finalproject.utils.RequestWrapper;
import org.json.JSONObject;

public class PostPresenter implements Post.Presenter {

    Post.View postView;
    PostModel postModel;

    public PostPresenter(Post.View postView, String username, String hostname) {
        this.postView = postView;
        postModel = new PostModel(username, hostname);
    }

    @Override
    public void onPost(String content) {
        postModel.doPost(content, new RequestWrapper.ApiListener() {
            @Override
            public void onSuccess(JSONObject json) {
                postView.goPreviousActivity();
            }

            @Override
            public void onFail(JSONObject json) {

            }

            @Override
            public void onError(String msg) {

            }
        });

    }
}
