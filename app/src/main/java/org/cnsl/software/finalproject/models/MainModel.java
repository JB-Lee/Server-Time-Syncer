package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.Async;
import org.cnsl.software.finalproject.utils.GlobalVariable;
import org.cnsl.software.finalproject.utils.HttpHelper;
import org.cnsl.software.finalproject.utils.RequestWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainModel {

    String id;
    String email;

    public MainModel(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void doServerTimeLookup(String url, ServerTimeListener listener) {

        new Async.Executor<Void>()
                .setCallable(() -> {
                    HttpHelper.Connection conn = new HttpHelper.Connection(url);
                    listener.onSuccess(conn.getHost(), conn.getServerTime());
                    conn.close();
                    return null;
                })
                .setCallback(new Async.Callback<Void>() {
                    @Override
                    public void onResult(Void result) {

                    }

                    @Override
                    public void onError(Exception e) {
                        listener.onError(e);
                    }
                })
                .execute();
    }

    public void doLookup(String category, RequestWrapper.ListApiListener listener) {
        Map<String, Object> param = new HashMap<>();

        if (category != null)
            param.put("cat", category);
        param.put("nums", 20);

        RequestWrapper.doRequest(
                GlobalVariable.getServerUrl() + "/board/lookup",
                param,
                listener
        );
    }

    public interface ServerTimeListener {
        void onSuccess(String hostname, Date date);

        void onError(Exception e);
    }

}
