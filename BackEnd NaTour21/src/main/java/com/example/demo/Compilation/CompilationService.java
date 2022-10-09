package com.example.demo.Compilation;

import com.example.demo.Itinerario.Itinerario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompilationService implements CompilationServiceDAO {
    @Autowired
    private final CompilationRepository compilationRepository;

    public CompilationService (CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public List<Compilation> getCompilation() {
        return compilationRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public List<Itinerario> getListaItinerariCompilationById(Long compilationId) {
        return compilationRepository.findItinerariById(compilationId);
    }

    @Override
    public void addNewCompilation(Compilation compilation) {
        compilationRepository.save(compilation);
    }

}
