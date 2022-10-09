package com.example.natour.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.natour.Presenter.SettingsPresenter;
import com.example.natour.R;

public class SettingsActivity extends AppCompatActivity {

    private SettingsPresenter settingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViewComponents();
    }

    private void initViewComponents() {

        settingsPresenter = new SettingsPresenter(this);

        Button btn_info = findViewById(R.id.btn_info);
        Button btn_modifica_pass = findViewById(R.id.btn_Modifica_pass);
        Button btn_modifica_nickname = findViewById(R.id.btn_Modifica_nickname);

        btn_modifica_pass.setOnClickListener(view -> settingsPresenter.onBtnModificaPassClicked());
        btn_modifica_nickname.setOnClickListener(view -> settingsPresenter.onBtnModificaNicknameClicked());
        btn_info.setOnClickListener(view -> settingsPresenter.onBtnInfoClicked());

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