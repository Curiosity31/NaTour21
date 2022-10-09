package com.example.natour.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.natour.Activity.LoginActivity;
import com.example.natour.Activity.SignupActivity;
import com.example.natour.Presenter.VerificationPresenter;
import com.example.natour.R;

public class VerificationFragment extends Fragment {

    private String email;
    private boolean userConfirmed;
    private SignupActivity signupActivity;
    private LoginActivity loginActivity;
    private VerificationPresenter verificationPresenter;

    private EditText etVerify;
    private Button btnVerify;
    private Button btnResend;

    public VerificationFragment () {}

    @NonNull
    public static VerificationFragment newInstanceVerificationFragment (String email, AppCompatActivity activity, boolean userConfirmed) {

        VerificationFragment verificationFragment = new VerificationFragment();
        verificationFragment.email = email;
        verificationFragment.userConfirmed = userConfirmed;

        if (activity.getClass() == SignupActivity.class) {

            verificationFragment.signupActivity = (SignupActivity) activity;
        }
        else {

            verificationFragment.loginActivity = (LoginActivity) activity;
        }

        return verificationFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        verificationPresenter = new VerificationPresenter(this);
        etVerify = view.findViewById(R.id.etVerify);
        btnVerify = view.findViewById(R.id.btnVerify);
        btnResend = view.findViewById(R.id.btnResendCode);
        initViewComponents();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_verification, container, false);
    }


    private void initViewComponents() {

        clearActivity();

        btnVerify.setOnClickListener(view -> verificationPresenter.onBtnVerifyClicked());
        btnResend.setOnClickListener(view -> verificationPresenter.onBtnResendClicked());
    }

    private void clearActivity () {

        if (signupActivity != null) {
            signupActivity.findViewById(R.id.etNome).setVisibility(View.GONE);
            signupActivity.findViewById(R.id.etCognome).setVisibility(View.GONE);
            signupActivity.findViewById(R.id.etNickName).setVisibility(View.GONE);
            signupActivity.findViewById(R.id.etEmail).setVisibility(View.GONE);
            signupActivity.findViewById(R.id.etPass).setVisibility(View.GONE);
            signupActivity.findViewById(R.id.etRepeatPass).setVisibility(View.GONE);
            signupActivity.findViewById(R.id.btnSignUp).setVisibility(View.GONE);
            signupActivity.findViewById(R.id.cbAgree).setVisibility(View.GONE);
        }
        else {
            loginActivity.findViewById(R.id.etEmail).setVisibility(View.GONE);
            loginActivity.findViewById(R.id.etPassword).setVisibility(View.GONE);
            loginActivity.findViewById(R.id.btnLogin).setVisibility(View.GONE);
            loginActivity.findViewById(R.id.txtNotAccount).setVisibility(View.GONE);
            loginActivity.findViewById(R.id.txtForgetPass).setVisibility(View.GONE);
            loginActivity.findViewById(R.id.btnGoogle).setVisibility(View.GONE);
            loginActivity.findViewById(R.id.btnFacebook).setVisibility(View.GONE);
            loginActivity.findViewById(R.id.imageView).setVisibility(View.GONE);
        }

    }

    public String getConfCode() {

        Editable confCode = etVerify.getText();
        if(confCode == null) return "";
        return etVerify.getText().toString().replace(" ", "");
    }

    public String getEmail() {

        return email;
    }

    public boolean getUserConfirmed() {

        return userConfirmed;
    }

    public void setTextInputCodeError (String error) {

        etVerify.setError(error);
    }


}