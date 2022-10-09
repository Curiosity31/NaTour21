package com.example.natour.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.natour.Activity.LoginActivity;
import com.example.natour.Presenter.ForgetPassPresenter;
import com.example.natour.R;


public class ForgetPassFragment extends Fragment {

    private LoginActivity loginActivity;
    private EditText etEmailForget;
    private EditText etCodice;
    private EditText etNuovaPass;
    private EditText etRipetiPass;
    private Button btnRecAccount;
    private Button btnConferma;

    private ForgetPassPresenter forgetPassPresenter;

    public ForgetPassFragment() { }


    public static ForgetPassFragment newInstanceForgetPass (LoginActivity loginActivity) {
        ForgetPassFragment forgetPassFragment = new ForgetPassFragment();
        forgetPassFragment.loginActivity = loginActivity;
        return forgetPassFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forgetPassPresenter = new ForgetPassPresenter(this);
        etEmailForget = view.findViewById(R.id.etEmail_fragment_forget);
        etNuovaPass = view.findViewById(R.id.etNuova_pass);
        etRipetiPass = view.findViewById(R.id.etRip_nuova_pass);
        etCodice = view.findViewById(R.id.etCodiceRecuperoPass);
        btnRecAccount = view.findViewById(R.id.btnRecuperaAccount);
        btnConferma = view.findViewById(R.id.btn_conferma_recupero);
        initViewComponents();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forget_pass, container, false);
    }

    private void initViewComponents() {
        clearActivity();

        btnRecAccount.setOnClickListener(view -> forgetPassPresenter.onBtnRecAccountClicked(loginActivity));
        btnConferma.setOnClickListener(view -> forgetPassPresenter.onBtnConfermaClicked());
    }

    private void clearActivity() {
        loginActivity.findViewById(R.id.etEmail).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.etPassword).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.btnLogin).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.txtNotAccount).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.txtForgetPass).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.btnGoogle).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.btnFacebook).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.imageView).setVisibility(View.GONE);

        loginActivity.findViewById(R.id.etNuova_pass).setEnabled(false);
        loginActivity.findViewById(R.id.etRip_nuova_pass).setEnabled(false);
        loginActivity.findViewById(R.id.etCodiceRecuperoPass).setEnabled(false);
        loginActivity.findViewById(R.id.btn_conferma_recupero).setEnabled(false);
    }

    public String getEmail() {

        Editable email = etEmailForget.getText();
        if(email == null) return "";
        return etEmailForget.getText().toString().replace(" ", "");
    }

    public String getPassword() {

        Editable pass = etNuovaPass.getText();
        if(pass == null) return "";
        return etNuovaPass.getText().toString().replace(" ", "");
    }

    public String getRepeatPassword() {

        Editable repeatPass = etRipetiPass.getText();
        if(repeatPass == null) return "";
        return etRipetiPass.getText().toString().replace(" ", "");
    }

    public String getCode() {

        Editable code = etCodice.getText();
        if(code == null) return "";
        return etCodice.getText().toString().replace(" ", "");
    }

    public void setTextInputEmailError (String error) {
        etEmailForget.setError(error);
    }

    public void setTextInputPasswordError (String error) {
        etNuovaPass.setError(error);
    }

    public void setTextInputRepeatPasswordError (String error) {
        etRipetiPass.setError(error);
    }

    public void setTextInputCodeError (String error) {
        etCodice.setError(error);
    }

}