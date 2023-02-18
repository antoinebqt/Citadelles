package citadelles.districts;


import citadelles.Hand;
import citadelles.bots.Bot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistrictTest {

    @Test
    void getValue() {
        assertEquals(1, new District("Test", 1, 4).getValue());
    }

    @Test
    void getEndGameValueTest() {
        assertEquals(5, new Laboratory().getEndGameValue());
    }

    @Test
    void getEndGameValueTestIfUniversity() {
        assertEquals(8, new University().getEndGameValue());
    }

    @Test
    void getName() {
        assertEquals("Test", new District("Test", 1, 4).getName());
    }

    @Test
    void getCategory() {
        assertEquals(4, new District("Test", 1, 4).getCategory());
    }

    @Test
    void canBeDestruct() {
        District d = new District("Test", 1, 1);
        assertTrue(d.isCanBeDestruct());
        d.setCanBeDestruct(false);
        assertFalse(d.isCanBeDestruct());
    }

    @Test
    void setGetCurrentBot(){
        District district = new District("random",1,1);
        Bot bot = new Bot(1,new Hand(new DistrictManager()));

        district.setCurrentBot(bot);

        assertEquals(bot,district.getCurrentBot());
    }
}