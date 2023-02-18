package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniversityTest {

    District university;

    @BeforeEach
    void setUp() {
        university = new University();
    }

    @Test
    void getEndGameValueTest() {
        assertEquals(8, university.getEndGameValue());
    }
}