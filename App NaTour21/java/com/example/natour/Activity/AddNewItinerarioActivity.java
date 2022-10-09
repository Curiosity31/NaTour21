package com.example.natour.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.natour.Presenter.AddNewItinerarioPresenter;
import com.example.natour.R;


public class AddNewItinerarioActivity extends AppCompatActivity {

    private EditText etNomeItinerario;
    private SearchView svPunto_inizio;
    private EditText etDescrizione;
    private RadioGroup groupDifficolta;
    private NumberPicker npOre;
    private NumberPicker npMinuti;
    private CheckBox cbAccessibilitaDisabili;
    private AddNewItinerarioPresenter addNewItinerarioPresenter;
    private TextView tvContaCaratteriDescrizione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_itinerario);

        addNewItinerarioPresenter = new AddNewItinerarioPresenter(this);

        etNomeItinerario = findViewById(R.id.etNomeItinerario);
        svPunto_inizio = findViewById(R.id.svPunto_inizio);
        etDescrizione = findViewById(R.id.etDescrizione);
        npOre = findViewById(R.id.NP_ore);
        npMinuti = findViewById(R.id.NP_minuti);
        groupDifficolta = findViewById(R.id.radioGroup);
        etDescrizione = findViewById(R.id.etDescrizione);
        cbAccessibilitaDisabili = findViewById(R.id.cbAccessibilitàDisabili);
        tvContaCaratteriDescrizione = findViewById(R.id.tvContaCaratteriDescrizione);

        npOre.setMinValue(0);
        npOre.setMaxValue(13);
        npMinuti.setMinValue(0);
        npMinuti.setMaxValue(59);

        Intent intent = getIntent();
        if ( intent.getBooleanExtra("ripristino", false)) {
            ripristino(intent);
        }

        initViewComponents();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addNewItinerarioPresenter.onActivityResult(requestCode, resultCode, data);
    }

    private void initViewComponents() {
        etDescrizione.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etDescrizione.getText().toString();
                tvContaCaratteriDescrizione.setText(text.length() + "/200");
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        svPunto_inizio.setOnClickListener(view -> addNewItinerarioPresenter.onSvPunto_inizioClicked());
        svPunto_inizio.setOnQueryTextFocusChangeListener((v, hasFocus) -> addNewItinerarioPresenter.onSvPunto_inizioClicked(hasFocus));


        Button btnInserisciTracciato = findViewById(R.id.btnInserisciTracciato);
        Button btnSalvaItinerario = findViewById(R.id.btnSalvaItinerario);

        npOre.setOnValueChangedListener((picker, oldVal, newVal) -> addNewItinerarioPresenter.setOnValueChangedListenerOre(newVal, npMinuti));

        btnInserisciTracciato.setOnClickListener(view -> addNewItinerarioPresenter.onBtnInserisciTracciatoClicked());


        btnSalvaItinerario.setOnClickListener(view -> addNewItinerarioPresenter.onBtnSalvaItinerario());
    }


    public String getNameItinerario() {

        Editable name = etNomeItinerario.getText();
        if(name == null) return "";
        return etNomeItinerario.getText().toString();
    }

    public void setTextInputNameError(String error) {

        etNomeItinerario.setError(error);
    }

    public String getPuntoDiInizio() {

        return svPunto_inizio.getQuery().toString();
    }

    public void setTextPuntoDiInizio(String text) {
        svPunto_inizio.setQuery(text, false);
    }

    public void setTextInputPuntoDiInizioError(String error) {
        TextView tvPunto_inizio = findViewById(R.id.tv_punto_di_inizio);
        tvPunto_inizio.setError(error);
    }

    public void setTextInputDifficoltaError(String error) {
        TextView tv_difficolta = findViewById(R.id.id_difficolta);
        tv_difficolta.setError(error);
    }

    public boolean getCbAccessibilitaDisabili() {
        return cbAccessibilitaDisabili.isChecked();
    }

    public RadioButton getDifficolta() {
        return findViewById(groupDifficolta.getCheckedRadioButtonId());
    }

    public String getDescrizione() {

        Editable name = etDescrizione.getText();
        if(name == null) return "No descrizione";
        return etDescrizione.getText().toString();
    }

    public void setTextTempoTotaleError(String error) {
        TextView tv_tempo_totale = findViewById(R.id.id_tempo_totale);
        tv_tempo_totale.setError(error);
    }

    public int getOre () {
        return npOre.getValue();
    }

    public int getMinuti () {
        return npMinuti.getValue();
    }

    @SuppressLint("SetTextI18n")
    private void ripristino (Intent data) {

        etNomeItinerario.setText(data.getStringExtra("nome_itinerario"));
        npOre.setValue(data.getIntExtra("ore", -1));
        npMinuti.setValue(data.getIntExtra("minuti", -1));
        setTextPuntoDiInizio(data.getStringExtra("punto_di_inizio"));
        RadioButton checked = findViewById(data.getIntExtra("difficulties", -1));
        if (checked != null) {
            checked.setChecked(true);
        }
        String des = data.getStringExtra("descrizione");
        etDescrizione.setText(des);
        tvContaCaratteriDescrizione.setText(des.length() + "/200");
        cbAccessibilitaDisabili.setChecked(data.getBooleanExtra("acc_dis", false));
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