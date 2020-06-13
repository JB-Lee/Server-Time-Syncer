package org.cnsl.software.finalproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;

import org.cnsl.software.finalproject.contract.SignUp;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SignUpActivity extends AppCompatActivity implements SignUp.View {

    @BindView(R.id.et_signup_password)
    TextInputEditText etPass;
    @BindView(R.id.et_signup_confirm_password)
    TextInputEditText etChkPass;
    @BindView(R.id.et_signup_id)
    TextInputEditText etId;
    @BindView(R.id.et_signup_email)
    TextInputEditText etEmail;
    @BindView(R.id.btn_signup_sign_up)
    AppCompatButton btnSignUp;
    @BindView(R.id.tv_signup_risk_msg)
    AppCompatTextView tvRiskMsg;
    @BindView(R.id.tv_signup_password_msg)
    AppCompatTextView tvPassMsg;
    @BindView(R.id.tv_signup_email_message)
    AppCompatTextView tvEmailMsg;
    @BindView(R.id.tv_signup_user_message)
    AppCompatTextView tvUserMsg;
    @BindView(R.id.img_signup_risk_icon)
    AppCompatImageView imgRiskIcon;

    @BindColor(R.color.colorHighlight)
    int color_highlight;
    @BindColor(R.color.colorDisable)
    int color_disable;

    SignUp.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        presenter = new SignUpPresenter(this);

        ButterKnife.bind(this);
    }

    @OnTextChanged(R.id.et_signup_password)
    public void onPasswordChanged(CharSequence s, int start, int before, int count) {
        presenter.onPasswordChanged(s, start, before, count);
    }

    @OnClick(R.id.btn_signup_sign_up)
    public void onSignUp(View view) {
        presenter.onSignUp(
                String.valueOf(etId.getText()),
                String.valueOf(etEmail.getText()),
                String.valueOf(etPass.getText()),
                String.valueOf(etChkPass.getText())
        );
    }

    @Override
    public void setRiskIconColor(int color) {
        imgRiskIcon.setColorFilter(color);
    }

    @Override
    public void setRiskMsg(String string) {
        tvRiskMsg.setText(string);
    }

    @Override
    public void setUserMsg(String msg) {
        tvUserMsg.setText(msg);
    }

    @Override
    public void setEmailMsg(String msg) {
        tvEmailMsg.setText(msg);
    }

    @Override
    public void setPasswordMsg(String string) {
        tvPassMsg.setText(string);
    }

    @Override
    public void goPreviousActivity() {
        this.finish();
    }

    @Override
    public void toggleButton(boolean enable) {
        btnSignUp.setEnabled(enable);
        if (enable) {
            btnSignUp.setBackgroundColor(color_highlight);
        } else {
            btnSignUp.setBackgroundColor(color_disable);
        }
    }
}