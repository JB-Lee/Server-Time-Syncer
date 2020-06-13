package org.cnsl.software.finalproject.contract;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.cnsl.software.finalproject.board.BoardItem;

import java.util.Date;

public interface Main {

    interface View {
        void toggleUrlHeader();

        void toggleFabButton();

        void boardAddItem(BoardItem item);

        void setServerTime(Date date);

        void setHostName(String hostname);
    }

    interface Presenter {
        void onServerTimeLookup(String url);

        void onBoardRefresh(SwipeRefreshLayout swipe);

        void onPostArticle();
    }
}
