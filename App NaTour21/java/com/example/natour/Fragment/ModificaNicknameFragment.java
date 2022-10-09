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

import com.example.natour.Presenter.ModificaNicknamePresenter;
import com.example.natour.R;

public class ModificaNicknameFragment extends Fragment {

    private ModificaNicknamePresenter modificaNicknamePresenter;

    private EditText etNuovo_Nickname;

    public ModificaNicknameFragment() {}

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        modificaNicknamePresenter = new ModificaNicknamePresenter(this);

        etNuovo_Nickname = view.findViewById(R.id.etNuovo_Nickname);
        Button btnModifica_Nick = view.findViewById(R.id.btnModifica_Nick);

        btnModifica_Nick.setOnClickListener(view1 -> modificaNicknamePresenter.onBtnModificaNickNameClicked());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_modifica_nickname, container, false);
    }


    public String getNickname() {

        Editable nickName = etNuovo_Nickname.getText();
        if(nickName == null) return "";
        return etNuovo_Nickname.getText().toString();
    }


    public void setTextInputNicknameError(String error) {

        etNuovo_Nickname.setError(error);
    }

}
