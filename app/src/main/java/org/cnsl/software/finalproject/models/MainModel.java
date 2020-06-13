package org.cnsl.software.finalproject.models;

import org.cnsl.software.finalproject.utils.Async;
import org.cnsl.software.finalproject.utils.HttpHelper;

import java.util.Date;

public class MainModel {

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

    public interface ServerTimeListener {
        void onSuccess(String hostname, Date date);

        void onError(Exception e);
    }
}
