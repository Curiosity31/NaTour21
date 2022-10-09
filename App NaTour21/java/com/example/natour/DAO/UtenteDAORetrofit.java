package com.example.natour.DAO;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.natour.Activity.HomeActivity;
import com.example.natour.Activity.LoginActivity;
import com.example.natour.Client.Api;
import com.example.natour.Client.Retrieve;
import com.example.natour.Utente.Utente;
import com.example.natour.Utente.UtenteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class UtenteDAORetrofit implements  UtenteDAO {
    /* ------------------------------  GET ---------------------------------------------- */

    public void getLoggedIn(String email, HomeActivity homeActivity, Retrieve callbacks) {

        if (email == null) {
            Timber.d("Stato NON ricevuto, l'email è null");
            homeActivity.startActivity(new Intent(homeActivity.getApplicationContext(), LoginActivity.class));
        }

        UtenteService utenteService = Api.getUtenteService();
        Call<Boolean> call = utenteService.getLoggedIn(email);
        call.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {

                if (response.isSuccessful()) {
                    Boolean result = response.body();
                    Timber.d("Stato ricevuto correttamente: utente loggato: %s", result);
                    assert result != null;
                    callbacks.onSuccessBoolean(result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                callbacks.onError(t);
            }
        });

    }

    /* ------------------------------  POST ---------------------------------------------- */

    public void addUtente(Utente utente) {

        UtenteService utenteService = Api.getUtenteService();
        Call<Void> call = utenteService.registerNewUtente(utente);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Timber.d("Utente inserito correttamente nel Database");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Timber.d("Utente NON inserito nel Database: %s", t.getMessage());
            }
        });
    }


    /* ------------------------------  PUT ---------------------------------------------- */

    public void updateLoggedUtente(String email, boolean loggedIn) {

        UtenteService utenteService = Api.getUtenteService();
        Call<Void> call = utenteService.updateLoggedUtente(email, loggedIn);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                Timber.d("Stato cambiato correttamente");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Timber.d("Stato non cambiato nel Database: %s", t.getMessage());
            }
        });
    }


    public void updateNickname (String email, String nickName) {

        UtenteService utenteService = Api.getUtenteService();
        Call<Void> call = utenteService.updateNickname(email, nickName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Timber.d("nickName modificato correttamente.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Timber.d("Errore: nickName NON modificato : %s", t.getMessage());
            }
        });
    }

}
