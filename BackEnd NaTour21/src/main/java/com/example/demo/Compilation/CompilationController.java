package com.example.demo.Compilation;

import com.example.demo.Itinerario.Itinerario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/compilation")
public class CompilationController {

    @Autowired
    private CompilationServiceDAO compilationServiceDAO;

    @GetMapping("/getCompilation")
    public List<Compilation> getCompilation() {
        return compilationServiceDAO.getCompilation();
    }

    @GetMapping("/getListaItinerariCompilationById/{compilationId}")
    public List<Itinerario> getListaItinerariCompilationById(@PathVariable("compilationId") Long compilationId) {
        return compilationServiceDAO.getListaItinerariCompilationById(compilationId);
    }

    @PostMapping("/registerNewCompilation")
    public void registerNewCompilation (@RequestBody Compilation compilation) {
        compilationServiceDAO.addNewCompilation(compilation);
    }
}
