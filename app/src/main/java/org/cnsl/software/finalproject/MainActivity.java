package org.cnsl.software.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.cnsl.software.finalproject.board.BoardItem;
import org.cnsl.software.finalproject.board.BoardItemAdapter;
import org.cnsl.software.finalproject.contract.Main;
import org.cnsl.software.finalproject.utils.ViewAnimation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;

public class MainActivity extends AppCompatActivity implements Main.View {

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
    @BindView(R.id.rv_main_board)
    RecyclerView rvBoard;

    @BindView(R.id.li_main_open_url)
    LinearLayoutCompat liOpenUrl;
    @BindView(R.id.li_main_write)
    LinearLayoutCompat liWrite;
    @BindView(R.id.li_main_password_change)
    LinearLayoutCompat liPassChange;

    @BindView(R.id.fab_main_menu)
    FloatingActionButton fabMenu;
    @BindView(R.id.fab_main_write)
    FloatingActionButton fabWrite;
    @BindView(R.id.fab_main_open_url)
    FloatingActionButton fabOpenUrl;
    @BindView(R.id.fab_main_password_change)
    FloatingActionButton fabPassChange;

    @BindAnim(R.anim.fab_close)
    Animation animFabClose;
    @BindAnim(R.anim.fab_open)
    Animation animFabOpen;
    @BindAnim(R.anim.fab_main_close)
    Animation animFabMainClose;
    @BindAnim(R.anim.fab_main_open)
    Animation animFabMainOpen;


    Main.Presenter presenter;

    Calendar timer = Calendar.getInstance();
    BoardItemAdapter adapter;
    Handler handler = new Handler();

    Boolean fabOpened = false;


    @Override
    public void boardAddItem(BoardItem item) {
        adapter.appendItem(item);
    }

    @Override
    public void scrollTop() {
        rvBoard.scrollToPosition(0);
    }

    @Override
    public void setServerTime(Date date) {
        timer.setTime(date);
    }

    @Override
    public void setHostName(String hostname) {
        tvHostName.setText(hostname);
    }

    @Override
    public void toggleUrlHeader() {
        tlUrlHeader.measure(TextInputLayout.LayoutParams.MATCH_PARENT, TextInputLayout.LayoutParams.WRAP_CONTENT);
        final int measuredHeight = tlUrlHeader.getMeasuredHeight();

        if (tlUrlHeader.getHeight() == 0) {
            ViewAnimation.slideView(tlUrlHeader, 0, measuredHeight);
        } else {
            ViewAnimation.slideView(tlUrlHeader, measuredHeight, 0);
        }

    }

    @Override
    public void startPostActivity(String user) {
        Intent intent = new Intent(MainActivity.this, PostActivity.class)
                .putExtra("username", user)
                .putExtra("hostname", String.valueOf(etUrl.getText()));
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
            presenter.afterPostArticle(String.valueOf(etUrl.getText()));

    }

    @Override
    public void toggleFabButton() {
        if (fabOpened) {
            liWrite.startAnimation(animFabClose);
            liOpenUrl.startAnimation(animFabClose);
            liPassChange.startAnimation(animFabClose);
            fabMenu.startAnimation(animFabMainClose);
            fabWrite.setClickable(false);
            fabOpenUrl.setClickable(false);
            fabOpened = false;
        } else {
            liWrite.startAnimation(animFabOpen);
            liOpenUrl.startAnimation(animFabOpen);
            liPassChange.startAnimation(animFabOpen);
            fabMenu.startAnimation(animFabMainOpen);
            fabWrite.setClickable(true);
            fabOpenUrl.setClickable(true);
            fabOpened = true;
        }
    }

    @Override
    public void boardSetItem(List<BoardItem> list) {
        adapter.setNewItems(list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<BoardItem> list = new ArrayList<>();

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            presenter = new MainPresenter(this, extras.getString("id"), extras.getString("email"));
        adapter = new BoardItemAdapter(list);

        rvBoard.setLayoutManager(new LinearLayoutManager(this));
        rvBoard.setAdapter(adapter);

//        시계 작동
        handler.post(
                new Runnable() {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.US);

                    @Override
                    public void run() {
                        timer.add(Calendar.SECOND, 1);
                        tvTime.setText(format.format(timer.getTime()));

                        handler.postDelayed(this, 1000);
                    }
                }
        );

//        스와이프 리프레시
        srlSwipe.setOnRefreshListener(() -> presenter.onBoardRefresh(srlSwipe, String.valueOf(etUrl.getText())));

        presenter.onViewCreate();

    }

    @Override
    public void startChangePasswordActivity(String id, String email) {
        Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class)
                .putExtra("id", id)
                .putExtra("email", email);

        startActivity(intent);
    }

    @OnClick({R.id.fab_main_menu, R.id.fab_main_open_url, R.id.fab_main_write, R.id.fab_main_password_change})
    public void onFabClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_main_menu:
                toggleFabButton();
                break;
            case R.id.fab_main_open_url:
                toggleUrlHeader();
                break;
            case R.id.fab_main_write:
                presenter.onPostArticle();
                break;
            case R.id.fab_main_password_change:
                presenter.onChangePassword();
                break;
        }
    }

    @OnEditorAction(R.id.et_main_url)
    public boolean onEnter(int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT || event.getAction() == KeyEvent.ACTION_DOWN) {
            toggleUrlHeader();
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(etUrl.getWindowToken(), 0);
            presenter.onServerTimeLookup("http://" + etUrl.getText());
            return true;
        }
        return false;
    }

    @OnFocusChange(R.id.et_main_url)
    public void onFocusChange(boolean hasFocus) {
        if (!hasFocus)
            toggleUrlHeader();
    }
}