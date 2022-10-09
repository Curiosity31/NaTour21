package com.example.natour.Itinerario;

import com.example.natour.Utente.Utente;

public class Itinerario {

    private Long id;
    private String name;
    private Integer durationMinutes;
    private String startingPoint;
    private double startingPointLatitudine;
    private double startingPointLongitudine;
    private Integer difficulties;
    private String description;
    private boolean disAccess;
    private Utente utente;

    public Itinerario(String nomeItinerario, int min, String puntoDiInizio, double puntoDiInizioLatitudine,
                      double puntoDiInizioLongitudine, int difficolta,
                      String descrizione, boolean cbAccessibilitaDisabili) {

        name = nomeItinerario;
        durationMinutes = min;
        startingPoint = puntoDiInizio;
        startingPointLatitudine = puntoDiInizioLatitudine;
        startingPointLongitudine = puntoDiInizioLongitudine;
        difficulties = difficolta;
        description = descrizione;
        disAccess = cbAccessibilitaDisabili;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTime() {
        return durationMinutes;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public double getStartingPointLatitudine() {
        return startingPointLatitudine;
    }

    public double getStartingPointLongitudine() {
        return startingPointLongitudine;
    }

    public Integer getDifficulties() {
        return difficulties;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDisAccess() {
        return disAccess;
    }

    public Utente getUtente () { return utente; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public void setStartingPointLatitudine(double startingPointLatitudine) {
        this.startingPointLatitudine = startingPointLatitudine;
    }

    public void setStartingPointLongitudine(double startingPointLongitudine) {
        this.startingPointLongitudine = startingPointLongitudine;
    }

    public void setDifficulties(Integer difficulties) {
        this.difficulties = difficulties;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisAccess(boolean disAccess) {
        this.disAccess = disAccess;
    }


    public void setUtente (Utente utente) {
        this.utente = utente;
    }

}
