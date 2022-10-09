package com.example.natour.DAO;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.Activity.HomeActivity;
import com.example.natour.Activity.ListaItinerariInCompilationActivity;
import com.example.natour.Activity.ProfiloActivity;
import com.example.natour.Itinerario.Itinerario;

import java.util.ArrayList;

public interface ItinerarioDAO {

    /* ------------------------------  GET ---------------------------------------------- */

    void getListaItinerari(RecyclerView recycle_itinerari, Context appContext, Bundle savedInstanceState, HomeActivity homeActivity);

    void getListaItinerariCompilation(RecyclerView recycle_itinerari_compilation, Context appContext, Bundle savedInstanceState);

    void getListaItinerariCompilationById(long compilationId, Context appContext, Bundle savedInstanceState, RecyclerView recycle_itinerari_in_compilation_view, ListaItinerariInCompilationActivity activity);

    void getListaItinerariByUtenteId(String email, TextView tv_nickname_profilo, Context appContext, Bundle bundle, RecyclerView recycle_itinerari_profilo, ProfiloActivity profiloActivity);

    void applicaFiltri(double latitudine, double longitudine, int min_durata, int max_durata, boolean[] difficolta, boolean accessibilita, RecyclerView recycle_itinerari, Bundle savedInstanceState, Context appContext, AppCompatActivity activity);


    /* ------------------------------  POST ---------------------------------------------- */

    void addItinerario(Itinerario itinerario, String email, ArrayList<Parcelable> points);


    /* ------------------------------  PUT ---------------------------------------------- */


    void updateDurationItinerario(long itinerarioId, Integer time);

    void updateDifficultiesItinerario(long itinerarioId, int diff3);

}
