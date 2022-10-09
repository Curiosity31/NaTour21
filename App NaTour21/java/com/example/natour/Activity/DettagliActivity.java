package com.example.natour.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natour.DAO.DAOFactory;
import com.example.natour.DAO.ItinerarioDAO;
import com.example.natour.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import timber.log.Timber;

public class DettagliActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli);
        initViewComponents();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi", "ResourceAsColor"})
    private void initViewComponents() {
        ItinerarioDAO itinerarioDAO = DAOFactory.getDaoFactory().getItinerarioDAO();
        Context appContext = getApplicationContext();
        Intent intent = getIntent();
        TextView id_nome_itinerario_dettagli = findViewById(R.id.id_nome_itinerario_dettagli);
        id_nome_itinerario_dettagli.setText(intent.getStringExtra("nome_it"));
        TextView id_tempo_totale_dettagli = findViewById(R.id.id_tempo_totale_dettagli);
        int tim = intent.getIntExtra("time",0);
        String time = "ore: " + tim / 60 + "      minuti: " + tim % 60;
        id_tempo_totale_dettagli.setText(time);
        TextView id_difficolta_dettagli = findViewById(R.id.id_difficolta_dettagli);
        int diff = intent.getIntExtra("difficolta",-1);
        String difficolta = getDifficolta(diff);
        id_difficolta_dettagli.setText(difficolta);
        TextView punto_di_inizio_dettagli = findViewById(R.id.punto_di_inizio_dettagli);
        punto_di_inizio_dettagli.setText(intent.getStringExtra("punto_di_inizio"));
        CheckBox cbAccessibilitaDisabili = findViewById(R.id.cbAccessibilitàDisabili);
        cbAccessibilitaDisabili.setChecked(intent.getBooleanExtra("accessibilita_dis", false));
        cbAccessibilitaDisabili.setEnabled(false);
        TextView Descrizione_dettagli = findViewById(R.id.Descrizione_dettagli);
        Descrizione_dettagli.setText(intent.getStringExtra("descrizione"));

        Button btnModificaTempoTotale = findViewById(R.id.btnModificaTempoTotale);
        btnModificaTempoTotale.setOnClickListener(v -> {
            NumberPicker inputOre = new NumberPicker(appContext);
            NumberPicker inputMinuti = new NumberPicker(appContext);
            inputOre.setTextColor(getColor(R.color.white));
            inputOre.setMinValue(0);
            inputOre.setMaxValue(13);
            inputOre.setOnValueChangedListener((picker, oldVal, newVal) -> inputMinuti.setEnabled(newVal != 13));
            inputMinuti.setTextColor(getColor(R.color.white));
            inputMinuti.setMinValue(0);
            inputMinuti.setMaxValue(59);
            LinearLayout layout = new LinearLayout(appContext);
            layout.addView(inputOre);
            layout.addView(inputMinuti);
            layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

            new MaterialAlertDialogBuilder(DettagliActivity.this, R.style.Theme_NaTour_AlertDialog)
                .setBackground(getDrawable(R.drawable.for1))
                .setTitle("Modifica Tempo Totale")
                .setMessage("Inserisci il tempo totale in minuti")
                .setView(layout)
                .setPositiveButton("Salva Modifica", (dialogInterface, i) -> {
                    int ore = inputOre.getValue();
                    int minuti = inputMinuti.getValue();
                    if (ore == 0 && minuti == 0) {
                        Toast.makeText(appContext, R.string.Error_tempo_totale_dettagli, Toast.LENGTH_LONG).show();
                    } else {
                        int tim2 = (((ore * 60) + minuti) + tim) / 2;
                        String time2 = "ore: " + tim2 / 60 + "      minuti: " + tim2 % 60;
                        if (tim != tim2) {
                            id_tempo_totale_dettagli.setText(time2);
                            itinerarioDAO.updateDurationItinerario(intent.getLongExtra("itinerarioId", -1), tim2);
                        }
                        Timber.d("tempo totale cambiato con successo.");
                        Toast.makeText(appContext, R.string.tempo_totale_cambiato, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Annulla", (dialogInterface, i) -> Timber.d("Modifica annullata."))
                .show();});


        Button btnModificaDifficolta = findViewById(R.id.btnModificaDifficolta);
        btnModificaDifficolta.setOnClickListener(v -> {
            RadioGroup groupDifficolta = new RadioGroup(appContext);
            RadioButton rbFacile = new RadioButton(appContext);
            rbFacile.setText(R.string.rb_facile_dettagli);
            rbFacile.setTextColor(Color.WHITE);
            rbFacile.setButtonTintList(ColorStateList.valueOf(getColor(R.color.verde_chiarissimo)));
            RadioButton rbIntermedio = new RadioButton(appContext);
            rbIntermedio.setText(R.string.rb_intermedio_dettagli);
            rbIntermedio.setTextColor(Color.WHITE);
            rbIntermedio.setButtonTintList(ColorStateList.valueOf(getColor(R.color.verde_chiarissimo)));
            RadioButton rbDifficile = new RadioButton(appContext);
            rbDifficile.setText(R.string.rb_difficile_dettagli);
            rbDifficile.setTextColor(Color.WHITE);
            rbDifficile.setButtonTintList(ColorStateList.valueOf(getColor(R.color.verde_chiarissimo)));
            RadioButton rbEstremo = new RadioButton(appContext);
            rbEstremo.setText(R.string.rb_estremo_dettagli);
            rbEstremo.setTextColor(Color.WHITE);
            rbEstremo.setButtonTintList(ColorStateList.valueOf(getColor(R.color.verde_chiarissimo)));

            groupDifficolta.addView(rbFacile);
            groupDifficolta.addView(rbIntermedio);
            groupDifficolta.addView(rbDifficile);
            groupDifficolta.addView(rbEstremo);

            new MaterialAlertDialogBuilder(DettagliActivity.this, R.style.Theme_NaTour_AlertDialog)
                    .setBackground(getDrawable(R.drawable.for1))
                    .setTitle("Modifica Difficoltà")
                    .setMessage("Seleziona la difficoltà")
                    .setView(groupDifficolta)
                    .setPositiveButton("Salva Modifica", (dialogInterface, i) -> {
                        boolean fac, inter, diffic ,estr;
                        fac = rbFacile.isChecked();
                        inter = rbIntermedio.isChecked();
                        diffic = rbDifficile.isChecked();
                        estr = rbEstremo.isChecked();
                        if ( fac || inter || diffic || estr) {
                            int diff3;
                            if (fac) {
                                diff3 = 0;
                            } else if (inter) {
                                diff3 = 1;
                            } else if (diffic) {
                                diff3 = 2;
                            } else {
                                diff3 = 3;
                            }
                            diff3 = (diff3 + diff) / 2;
                            if (diff3 != diff) {
                                id_difficolta_dettagli.setText(getDifficolta(diff3));
                                itinerarioDAO.updateDifficultiesItinerario(intent.getLongExtra("itinerarioId", -1), diff3);
                                Timber.d("Difficoltà cambiata con successo.");
                            }
                            Toast.makeText(appContext, R.string.difficolta_cambiata, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(appContext, R.string.Error_difficolta_dettagli, Toast.LENGTH_LONG).show();
                        }

                    })
                    .setNegativeButton("Annulla", (dialogInterface, i) -> Timber.d("Modifica annullata."))
                    .show();
        });
    }

    private String getDifficolta(int diff) {
        String difficolta;
        switch (diff) {
            case 0:
                difficolta = "facile";
                break;
            case 1:
                difficolta = "Intermedio";
                break;
            case 2:
                difficolta = "Difficile";
                break;
            default:
                difficolta = "Estremo";
                break;
        }
        return difficolta;
    }

}