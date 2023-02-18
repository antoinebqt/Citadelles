package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    District library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Test
    void getValueTest() {
        assertEquals(6, library.getValue());
    }
}