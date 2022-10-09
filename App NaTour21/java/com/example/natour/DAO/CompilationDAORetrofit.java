package com.example.natour.DAO;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.Client.Api;
import com.example.natour.Compilation.Compilation;
import com.example.natour.Compilation.CompilationAdapterView;
import com.example.natour.Utente.Utente;
import com.example.natour.Utente.UtenteService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class CompilationDAORetrofit implements  CompilationDAO {

    /* ------------------------------  GET ---------------------------------------------- */
    public void getListaCompilation(RecyclerView recycle_compilation_view, Context appContext) {
        UtenteService utenteService = Api.getUtenteService();
        Call<List<Compilation>> call = utenteService.getCompilation();
        call.enqueue(new Callback<List<Compilation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Compilation>> call, @NonNull Response<List<Compilation>> response) {
                recycle_compilation_view.setAdapter(new CompilationAdapterView(response.body(), appContext));
                Timber.d("Get lista compilation Ok");
            }

            @Override
            public void onFailure(@NonNull Call<List<Compilation>> call, @NonNull Throwable t) {
                Timber.d("Get lista compilation fallita: %s", t.getMessage());
            }
        });
    }


    /* ------------------------------  POST ---------------------------------------------- */

    public void addCompilation(Compilation compilation, String email) {
        UtenteService utenteService = Api.getUtenteService();
        Call<Utente> call = utenteService.getUtenteByEmail(email);
        call.enqueue(new Callback<Utente>() {
            @Override
            public void onResponse(@NonNull Call<Utente> call, @NonNull Response<Utente> response) {
                if (response.isSuccessful()) {
                    Timber.d("getUtenteByEmail succes");

                    compilation.setUtente(response.body());
                    Call<Void> call2 = utenteService.registerNewCompilation(compilation);
                    call2.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call2, @NonNull Response<Void> response2) {
                            if (response2.isSuccessful()) {
                                Timber.d("Compilation inserita correttamente nel Database");
                            } else {
                                Timber.d("Compilation NON inserita correttamente nel Database");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call2, @NonNull Throwable t2) {
                            Timber.d("Compilation NON inserito nel Database: %s", t2.getMessage());
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

}
