package com.example.demo.Itinerario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/itinerario")
public class ItinerarioController {

    @Autowired
    private ItinerarioServiceDAO itinerarioServiceDAO;

    @GetMapping("/getItinerari")
    public List<Itinerario> getItinerari() {
        return itinerarioServiceDAO.getItinerari();
    }

    @GetMapping("/getItinerarioIdByItinerario/{itinerario}")
    public Long getItinerarioIdByItinerario(@PathVariable("itinerario") Itinerario itinerario) {
        return itinerarioServiceDAO.getItinerarioIdByItinerario(itinerario);
    }

    @GetMapping("/getItinerariByUtenteId/{utenteId}")
    public List<Itinerario> getItinerariByUtenteId(@PathVariable("utenteId") Long utenteId) {
        return itinerarioServiceDAO.getItinerariByUtenteId(utenteId);
    }

    @PostMapping("/registerNewItinerario")
    public Itinerario registerNewItinerario (@RequestBody Itinerario itinerario) {
        return itinerarioServiceDAO.addNewItinerario(itinerario);
    }

    @DeleteMapping(path="/deleteItinerario/{itinerarioId}")
    public void deleteItinerario(@PathVariable("itinerarioId") Long itinerarioId) {
        itinerarioServiceDAO.deleteItinerario(itinerarioId);
    }

    @PutMapping(path="/updateDurationItinerario/{itinerarioId}")
    public void updateDurationItinerario(
            @PathVariable("itinerarioId") Long utenteId,
            @RequestBody Integer durationMinutes) {

        itinerarioServiceDAO.updateDurationItinerario(utenteId, durationMinutes);
    }

    @PutMapping(path="/updateDifficultiesItinerario/{itinerarioId}")
    public void updateDifficultiesItinerario(
            @PathVariable("itinerarioId") Long utenteId,
            @RequestBody Integer difficulties) {

        itinerarioServiceDAO.updateDifficultiesItinerario(utenteId, difficulties);
    }
}
