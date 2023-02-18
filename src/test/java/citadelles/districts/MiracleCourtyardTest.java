package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MiracleCourtyardTest {

    District miracleCourtyard;

    @BeforeEach
    void setUp() {
        miracleCourtyard = new MiracleCourtyard();
    }

    @Test
    void getValueTest() {
        assertEquals(2, miracleCourtyard.getValue());
    }
}