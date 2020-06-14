package org.cnsl.software.finalproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;

import org.cnsl.software.finalproject.contract.FindPassword;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPasswordActivity extends AppCompatActivity implements FindPassword.View {

    @BindView(R.id.et_findpassword_email)
    TextInputEditText etEmail;
    @BindView(R.id.et_findpassword_id)
    TextInputEditText etId;
    @BindView(R.id.tv_findpassword_msg)
    AppCompatTextView tvMsg;
    @BindView(R.id.btn_findpassword_submit)
    AppCompatButton btnSubmit;

    FindPassword.Presenter presenter;

    @OnClick(R.id.btn_findpassword_submit)
    public void onClickSubmit(View view) {
        presenter.onSubmit(String.valueOf(etId.getText()), String.valueOf(etEmail.getText()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);

        presenter = new FindPasswordPresenter(this);
    }

    @Override
    public void goPreviousActivity() {
        finish();
    }

    @Override
    public void setMsg(String msg) {
        tvMsg.setText(msg);
    }
}