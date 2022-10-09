package com.example.natour.Client;

import com.example.natour.Utente.UtenteService;

public class Api {

    public static final String URL = "end point di EC2";

    public static UtenteService getUtenteService() {

        return Client.getClient(URL).create(UtenteService.class);
    }
}
