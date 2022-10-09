package com.example.natour.Utente;

import com.example.natour.Compilation.Compilation;
import com.example.natour.Itinerario.Itinerario;
import com.example.natour.Points.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UtenteService {

    @GET("utente/getUtente")
    Call<List<Utente>> getUtente();

    @GET("utente/getUtenteByEmail/{email}")
    Call<Utente> getUtenteByEmail(@Path("email") String email);

    @GET("utente/getLoggedIn/{email}")
    Call<Boolean> getLoggedIn(@Path("email") String email);

    @GET("itinerario/getItinerari")
    Call<List<Itinerario>> getItinerari();

    @GET("point/getPointsTracciatoById/{itinerarioId}")
    Call<List<Point>> getPointsTracciatoById(@Path("itinerarioId") Long itinerarioId);

    @GET("compilation/getCompilation")
    Call<List<Compilation>> getCompilation();

    @GET("compilation/getListaItinerariCompilationById/{compilationId}")
    Call<List<Itinerario>> getListaItinerariCompilationById(@Path("compilationId") long compilationId);

    @GET("itinerario/getItinerariByUtenteId/{utenteId}")
    Call<List<Itinerario>> getItinerariByUtenteId(@Path("utenteId") Long utenteId);

    @POST("utente/registerNewUtente")
    Call<Void> registerNewUtente (@Body Utente utente);

    @POST("itinerario/registerNewItinerario")
    Call<Itinerario> registerNewItinerario (@Body Itinerario itinerario);

    @POST("point/registerNewPoint")
    Call<Void> registerNewPoint (@Body Point point);

    @POST("compilation/registerNewCompilation")
    Call<Void> registerNewCompilation (@Body Compilation compilation);

    @PUT("utente/updateLoggedUtente/{email}")
    Call<Void> updateLoggedUtente (@Path("email") String email, @Body boolean loggedIn);

    @PUT("utente/updateNickname/{email}")
    Call<Void> updateNickname(@Path("email") String email, @Body String nickName);

    @PUT("itinerario/updateDurationItinerario/{itinerarioId}")
    Call<Void> updateDurationItinerario(@Path("itinerarioId") long itinerarioId, @Body Integer time);

    @PUT("itinerario/updateDifficultiesItinerario/{itinerarioId}")
    Call<Void> updateDifficultiesItinerario(@Path("itinerarioId") long itinerarioId, @Body int diff3);

}
