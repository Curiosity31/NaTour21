package com.example.natour.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.natour.AWS.Cognito;
import com.example.natour.Activity.AddNewItinerarioActivity;
import com.example.natour.Activity.HomeActivity;
import com.example.natour.Activity.InserisciTracciatoActivity;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.DAO.ItinerarioDAO;
import com.example.natour.Itinerario.Itinerario;
import com.example.natour.R;
import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.ArrayList;
import java.util.Objects;

import timber.log.Timber;

public class AddNewItinerarioPresenter {

    private final AddNewItinerarioActivity addNewItinerarioActivity;
    private final Context appContext;
    private int ore;
    private int minuti;
    private final Resources res;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private double staringPointLatitude;
    private double startingPointLongitude;

    public AddNewItinerarioPresenter (@NonNull AddNewItinerarioActivity addNewItinerarioActivity) {
        this.addNewItinerarioActivity = addNewItinerarioActivity;
        appContext = addNewItinerarioActivity.getApplicationContext();
        res = addNewItinerarioActivity.getResources();
        staringPointLatitude = -1000;
        startingPointLongitude = -1000;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            addNewItinerarioActivity.setTextPuntoDiInizio(feature.text());
            staringPointLatitude = ((Point) Objects.requireNonNull(feature.geometry())).latitude();
            startingPointLongitude = ((Point) Objects.requireNonNull(feature.geometry())).longitude();
        }
    }

    public void onSvPunto_inizioClicked() {

        Mapbox.getInstance(appContext, addNewItinerarioActivity.getString(R.string.Map_Box_access_token));
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : addNewItinerarioActivity.getString(R.string.Map_Box_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(res.getColor(R.color.cMap))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(addNewItinerarioActivity);
        addNewItinerarioActivity.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

    public void onSvPunto_inizioClicked(boolean hasFocus) {

        if (hasFocus) {
            Mapbox.getInstance(appContext, addNewItinerarioActivity.getString(R.string.Map_Box_access_token));
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : addNewItinerarioActivity.getString(R.string.Map_Box_access_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(res.getColor(R.color.cMap))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(addNewItinerarioActivity);
            addNewItinerarioActivity.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        }
    }

    public void onBtnInserisciTracciatoClicked() {

        ore = addNewItinerarioActivity.getOre();
        minuti = addNewItinerarioActivity.getMinuti();
        Intent intent = new Intent(appContext, InserisciTracciatoActivity.class);
        intent.putExtra("nome_itinerario", addNewItinerarioActivity.getNameItinerario());
        intent.putExtra("ore", ore);
        intent.putExtra("minuti", minuti);
        intent.putExtra ("punto_di_inizio", addNewItinerarioActivity.getPuntoDiInizio());
        intent.putExtra("staringPointLatitude", staringPointLatitude);
        intent.putExtra("startingPointLongitude", startingPointLongitude);
        int [] diff = getDifficolta();
        intent.putExtra("difficulties", diff[0]);
        intent.putExtra("descrizione", addNewItinerarioActivity.getDescrizione());
        intent.putExtra("acc_dis", addNewItinerarioActivity.getCbAccessibilitaDisabili());

        addNewItinerarioActivity.startActivity(intent);
    }


    public void onBtnSalvaItinerario() {
        ItinerarioDAO itinerarioDAO = DAOFactory.getDaoFactory().getItinerarioDAO();
        Intent intentOld = addNewItinerarioActivity.getIntent();
        ArrayList<Parcelable> points = intentOld.getParcelableArrayListExtra("points");

        if (staringPointLatitude == -1000 && startingPointLongitude == -1000) {
            staringPointLatitude = intentOld.getDoubleExtra("staringPointLatitude", -1000);
            startingPointLongitude = intentOld.getDoubleExtra("startingPointLongitude", -1000);
        }

        double lat = -1000;
        double lon = -1000;
        if (points != null) {
            lat = ((LatLng) points.get(0)).getLatitude();
            lon = ((LatLng) points.get(0)).getLongitude();
        }
        String nomeItinerario = addNewItinerarioActivity.getNameItinerario();
        String puntoDiInizio = addNewItinerarioActivity.getPuntoDiInizio();
        ore = addNewItinerarioActivity.getOre();
        minuti = addNewItinerarioActivity.getMinuti();
        int[] difficolta = getDifficolta();

        if (nomeItinerario == null || nomeItinerario.length() == 0) {
            addNewItinerarioActivity.setTextInputNameError(res.getString(R.string.Error_nome_it));
            Toast.makeText(appContext, R.string.Error_nome_it, Toast.LENGTH_LONG).show();
        } else if (ore == 0 && minuti == 0) {
            addNewItinerarioActivity.setTextTempoTotaleError(res.getString(R.string.Error_tempo_totale));
            Toast.makeText(appContext, R.string.Error_tempo_totale, Toast.LENGTH_LONG).show();
        } else if (difficolta[0] == -1) {
            addNewItinerarioActivity.setTextInputDifficoltaError(res.getString(R.string.Error_difficolta));
            Toast.makeText(appContext, R.string.Error_difficolta, Toast.LENGTH_LONG).show();
        } else if (puntoDiInizio == null || puntoDiInizio.length() == 0) {
            addNewItinerarioActivity.setTextInputPuntoDiInizioError(res.getString(R.string.Error_punto_di_inizio));
            Toast.makeText(appContext, R.string.Error_punto_di_inizio, Toast.LENGTH_LONG).show();
        } else if (lat != -1000 && lon != -1000 && (! (lat >= staringPointLatitude - 0.5 && lat <= staringPointLatitude + 0.5 && lon >= startingPointLongitude - 0.5 && lon <= startingPointLongitude + 0.5))) {
            addNewItinerarioActivity.setTextInputPuntoDiInizioError(res.getString(R.string.Error_punto_di_inizio_Latlng));
            Toast.makeText(appContext, R.string.Error_punto_di_inizio_Latlng, Toast.LENGTH_LONG).show();
        } else {
            int min = (ore * 60) + minuti;
            boolean cbAccessibilitaDisabili = addNewItinerarioActivity.getCbAccessibilitaDisabili();
            String descrizione = addNewItinerarioActivity.getDescrizione();
            String email = null;
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(appContext);
            AccessToken accessToken = AccessToken.getCurrentAccessToken();

            Itinerario itinerario = new Itinerario(nomeItinerario, min, puntoDiInizio, staringPointLatitude,
            startingPointLongitude, difficolta[1], descrizione, cbAccessibilitaDisabili);

            if (acct != null) {
                email = acct.getEmail();
            }
            else if (accessToken != null) {
                GraphRequest request = GraphRequest.newMeRequest(accessToken, (jsonObject, graphResponse) -> {

                    assert graphResponse != null;
                    FacebookRequestError facebookRequestError = graphResponse.getError();
                    if (facebookRequestError != null) {
                        Timber.d("Facebook: GraphResponse Error%s", facebookRequestError);
                    } else {
                        String jsonresult = String.valueOf(jsonObject);
                        Timber.d("Facebook: JSON Result: %s", jsonresult);

                        assert jsonObject != null;
                        itinerarioDAO.addItinerario(itinerario, jsonObject.optString("email"), points);

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,email,last_name");
                request.setParameters(parameters);
                request.executeAsync();

                Toast.makeText(appContext, R.string.Itinerario_salvato, Toast.LENGTH_LONG).show();
                addNewItinerarioActivity.startActivity(new Intent(appContext, HomeActivity.class));
            }
            else {
                email = Cognito.getCognitoInstance(appContext).getUserPool().getCurrentUser().getUserId();
            }

            itinerarioDAO.addItinerario(itinerario, email, points);
            Toast.makeText(appContext, R.string.Itinerario_salvato, Toast.LENGTH_LONG).show();
            addNewItinerarioActivity.startActivity(new Intent(appContext, HomeActivity.class));
        }

    }

    public void setOnValueChangedListenerOre(int newVal, NumberPicker npMinuti) {
        npMinuti.setEnabled(newVal != 13);
    }

    public int [] getDifficolta() {

        RadioButton rb_difficolta = addNewItinerarioActivity.getDifficolta();
        int [] difficolta = new int[2];
        difficolta[0] = -1;
        difficolta[1] = -1;

        if (rb_difficolta != null) {

            String diff = rb_difficolta.getText().toString();
            switch (diff) {
                case "Facile":
                    difficolta[0] = R.id.rb_facile;
                    difficolta[1] = 0;
                    break;
                case "Intermedio":
                    difficolta[0] = R.id.rb_intermedio;
                    difficolta[1] = 1;
                    break;
                case "Difficile":
                    difficolta[0] = R.id.rb_difficile;
                    difficolta[1] = 2;
                    break;
                default:
                    difficolta[0] = R.id.rb_estremo;
                    difficolta[1] = 3;
                    break;
            }
        }
        return difficolta;
    }

}
