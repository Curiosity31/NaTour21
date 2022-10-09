package com.example.natour.Compilation;

import com.example.natour.Itinerario.Itinerario;
import com.example.natour.Utente.Utente;

import java.util.List;

public class Compilation {

    private long id;
    private String name;
    private final String description;
    private Utente utente;
    private final List<Itinerario> compilation_itinerari;

    public Compilation (String name, String description, List<Itinerario> compilation_itinerari) {
        this.name = name;
        this.description = description;
        this.compilation_itinerari = compilation_itinerari;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Utente getUtente() {
        return utente;
    }

    public List<Itinerario> getCompilation_itinerari() {
        return compilation_itinerari;
    }


    public void setUtente(Utente utente){
        this.utente=utente;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id){this.id = id;}

}
