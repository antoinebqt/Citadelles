package citadelles.characters;

import citadelles.Hand;
import citadelles.bots.Bot;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArchitectTest {
    private Character architect;
    private Bot bot;

    @BeforeEach
    void setUp() {
        architect = new Architect();

        DistrictManager districtManager = new DistrictManager();
        bot = new Bot(0, new Hand(districtManager));
        architect.setCurrentBot(bot);
    }

    @Test
    void startTurnAction() {
        int handSizeBefore = bot.getHandSize();
        architect.startTurnAction();
        int handSizeAfter = bot.getHandSize();
        assertEquals(handSizeBefore+2,handSizeAfter);
    }

    @Test
    public void action() {
        architect.action();
        assertEquals(0, bot.getDistrictPlaced().size());
    }

}