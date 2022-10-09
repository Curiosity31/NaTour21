package com.example.natour.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.natour.Presenter.FiltriPresenter;
import com.example.natour.R;

public class FiltriActivity extends AppCompatActivity {

    private FiltriPresenter filtriPresenter;
    private SearchView svArea_geografica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtri);
        filtriPresenter = new FiltriPresenter(this);
        initViewComponents();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filtriPresenter.onActivityResult(requestCode, resultCode, data);
    }

    private void initViewComponents() {
        svArea_geografica = findViewById(R.id.svArea_geografica);

        svArea_geografica.setOnClickListener(view -> filtriPresenter.onSvAreaGeograficaClicked());
        svArea_geografica.setOnQueryTextFocusChangeListener((v, hasFocus) -> filtriPresenter.onSvAreaGeograficaClicked(hasFocus));

        Button btnApplicaFiltri = findViewById(R.id.btnApplicaFiltri);
        btnApplicaFiltri.setOnClickListener(v -> filtriPresenter.onBtnApplicaFiltriClicked());

        Button btnCancellaFiltri = findViewById(R.id.btnCancellaFiltri);
        btnCancellaFiltri.setOnClickListener(v -> filtriPresenter.onBtnCancellaFiltriClicked());
    }

    public void setTextAreaGeografica(String text) {
        svArea_geografica.setQuery(text, false);
    }

}