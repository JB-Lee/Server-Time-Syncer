package org.cnsl.software.finalproject;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;

import org.cnsl.software.finalproject.contract.Post;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends AppCompatActivity implements Post.View {

    Post.Presenter presenter;

    @BindView(R.id.tv_post_writer)
    AppCompatTextView tvWriter;
    @BindView(R.id.tv_post_hostname)
    AppCompatTextView tvHostname;
    @BindView(R.id.et_post_content)
    TextInputEditText etContent;
    @BindView(R.id.btn_post_post)
    AppCompatButton btnPost;

    @Override
    public void goPreviousActivity() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            presenter = new PostPresenter(this, extras.getString("username"), extras.getString("hostname"));
            tvHostname.setText(getString(R.string.common_hostname_placeholder, extras.getString("hostname")));
            tvWriter.setText(extras.getString("username"));
        }
    }

    @OnClick(R.id.btn_post_post)
    public void onPostClick() {
        presenter.onPost(String.valueOf(etContent.getText()));

    }
}