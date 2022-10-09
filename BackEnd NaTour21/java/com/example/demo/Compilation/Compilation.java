package com.example.demo.Compilation;

import com.example.demo.Itinerario.Itinerario;
import com.example.demo.Utente.Utente;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "Compilation")
@Table(name = "compilation")
public class Compilation {

    @Id
    @SequenceGenerator(
            name = "compilation_sequence",
            sequenceName = "compilation_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "compilation_sequence"
    )

    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @ManyToMany()
    private List<Itinerario> compilation_itinerari = new LinkedList<>();


    public Long getId() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public void setCompilation_itinerari(List<Itinerario> compilation_itinerari) {
        this.compilation_itinerari = compilation_itinerari;
    }
}
