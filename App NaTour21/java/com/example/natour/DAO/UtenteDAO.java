package com.example.natour.DAO;

import com.example.natour.Activity.HomeActivity;
import com.example.natour.Client.Retrieve;
import com.example.natour.Utente.Utente;


public interface UtenteDAO {

    /* ------------------------------  GET ---------------------------------------------- */

    void getLoggedIn(String email, HomeActivity homeActivity, Retrieve callbacks);

    /* ------------------------------  POST ---------------------------------------------- */

    void addUtente(Utente utente);

    /* ------------------------------  PUT ---------------------------------------------- */

    void updateLoggedUtente(String email, boolean loggedIn);


    void updateNickname (String email, String nickName);

}
