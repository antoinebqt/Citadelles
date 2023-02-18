package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraveyardTest {

    District graveyard;

    @BeforeEach
    void setUp() {
        graveyard = new Graveyard();
    }

    @Test
    void getValueTest() {
        assertEquals(5, graveyard.getValue());
    }
}