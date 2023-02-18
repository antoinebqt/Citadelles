package citadelles;

import citadelles.bots.Bot;
import citadelles.bots.BotManager;
import citadelles.characters.CharacterEnum;
import citadelles.characters.CharacterManager;
import citadelles.characters.King;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameViewTest {

    GameView gameView;
    BotManager botManager;
    DistrictManager districtManager;

    @BeforeEach
    void setUp() {
        districtManager = new DistrictManager();
        botManager = new BotManager(1, 1, 1, 0,districtManager);
        gameView = new GameView(botManager, new CharacterManager(3));
    }

    @Test
    void getDeadBotTest() {
        int botID = 0;
        botManager.getListBot().get(botID).setDead();
        assertEquals(botID, gameView.getDeadBot());
        botManager.getListBot().get(botID).setAlive();

        botID = 2;
        botManager.getListBot().get(botID).setDead();
        assertEquals(botID, gameView.getDeadBot());
    }

    @Test
    void updateView() {
        Bot bot1 = botManager.getListBot().get(1);
        ArrayList<District> l = new ArrayList<>();
        District d = new District("random", 1, 1);
        l.add(d);
        bot1.setDistrictPlaced(l);

        assertEquals(0, gameView.getListAllBots().get(1).getDistrictsPlaced().size());

        gameView.updateView();

        assertEquals(d, gameView.getListAllBots().get(1).getDistrictsPlaced().get(0));
    }

    @Test
    void updateCharacterTest() {
        gameView.updateCharacterList();
        Bot bot1 = botManager.getListBot().get(1);
        bot1.setCharacter(new King());

        assertNull(gameView.getListCharacters().get(1));
        gameView.updateView();
        gameView.updateCharacterList();
        assertEquals(CharacterEnum.KING.getRole(), gameView.getListCharacters().get(1).getRole());
    }

    @Test
    void getSizeBiggerCityTest() {
        BotView maxCityBot = mock(BotView.class);
        when(maxCityBot.getDistrictsPlacedSize()).thenReturn(10);

        BotView normalCityBot = mock(BotView.class);
        when(normalCityBot.getDistrictsPlacedSize()).thenReturn(3);

        ArrayList<BotView> botViewArrayList = new ArrayList<>();
        botViewArrayList.add(normalCityBot);
        botViewArrayList.add(normalCityBot);
        botViewArrayList.add(maxCityBot);
        botViewArrayList.add(normalCityBot);
        botViewArrayList.add(normalCityBot);

        gameView.setListCharacters(botViewArrayList);

        assertEquals(10, gameView.getSizeBiggerCity());
    }
}