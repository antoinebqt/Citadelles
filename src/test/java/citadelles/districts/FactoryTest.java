package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryTest {

    District factory;

    @BeforeEach
    void setUp() {
        factory = new Factory();
    }

    @Test
    void getValueTest() {
        assertEquals(5, factory.getValue());
    }
}