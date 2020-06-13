package org.cnsl.software.finalproject;

import android.webkit.URLUtil;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.cnsl.software.finalproject.contract.Main;
import org.cnsl.software.finalproject.models.MainModel;

import java.util.Date;

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
            }

            @Override
            public void onError(Exception e) {
                mainView.setHostName("유효하지 않은 주소");
            }
        });
    }

    @Override
    public void onBoardRefresh(SwipeRefreshLayout swipe) {

        swipe.setRefreshing(false);
    }

    @Override
    public void onPostArticle() {

    }
}
