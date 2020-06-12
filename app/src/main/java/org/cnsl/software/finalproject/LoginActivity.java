package org.cnsl.software.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import org.cnsl.software.finalproject.contract.Login;

public class LoginActivity extends AppCompatActivity implements Login.View {

    @BindView(R.id.et_login_id)
    TextInputEditText etId;
    @BindView(R.id.et_login_password)
    TextInputEditText etPass;
    @BindView(R.id.btn_login_login)
    AppCompatButton btnLogin;
    @BindView(R.id.btn_login_forgot_password)
    AppCompatTextView btnFindPass;
    @BindView(R.id.btn_login_sign_up)
    AppCompatTextView btnSignUp;

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
                etId.getText().toString(),
                etPass.getText().toString()
        );
    }

    @OnClick(R.id.btn_login_sign_up)
    public void signUp(View view) {
        presenter.onSignUp();
    }

    @Override
    public void showForgotPassword(boolean show) {
        btnFindPass.setVisibility( show ? View.VISIBLE : View.INVISIBLE );
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
    public void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void startSignUpActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void startFindPasswordActivity() {

    }
}