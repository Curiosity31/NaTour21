package com.example.natour.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.natour.Presenter.SignupPresenter;
import com.example.natour.R;

public class SignupActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etCognome;
    private EditText etNickName;
    private EditText etEmail;
    private EditText etPass;
    private EditText etRepeatPass;

    private SignupPresenter signupPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupPresenter = new SignupPresenter(this);
        initViewComponents();
    }

    private void initViewComponents(){

        etNome = findViewById(R.id.etNome);
        etCognome = findViewById(R.id.etCognome);
        etNickName = findViewById(R.id.etNickName);
        etEmail= findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etRepeatPass = findViewById(R.id.etRepeatPass);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(view -> signupPresenter.onBtnSignupClicked());

    }


    public String getName() {

        Editable name = etNome.getText();
        if(name == null) return "";
        return etNome.getText().toString().replace(" ", "");
    }

    public String getSurname() {

        Editable surname = etCognome.getText();
        if(surname == null) return "";
        return etCognome.getText().toString();
    }

    public String getEmail() {

        Editable email = etEmail.getText();
        if(email == null) return "";
        return etEmail.getText().toString().replace(" ", "");
    }

    public String getNickname() {

        Editable nickName = etNickName.getText();
        if(nickName == null) return "";
        return etNickName.getText().toString();
    }

    public String getPassword() {

        Editable pass = etPass.getText();
        if(pass == null) return "";
        return etPass.getText().toString().replace(" ", "");
    }

    public String getRepeatPassword() {

        Editable repeatPass = etRepeatPass.getText();
        if(repeatPass == null) return "";
        return etRepeatPass.getText().toString().replace(" ", "");
    }

    public void setTextInputNameError(String error) {

        etNome.setError(error);
    }

    public void setTextInputSurnameError(String error) {

        etCognome.setError(error);
    }

    public void setTextInputNicknameError(String error) {

        etNickName.setError(error);
    }

    public void setTextInputEmailError(String error) {

        etEmail.setError(error);
    }

    public void setTextInputPasswordError(String error) {

        etPass.setError(error);
    }

    public void setTextInputRepeatPasswordError(String error) {

        etRepeatPass.setError(error);
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