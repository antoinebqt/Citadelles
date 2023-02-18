package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObservatoryTest {

    District observatory;

    @BeforeEach
    void setUp() {
        observatory = new Observatory();
    }

    @Test
    void getValueTest() {
        assertEquals(5, observatory.getValue());
    }
}