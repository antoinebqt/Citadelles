package citadelles.bots;

import citadelles.BotView;
import citadelles.GameView;
import citadelles.Hand;
import citadelles.characters.*;
import citadelles.characters.Character;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import citadelles.districts.Keep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static citadelles.characters.CharacterEnum.ASSASSIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MediumBotTest {
    private MediumBot bot;
    GameView gameView;

    @Mock
    GameView gameViewMock;

    @BeforeEach
    void setUp() {
        DistrictManager districtManager = new DistrictManager();
        bot = new MediumBot(0, new Hand(districtManager));
        BotManager botManager = new BotManager(0,1,0,0,districtManager);
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
    void logicWarlordTestEmptyListOfBotView(){
        GameView lwGameView = mock(GameView.class);
        when(lwGameView.getListAllBots()).thenReturn(new ArrayList<>());

        assertNull(bot.logicWarlord());
    }

    @Test
    void logicWarlordTestMaxCityWith0golds(){
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand playerHand = new Hand(dm);

        Bot bot0 = new Bot(0, playerHand);
        Bot bot1 = new Bot(1, playerHand);
        Bot botWarlord = new MediumBot(2, playerHand);

        bot0.setCharacter(new Assassin());
        bot1.setCharacter(new King());
        botWarlord.setCharacter(new Warlord());

        ArrayList<District> districtsPlacedDefault = new ArrayList<>();
        districtsPlacedDefault.add(new District("Test", 3, 1));
        districtsPlacedDefault.add(new District("Test", 4, 1));
        districtsPlacedDefault.add(new District("Test", 5, 1));

        ArrayList<District> districtsPlacedTarget = new ArrayList<>();
        districtsPlacedDefault.add(new District("Test", 4, 1));
        districtsPlacedDefault.add(new District("Test", 5, 1));
        districtsPlacedDefault.add(new District("Test", 5, 1));
        districtsPlacedDefault.add(new District("Test", 6, 1));

        bot0.setDistrictPlaced(districtsPlacedDefault);
        botWarlord.setDistrictPlaced(districtsPlacedDefault);

        bot1.setDistrictPlaced(districtsPlacedTarget);

        botWarlord.setGold(0);

        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(bot0);
        botList.add(bot1);
        botList.add(botWarlord);

        BotManager bm = mock(BotManager.class);
        when(bm.getListBot()).thenReturn(botList);
        GameView lwGameView = new GameView(bm, new CharacterManager(3));

        botWarlord.setGameView(lwGameView);
        assertNull(botWarlord.logicWarlord());
    }

    @Test
    void logicWarlordTestMaxCityWith3golds(){
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand playerHand = new Hand(dm);

        Bot bot0 = new Bot(0, playerHand);
        Bot bot1 = new Bot(1, playerHand);
        Bot botWarlord = new MediumBot(2, playerHand);

        bot0.setCharacter(new Assassin());
        bot1.setCharacter(new King());
        botWarlord.setCharacter(new Warlord());

        ArrayList<District> districtsPlacedDefault = new ArrayList<>();
        districtsPlacedDefault.add(new District("Test", 3, 1));
        districtsPlacedDefault.add(new District("Test", 4, 1));
        districtsPlacedDefault.add(new District("Test", 5, 1));

        ArrayList<District> districtsPlacedTarget = new ArrayList<>();
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new District("Test", 4, 1));
        districtsPlacedTarget.add(new District("Test", 6, 1));

        bot0.setDistrictPlaced(districtsPlacedDefault);
        botWarlord.setDistrictPlaced(districtsPlacedDefault);

        bot1.setDistrictPlaced(districtsPlacedTarget);

        botWarlord.setGold(3);

        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(bot0);
        botList.add(bot1);
        botList.add(botWarlord);

        BotManager bm = mock(BotManager.class);
        when(bm.getListBot()).thenReturn(botList);
        GameView lwGameView = new GameView(bm, new CharacterManager(3));
        lwGameView.updateCharacterList();
        lwGameView.updateView();

        botWarlord.setGameView(lwGameView);
        int[] dataWarlordTest = botWarlord.logicWarlord();
        assertNotNull(dataWarlordTest);
        assertEquals(1, dataWarlordTest[0]);
        assertEquals(2, dataWarlordTest[1]);
    }

    @Test
    void logicWarlordTestMaxCityWith10golds(){
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand playerHand = new Hand(dm);

        Bot bot0 = new Bot(0, playerHand);
        Bot bot1 = new Bot(1, playerHand);
        Bot botWarlord = new MediumBot(2, playerHand);

        bot0.setCharacter(new Assassin());
        bot1.setCharacter(new King());
        botWarlord.setCharacter(new Warlord());

        ArrayList<District> districtsPlacedDefault = new ArrayList<>();
        districtsPlacedDefault.add(new District("Test", 3, 1));
        districtsPlacedDefault.add(new District("Test", 4, 1));
        districtsPlacedDefault.add(new District("Test", 5, 1));

        ArrayList<District> districtsPlacedTarget = new ArrayList<>();
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new District("Test", 4, 1));
        districtsPlacedTarget.add(new District("Test", 6, 1));

        bot0.setDistrictPlaced(districtsPlacedDefault);
        botWarlord.setDistrictPlaced(districtsPlacedDefault);

        bot1.setDistrictPlaced(districtsPlacedTarget);

        botWarlord.setGold(10);

        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(bot0);
        botList.add(bot1);
        botList.add(botWarlord);

        BotManager bm = mock(BotManager.class);
        when(bm.getListBot()).thenReturn(botList);
        GameView lwGameView = new GameView(bm, new CharacterManager(3));
        lwGameView.updateCharacterList();
        lwGameView.updateView();

        botWarlord.setGameView(lwGameView);
        int[] dataWarlordTest = botWarlord.logicWarlord();
        assertNotNull(dataWarlordTest);
        assertEquals(1, dataWarlordTest[0]);
        assertEquals(0, dataWarlordTest[1]);
    }

    @Test
    void logicWarlordTestWarlordMAxCityWith10golds(){
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand playerHand = new Hand(dm);

        Bot bot0 = new Bot(0, playerHand);
        Bot bot1 = new Bot(1, playerHand);
        Bot botWarlord = new MediumBot(2, playerHand);

        bot0.setCharacter(new Assassin());
        bot1.setCharacter(new King());
        botWarlord.setCharacter(new Warlord());

        ArrayList<District> districtsPlacedDefault = new ArrayList<>();
        districtsPlacedDefault.add(new District("Test", 3, 1));
        districtsPlacedDefault.add(new District("Test", 4, 1));
        districtsPlacedDefault.add(new District("Test", 5, 1));

        ArrayList<District> districtsPlacedTarget = new ArrayList<>();
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new District("Test", 4, 1));
        districtsPlacedTarget.add(new District("Test", 6, 1));

        bot0.setDistrictPlaced(districtsPlacedDefault);
        bot1.setDistrictPlaced(districtsPlacedDefault);

        botWarlord.setDistrictPlaced(districtsPlacedTarget);

        botWarlord.setGold(10);

        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(bot0);
        botList.add(bot1);
        botList.add(botWarlord);

        BotManager bm = mock(BotManager.class);
        when(bm.getListBot()).thenReturn(botList);
        GameView lwGameView = new GameView(bm, new CharacterManager(3));
        lwGameView.updateCharacterList();
        lwGameView.updateView();

        botWarlord.setGameView(lwGameView);
        int[] dataWarlordTest = botWarlord.logicWarlord();
        assertNotNull(dataWarlordTest);
        assertEquals(0, dataWarlordTest[0]);
        assertEquals(0, dataWarlordTest[1]);
    }

    @Test
    void logicWarlordTestBishopMaxCityWith10golds(){
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand playerHand = new Hand(dm);

        Bot bot0 = new Bot(0, playerHand);
        Bot bot1 = new Bot(1, playerHand);
        Bot botWarlord = new MediumBot(2, playerHand);

        bot0.setCharacter(new Assassin());
        bot1.setCharacter(new Bishop());
        botWarlord.setCharacter(new Warlord());

        ArrayList<District> districtsPlacedDefault = new ArrayList<>();
        districtsPlacedDefault.add(new District("Test", 3, 1));
        districtsPlacedDefault.add(new District("Test", 4, 1));
        districtsPlacedDefault.add(new District("Test", 5, 1));

        ArrayList<District> districtsPlacedTarget = new ArrayList<>();
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new District("Test", 4, 1));
        districtsPlacedTarget.add(new District("Test", 6, 1));

        bot0.setDistrictPlaced(districtsPlacedDefault);
        botWarlord.setDistrictPlaced(districtsPlacedDefault);

        bot1.setDistrictPlaced(districtsPlacedTarget);

        botWarlord.setGold(10);

        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(bot0);
        botList.add(bot1);
        botList.add(botWarlord);

        BotManager bm = mock(BotManager.class);
        when(bm.getListBot()).thenReturn(botList);
        GameView lwGameView = new GameView(bm, new CharacterManager(3));
        lwGameView.updateCharacterList();
        lwGameView.updateView();

        botWarlord.setGameView(lwGameView);
        int[] dataWarlordTest = botWarlord.logicWarlord();
        assertNotNull(dataWarlordTest);
        assertEquals(0, dataWarlordTest[0]);
        assertEquals(0, dataWarlordTest[1]);
    }

    @Test
    void logicWarlordTestMaxCityWith10goldsAndKeepMinValue(){
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand playerHand = new Hand(dm);

        Bot bot0 = new Bot(0, playerHand);
        Bot bot1 = new Bot(1, playerHand);
        Bot botWarlord = new MediumBot(2, playerHand);

        bot0.setCharacter(new Assassin());
        bot1.setCharacter(new King());
        botWarlord.setCharacter(new Warlord());

        ArrayList<District> districtsPlacedDefault = new ArrayList<>();
        districtsPlacedDefault.add(new District("Test", 3, 1));
        districtsPlacedDefault.add(new District("Test", 4, 1));
        districtsPlacedDefault.add(new District("Test", 5, 1));

        ArrayList<District> districtsPlacedTarget = new ArrayList<>();
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new District("Test", 5, 1));
        districtsPlacedTarget.add(new Keep());
        districtsPlacedTarget.add(new District("Test", 6, 1));

        bot0.setDistrictPlaced(districtsPlacedDefault);
        botWarlord.setDistrictPlaced(districtsPlacedDefault);

        bot1.setDistrictPlaced(districtsPlacedTarget);

        botWarlord.setGold(10);

        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(bot0);
        botList.add(bot1);
        botList.add(botWarlord);

        BotManager bm = mock(BotManager.class);
        when(bm.getListBot()).thenReturn(botList);
        GameView lwGameView = new GameView(bm, new CharacterManager(4));
        lwGameView.updateCharacterList();
        lwGameView.updateView();

        botWarlord.setGameView(lwGameView);
        int[] dataWarlordTest = botWarlord.logicWarlord();
        assertNotNull(dataWarlordTest);
        assertEquals(1, dataWarlordTest[0]);
        assertEquals(0, dataWarlordTest[1]);
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

        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 3, 0));
        handTest.add(new District("District 2", 2, 0));
        handTest.add(new District("District 3", 4, 0));
        bot.setHandList(handTest);

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
    void logicLaboratoryTest() {
        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 4, 0));
        handTest.add(new District("District 2", 2, 0));
        bot.setHandList(handTest);
        assertTrue(bot.logicLaboratory() != -1);
    }

    @Test
    void logicLaboratoryTestWithNoCardUnder3() {
        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 4, 0));
        handTest.add(new District("District 2", 3, 0));
        bot.setHandList(handTest);

        assertEquals(-1, bot.logicLaboratory());
    }

    @Test
    void logicLaboratoryTestWithNoCard() {
        ArrayList<District> emptyList = new ArrayList<>();
        bot.setHandList(emptyList);
        assertEquals(-1, bot.logicLaboratory());
    }

    @Test
    void logicFactoryTestWithLessThan7Golds() {
        bot.setGold(5);
        assertFalse(bot.logicFactory());
    }

    @Test
    void logicFactoryTestWithMoreThan7Golds() {
        bot.setGold(8);
        assertTrue(bot.logicFactory());
    }

    @Test
    void logicGraveyardTest() {
        assertTrue(bot.logicGraveyard(new District("Random District", 0, 0)));
    }

    @Test
    void logicGraveyardTestWithADistrictAlreadyPlaced() {

        ArrayList<District> districtPlaced = new ArrayList<>();
        districtPlaced.add(new District("District 1", 0, 0));
        bot.setDistrictPlaced(districtPlaced);

        assertFalse(bot.logicGraveyard(new District("District 1", 0, 0)));
    }

    @Test
    void toStringTest(){
        assertEquals("(Medium) -->",bot.toString());
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

        assertEquals(d1, bot.chooseOneCard(districtsList));
    }

    @Test
    void chooseOneCharacterTestNoGoldCharacter() {
        Character noGoldChar = new Magician();
        ArrayList<Character> characterArrayList = new ArrayList<>();
        characterArrayList.add(noGoldChar);
        characterArrayList.add(new Assassin());
        characterArrayList.add(new Thief());

        assertEquals(noGoldChar, bot.chooseOneCharacter(characterArrayList));
    }

    @Test
    void chooseOneCharacterTest1GoldCharacter() {
        Character goldChar = new King();
        ArrayList<Character> characterArrayList = new ArrayList<>();
        characterArrayList.add(new Assassin());
        characterArrayList.add(new Magician());
        characterArrayList.add(new Thief());
        characterArrayList.add(goldChar);

        assertEquals(goldChar, bot.chooseOneCharacter(characterArrayList));
    }

    @Test
    void chooseOneCharacterTestOnlyGoldCharacter() {
        Character goldChar1 = new Bishop();
        ArrayList<Character> characterArrayList = new ArrayList<>();
        characterArrayList.add(goldChar1);
        characterArrayList.add(new King());
        characterArrayList.add(new Warlord());
        characterArrayList.add(new Merchant());

        assertEquals(goldChar1, bot.chooseOneCharacter(characterArrayList));
    }
}