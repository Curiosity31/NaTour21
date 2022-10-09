package com.example.natour.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.natour.Activity.FiltriActivity;
import com.example.natour.Activity.HomeActivity;
import com.example.natour.R;
import com.google.android.material.slider.RangeSlider;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.LinkedList;
import java.util.Objects;

public class FiltriPresenter {

    private final FiltriActivity filtriActivity;
    private final Context appContext;
    private final Resources res;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private double latitudine;
    private double longitudine;

    public FiltriPresenter (FiltriActivity filtriActivity) {
        this.filtriActivity = filtriActivity;
        appContext = filtriActivity.getApplicationContext();
        res = filtriActivity.getResources();
        latitudine = -1000;
        longitudine = -1000;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            filtriActivity.setTextAreaGeografica(feature.text());
            latitudine = ((Point) Objects.requireNonNull(feature.geometry())).latitude();
            longitudine = ((Point) Objects.requireNonNull(feature.geometry())).longitude();
        }
    }

    public void onSvAreaGeograficaClicked() {

        Mapbox.getInstance(appContext, filtriActivity.getString(R.string.Map_Box_access_token));
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : filtriActivity.getString(R.string.Map_Box_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(res.getColor(R.color.cMap))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(filtriActivity);
        filtriActivity.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

    public void onSvAreaGeograficaClicked(boolean hasFocus) {

        if (hasFocus) {
            Mapbox.getInstance(appContext, filtriActivity.getString(R.string.Map_Box_access_token));
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : filtriActivity.getString(R.string.Map_Box_access_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(res.getColor(R.color.cMap))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(filtriActivity);
            filtriActivity.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        }
    }



    public void onBtnApplicaFiltriClicked() {

        RangeSlider sliderRange_durata = filtriActivity.findViewById(R.id.sliderRange_durata);
        LinkedList<Float> values = new LinkedList<>(sliderRange_durata.getValues());
        int min_durata = values.getFirst().intValue();
        int max_durata = values.getLast().intValue();

        if (min_durata == 0 && max_durata == 0) {
            Toast.makeText(appContext, R.string.Error_durata, Toast.LENGTH_LONG).show();
        } else {
            boolean difficolta_facile = ((CheckBox) filtriActivity.findViewById(R.id.cbDiffFacile)).isChecked();
            boolean difficolta_intermedio = ((CheckBox) filtriActivity.findViewById(R.id.cbDiffIntermedio)).isChecked();
            boolean difficolta_difficile = ((CheckBox) filtriActivity.findViewById(R.id.cbDiffDifficile)).isChecked();
            boolean difficolta_estremo = ((CheckBox) filtriActivity.findViewById(R.id.cbDiffEstremo)).isChecked();
            boolean [] difficolta = new boolean[4];
            if (difficolta_facile || difficolta_intermedio || difficolta_difficile || difficolta_estremo) {
                difficolta[0] = difficolta_facile;
                difficolta[1] = difficolta_intermedio;
                difficolta[2] = difficolta_difficile;
                difficolta[3] = difficolta_estremo;
            } else {
                difficolta[0] = true;
                difficolta[1] = true;
                difficolta[2] = true;
                difficolta[3] = true;
            }

            boolean accessibilita = ((CheckBox) filtriActivity.findViewById(R.id.cbAccessibilitàDisabili_filtri)).isChecked();

            Intent intent = new Intent(appContext, HomeActivity.class);
            intent.putExtra("latitudine", latitudine);
            intent.putExtra("longitudine", longitudine);
            intent.putExtra("min_durata", min_durata);
            intent.putExtra("max_durata", max_durata);
            intent.putExtra("difficolta", difficolta);
            intent.putExtra("accessibilita", accessibilita);
            intent.putExtra("filtri", true);
            filtriActivity.startActivity(intent);
        }

    }

    public void onBtnCancellaFiltriClicked() {
        filtriActivity.startActivity(new Intent(appContext, HomeActivity.class));
    }
}
