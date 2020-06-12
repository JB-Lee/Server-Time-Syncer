package org.cnsl.software.finalproject;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.cnsl.software.finalproject.board.BoardItem;
import org.cnsl.software.finalproject.board.BoardItemAdapter;
import org.cnsl.software.finalproject.utils.HttpHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;

public class MainActivity extends AppCompatActivity {
    String TAG = "test";

    Calendar timer = Calendar.getInstance();

    @BindView(R.id.tv_main_time)
    AppCompatTextView tvTime;
    @BindView(R.id.et_main_url)
    TextInputEditText etUrl;
    @BindView(R.id.tl_main_url_header)
    TextInputLayout tlUrlHeader;
    @BindView(R.id.tv_main_hostname)
    AppCompatTextView tvHostName;
    @BindView(R.id.srl_main_swipe)
    SwipeRefreshLayout srlSwipe;

    Handler mHandler = new Handler() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.US);

        @Override
        public void handleMessage(Message msg) {
            timer.add(Calendar.SECOND, 1);
            tvTime.setText(format.format(timer.getTime()));
            mHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    public static void slideView(View view,
                                 int currentHeight,
                                 int newHeight) {

        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(500);

        /* We use an update listener which listens to each tick
         * and manually updates the height of the view  */

        slideAnimator.addUpdateListener(animation1 -> {
            view.getLayoutParams().height = (Integer) animation1.getAnimatedValue();
            view.requestLayout();
        });

        /*  We use an animationSet to play the animation  */

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }

    public void toggleView() {
        tlUrlHeader.measure(TextInputLayout.LayoutParams.MATCH_PARENT, TextInputLayout.LayoutParams.WRAP_CONTENT);
        final int measuredHeight = tlUrlHeader.getMeasuredHeight();

        Log.d(TAG, "toggleUrlInput: " + measuredHeight);

        if (tlUrlHeader.getHeight() == 0) {
            slideView(tlUrlHeader, 0, measuredHeight);
        } else {
            slideView(tlUrlHeader, measuredHeight, 0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<BoardItem> list = new ArrayList<>();

        ButterKnife.bind(this);


        for (int i = 0; i < 100; i++) {

            list.add(new BoardItem(
                    String.format(Locale.US, "Name %d", i),
                    "cnsl.org",
                    String.format(Locale.US, "Lorem Ipsum %d", i),
                    (int) (System.currentTimeMillis() / 1000) - 10000 * i
            ));

        }

        RecyclerView recyclerView = findViewById(R.id.rv_main_board);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BoardItemAdapter adapter = new BoardItemAdapter(list);
        recyclerView.setAdapter(adapter);
        mHandler.sendEmptyMessage(0);

        srlSwipe.setOnRefreshListener(() -> srlSwipe.setRefreshing(false));

    }

    @OnClick(R.id.tv_main_time)
    public void test(View view) {
    }

    @OnEditorAction(R.id.et_main_url)
    public boolean onEnter(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT || event.getAction() == KeyEvent.ACTION_DOWN) {
            toggleView();
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etUrl.getWindowToken(), 0);
            new Thread() {
                @Override
                public void run() {
                    if (URLUtil.isValidUrl(etUrl.getText().toString())) {
                        HttpHelper.Conn conn = new HttpHelper.Conn(etUrl.getText().toString());
                        //Date time = HttpHelper.getServerTime(url.getText().toString());
                        Date time = conn.getServerTime();
                        String title = null;
                        try {
                            title = new URL(etUrl.getText().toString()).getHost();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        if (time != null)
                            timer.setTime(time);
                        tvHostName.setText(title);
                    }

                }
            }.start();
            return true;
        }
        return false;
    }

    @OnFocusChange(R.id.et_main_url)
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus)
            toggleView();
    }
}