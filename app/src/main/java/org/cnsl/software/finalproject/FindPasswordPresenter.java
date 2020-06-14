package org.cnsl.software.finalproject;

import org.cnsl.software.finalproject.contract.FindPassword;
import org.cnsl.software.finalproject.models.FindPasswordModel;
import org.cnsl.software.finalproject.utils.Async;
import org.cnsl.software.finalproject.utils.RequestWrapper;
import org.json.JSONException;
import org.json.JSONObject;

public class FindPasswordPresenter implements FindPassword.Presenter {

    FindPassword.View findPasswordView;
    FindPasswordModel findPasswordModel;

    public FindPasswordPresenter(FindPassword.View findPasswordView) {
        this.findPasswordView = findPasswordView;
        this.findPasswordModel = new FindPasswordModel();
    }

    @Override
    public void onSubmit(String id, String msg) {
        findPasswordModel.doSubmit(id, msg, new RequestWrapper.ApiListener() {
            @Override
            public void onSuccess(JSONObject json) {
                try {
                    String gen_pw = json.getString("changed");
                    Async.syncRunTask(() -> findPasswordView.setMsg(gen_pw));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(JSONObject json) {
                if (json.has("reason")) {
                    Async.syncRunTask(() -> {
                        try {
                            findPasswordView.setMsg(json.getString("reason"));
                        } catch (JSONException e) {
                            findPasswordView.setMsg("변경 실패");
                        }
                    });
                }
            }

            @Override
            public void onError(String msg) {
                Async.syncRunTask(() -> findPasswordView.setMsg("변경 오류"));
            }
        });
    }
}
