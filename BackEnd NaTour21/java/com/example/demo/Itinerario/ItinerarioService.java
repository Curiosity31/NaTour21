package com.example.demo.Itinerario;

import com.example.demo.Exception.DifficultiesIsNotValidException;
import com.example.demo.Exception.DurationIsNotValidException;
import com.example.demo.Exception.IdNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ItinerarioService implements ItinerarioServiceDAO {

    @Autowired
    private final ItinerarioRepository itinerarioRepository;

    public ItinerarioService(ItinerarioRepository itinerarioRepository) {
        this.itinerarioRepository = itinerarioRepository;
    }

    @Override
    public List<Itinerario> getItinerari() {
        return itinerarioRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Itinerario getItinerarioById(Long itinerarioId) {
        return itinerarioRepository.getItinerarioById(itinerarioId);
    }

    @Override
    public Long getItinerarioIdByItinerario(Itinerario itinerario) {
        return itinerarioRepository.getItinerarioIdByItinerario(itinerario);
    }


    @Override
    public List<Itinerario> getItinerariByUtenteId(Long utenteId) {
        return itinerarioRepository.getItinerariByUtenteId(utenteId);
    }


    @Override
    public Itinerario addNewItinerario(Itinerario itinerario) {
        return itinerarioRepository.save(itinerario);
    }


    @Override
    public void deleteItinerario(Long itinerarioId) {

        boolean exists = itinerarioRepository.existsById(itinerarioId);
        if (!exists) {
            throw new IdNotExistException("Itinerario con id " + itinerarioId + " non è presente nel DB");
        }

        itinerarioRepository.deleteById(itinerarioId);
    }

    @Override
    @Transactional
    public void updateDifficultiesItinerario(Long itinerarioId, Integer difficulties) {
        if (itinerarioId == null) {
            throw new IllegalArgumentException("L'id dell'itinerario non può essere null");
        }
        Itinerario itinerario = itinerarioRepository.findById(itinerarioId).
                orElseThrow(() -> new IdNotExistException("Itinerario con id " + itinerarioId + " non è presente nel DB"));
        if (difficulties == null) {
            throw new IllegalArgumentException("La difficoltà dell'itinerario non può essere null");
        }
        else if (difficulties > -1 && difficulties < 4) {
            itinerario.setDifficulties(difficulties);
        } else {
            throw new DifficultiesIsNotValidException("La difficoltà inserita per l'itinerario non è valida");
        }
    }

    @Override
    @Transactional
    public void updateDurationItinerario(Long itinerarioId, Integer durationMinutes) {
        if (itinerarioId == null) {
            throw new IllegalArgumentException("L'id dell'itinerario non può essere null");
        }
        Itinerario itinerario = itinerarioRepository.findById(itinerarioId).
                orElseThrow(() -> new IdNotExistException("Itinerario con id " + itinerarioId + " non è presente nel DB"));

        if (durationMinutes == null) {
            throw new IllegalArgumentException("La durata dell'itinerario non può essere null");
        }
        else if (durationMinutes < 1) {
            throw new DurationIsNotValidException("La durata di percorrenza inserita per l'itinerario non è valida");
        }
        itinerario.setDurationMinutes(durationMinutes);
    }

}