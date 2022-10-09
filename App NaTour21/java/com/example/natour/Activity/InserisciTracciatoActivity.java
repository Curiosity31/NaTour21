package com.example.natour.Activity;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;


import android.widget.Button;

import com.example.natour.Presenter.InserisciTracciatoPresenter;
import com.example.natour.R;

import com.mapbox.android.core.permissions.PermissionsListener;

import com.mapbox.mapboxsdk.Mapbox;

import com.mapbox.mapboxsdk.geometry.LatLng;

import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;


import java.util.List;



public class InserisciTracciatoActivity extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private Button btnSalvaTracciato;
    private Button btnInserisciFileGPX;
    private Button btnIniziaARegistrare;
    private Button btnBack;
    private InserisciTracciatoPresenter inserisciTracciatoPresenter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.Map_Box_access_token));
        setContentView(R.layout.activity_inserisci_tracciato);
        inserisciTracciatoPresenter = new InserisciTracciatoPresenter(this, savedInstanceState);
        initViewComponents();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViewComponents() {
        btnInserisciFileGPX = findViewById(R.id.btnInserisciFileGPX);
        btnIniziaARegistrare = findViewById(R.id.btnIniziaARegistrare);
        btnSalvaTracciato = findViewById(R.id.btnSalvaTracciato);
        btnBack = findViewById(R.id.btn_back);

        btnIniziaARegistrare.setOnClickListener(view -> inserisciTracciatoPresenter.onBtnIniziaARegistrare());
        btnInserisciFileGPX.setOnClickListener(view -> inserisciTracciatoPresenter.onBtnInserisciFileGPX());
        btnSalvaTracciato.setOnClickListener(view -> inserisciTracciatoPresenter.onBtnSalvaTracciato());
        btnBack.setOnClickListener(view -> inserisciTracciatoPresenter.onBtnBack(btnBack, btnInserisciFileGPX, btnIniziaARegistrare, btnSalvaTracciato));
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mapboxMap.addOnMapClickListener(this);
        inserisciTracciatoPresenter.onMapReady(mapboxMap);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inserisciTracciatoPresenter.onActivityResult( requestCode, resultCode, data);

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        inserisciTracciatoPresenter.onExplanationNeeded();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        inserisciTracciatoPresenter.onPermissionResult(granted);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        inserisciTracciatoPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return inserisciTracciatoPresenter.onMapClick(point,btnInserisciFileGPX,btnIniziaARegistrare,btnSalvaTracciato, btnBack);

    }


    @Override
    protected void onStart() {
        super.onStart();
        inserisciTracciatoPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inserisciTracciatoPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        inserisciTracciatoPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        inserisciTracciatoPresenter.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        inserisciTracciatoPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        inserisciTracciatoPresenter.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inserisciTracciatoPresenter.onDestroy();
    }

}