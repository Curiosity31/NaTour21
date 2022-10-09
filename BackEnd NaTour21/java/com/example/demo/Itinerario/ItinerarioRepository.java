package com.example.demo.Itinerario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    @Query("SELECT i.id FROM Itinerario i WHERE i = ?1")
    Long getItinerarioIdByItinerario(Itinerario itinerario);

    @Query("SELECT i FROM Itinerario i WHERE i.utente.id = ?1 ORDER BY i.id DESC")
    List<Itinerario> getItinerariByUtenteId(Long utenteId);

    @Query("SELECT i FROM Itinerario i WHERE i.id = ?1")
    Itinerario getItinerarioById(Long itinerarioId);
}
