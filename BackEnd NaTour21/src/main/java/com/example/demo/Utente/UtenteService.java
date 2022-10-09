package com.example.demo.Utente;

import com.example.demo.Exception.EmailAlreadyPresentException;
import com.example.demo.Exception.EmailNotExistException;
import com.example.demo.Exception.IdNotExistException;
import com.example.demo.Exception.NickNameNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UtenteService implements UtenteServiceDAO {

    @Autowired
    private final UtenteRepository utenteRepository;


    public UtenteService(UtenteRepository utenteRepository) {

        this.utenteRepository = utenteRepository;
    }

    @Override
    public List<Utente> getUtente() {
        return utenteRepository.findAll();
    }

    @Override
    public Utente getUtenteByEmail(String email) {
        Optional<Utente> utente = utenteRepository.findUtenteByEmail(email);
        if (utente.isEmpty()) {
            throw new EmailNotExistException("Nel database non è presente un utente con email: " + email);
        }
        return utente.get();
    }

    @Override
    public Boolean getLoggedIn(String email) { return utenteRepository.findStateByEmail(email); }


    @Override
    public void addNewUtente(Utente utente) {
        Optional<Utente> utenteByEmail = utenteRepository.findUtenteByEmail(utente.getEmail());
        if (utenteByEmail.isPresent()) {
            throw new EmailAlreadyPresentException("Email già esistente.");
        }
        utenteRepository.save(utente);
    }


    @Override
    public void deleteUtente(Long utenteId) {

        boolean exists = utenteRepository.existsById(utenteId);
        if (!exists) {
            throw new IdNotExistException("Utente con id " + utenteId + " non è presente nel DB");
        }

        utenteRepository.deleteById(utenteId);
    }


    @Override
    @Transactional
    public void updateNickname(String email, String nickname) {
        Utente utente = utenteRepository.findUtenteByEmail(email).
                orElseThrow(() -> new EmailNotExistException("Utente con email " + email + " non è presente nel DB"));

        if (nickname == null || nickname.length() < 1) {
            throw new NickNameNotValidException("Il NickName Inserito non è valido");
        }
        if ( !utente.getNickname().equals(nickname)) {
            utente.setNickname(nickname);
        }
    }


    @Override
    @Transactional
    public void updateEmail(String email, String newEmail) {
        Utente utente = utenteRepository.findUtenteByEmail(email).
                orElseThrow(() -> new EmailNotExistException("Utente con email " + email + " non è presente nel DB"));

        if (newEmail != null && newEmail.length() > 0 && !email.equals(newEmail)) {
            Optional<Utente> utenteOptional = utenteRepository.findUtenteByEmail(newEmail);
            if (utenteOptional.isPresent()) {
                throw new EmailAlreadyPresentException("Email già esistente.");
            }
            utente.setEmail(newEmail);
        }
    }


    @Override
    @Transactional
    public void updateLoggedUtente(String email, boolean loggedIn) {

        Utente utente = utenteRepository.findUtenteByEmail(email).
                orElseThrow(() -> new EmailNotExistException("Utente con email " + email + " non è presente nel DB"));

        if (utente.getLoggedIn() != loggedIn) {
            utente.setLoggedIn(loggedIn);
        }
    }
}
