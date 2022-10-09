package com.example.demo.Utente;

import com.example.demo.Exception.EmailNotExistException;
import com.example.demo.Exception.NickNameNotValidException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UtenteServiceTest {

    @Autowired private UtenteRepository utenteRepositoryTest;
    @Autowired private UtenteService utenteServiceTest;
    private Utente utente;

    @BeforeEach
    void setup() {
        utente = new Utente("Carmine", "DiMa", "Cardima31", "test@test.it", false);
        utenteRepositoryTest.save(utente);
    }

    @AfterEach
    void tearDown() {
        utenteRepositoryTest.delete(utente);
    }

    /* ---------------------------------- BLACK BOX --------------------------------------------------------- */

    @Test
    void checkEmailIsNullAndNickNameIsValid() {
        Assertions.assertThrows(EmailNotExistException.class, ()-> utenteServiceTest.updateNickname(null, "nickname"));
    }


    @Test
    void checkEmailIsBlankAndNickNameIsValid() {
        Assertions.assertThrows(EmailNotExistException.class, ()-> utenteServiceTest.updateNickname("", "nickname"));
    }


    @Test
    void checkEmailIsValidAndNickNameIsValid() {
        utenteServiceTest.updateNickname("test@test.it", "CarDiMa313");
        Assertions.assertEquals("CarDiMa313", utenteRepositoryTest.findUtenteByEmail("test@test.it").get().getNickname());
    }

    @Test
    void checkEmailIsNotValidAndNickNameIsValid() {
        Assertions.assertThrows(EmailNotExistException.class, ()-> utenteServiceTest.updateNickname("emailNotRegistered", "nickname"));
    }

    @Test
    void checkEmailIsValidAndNickNameIsNull() {
        Assertions.assertThrows(NickNameNotValidException.class, ()-> utenteServiceTest.updateNickname("carmine.7@hotmail.it", null));
    }

    @Test
    void checkEmailIsValidAndNickNameIsNotValid() {
        Assertions.assertThrows(NickNameNotValidException.class, ()-> utenteServiceTest.updateNickname("carmine.7@hotmail.it", ""));
    }


    /* ---------------------------------- WHITE BOX --------------------------------------------------------- */

    @Test
    void check_path_1_2() {
        Assertions.assertThrows(EmailNotExistException.class, ()-> utenteServiceTest.updateNickname("emailNotRegistered", "nickname"));
    }

    @Test
    void check_path_1_4_5() {
        Assertions.assertThrows(NickNameNotValidException.class, ()-> utenteServiceTest.updateNickname("test@test.it", null));
    }

    @Test
    void check_path_1_4_7() {
        utenteServiceTest.updateNickname("test@test.it", "CarDiMa31");
        Assertions.assertEquals("CarDiMa31", utenteRepositoryTest.findUtenteByEmail("test@test.it").get().getNickname());
    }

    @Test
    void check_path_1_4_7_8() {
        utenteServiceTest.updateNickname("test@test.it", "Curiosity");
        Assertions.assertEquals("Curiosity", utenteRepositoryTest.findUtenteByEmail("test@test.it").get().getNickname());
    }

}