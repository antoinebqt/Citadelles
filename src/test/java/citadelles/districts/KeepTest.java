package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeepTest {

    District keep;

    @BeforeEach
    void setUp() {
        keep = new Keep();
    }

    @Test
    void getValueTest() {
        assertEquals(3, keep.getValue());
        assertFalse(keep.isCanBeDestruct());
    }
}