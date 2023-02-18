package citadelles;

import citadelles.bots.Bot;
import citadelles.bots.BotManager;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotViewTest {

    BotView botView;
    BotManager botManager;
    DistrictManager districtManager;

    @BeforeEach
    void setUp() {
        districtManager = new DistrictManager();
        botManager = new BotManager(1, 1, 1, 0,districtManager);
        botView = new BotView(botManager, 0);
    }

    @Test
    void getGoldTest() {
        assertEquals(botManager.getListBot().get(0).getGold(), botView.getGold());
    }

    @Test
    void getNbCardHandTest() {
        assertEquals(botManager.getListBot().get(0).getHandSize(), botView.getNbCardHand());
    }

    @Test
    void getDisrtrictsPlacedTest() {
        assertEquals(botManager.getListBot().get(0).getDistrictPlaced(), botView.getDistrictsPlaced());
    }

    @Test
    void getIdTest() {
        assertEquals(0, botView.getID());
    }

}