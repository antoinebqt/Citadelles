package citadelles.districts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryEnumTest {

    @Test
    void getValueOfReligious() {
        assertEquals(1, CategoryEnum.RELIGIOUS.getValue());
    }

    @Test
    void getValueOfMilitary() {
        assertEquals(4, CategoryEnum.MILITARY.getValue());
    }
}
