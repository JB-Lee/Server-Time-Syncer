package org.cnsl.software.finalproject;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;

import org.cnsl.software.finalproject.contract.ChangePassword;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ChangePasswordActivity extends AppCompatActivity implements ChangePassword.View {

    @BindView(R.id.et_changepw_password)
    TextInputEditText etPass;
    @BindView(R.id.et_changepw_confirm_password)
    TextInputEditText etChkPass;

    @BindView(R.id.tv_changepw_password_msg)
    AppCompatTextView tvPwMsg;
    @BindView(R.id.tv_changepw_risk_msg)
    AppCompatTextView tvRiskMsg;

    @BindView(R.id.img_changepw_risk_icon)
    AppCompatImageView imgRiskIcon;

    @BindView(R.id.btn_changepw_submit)
    AppCompatButton btnSubmit;

    @BindColor(R.color.colorHighlight)
    int color_highlight;
    @BindColor(R.color.colorDisable)
    int color_disable;

    ChangePassword.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ButterKnife.bind(this);

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            presenter = new ChangePasswordPresenter(
                    this,
                    extra.getString("id"),
                    extra.getString("email")
            );
        }
    }

    @Override
    public void goPreviousActivity() {
        finish();
    }

    @Override
    public void toggleButton(boolean enable) {
        btnSubmit.setEnabled(enable);
        if (enable) {
            btnSubmit.setBackgroundTintList(ColorStateList.valueOf(color_highlight));
        } else {
            btnSubmit.setBackgroundTintList(ColorStateList.valueOf(color_disable));
        }
    }

    @Override
    public void setRiskMsg(String string) {
        tvRiskMsg.setText(string);
    }

    @Override
    public void setRiskIconColor(int color) {
        imgRiskIcon.setColorFilter(color);
    }

    @Override
    public void setPasswordMsg(String msg) {
        tvPwMsg.setText(msg);
    }

    @OnTextChanged(R.id.et_changepw_password)
    public void onPasswordChanged(CharSequence s, int start, int before, int count) {
        presenter.onPasswordChanged(s, start, before, count);
    }

    @OnClick(R.id.btn_changepw_submit)
    public void onChangePassword(View view) {
        presenter.onChangePassword(
                String.valueOf(etPass.getText()),
                String.valueOf(etChkPass.getText())
        );
    }
}