package com.example.demo.Utente;

import javax.transaction.Transactional;
import java.util.List;

public interface UtenteServiceDAO {

    List<Utente> getUtente();

    Utente getUtenteByEmail(String email);

    Boolean getLoggedIn(String email);

    void addNewUtente(Utente utente);

    void deleteUtente(Long utenteId);

    @Transactional
    void updateNickname(String email, String nickname);

    @Transactional
    void updateEmail(String email, String newEmail);

    @Transactional
    void updateLoggedUtente(String email, boolean loggedIn);

}
