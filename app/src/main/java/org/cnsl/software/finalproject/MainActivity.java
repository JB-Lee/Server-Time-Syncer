package org.cnsl.software.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import org.cnsl.software.finalproject.utils.HttpHelper;

public class MainActivity extends AppCompatActivity {
    String TAG = "test";

    Calendar timer = Calendar.getInstance();

    @BindView(R.id.test)
    AppCompatTextView testtext;
    @BindView(R.id.url)
    TextInputEditText url;
    @BindView(R.id.url_header)
    TextInputLayout url_header;
    @BindView(R.id.stats)
    AppCompatTextView stats;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;

    Handler mHandler = new Handler() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        @Override
        public void handleMessage(Message msg) {
            timer.add(Calendar.SECOND, 1);
            testtext.setText(format.format(timer.getTime()));
            mHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };



    public void toggleView() {
        url_header.measure(TextInputLayout.LayoutParams.MATCH_PARENT, TextInputLayout.LayoutParams.WRAP_CONTENT);
        final int measuredHeight = url_header.getMeasuredHeight();

        Log.d(TAG, "toggleUrlInput: "+measuredHeight);

        if (url_header.getHeight() == 0){
            slideView(url_header, 0, measuredHeight);
        }else{
            slideView(url_header, measuredHeight, 0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> list = new ArrayList<>();

        ButterKnife.bind(this);


        for (int i = 0; i < 100; i++) {
            list.add(String.format("Text %d", i));
        }

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextAdapter adapter = new TextAdapter(list);
        recyclerView.setAdapter(adapter);
        mHandler.sendEmptyMessage(0);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
            }
        });



//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                if (!recyclerView.canScrollVertically(-1)) {
//                    url_header.setVisibility(View.VISIBLE);
//                } else {
//                    url_header.setVisibility(View.GONE);
//                }
//
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });
    }

    @OnClick(R.id.test)
    public void test(View view) {
        toggleView();
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", "ABCDEFG");
//        HttpHelper.request("http://192.168.0.18:5000/", HttpHelper.Method.POST, map, new HttpHelper.ResponseListener() {
//            @Override
//            public void onSuccess(JSONObject json) {
//                Log.d("TEST", json.toString());
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                Log.d("TEST", message);
//            }
//        });
    }


    @OnEditorAction(R.id.url)
    public boolean onEnter(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT || event.getAction() == KeyEvent.ACTION_DOWN){
            Log.d(TAG, "onEnter: "+URLUtil.isValidUrl(url.getText().toString()));
            toggleView();
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(url.getWindowToken(), 0);
            new Thread() {
                @Override
                public void run() {
                    if (URLUtil.isValidUrl(url.getText().toString())){
                        HttpHelper.Conn conn = new HttpHelper.Conn(url.getText().toString());
                        //Date time = HttpHelper.getServerTime(url.getText().toString());
                        Date time = conn.getServerTime();
                        String title = null;
                        try {
                            title = new URL(url.getText().toString()).getHost();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        if (time != null)
                            timer.setTime(time);
                        stats.setText(title);
                    }

                }
            }.start();
            return true;
        }
        return false;
    }

    @OnFocusChange(R.id.url)
    public void onFocusChange(View view, boolean hasFocus){
        Log.d(TAG, "onFocusChange: "+hasFocus);
        if (!hasFocus)
            toggleView();
    }

    public static void slideView(View view,
                                 int currentHeight,
                                 int newHeight) {

        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(500);

        /* We use an update listener which listens to each tick
         * and manually updates the height of the view  */

        slideAnimator.addUpdateListener(animation1 -> {
            Integer value = (Integer) animation1.getAnimatedValue();
            view.getLayoutParams().height = value.intValue();
            view.requestLayout();
        });

        /*  We use an animationSet to play the animation  */

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }
}