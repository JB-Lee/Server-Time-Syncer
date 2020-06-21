package org.cnsl.software.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;

import org.cnsl.software.finalproject.contract.Login;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class LoginActivity extends AppCompatActivity implements Login.View {

    @BindView(R.id.et_login_id)
    TextInputEditText etId;
    @BindView(R.id.et_login_password)
    TextInputEditText etPass;
    @BindView(R.id.et_login_server_url)
    TextInputEditText etServerUrl;
    @BindView(R.id.btn_login_login)
    AppCompatButton btnLogin;
    @BindView(R.id.btn_login_server_check)
    AppCompatButton btnServerCheck;
    @BindView(R.id.tv_login_forgot_password)
    AppCompatTextView tvFindPass;
    @BindView(R.id.tv_login_sign_up)
    AppCompatTextView tvSignUp;
    @BindView(R.id.tv_login_server_message)
    AppCompatTextView tvServerMsg;

    @BindString(R.string.pref_server_url)
    String str_server_url;

    Login.Presenter presenter;
    SharedPreferences sharedPref;

    @Override
    public void setServerMsg(String msg) {
        tvServerMsg.setText(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this);

        ButterKnife.bind(this);

        sharedPref = getPreferences(Context.MODE_PRIVATE);

        if (sharedPref.contains(str_server_url)) {
            etServerUrl.setText(sharedPref.getString(str_server_url, ""));
            serverCheck();
        }

    }

    @OnClick(R.id.btn_login_server_check)
    public void serverCheck() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(str_server_url, String.valueOf(etServerUrl.getText()));
        editor.apply();
        presenter.onCheckServer("http://" + etServerUrl.getText());
    }

    @OnClick(R.id.btn_login_login)
    public void login() {
        presenter.onLogin(
                String.valueOf(etId.getText()),
                String.valueOf(etPass.getText())
        );
    }

    @OnClick(R.id.tv_login_sign_up)
    public void signUp() {
        presenter.onSignUp();
    }

    @OnClick(R.id.tv_login_forgot_password)
    public void findPassword() {
        presenter.onFindPassword();
    }

    @OnTextChanged(R.id.et_login_server_url)
    public void onServerUrlChanged(CharSequence s, int start, int before, int count) {
        presenter.onServerUrlChanged(s, start, before, count);
    }

    @Override
    public void showForgotPassword(boolean show) {
        tvFindPass.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void clearId() {
        etId.setText("");
    }

    @Override
    public void clearPw() {
        etPass.setText("");
    }

    @Override
    public void startMainActivity(String id, String email) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    @Override
    public void startSignUpActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void startFindPasswordActivity() {
        Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
        startActivity(intent);
    }
}