package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DragonGateTest {

    District dragonGate;

    @BeforeEach
    void setUp() {
        dragonGate = new DragonGate();
    }

    @Test
    void getEndGameValueTest() {
        assertEquals(8, dragonGate.getEndGameValue());
    }

}