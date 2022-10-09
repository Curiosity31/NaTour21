package com.example.natour.Points;

import com.example.natour.Itinerario.Itinerario;

public class Point {

    private double latitude;
    private double longitude;
    private Itinerario itinerario;

    public Point(double latitude, double longitude, Itinerario itinerario) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.itinerario = itinerario;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setItinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
    }
}
