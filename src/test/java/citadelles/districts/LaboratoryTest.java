package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LaboratoryTest {

    District laboratory;

    @BeforeEach
    void setUp() {
        laboratory = new Laboratory();
    }

    @Test
    void getValueTest() {
        assertEquals(5, laboratory.getValue());
    }
}