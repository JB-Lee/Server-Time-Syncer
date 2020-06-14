package org.cnsl.software.finalproject.contract;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.cnsl.software.finalproject.board.BoardItem;

import java.util.Date;
import java.util.List;

public interface Main {

    interface View {
        void toggleUrlHeader();

        void toggleFabButton();

        void boardAddItem(BoardItem item);

        void boardSetItem(List<BoardItem> list);

        void setServerTime(Date date);

        void setHostName(String hostname);

        void startPostActivity(String user);

        void scrollTop();
    }

    interface Presenter {
        void onServerTimeLookup(String url);

        void onBoardRefresh(SwipeRefreshLayout swipe, String hostname);

        void onPostArticle();

        void onViewCreate();

        void afterPostArticle(String category);
    }
}
