package com.example.demo.Points;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("SELECT p FROM Point p WHERE p.itinerario.id = ?1")
    Optional<List<Point>> findPointsByItinerario (Long id);
}
