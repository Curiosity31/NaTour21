package com.example.natour.DAO;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.natour.Client.Api;
import com.example.natour.Points.Point;
import com.example.natour.R;
import com.example.natour.Utente.UtenteService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PointDAORetrofit implements  PointDAO {

    /* ------------------------------  GET ---------------------------------------------- */

    public void getPointsTracciatoById(MapboxMap mapboxMap, Long itinerarioId, Context appContext) {

        UtenteService utenteService = Api.getUtenteService();
        Call<List<Point>> call = utenteService.getPointsTracciatoById(itinerarioId);
        call.enqueue(new Callback<List<Point>>() {
            @Override
            public void onResponse(@NonNull Call<List<Point>> call, @NonNull Response<List<Point>> response) {
                Timber.d("Get lista points tracciato ok");
                if (response.body() != null && response.body().size() != 0) {
                    int sizeCenter = response.body().size()/2;
                    int count = 0;
                    List<LatLng> listPoint = new LinkedList<>();
                    for (Point p: response.body()) {
                        count ++;
                        listPoint.add(new LatLng(p.getLatitude(), p.getLongitude()));
                        if (count == sizeCenter) {
                            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                                    new CameraPosition.Builder()
                                            .target(new LatLng(p.getLatitude(), p.getLongitude())).zoom(13).build()), 4000);
                        }

                    }
                    mapboxMap.addPolyline(new PolylineOptions()
                            .addAll(listPoint)
                            .color(appContext.getResources().getColor(R.color.celestino))
                            .width(4));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Point>> call, @NonNull Throwable t) {
                Timber.d("Get lista points tracciato fallita: %s", t.getMessage());
            }
        });
    }


    public void getPointsForDownloadFile(Long itinerarioId, String name, Context appContext, AppCompatActivity activity) {
        UtenteService utenteService = Api.getUtenteService();
        Call<List<Point>> call = utenteService.getPointsTracciatoById(itinerarioId);
        call.enqueue(new Callback<List<Point>>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onResponse(@NonNull Call<List<Point>> call, @NonNull Response<List<Point>> response) {
                Timber.d("Get lista points tracciato per file gpx ok");
                if (response.body() != null && response.body().size() != 0) {
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
                        if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"NaTou21\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
                                fos.write(header.getBytes());
                                for (Point p : response.body()) {
                                    String s = "<trkpt lat=\"" + p.getLatitude() + "\" lon=\"" + p.getLongitude() + "\"></trkpt>\n";
                                    fos.write(s.getBytes());
                                }
                                String footer = "</trkseg></trk></gpx>";
                                fos.write(footer.getBytes());

                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                intent.setData(Uri.fromFile(file));
                                activity.sendBroadcast(intent);
                                fos.close();
                                Timber.d("File scaricato correttamente : %s", file.getPath());
                                new MaterialAlertDialogBuilder(activity, R.style.Theme_NaTour_AlertDialog)
                                        .setBackground(activity.getDrawable(R.drawable.for1))
                                        .setTitle("File " + name + " salvato correttamente")
                                        .setMessage("Il file è stato salvato nella cartella download del tuo dispositivo")
                                        .setPositiveButton("OK", (dialogInterface, i) -> Timber.d("Uscita dal material."))
                                        .show();
                            } catch (FileNotFoundException e) {
                                Timber.d("FileNotFoundException %s", e.getMessage());
                            } catch (IOException e) {
                                Timber.d("IOException %s", e.getMessage());
                            }
                        } else {
                            Timber.d("NON SCARICATO 1 ");
                        }

                    } else {
                        Timber.d("NON SCARICATO 2");
                    }
                } else {
                    Toast.makeText(appContext, R.string.noTracciato, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Point>> call, @NonNull Throwable t) {
                Timber.d("Get lista points tracciato per file gpx fallita: %s", t.getMessage());
            }
        });

    }


    /* ------------------------------  POST ---------------------------------------------- */


    public void addPoint(Point point) {

        UtenteService utenteService = Api.getUtenteService();
        Call<Void> call = utenteService.registerNewPoint(point);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Timber.d("Point inserito correttamente nel Database");

                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Timber.d("Point NON inserito nel Database: %s", t.getMessage());
            }
        });
    }

}
