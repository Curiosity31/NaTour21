package com.example.demo.Points;

import com.example.demo.Itinerario.Itinerario;

import javax.persistence.*;

@Entity(name = "Point")
@Table(name = "point")
public class Point {

    @Id
    @SequenceGenerator(
            name = "point_sequence",
            sequenceName = "point_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "point_sequence"
    )

    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "itinerario_id")
    private Itinerario itinerario;

    public Point(){}

    public Point(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
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
