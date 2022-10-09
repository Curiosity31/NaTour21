package com.example.demo.Itinerario;

import javax.transaction.Transactional;
import java.util.List;

public interface ItinerarioServiceDAO {

    List<Itinerario> getItinerari();

    Itinerario getItinerarioById(Long itinerarioId);

    Long getItinerarioIdByItinerario(Itinerario itinerario);

    Itinerario addNewItinerario(Itinerario itinerario);

    void deleteItinerario(Long itinerarioId);

    @Transactional
    void updateDurationItinerario(Long itinerarioId, Integer durationMinutes);

    @Transactional
    void updateDifficultiesItinerario(Long itinerarioId, Integer difficulties);

    List<Itinerario> getItinerariByUtenteId(Long utenteId);
}
