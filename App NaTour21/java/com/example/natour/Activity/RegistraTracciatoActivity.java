package com.example.natour.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import com.example.natour.Presenter.RegistraTracciatoPresenter;
import com.example.natour.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;

import timber.log.Timber;

public class RegistraTracciatoActivity extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback {

    private RegistraTracciatoPresenter registraTracciatoPresenter;
    private Button btnIniziaARegistrare2;
    private Button btnStopRegistrazione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.Map_Box_access_token));
        setContentView(R.layout.activity_registra_tracciato);
        registraTracciatoPresenter = new RegistraTracciatoPresenter(this, savedInstanceState);
        initViewComponents();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initViewComponents() {
        btnIniziaARegistrare2 = findViewById(R.id.btnIniziaARegistrare2);
        btnStopRegistrazione = findViewById(R.id.btnStopRegistrazione);

        btnIniziaARegistrare2.setOnClickListener(v -> registraTracciatoPresenter.onClickBtnIniziaARegistrare2Listener(btnStopRegistrazione, btnIniziaARegistrare2));
        btnStopRegistrazione.setOnClickListener(v ->
                new MaterialAlertDialogBuilder(RegistraTracciatoActivity.this, R.style.Theme_NaTour_AlertDialog)
                        .setBackground(getDrawable(R.drawable.for1))
                        .setTitle("Termina la registrazione")
                        .setMessage("Sei sicuro di voler terminare la registrazione?")
                        .setPositiveButton("Termina", (dialogInterface, i) -> {
                            Timber.d("Registrazione terminata.");
                            registraTracciatoPresenter.onClickBtnStopRegistrazioneListener();
                        })
                        .setNegativeButton("Annulla", (dialogInterface, i) -> Timber.d("Terminazione annullata."))
                        .show());

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        registraTracciatoPresenter.onExplanationNeeded();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        registraTracciatoPresenter.onPermissionResult(granted);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        registraTracciatoPresenter.onMapReady(mapboxMap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        registraTracciatoPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registraTracciatoPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registraTracciatoPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        registraTracciatoPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        registraTracciatoPresenter.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        registraTracciatoPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        registraTracciatoPresenter.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registraTracciatoPresenter.onDestroy();
    }

}