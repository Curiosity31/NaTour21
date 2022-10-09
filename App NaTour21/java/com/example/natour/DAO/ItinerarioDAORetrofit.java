package com.example.natour.DAO;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.Activity.HomeActivity;
import com.example.natour.Activity.ListaItinerariInCompilationActivity;
import com.example.natour.Activity.ProfiloActivity;
import com.example.natour.Client.Api;
import com.example.natour.Compilation.CompilationAdapter;
import com.example.natour.Itinerario.Itinerario;
import com.example.natour.Itinerario.ItinerarioAdapter;
import com.example.natour.Points.Point;
import com.example.natour.Utente.Utente;
import com.example.natour.Utente.UtenteService;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ItinerarioDAORetrofit implements  ItinerarioDAO {

    /* ------------------------------  GET ---------------------------------------------- */

    public void getListaItinerari(RecyclerView recycle_itinerari, Context appContext, Bundle savedInstanceState, HomeActivity homeActivity) {

        UtenteService utenteService = Api.getUtenteService();
        Call<List<Itinerario>> call = utenteService.getItinerari();
        call.enqueue(new Callback<List<Itinerario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Itinerario>> call, @NonNull Response<List<Itinerario>> response) {
                recycle_itinerari.setAdapter(new ItinerarioAdapter(response.body(), appContext, savedInstanceState, homeActivity));
                Timber.d("Get lista itinerari Ok");
            }

            @Override
            public void onFailure(@NonNull Call<List<Itinerario>> call, @NonNull Throwable t) {
                Timber.d("Get lista itinerari fallita: %s", t.getMessage());
            }
        });
    }

    public void getListaItinerariCompilation(RecyclerView recycle_itinerari_compilation, Context appContext, Bundle savedInstanceState) {

        UtenteService utenteService = Api.getUtenteService();
        Call<List<Itinerario>> call = utenteService.getItinerari();
        call.enqueue(new Callback<List<Itinerario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Itinerario>> call, @NonNull Response<List<Itinerario>> response) {
                recycle_itinerari_compilation.setAdapter(new CompilationAdapter(response.body(), appContext, savedInstanceState));
                Timber.d("Get lista itinerari Ok");
            }

            @Override
            public void onFailure(@NonNull Call<List<Itinerario>> call, @NonNull Throwable t) {
                Timber.d("Get lista itinerari fallita: %s", t.getMessage());
            }
        });
    }


    public void getListaItinerariCompilationById(long compilationId, Context appContext, Bundle savedInstanceState, RecyclerView recycle_itinerari_in_compilation_view, ListaItinerariInCompilationActivity activity) {
        UtenteService utenteService = Api.getUtenteService();
        Call<List<Itinerario>> call = utenteService.getListaItinerariCompilationById(compilationId);
        call.enqueue(new Callback<List<Itinerario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Itinerario>> call, @NonNull Response<List<Itinerario>> response) {
                recycle_itinerari_in_compilation_view.setAdapter(new ItinerarioAdapter(response.body(), appContext, savedInstanceState, activity));
                Timber.d("Get lista itinerari compilation Ok");
            }

            @Override
            public void onFailure(@NonNull Call<List<Itinerario>> call, @NonNull Throwable t) {
                Timber.d("Get lista itinerari compilation fallita: %s", t.getMessage());
            }
        });
    }


    public void getListaItinerariByUtenteId(String email, TextView tv_nickname_profilo, Context appContext, Bundle bundle, RecyclerView recycle_itinerari_profilo, ProfiloActivity profiloActivity) {
        UtenteService utenteService = Api.getUtenteService();
        Call<Utente> call = utenteService.getUtenteByEmail(email);
        call.enqueue(new Callback<Utente>() {
            @Override
            public void onResponse(@NonNull Call<Utente> call, @NonNull Response<Utente> response) {
                if (response.isSuccessful()) {
                    Timber.d("getUtenteByEmail succes");
                    assert response.body() != null;
                    Long utenteId = response.body().getId();
                    tv_nickname_profilo.setText(response.body().getNickname());
                    Call<List<Itinerario>> call2 = utenteService.getItinerariByUtenteId(utenteId);
                    call2.enqueue(new Callback<List<Itinerario>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Itinerario>> call2, @NonNull Response<List<Itinerario>> response2) {
                            if (response2.isSuccessful()) {
                                Timber.d("Lista itinerari per profilo ricevuta correttamente");
                                recycle_itinerari_profilo.setAdapter(new ItinerarioAdapter(response2.body(), appContext, bundle, profiloActivity));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Itinerario>> call2, @NonNull Throwable t2) {
                            Timber.d("Lista itinerari per profilo NON ricevuta correttamente\": %s", t2.getMessage());
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NonNull Call<Utente> call, @NonNull Throwable t) {
                Timber.d("Errore getUtenteByEmail: %s", t.getMessage());
            }
        });
    }


    public void applicaFiltri(double latitudine, double longitudine, int min_durata, int max_durata, boolean[] difficolta, boolean accessibilita, RecyclerView recycle_itinerari, Bundle savedInstanceState, Context appContext, AppCompatActivity activity) {
        UtenteService utenteService = Api.getUtenteService();
        Call<List<Itinerario>> call = utenteService.getItinerari();
        call.enqueue(new Callback<List<Itinerario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Itinerario>> call, @NonNull Response<List<Itinerario>> response) {
                List<Itinerario> list = new LinkedList<>();
                double lat;
                double lon;
                assert response.body() != null;
                if (latitudine != -1000 && longitudine != -1000) {
                    for (Itinerario it : response.body()) {
                        lat = it.getStartingPointLatitudine();
                        lon = it.getStartingPointLongitudine();
                        if ((lat >= latitudine - 0.5 && lat <= latitudine + 0.5) && (lon >= longitudine - 0.5 && lon <= longitudine + 0.5)) {
                            int time = it.getTime() / 60;
                            if (time >= min_durata && time <= max_durata) {
                                if (difficolta[it.getDifficulties()]) {
                                    if (accessibilita) {
                                        if (it.isDisAccess()) {
                                            list.add(it);
                                        }
                                    } else {
                                        list.add(it);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (Itinerario it : response.body()) {
                        int time = it.getTime() / 60;
                        if (time >= min_durata && time <= max_durata) {
                            if (difficolta[it.getDifficulties()]) {
                                if (accessibilita) {
                                    if (it.isDisAccess()) {
                                        list.add(it);
                                    }
                                } else {
                                    list.add(it);
                                }
                            }
                        }
                    }
                }
                if (list.size() == 0) {
                    Toast.makeText(appContext, "I filtri inseriti non hanno prodotto alcun risultato", Toast.LENGTH_LONG).show();
                } else {
                    recycle_itinerari.setAdapter(new ItinerarioAdapter(list, appContext, savedInstanceState, activity));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Itinerario>> call, @NonNull Throwable t) {
                Timber.d("Get lista itinerari filtrati fallita: %s", t.getMessage());
            }
        });
    }



    /* ------------------------------  POST ---------------------------------------------- */


    public void addItinerario(Itinerario itinerario, String email, ArrayList<Parcelable> points) {

        DAOFactory daoFactory = DAOFactory.getDaoFactory();


        UtenteService utenteService = Api.getUtenteService();
        Call<Utente> call = utenteService.getUtenteByEmail(email);
        call.enqueue(new Callback<Utente>() {
            @Override
            public void onResponse(@NonNull Call<Utente> call, @NonNull Response<Utente> response) {
                if (response.isSuccessful()) {
                    Timber.d("getUtenteByEmail succes");

                    itinerario.setUtente(response.body());
                    Call<Itinerario> call2 = utenteService.registerNewItinerario(itinerario);
                    call2.enqueue(new Callback<Itinerario>() {
                        @Override
                        public void onResponse(@NonNull Call<Itinerario> call2, @NonNull Response<Itinerario> response2) {
                            if (response2.isSuccessful()) {
                                Timber.d("Itinerario inserito correttamente nel Database");

                                // Inserimento punti in database
                                for (Parcelable p : points) {
                                    LatLng latLng = (LatLng) p;
                                    Point point = new Point(latLng.getLatitude(), latLng.getLongitude(), response2.body());
                                    daoFactory.getPointDAO().addPoint(point);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Itinerario> call2, @NonNull Throwable t2) {
                            Timber.d("Itinerario NON inserito nel Database: %s", t2.getMessage());
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NonNull Call<Utente> call, @NonNull Throwable t) {
                Timber.d("Errore getUtenteByEmail: %s", t.getMessage());
            }
        });

    }



    /* ------------------------------  PUT ---------------------------------------------- */

    public void updateDurationItinerario(long itinerarioId, Integer time) {
        UtenteService utenteService = Api.getUtenteService();
        Call<Void> call = utenteService.updateDurationItinerario(itinerarioId, time);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Timber.d("Tempo totale modificato correttamente.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Timber.d("Errore: Tempo totale NON modificato : %s", t.getMessage());
            }
        });
    }

    public void updateDifficultiesItinerario(long itinerarioId, int diff3) {
        UtenteService utenteService = Api.getUtenteService();
        Call<Void> call = utenteService.updateDifficultiesItinerario(itinerarioId, diff3);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Timber.d("Difficoltà modificata correttamente.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Timber.d("Errore: Difficoltà NON modificato : %s", t.getMessage());
            }
        });
    }

}
