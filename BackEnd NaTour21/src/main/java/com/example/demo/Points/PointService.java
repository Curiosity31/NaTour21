package com.example.demo.Points;

import com.example.demo.Exception.ListPointNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointService implements PointServiceDAO {

    @Autowired
    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public void addNewPoint(Point point) {

        pointRepository.save(point);
    }

    @Override
    public List<Point> getPointsTracciatoById(Long itinerarioId) {
        Optional<List<Point>> listaPoint = pointRepository.findPointsByItinerario(itinerarioId);
        if (listaPoint.isEmpty()) {
            throw new ListPointNotExistException("Nel database non sono presenti punti per l'itinerario con id: " + itinerarioId);
        }
        return listaPoint.get();
    }
}
