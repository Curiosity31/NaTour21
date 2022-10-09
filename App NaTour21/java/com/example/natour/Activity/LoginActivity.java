package com.example.natour.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.natour.Presenter.LoginPresenter;
import com.example.natour.R;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        initViewComponents();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginPresenter.onActivityResult(requestCode, resultCode, data);
    }


    private void initViewComponents(){

        loginPresenter = new LoginPresenter(this);

        TextView txtNotAccount = findViewById(R.id.txtNotAccount);
        TextView txtForgetPass = findViewById(R.id.txtForgetPass);
        Button btnLogin = findViewById(R.id.btnLogin);
        SignInButton btnGoogle = findViewById(R.id.btnGoogle);
        LoginButton btnFacebook = findViewById(R.id.btnFacebook);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);


        txtNotAccount.setOnClickListener(view -> loginPresenter.onBtnNotAccountClicked());

        txtForgetPass.setOnClickListener(view -> loginPresenter.onBtnForgetPassClicked());

        btnLogin.setOnClickListener(view -> loginPresenter.onBtnLoginClicked());

        btnGoogle.setOnClickListener(view -> loginPresenter.onBtnGoogleClicked());

        btnFacebook.setOnClickListener(view -> loginPresenter.onBtnFacebookClicked());
    }

    public void setTextInputEmailError(String error) {

        etEmail.setError(error);
    }

    public void setTextInputPasswordError(String error) {

        etPassword.setError(error);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}