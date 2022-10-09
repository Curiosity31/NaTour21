package com.example.natour.DAO;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.example.natour.Points.Point;
import com.mapbox.mapboxsdk.maps.MapboxMap;


public interface PointDAO {

    /* ------------------------------  GET ---------------------------------------------- */
    void getPointsTracciatoById(MapboxMap mapboxMap, Long itinerarioId, Context appContext);

    void getPointsForDownloadFile(Long itinerarioId, String name, Context appContext, AppCompatActivity activity);


    /* ------------------------------  POST ---------------------------------------------- */

    void addPoint(Point point);
}
