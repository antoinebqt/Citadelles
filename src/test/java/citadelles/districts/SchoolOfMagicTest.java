package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoolOfMagicTest {

    District schoolOfMagic;

    @BeforeEach
    void setUp() {
        schoolOfMagic = new SchoolOfMagic();
    }

    @Test
    void getValueTest() {
        assertEquals(6, schoolOfMagic.getValue());
    }
}