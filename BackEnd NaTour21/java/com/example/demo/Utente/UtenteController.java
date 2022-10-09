package com.example.demo.Utente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/utente")
public class UtenteController {

    @Autowired
    private UtenteServiceDAO utenteServiceDAO;

    @GetMapping("/getUtente")
    public List<Utente> getUtente() {
       return utenteServiceDAO.getUtente();
    }

    @GetMapping("/getUtenteByEmail/{email}")
    public Utente getUtenteByEmail(@PathVariable("email") String email) {return utenteServiceDAO.getUtenteByEmail(email);}

    @GetMapping("/getLoggedIn/{email}")
    public Boolean getLoggedIn(@PathVariable("email") String email) { return utenteServiceDAO.getLoggedIn(email); }

    @PostMapping("/registerNewUtente")
    public void registerNewUtente (@RequestBody Utente utente) {
        utenteServiceDAO.addNewUtente(utente);
    }

    @DeleteMapping(path="/deleteUtente/{utenteId}")
    public void deleteUtente(@PathVariable("utenteId") Long utenteId) {
        utenteServiceDAO.deleteUtente(utenteId);
    }

    @PutMapping(path="/updateNickname/{email}")
    public void updateNickname(
            @PathVariable("email") String email,
            @RequestBody String newNickname) {
        utenteServiceDAO.updateNickname(email, newNickname.replace("\"", ""));
    }

    @PutMapping(path="/updateEmail/{email}")
    public void updateEmail(
            @PathVariable("email") String email,
            @RequestBody String newEmail) {
        utenteServiceDAO.updateEmail(email, newEmail);
    }

    @PutMapping(path="/updateLoggedUtente/{email}")
    public void updateLoggedUtente(
            @PathVariable("email") String email,
            @RequestBody boolean loggedIn ) {

        utenteServiceDAO.updateLoggedUtente(email, loggedIn);
    }


}