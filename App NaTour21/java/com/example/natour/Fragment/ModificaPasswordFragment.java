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

import com.example.natour.Presenter.ModificaPasswordPresenter;
import com.example.natour.R;

public class ModificaPasswordFragment extends Fragment {

    private ModificaPasswordPresenter modificaPasswordPresenter;

    private EditText etEmail_modifica_pass;
    private EditText etOld_pass;
    private EditText etModifica_password_settings;
    private EditText etConferma_modifica_pass;

    public ModificaPasswordFragment() {}

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        modificaPasswordPresenter = new ModificaPasswordPresenter(this);

        etEmail_modifica_pass = view.findViewById(R.id.etEmail_modifica_pass);
        etOld_pass = view.findViewById(R.id.old_pass_mod);
        etModifica_password_settings = view.findViewById(R.id.etModifica_password_settings);
        etConferma_modifica_pass = view.findViewById(R.id.etConferma_modifica_pass);
        Button btn_conf_modifica_pass = view.findViewById(R.id.btn_conf_modifica_pass);

        btn_conf_modifica_pass.setOnClickListener(view1 -> modificaPasswordPresenter.onBtnConfModificaClicked());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_modifica_password, container, false);
    }

    public String getEmail() {

        Editable email = etEmail_modifica_pass.getText();
        if(email == null) return "";
        return etEmail_modifica_pass.getText().toString().replace(" ", "");
    }

    public String getOldPassword() {

        Editable old_pass = etOld_pass.getText();
        if(old_pass == null) return "";
        return etOld_pass.getText().toString().replace(" ", "");
    }

    public String getPassword() {

        Editable pass = etModifica_password_settings.getText();
        if(pass == null) return "";
        return etModifica_password_settings.getText().toString().replace(" ", "");
    }

    public String getRepeatPassword() {

        Editable repeatPass = etConferma_modifica_pass.getText();
        if(repeatPass == null) return "";
        return etConferma_modifica_pass.getText().toString().replace(" ", "");
    }


    public void setTextInputEmailError(String error) {

        etEmail_modifica_pass.setError(error);
    }

    public void setTextInputOldPassError(String error) {

        etOld_pass.setError(error);
    }

    public void setTextInputPasswordError(String error) {

        etModifica_password_settings.setError(error);
    }

    public void setTextInputRepeatPasswordError(String error) {

        etConferma_modifica_pass.setError(error);
    }

}