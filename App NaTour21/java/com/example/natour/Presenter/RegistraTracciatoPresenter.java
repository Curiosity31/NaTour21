package com.example.natour.Presenter;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.natour.Activity.InserisciTracciatoActivity;
import com.example.natour.Activity.RegistraTracciatoActivity;
import com.example.natour.R;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.Objects;

import timber.log.Timber;

public class RegistraTracciatoPresenter {

    private final MapView mapViewRegistra;
    private MapboxMap mapboxMap;
    private final RegistraTracciatoActivity registraTracciatoActivity;
    private final Context appContext;
    private final Resources res;
    private PermissionsManager permissionsManager;
    private final RegistraTracciatoPresenter.LocationListeningCallback callbackLocation;
    private final ArrayList<LatLng> points;

    public RegistraTracciatoPresenter(RegistraTracciatoActivity registraTracciatoActivity, Bundle savedInstanceState) {

        this.registraTracciatoActivity = registraTracciatoActivity;
        appContext = registraTracciatoActivity.getApplicationContext();
        mapViewRegistra = registraTracciatoActivity.findViewById(R.id.mapViewRegistra);
        mapViewRegistra.onCreate(savedInstanceState);
        mapViewRegistra.getMapAsync(registraTracciatoActivity);
        res = registraTracciatoActivity.getResources();
        callbackLocation = new RegistraTracciatoPresenter.LocationListeningCallback();
        points = new ArrayList<>();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onExplanationNeeded() {
        Toast.makeText(appContext, R.string.Message_about_location_permissions, Toast.LENGTH_LONG).show();
    }

    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
    }

    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(appContext)) {
            initializeLocationEngine();
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(appContext, Objects.requireNonNull(mapboxMap.getStyle())).build());
            if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                onExplanationNeeded();
                permissionsManager = new PermissionsManager(registraTracciatoActivity);
                permissionsManager.requestLocationPermissions(registraTracciatoActivity);
            } else {
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.setRenderMode(RenderMode.COMPASS);
            }
        } else {
            permissionsManager = new PermissionsManager(registraTracciatoActivity);
            permissionsManager.requestLocationPermissions(registraTracciatoActivity);
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
            permissionsManager = new PermissionsManager(registraTracciatoActivity);
            permissionsManager.requestLocationPermissions(registraTracciatoActivity);
        } else {
            locationEngine.requestLocationUpdates(request, callbackLocation, registraTracciatoActivity.getMainLooper());
            locationEngine.getLastLocation(callbackLocation);
        }
    }

    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            enableLocation();
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

    public void onDestroy() {
        mapViewRegistra.onDestroy();
    }

    public void onLowMemory() {
        mapViewRegistra.onLowMemory();
    }

    public void onSaveInstanceState(Bundle outState) {
        mapViewRegistra.onSaveInstanceState(outState);
    }

    public void onStop() {
        mapViewRegistra.onStop();
    }

    public void onPause() {
        mapViewRegistra.onPause();
    }

    public void onResume() {
        mapViewRegistra.onResume();
    }

    public void onStart() {
        mapViewRegistra.onStart();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onClickBtnIniziaARegistrare2Listener(Button btnStopRegistrazione, Button btnIniziaARegistrare2) {
        btnIniziaARegistrare2.setEnabled(false);
        btnIniziaARegistrare2.setBackground(res.getDrawable(R.drawable.button_rounded_grey));
        btnStopRegistrazione.setEnabled(true);
        btnStopRegistrazione.setBackground(res.getDrawable(R.drawable.button_rounded_red));

        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        locationComponent.addOnIndicatorPositionChangedListener(point -> {
            if (points.size() == 0) {
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(appContext, Objects.requireNonNull(mapboxMap.getStyle())).build());

                if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    onExplanationNeeded();
                    permissionsManager = new PermissionsManager(registraTracciatoActivity);
                    permissionsManager.requestLocationPermissions(registraTracciatoActivity);
                }
                else {
                    locationComponent.setLocationComponentEnabled(true);
                    locationComponent.setRenderMode(RenderMode.GPS);
                    locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
                    locationComponent.zoomWhileTracking(17);
                }
                points.add(new LatLng(point.latitude(), point.longitude()));
            }

            double floor_lat = Math.round(points.get(points.size()-1).getLatitude() * 10000.0)/10000.0;
            double floor_lat2 = Math.round(point.latitude() * 10000.0)/10000.0;
            double floor_lon = Math.round(points.get(points.size()-1).getLongitude() * 10000.0)/10000.0;
            double floor_lon2 = Math.round(point.longitude() * 10000.0)/10000.0;
            if (floor_lat != floor_lat2 || floor_lon != floor_lon2) {
                points.add(new LatLng(point.latitude(), point.longitude()));
                if (points.size() > 1) {
                    mapboxMap.addPolyline(new PolylineOptions()
                            .addAll(points)
                            .color(res.getColor(R.color.celestino))
                            .width(4));
                }
            }
        });
    }

    public void onClickBtnStopRegistrazioneListener() {
        Intent intentOld = registraTracciatoActivity.getIntent();
        Intent intent = new Intent(appContext, InserisciTracciatoActivity.class);
        intent.putParcelableArrayListExtra("points", points);
        intent.putExtra("registra", true);
        intent.putExtra("nome_itinerario", intentOld.getStringExtra("nome_itinerario"));
        intent.putExtra("ore", intentOld.getIntExtra("ore", -1));
        intent.putExtra("minuti", intentOld.getIntExtra("minuti", -1));
        intent.putExtra("punto_di_inizio", intentOld.getStringExtra("punto_di_inizio"));
        intent.putExtra("staringPointLatitude", intentOld.getDoubleExtra("staringPointLatitude", -1000));
        intent.putExtra("startingPointLongitude", intentOld.getDoubleExtra("startingPointLongitude", -1000));
        intent.putExtra("difficulties", intentOld.getIntExtra("difficulties", -1));
        intent.putExtra("descrizione", intentOld.getStringExtra("descrizione"));
        intent.putExtra("acc_dis", intentOld.getBooleanExtra("acc_dis", false));
        registraTracciatoActivity.startActivity(intent);
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
