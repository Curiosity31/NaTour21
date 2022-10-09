package com.example.demo.Itinerario;

import com.example.demo.Exception.DifficultiesIsNotValidException;
import com.example.demo.Exception.DurationIsNotValidException;
import com.example.demo.Exception.IdNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItinerarioServiceTest {

    @Autowired private ItinerarioRepository itinerarioRepositoryTest;
    @Autowired private ItinerarioService itinerarioServiceTest;
    private Itinerario itinerarioTest;

    @BeforeEach
    void setup() {
        itinerarioTest = new Itinerario("Test", 300, "T.A.", 1, "test", true);
        itinerarioRepositoryTest.save(itinerarioTest);
    }

    @AfterEach
    void tearDown() {
        itinerarioRepositoryTest.delete(itinerarioTest);
    }

    /* ---------------------------------------- TEST DIFFICULTIES -------------------------------------------------------- */

        /* --------------------------------------- BLACK BOX ------------------------------------------------------ */

    @Test
    void checkItinerarioIDIsNotPositiveAndDifficultiesIsValid() {
        Assertions.assertThrows(IdNotExistException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(-1L, 1));
    }

    @Test
    void checkItinerarioIDIsNullAndDifficultiesIsValid() {
        Assertions.assertThrows(IllegalArgumentException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(null, 1));
    }

    @Test
    void checkItinerarioIDIsNotValidAndDifficultiesIsValid() {
        Assertions.assertThrows(IdNotExistException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(500L, 1));
    }

    @Test
    void checkItinerarioIDIsValidAndDifficultiesIsValid() {
        itinerarioServiceTest.updateDifficultiesItinerario(itinerarioRepositoryTest.getItinerarioIdByItinerario(itinerarioTest), 3);
        Assertions.assertEquals(3, itinerarioRepositoryTest.getItinerarioById(itinerarioRepositoryTest.getItinerarioIdByItinerario(itinerarioTest)).getDifficulties());
    }

    @Test
    void checkItinerarioIDIsValidAndDifficultiesIsNegative() {
        Assertions.assertThrows(DifficultiesIsNotValidException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(35L, -1));
    }

    @Test
    void checkItinerarioIDIsValidAndDifficultiesIsNotValid() {
        Assertions.assertThrows(DifficultiesIsNotValidException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(35L, 7));
    }

    @Test
    void checkItinerarioIDIsValidAndDifficultiesIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(35L, null));
    }

        /* --------------------------------------- WHITE BOX ------------------------------------------------------ */

    @Test
    void check_path_1_2() {
        Assertions.assertThrows(IllegalArgumentException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(null, 1));
    }


    @Test
    void check_path_1_4_5() {
        Assertions.assertThrows(IdNotExistException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(500L, 1));
    }

    @Test
    void check_path_1_4_6_7() {
        Assertions.assertThrows(IllegalArgumentException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(35L, null));
    }

    @Test
    void check_path_1_4_6_9_10() {
        itinerarioServiceTest.updateDifficultiesItinerario(itinerarioRepositoryTest.getItinerarioIdByItinerario(itinerarioTest), 3);
        Assertions.assertEquals(3, itinerarioRepositoryTest.getItinerarioById(itinerarioRepositoryTest.getItinerarioIdByItinerario(itinerarioTest)).getDifficulties());
    }

    @Test
    void check_path_1_4_6_9_12() {
        Assertions.assertThrows(DifficultiesIsNotValidException.class, ()-> itinerarioServiceTest.updateDifficultiesItinerario(35L, 7));
    }


    /* ----------------------------------------- TEST DURATION ----------------------------------------------------------*/

       /* ---------------------------------------- BLACK BOX ------------------------------------------------------ */

    @Test
    void checkItinerarioIDIsNotPositiveAndDurationIsValid() {
        Assertions.assertThrows(IdNotExistException.class, ()-> itinerarioServiceTest.updateDurationItinerario(-1L, 10));
    }

    @Test
    void checkItinerarioIDIsNullAndDurationIsValid() {
        Assertions.assertThrows(IllegalArgumentException.class, ()-> itinerarioServiceTest.updateDurationItinerario(null, 10));
    }

    @Test
    void checkItinerarioIDIsValidAndDurationIsValid() {
        itinerarioServiceTest.updateDurationItinerario(itinerarioRepositoryTest.getItinerarioIdByItinerario(itinerarioTest), 100);
        Assertions.assertEquals(100, itinerarioRepositoryTest.getItinerarioById(itinerarioRepositoryTest.getItinerarioIdByItinerario(itinerarioTest))
                .getDurationMinutes());
    }

    @Test
    void checkItinerarioIDIsNotValidAndDurationIsValid() {
        Assertions.assertThrows(IdNotExistException.class, ()-> itinerarioServiceTest.updateDurationItinerario(500L, 10));
    }

    @Test
    void checkItinerarioIDIsValidAndDurationIsNotPositive() {
        Assertions.assertThrows(DurationIsNotValidException.class, ()-> itinerarioServiceTest.updateDurationItinerario(35L, -1));
    }

    @Test
    void checkItinerarioIDIsValidAndDurationIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, ()-> itinerarioServiceTest.updateDurationItinerario(35L, null));
    }


       /* --------------------------------------- WHITE BOX ------------------------------------------------------ */

    @Test
    void check_path_1_2b() {
        Assertions.assertThrows(IllegalArgumentException.class, ()-> itinerarioServiceTest.updateDurationItinerario(null, 10));
    }


    @Test
    void check_path_1_4_5b() {
        Assertions.assertThrows(IdNotExistException.class, ()-> itinerarioServiceTest.updateDurationItinerario(500L, 10));
    }

    @Test
    void check_path_1_4_7_8b() {
        Assertions.assertThrows(IllegalArgumentException.class, ()-> itinerarioServiceTest.updateDurationItinerario(35L, null));
    }

    @Test
    void check_path_1_4_7_10_11b() {
        Assertions.assertThrows(DurationIsNotValidException.class, ()-> itinerarioServiceTest.updateDurationItinerario(35L, -1));
    }

    @Test
    void check_path_1_4_7_10_13b() {
        itinerarioServiceTest.updateDurationItinerario(itinerarioRepositoryTest.getItinerarioIdByItinerario(itinerarioTest), 100);
        Assertions.assertEquals(100, itinerarioRepositoryTest.getItinerarioById(itinerarioRepositoryTest.getItinerarioIdByItinerario(itinerarioTest))
                .getDurationMinutes());
    }
}
