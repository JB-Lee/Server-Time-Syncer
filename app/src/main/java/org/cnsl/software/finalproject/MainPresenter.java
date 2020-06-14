package org.cnsl.software.finalproject;

import android.os.Handler;
import android.os.Looper;
import android.webkit.URLUtil;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.cnsl.software.finalproject.board.BoardItem;
import org.cnsl.software.finalproject.contract.Main;
import org.cnsl.software.finalproject.models.MainModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MainPresenter implements Main.Presenter {

    Main.View mainView;
    MainModel mainModel;

    public MainPresenter(Main.View mainView) {
        this.mainView = mainView;
        this.mainModel = new MainModel();
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

    public void refreshBoard(String category) {
        Map<String, Object> param = new HashMap<>();

        if (category != null)
            param.put("cat", category);
        param.put("nums", 20);

        mainModel.doLookup(param, new MainModel.ApiListener() {
            @Override
            public void onSuccess(JSONArray json) {
                try {
                    List<BoardItem> list = new ArrayList<>();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    int offset = TimeZone.getDefault().getRawOffset() / 1000;
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject j = (JSONObject) json.get(i);
                        list.add(new BoardItem(
                                j.getString("writer"),
                                j.getString("category"),
                                j.getString("content"),
                                (int) (df.parse(j.getString("timestamp")).getTime() / 1000) + offset
                        ));

                    }
                    new Handler(Looper.getMainLooper()).post(() -> mainView.boardSetItem(list));

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String msg) {
            }
        });
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
        mainView.startPostActivity();
    }
}
