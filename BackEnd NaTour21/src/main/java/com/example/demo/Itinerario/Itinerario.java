package com.example.demo.Itinerario;

import com.example.demo.Compilation.Compilation;
import com.example.demo.Utente.Utente;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "Itinerario")
@Table(name = "itinerario")
public class Itinerario {

    @Id
    @SequenceGenerator(
            name = "itinerario_sequence",
            sequenceName = "itinerario_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "itinerario_sequence"
    )

    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "starting_point", nullable = false, columnDefinition = "TEXT")
    private String startingPoint;

    @Column(name = "starting_point_latitudine", nullable = false)
    private double startingPointLatitudine;

    @Column(name = "starting_point_longitudine", nullable = false)
    private double startingPointLongitudine;

    @Column(name = "difficulties", nullable = false)
    private Integer difficulties;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "dis_access", nullable = false)
    private boolean disAccess;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @ManyToMany(mappedBy = "compilation_itinerari")
    private List<Compilation> compilations = new LinkedList<>();

    public Itinerario () {}

    public Itinerario(String name, Integer durationMinutes, String startingPoint, Integer difficulties, String description, boolean disAccess) {
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.startingPoint = startingPoint;
        this.difficulties = difficulties;
        this.description = description;
        this.disAccess = disAccess;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getDurationMinutes() {
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


    public Utente getUtente() {
        return utente;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDurationMinutes(Integer durationMinutes) {
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

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
