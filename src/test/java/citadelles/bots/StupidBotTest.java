package citadelles.bots;

import citadelles.BotView;
import citadelles.GameView;
import citadelles.Hand;
import citadelles.characters.Character;
import citadelles.characters.CharacterManager;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static citadelles.characters.CharacterEnum.ASSASSIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StupidBotTest {

    private StupidBot bot;
    GameView gameView;

    @Mock
    GameView gameViewMock;

    @BeforeEach
    void setUp() {
        DistrictManager districtManager = new DistrictManager();
        bot = new StupidBot(0, new Hand(districtManager));
        BotManager botManager = new BotManager(0,0,1,0,districtManager);
        CharacterManager characterManager = new CharacterManager(1);
        characterManager.init();
        bot.setCharacter(characterManager.getCharacters().get(5));
        gameView = new GameView(botManager, characterManager);

        gameViewMock = mock(GameView.class);
        BotView bv = mock(BotView.class);
        ArrayList<District> districtList = new ArrayList<>();
        districtList.add(new District("O310",10,10));
        when(bv.getDistrictsPlaced()).thenReturn(districtList);

        ArrayList<BotView> listView = new ArrayList<>();
        listView.add(bv);
        when(gameViewMock.getListAllBots()).thenReturn(listView);

        ArrayList<Character> charList = new ArrayList<>(characterManager.getCharacters());
        when(gameViewMock.getListCharacters()).thenReturn(charList);

        bot.setGameView(gameView);
    }

    @Test
    void chooseCardTest(){
        int initGold = bot.getGold();
        bot.chooseCardOrGold();
        assertEquals(initGold+2,bot.getGold());

        bot.getHand().getListCard().clear();
        bot.chooseCardOrGold();
        assertEquals(1,bot.getHandSize());
    }

    @Test
    void eventuallyPlaceADistrictTest(){
        ArrayList<District> newHand = new ArrayList<>();
        newHand.add(new District("O310",1,1));
        bot.setHandList(newHand);

        assertEquals(0,bot.getNumberDistrictPlaced());

        bot.eventuallyPlaceADistrict();
        assertEquals(1,bot.getNumberDistrictPlaced());
        bot.eventuallyPlaceADistrict();
        assertEquals(1,bot.getNumberDistrictPlaced());
    }

    @Test
    void logicAssassinTest(){
        int id = bot.logicAssassin();
        assertTrue(id >= 0 && id < 8);
        assertTrue(id!= ASSASSIN.getId());
    }

    @Test
    void logicWarlordTest(){
        bot.setGameView(gameView);
        int[] values = bot.logicWarlord();
        assertNull(values);

        bot.setGameView(gameViewMock);
        bot.setGold(100);
        values = bot.logicWarlord();

        assertNotNull(values);
        assertTrue(values[0]>=0 && values[0]<8);
        assertTrue(values[1]>=0);
    }

    @Test
    void logicThiefTest(){

        bot.setGameView(gameView);
        int id = bot.logicThief();
        assertTrue(id >= 0 && id < 8);
    }

    @Test
    void logicMagicianTest(){
        int id = bot.logicMagician();
        assertTrue(id>=-1);
    }

    @Test
    void logicArchitectTest(){

        ArrayList<District> districtToPlace = bot.logicArchitect();

        assertEquals(2, districtToPlace.size());
    }

    @Test
    void logicArchitectTestWithNoCard(){

        ArrayList<District> handTest = new ArrayList<>();
        bot.setHandList(handTest);

        ArrayList<District> districtToPlace = bot.logicArchitect();

        assertEquals(0, districtToPlace.size());
    }

    @Test
    void logicArchitectTestWith1Card(){

        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 2, 0));
        bot.setHandList(handTest);

        ArrayList<District> districtToPlace = bot.logicArchitect();

        assertEquals(1, districtToPlace.size());
    }

    @Test
    void logicLaboratoryTest(){
        assertEquals(1, bot.logicLaboratory());
    }

    @Test
    void logicLaboratoryTestWithNoCard() {
        ArrayList<District> emptyList = new ArrayList<>();

        bot.setHandList(emptyList);

        assertEquals(-1,bot.logicLaboratory());
    }

    @Test
    void logicFactoryTest() {
        assertTrue(bot.logicFactory());
    }

    @Test
    void logicGraveyardTest() {
        assertTrue(bot.logicGraveyard(new District("Random District",0,0)));
    }

    @Test
    void toStringTest(){
        assertEquals("(Stupid) -->",bot.toString());
    }

    @Test
    void discardListTest(){
        assertEquals(bot.getHand().getListCard(), bot.discardList());
    }

    @Test
    void chooseOneCardWithD1() {
        ArrayList<District> districtsList = new ArrayList<>();
        District d1 = new District("d1", 1, 1);
        District d2 = new District("d2", 1, 1);
        districtsList.add(d1);
        districtsList.add(d2);

        bot.getHandList().add(d1);

        assertEquals(d1, bot.chooseOneCard(districtsList));
    }

    @Test
    void chooseOneCardWIthD2() {
        ArrayList<District> districtsList = new ArrayList<>();
        District d1 = new District("d1", 1, 1);
        District d2 = new District("d2", 1, 1);
        districtsList.add(d1);
        districtsList.add(d2);

        bot.getHandList().add(d2);

        assertEquals(d2, bot.chooseOneCard(districtsList));
    }

}