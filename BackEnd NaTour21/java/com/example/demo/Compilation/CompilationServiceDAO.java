package com.example.demo.Compilation;

import com.example.demo.Itinerario.Itinerario;

import java.util.List;

public interface CompilationServiceDAO {

    List<Compilation> getCompilation();

    void addNewCompilation(Compilation compilation);

    List<Itinerario> getListaItinerariCompilationById(Long compilationId);
}
