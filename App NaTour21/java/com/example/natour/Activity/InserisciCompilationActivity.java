package com.example.natour.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.natour.DAO.DAOFactory;
import com.example.natour.Presenter.InserisciCompilationPresenter;
import com.example.natour.R;

public class InserisciCompilationActivity extends AppCompatActivity {

    private InserisciCompilationPresenter inserisciCompilationPresenter;
    private RecyclerView recycle_itinerari_compilation;
    EditText etDescrizione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserisci_compilation);
        Context appContext = getApplicationContext();
        recycle_itinerari_compilation = findViewById(R.id.recycle_itinerari_compilation);
        recycle_itinerari_compilation.setLayoutManager(new LinearLayoutManager(appContext));
        recycle_itinerari_compilation.setHasFixedSize(true);
        DAOFactory.getDaoFactory().getItinerarioDAO().getListaItinerariCompilation(recycle_itinerari_compilation, appContext, savedInstanceState);
        inserisciCompilationPresenter = new InserisciCompilationPresenter(this);
        initViewComponents();
    }

    private void initViewComponents() {
        etDescrizione = findViewById(R.id.etDescrizioneCompilation);
        TextView tvContaCaratteriDescrizioneCompilation = findViewById(R.id.tvContaCaratteriDescrizioneCompilation);
        etDescrizione.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etDescrizione.getText().toString();
                tvContaCaratteriDescrizioneCompilation.setText(text.length() + "/200");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button btnSalvaCompilation = findViewById(R.id.btnSalvaCompilation);
        btnSalvaCompilation.setOnClickListener(v -> inserisciCompilationPresenter.onBtnSalvaCompilationClicked(recycle_itinerari_compilation));
    }

    public String getNomeCompilation() {

        EditText etNomeCompilation = findViewById(R.id.etNomeCompilation);
        Editable nomeCompilation = etNomeCompilation.getText();
        if(nomeCompilation == null) return "";
        return nomeCompilation.toString();
    }

    public String getDescrizioneCompilation() {
        Editable descrizioneCompilation = etDescrizione.getText();
        if(descrizioneCompilation == null) return "";
        return descrizioneCompilation.toString();
    }

    public void setErrorNomeCompilation(String error) {
        TextView id_nome_itinerario = findViewById(R.id.id_nome_itinerario);
        id_nome_itinerario.setError(error);
    }

    public void setErrorDescrizioneCompilation(String error) {
        TextView tvDescrizione = findViewById(R.id.id_nome_itinerario);
        tvDescrizione.setError(error);
    }

}