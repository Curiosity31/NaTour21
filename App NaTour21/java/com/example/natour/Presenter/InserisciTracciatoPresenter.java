package com.example.natour.Presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import static com.example.natour.R.drawable.ic_baseline_backspace_24;
import static com.example.natour.R.drawable.ic_baseline_backspace_green;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import com.example.natour.Activity.AddNewItinerarioActivity;
import com.example.natour.Activity.InserisciTracciatoActivity;
import com.example.natour.Activity.RegistraTracciatoActivity;
import com.example.natour.R;
import com.mapbox.android.core.location.LocationEngine;

import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsManager;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import io.ticofab.androidgpxparser.parser.domain.TrackPoint;
import io.ticofab.androidgpxparser.parser.domain.TrackSegment;
import timber.log.Timber;


public class InserisciTracciatoPresenter {

    private final MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private final LocationListeningCallback callbackLocation;

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int REQUEST_CODE_LOAD_GPX = 3;

    private ArrayList<LatLng> points;
    private final InserisciTracciatoActivity inserisciTracciatoActivity;
    private final Context appContext;
    private final Resources res;
    private final Intent intentOld;

    public InserisciTracciatoPresenter(InserisciTracciatoActivity inserisciTracciatoActivity, Bundle savedInstanceState) {
        this.inserisciTracciatoActivity = inserisciTracciatoActivity;
        appContext = inserisciTracciatoActivity.getApplicationContext();
        mapView = inserisciTracciatoActivity.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(inserisciTracciatoActivity);
        res = inserisciTracciatoActivity.getResources();
        callbackLocation = new LocationListeningCallback();

        intentOld = inserisciTracciatoActivity.getIntent();

        if (intentOld.getBooleanExtra("registra", false)) {
            points = new ArrayList<>();
            for (Parcelable p : intentOld.getParcelableArrayListExtra("points")) {
                points.add((LatLng) p);
            }
            onBtnSalvaTracciato();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(res.getString(R.string.geojsonSourceLayerId));
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) Objects.requireNonNull(selectedCarmenFeature.geometry())).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude())).zoom(14).build()), 4000);
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_LOAD_GPX) {

            Uri uri = data.getData();

            GPXParser parser = new GPXParser();
            Gpx parsedGpx = null;
            try {
                InputStream in = inserisciTracciatoActivity.getContentResolver().openInputStream(uri);
                parsedGpx = parser.parse(in);
            } catch (IOException | XmlPullParserException e) {
                Timber.d("Exception file GPX: %s", e);
                e.printStackTrace();
            }
            if (parsedGpx == null) {
                Timber.d("ParsedGpx == null");
            } else {

                points = new ArrayList<>();
                List<Track> tracks = parsedGpx.getTracks();

                for (int i = 0; i < tracks.size(); i++) {
                    Track track = tracks.get(i);
                    Timber.d("track " + i + ":");
                    List<TrackSegment> segments = track.getTrackSegments();
                    for (int j = 0; j < segments.size(); j++) {
                        TrackSegment segment = segments.get(j);
                        Timber.d("  segment " + j + ":");
                        for (TrackPoint trackPoint : segment.getTrackPoints()) {
                            LatLng point = new LatLng(trackPoint.getLatitude(), trackPoint.getLongitude());
                            points.add(point);
                        }
                    }
                }
                onBtnSalvaTracciato();
            }
        }
    }


    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            enableLocation();
            initSearchFab();
            style.addImage(res.getString(R.string.symbolIconId), BitmapFactory.decodeResource(res, com.mapbox.mapboxsdk.plugins.markerview.R.drawable.mapbox_marker_icon_default));
            setUpSource(style);
            setupLayer(style);
        });
    }

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(res.getString(R.string.geojsonSourceLayerId)));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer(res.getString(R.string.SYMBOL_LAYER_ID), res.getString(R.string.geojsonSourceLayerId)).withProperties(
                iconImage(res.getString(R.string.symbolIconId)),
                iconOffset(new Float[]{0f, -8f})
        ));
    }


    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionsManager = new PermissionsManager(inserisciTracciatoActivity);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            onBtnInserisciFileGPX();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public boolean onMapClick(LatLng point, Button btnInserisciFileGPX, Button btnIniziaARegistrare, Button btnSalvaTracciato, Button btnBack) {

        if (points == null) {
            points = new ArrayList<>();
        }

        mapboxMap.addMarker(new MarkerOptions().position(point));
        points.add(point);
        btnIniziaARegistrare.setEnabled(false);
        btnInserisciFileGPX.setEnabled(false);
        btnIniziaARegistrare.setBackgroundColor(res.getColor(R.color.grey_light));
        btnInserisciFileGPX.setBackgroundColor(res.getColor(R.color.grey_light));

        if (points.size() == 1) {
            btnBack.setEnabled(true);
            btnBack.setBackground(res.getDrawable(ic_baseline_backspace_green));
        }

        if (points.size() > 1) {
            mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(points)
                    .color(res.getColor(R.color.celestino))
                    .width(4));
            btnSalvaTracciato.setEnabled(true);
            btnSalvaTracciato.setBackgroundColor(res.getColor(R.color.verde_scuro));
        }

        return true;
    }

    public void onDestroy() {
        mapView.onDestroy();
    }

    public void onLowMemory() {
        mapView.onLowMemory();
    }

    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }

    public void onStop() {
        mapView.onStop();
    }

    public void onPause() {
        mapView.onPause();
    }

    public void onResume() {
        mapView.onResume();
    }

    public void onStart() {
        mapView.onStart();
    }

    private void initSearchFab() {
        inserisciTracciatoActivity.findViewById(R.id.fab_location_search).setOnClickListener(view -> {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : inserisciTracciatoActivity.getString(R.string.Map_Box_access_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(res.getColor(R.color.cMap))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(inserisciTracciatoActivity);
            inserisciTracciatoActivity.startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        });
    }


    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(appContext)) {
            initializeLocationEngine();
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(appContext, Objects.requireNonNull(mapboxMap.getStyle())).build());
            if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                onExplanationNeeded();
                permissionsManager = new PermissionsManager(inserisciTracciatoActivity);
                permissionsManager.requestLocationPermissions(inserisciTracciatoActivity);
            } else {
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.setRenderMode(RenderMode.COMPASS);
            }
        } else {
            permissionsManager = new PermissionsManager(inserisciTracciatoActivity);
            permissionsManager.requestLocationPermissions(inserisciTracciatoActivity);
        }
    }


    private void initializeLocationEngine() {
        long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
        long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(appContext);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_NO_POWER)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
                .build();

        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            onExplanationNeeded();
            permissionsManager = new PermissionsManager(inserisciTracciatoActivity);
            permissionsManager.requestLocationPermissions(inserisciTracciatoActivity);
        } else {
            locationEngine.requestLocationUpdates(request, callbackLocation, inserisciTracciatoActivity.getMainLooper());
            locationEngine.getLastLocation(callbackLocation);
        }

    }

    public void onExplanationNeeded() {
        Toast.makeText(appContext, R.string.Message_about_location_permissions, Toast.LENGTH_LONG).show();
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void onBtnBack(Button btnBack, Button btnInserisciFileGPX, Button btnIniziaARegistrare, Button btnSalvaTracciato) {

        int index = points.size() -1;
        mapboxMap.removeMarker(mapboxMap.getMarkers().remove(index));
        points.remove(index);

        if (index > 0) {
            mapboxMap.removePolyline(mapboxMap.getPolylines().remove(index -1));
        }

        if (points.size() == 0) {
            btnBack.setEnabled(false);
            btnSalvaTracciato.setEnabled(false);
            btnIniziaARegistrare.setEnabled(true);
            btnInserisciFileGPX.setEnabled(true);
            btnBack.setBackground(res.getDrawable(ic_baseline_backspace_24));
            btnSalvaTracciato.setBackgroundColor(res.getColor(R.color.grey_light));
            btnIniziaARegistrare.setBackgroundColor(res.getColor(R.color.verde_scuro));
            btnInserisciFileGPX.setBackgroundColor(res.getColor(R.color.verde_scuro));
        }
    }

    public void onBtnSalvaTracciato() {
        Intent intent = new Intent(appContext, AddNewItinerarioActivity.class);
        intent.putExtra("nome_itinerario", intentOld.getStringExtra("nome_itinerario"));
        intent.putExtra("ore", intentOld.getIntExtra("ore", -1));
        intent.putExtra("minuti", intentOld.getIntExtra("minuti", -1));
        intent.putExtra("punto_di_inizio", intentOld.getStringExtra("punto_di_inizio"));
        intent.putExtra("staringPointLatitude", intentOld.getDoubleExtra("staringPointLatitude", -1000));
        intent.putExtra("startingPointLongitude", intentOld.getDoubleExtra("startingPointLongitude", -1000));
        intent.putExtra("difficulties", intentOld.getIntExtra("difficulties", -1));
        intent.putExtra("descrizione", intentOld.getStringExtra("descrizione"));
        intent.putExtra("acc_dis", intentOld.getBooleanExtra("acc_dis", false));
        intent.putExtra("ripristino", true);
        intent.putParcelableArrayListExtra("points", points);

        if (points.size() > 1) {
            Toast.makeText(appContext, R.string.Tracciato_salvato, Toast.LENGTH_LONG).show();
            inserisciTracciatoActivity.startActivity(intent);
        } else {
            Toast.makeText(appContext, R.string.Error_numberPoint, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onBtnInserisciFileGPX() {

        if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intentGpx = new Intent(Intent.ACTION_GET_CONTENT);
            intentGpx.addCategory(Intent.CATEGORY_OPENABLE);
            intentGpx.setType("application/*");
            inserisciTracciatoActivity.startActivityForResult(intentGpx, REQUEST_CODE_LOAD_GPX);
        } else {
            inserisciTracciatoActivity.requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }


    public void onBtnIniziaARegistrare() {
        Intent intentReg = new Intent(appContext, RegistraTracciatoActivity.class);
        intentReg.putExtra("nome_itinerario", intentOld.getStringExtra("nome_itinerario"));
        intentReg.putExtra("ore", intentOld.getIntExtra("ore", -1));
        intentReg.putExtra("minuti", intentOld.getIntExtra("minuti", -1));
        intentReg.putExtra("punto_di_inizio", intentOld.getStringExtra("punto_di_inizio"));
        intentReg.putExtra("staringPointLatitude", intentOld.getDoubleExtra("staringPointLatitude", -1000));
        intentReg.putExtra("startingPointLongitude", intentOld.getDoubleExtra("startingPointLongitude", -1000));
        intentReg.putExtra("difficulties", intentOld.getIntExtra("difficulties", -1));
        intentReg.putExtra("descrizione", intentOld.getStringExtra("descrizione"));
        intentReg.putExtra("acc_dis", intentOld.getBooleanExtra("acc_dis", false));
        inserisciTracciatoActivity.startActivity(intentReg);
    }


    private static class LocationListeningCallback implements LocationEngineCallback<LocationEngineResult> {

        @Override
        public void onSuccess(LocationEngineResult result) {
            Timber.d(String.valueOf(R.string.Success_locationEngine));
        }

        @Override
        public void onFailure(@NonNull Exception exception) {
            Timber.d(String.valueOf(exception), R.string.Error_locationEngine);
        }
    }

}