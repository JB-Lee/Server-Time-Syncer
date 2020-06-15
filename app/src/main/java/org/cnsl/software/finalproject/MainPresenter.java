package org.cnsl.software.finalproject;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.cnsl.software.finalproject.board.BoardItem;
import org.cnsl.software.finalproject.contract.Main;
import org.cnsl.software.finalproject.models.MainModel;
import org.cnsl.software.finalproject.utils.Async;
import org.cnsl.software.finalproject.utils.RequestWrapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainPresenter implements Main.Presenter {

    Main.View mainView;
    MainModel mainModel;

    public MainPresenter(Main.View mainView, String id, String email) {
        this.mainView = mainView;
        this.mainModel = new MainModel(id, email);
    }

    @Override
    public void onServerTimeLookup(String url) {
        if (!URLUtil.isValidUrl(url)) {
            mainView.setHostName("유효하지 않은 주소");
            return;
        }

        mainModel.doServerTimeLookup(url, new MainModel.ServerTimeListener() {
            @Override
            public void onSuccess(String hostname, Date date) {
                mainView.setHostName(hostname);
                mainView.setServerTime(date);
                refreshBoard(hostname);
            }

            @Override
            public void onError(Exception e) {
                mainView.setHostName("유효하지 않은 주소");
            }
        });

        if (url.equals(""))
            refreshBoard();
    }

    @Override
    public void afterPostArticle(String category) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> refreshBoard(category), 3000);
    }

    public void refreshBoard(String category) {

        mainModel.doLookup(category, new RequestWrapper.ListApiListener() {
            @Override
            public void onSuccess(JSONArray json) {
                try {
                    List<BoardItem> list = new ArrayList<>();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    int offset = TimeZone.getDefault().getRawOffset() / 1000;
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject j = (JSONObject) json.get(i);
                        Date date = df.parse(j.getString("timestamp"));
                        if (date != null) {
                            list.add(new BoardItem(
                                    j.getString("writer"),
                                    j.getString("category"),
                                    j.getString("content"),
                                    (int) (date.getTime() / 1000) + offset
                            ));
                        }

                    }
                    Async.syncRunTask(() -> {
                        mainView.boardSetItem(list);
                        mainView.scrollTop();
                    });

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(JSONObject json) {
                Log.d("fail", json.toString());

            }

            @Override
            public void onError(String msg) {
                Log.d("error", msg);
            }
        });
    }

    @Override
    public void onChangePassword() {
        mainView.startChangePasswordActivity(mainModel.getId(), mainModel.getEmail());
    }

    public void refreshBoard() {
        refreshBoard(null);
    }

    @Override
    public void onViewCreate() {
        refreshBoard();
    }

    @Override
    public void onBoardRefresh(SwipeRefreshLayout swipe, String hostname) {
        refreshBoard(hostname);
        swipe.setRefreshing(false);
    }


    @Override
    public void onPostArticle() {
        mainView.startPostActivity(mainModel.getId());
    }
}
