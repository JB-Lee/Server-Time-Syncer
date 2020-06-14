package org.cnsl.software.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.textfield.TextInputEditText;

import org.cnsl.software.finalproject.contract.Login;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements Login.View {

    @BindView(R.id.et_login_id)
    TextInputEditText etId;
    @BindView(R.id.et_login_password)
    TextInputEditText etPass;
    @BindView(R.id.btn_login_login)
    AppCompatButton btnLogin;
    @BindView(R.id.tv_login_forgot_password)
    AppCompatTextView tvFindPass;
    @BindView(R.id.tv_login_sign_up)
    AppCompatTextView tvSignUp;

    Login.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login_login)
    public void login(View view){
        presenter.onLogin(
                Objects.requireNonNull(etId.getText()).toString(),
                Objects.requireNonNull(etPass.getText()).toString()
        );
    }

    @OnClick(R.id.tv_login_sign_up)
    public void signUp(View view) {
        presenter.onSignUp();
    }

    @OnClick(R.id.tv_login_forgot_password)
    public void findPassword(View view) {
        presenter.onFindPassword();
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