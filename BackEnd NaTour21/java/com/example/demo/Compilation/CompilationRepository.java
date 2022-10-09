package com.example.demo.Compilation;

import com.example.demo.Itinerario.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("SELECT c.compilation_itinerari FROM Compilation c WHERE c.id = ?1")
    List<Itinerario> findItinerariById(Long compilationId);

}
