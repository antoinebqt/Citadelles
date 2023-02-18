package citadelles.bots;

import citadelles.BotView;
import citadelles.GameView;
import citadelles.Hand;
import citadelles.characters.*;
import citadelles.characters.Character;
import citadelles.districts.CategoryEnum;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import citadelles.districts.Keep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static citadelles.characters.CharacterEnum.ASSASSIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SmartBotTest {
    private SmartBot bot;
    GameView gameView;

    @Mock
    GameView gameViewMock;

    @BeforeEach
    void setUp() {
        DistrictManager districtManager = new DistrictManager();
        bot = new SmartBot(0, new Hand(districtManager));
        BotManager botManager = new BotManager(1,0,0,0,districtManager);
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
    void chooseCardTest0GoldsAnd0CardsAndNoArchitect(){
        bot.setGold(0);
        ArrayList<District> districtsArrayList = new ArrayList<>();
        bot.setHandList(districtsArrayList);
        bot.setCharacter(new Assassin());

        bot.chooseCardOrGold();
        assertEquals(0,bot.getGold());
        assertEquals(1,bot.getHandSize());
    }

    @Test
    void chooseCardTest0GoldsAnd0CardsAndArchitect(){
        bot.setGold(0);
        ArrayList<District> districtsArrayList = new ArrayList<>();
        bot.setHandList(districtsArrayList);
        bot.setCharacter(new Architect());
        bot.chooseCardOrGold();
        assertEquals(2,bot.getGold());
        assertEquals(0,bot.getHandSize());
    }

    @Test
    void chooseCardTest6GoldsAnd0CardsAndNoArchitect(){
        bot.setGold(6);
        ArrayList<District> districtsArrayList = new ArrayList<>();
        bot.setHandList(districtsArrayList);
        bot.setCharacter(new Assassin());
        bot.chooseCardOrGold();
        assertEquals(6,bot.getGold());
        assertEquals(1,bot.getHandSize());
    }

    @Test
    void chooseCardTest0GoldsAnd3CardsAndNoArchitect(){
        bot.setGold(0);
        ArrayList<District> districtsArrayList = new ArrayList<>();
        districtsArrayList.add(new District("Test", 1, 1));
        districtsArrayList.add(new District("Test", 1, 1));
        districtsArrayList.add(new District("Test", 1, 1));
        bot.setHandList(districtsArrayList);
        bot.setCharacter(new Assassin());

        bot.chooseCardOrGold();
        assertEquals(2,bot.getGold());
        assertEquals(3,bot.getHandSize());
    }

    @Test
    void chooseCardTest6GoldsAnd3CardsAndNoArchitect(){
        bot.setGold(6);
        ArrayList<District> districtsArrayList = new ArrayList<>();
        districtsArrayList.add(new District("Test", 1, 1));
        districtsArrayList.add(new District("Test", 1, 1));
        districtsArrayList.add(new District("Test", 1, 1));
        bot.setHandList(districtsArrayList);
        bot.setCharacter(new Assassin());

        bot.chooseCardOrGold();
        assertEquals(8,bot.getGold());
        assertEquals(3,bot.getHandSize());
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
        Bot botWarlord = new SmartBot(2, playerHand);

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
        Bot botWarlord = new SmartBot(2, playerHand);

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
        Bot botWarlord = new SmartBot(2, playerHand);

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
        assertEquals(2, dataWarlordTest[1]);
    }

    @Test
    void logicWarlordTestWarlordMAxCityWith10golds(){
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand playerHand = new Hand(dm);

        Bot bot0 = new Bot(0, playerHand);
        Bot bot1 = new Bot(1, playerHand);
        Bot botWarlord = new SmartBot(2, playerHand);

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
        Bot botWarlord = new SmartBot(2, playerHand);

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
        Bot botWarlord = new SmartBot(2, playerHand);

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
        bot.setGold(20);

        ArrayList<District> districtToPlace = bot.logicArchitect();

        assertEquals(2,districtToPlace.size());
    }

    @Test
    void logicArchitectTestWithOnlyAUniqueDistrict(){
        bot.setGold(20);

        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("Random Unique District", 5, CategoryEnum.UNIQUE.getValue()));
        bot.setHandList(handTest);

        ArrayList<District> districtToPlace = bot.logicArchitect();

        assertEquals(1,districtToPlace.size());
    }

    @Test
    void logicArchitectTestWithOnlyAUniqueDistrictButTooExpensive(){
        bot.setGold(2);

        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("Random Unique District", 5, CategoryEnum.UNIQUE.getValue()));
        bot.setHandList(handTest);

        ArrayList<District> districtToPlace = bot.logicArchitect();

        assertEquals(0,districtToPlace.size());
    }

    @Test
    void logicArchitectTestWithOnlyAUniqueDistrictAndOtherCards(){
        bot.setGold(20);

        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("Random Unique District", 5, CategoryEnum.UNIQUE.getValue()));
        handTest.add(new District("Random District 1", 5, 0));
        handTest.add(new District("Random District 2", 4, 0));
        bot.setHandList(handTest);

        ArrayList<District> districtToPlace = bot.logicArchitect();

        assertEquals(2,districtToPlace.size());
    }

    @Test
    void logicArchitectTestWithNoCard(){

        ArrayList<District> emptyHandTest = new ArrayList<>();
        bot.setHandList(emptyHandTest);

        ArrayList<District> districtToPlace = bot.logicArchitect();

        assertEquals(0,districtToPlace.size());
    }

    @Test
    void logicLaboratoryTest(){
        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 2, 0));
        handTest.add(new District("District 2", 5, 0));
        bot.setHandList(handTest);
        assertTrue(bot.logicLaboratory() != -1);
    }

    @Test
    void logicLaboratoryTestWithMoreThan3gold(){
        bot.setGold(5);
        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 2, 0));
        handTest.add(new District("District 2", 5, 0));
        bot.setHandList(handTest);
        assertEquals(-1, bot.logicLaboratory());
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
    void logicFactoryTestWithMoreThan7GoldAnd2Cards() {
        bot.setGold(10);
        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 4, 0));
        handTest.add(new District("District 2", 3, 0));
        bot.setHandList(handTest);
        assertTrue(bot.logicFactory());
    }

    @Test
    void logicFactoryTestWithLessThan7Golds() {
        bot.setGold(5);
        assertFalse(bot.logicFactory());
    }

    @Test
    void logicFactoryTestWithMoreThan2Card() {
        bot.setGold(10);
        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 4, 0));
        handTest.add(new District("District 2", 3, 0));
        handTest.add(new District("District 3", 1, 0));
        bot.setHandList(handTest);
        assertFalse(bot.logicFactory());
    }

    @Test
    void logicGraveyardTestWithADistrictOf3Value() {
        assertFalse(bot.logicGraveyard(new District("Random District", 3,0)));
    }

    @Test
    void logicGraveyardTestWithAUniqueDistrict() {
        assertTrue(bot.logicGraveyard(new District("Random Unique District", 0, CategoryEnum.UNIQUE.getValue())));
    }

    @Test
    void logicGraveyardTestWithADistrictOf2Value() {
        assertFalse(bot.logicGraveyard(new District("District 1", 2, 0)));
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
        assertEquals("(Smart) --->",bot.toString());
    }

    @Test
    void discardListTest(){
        ArrayList<District> discards = new ArrayList<>();
        ArrayList<District> placed = new ArrayList<>();
        ArrayList<District> hand = new ArrayList<>();

        District d1 = new District("random1",10,10);
        District d2 = new District("random2",10,10);
        District d3 = new District("random3",10,10);
        District d4 = new District("random4",10,10);

        placed.add(d1);
        hand.add(d1);
        discards.add(d1);

        placed.add(d2);

        placed.add(d3);
        hand.add(d3);
        discards.add(d3);

        hand.add(d4);

        bot.setDistrictPlaced(placed);
        bot.setHandList(hand);

        assertEquals(discards,bot.discardList());
    }

    @Test
    void chooseOneCardWithD1() {
        ArrayList<District> districtsList = new ArrayList<>();
        District d1 = new District("d1", 1, 1);
        District d2 = new District("d2", 1, 1);
        districtsList.add(d1);
        districtsList.add(d2);

        bot.getHandList().add(d1);

        assertEquals(d2, bot.chooseOneCard(districtsList));
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
    void chooseOneCharacterWithFourMilitaryDistrictPlaced() {
        ArrayList<District> handList = new ArrayList<>();
        handList.add(new District("Test", 1, 1));
        bot.setHandList(handList);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test4", 1, CategoryEnum.MILITARY.getValue()));
        districtsPlaced.add(new District("Test3", 1, CategoryEnum.MILITARY.getValue()));
        districtsPlaced.add(new District("Test2", 1, CategoryEnum.MILITARY.getValue()));
        districtsPlaced.add(new District("Test1", 1, CategoryEnum.MILITARY.getValue()));

        bot.setDistrictPlaced(districtsPlaced);

        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Architect());
        charactersPicked.add(new Warlord());
        charactersPicked.add(new King());
        charactersPicked.add(new Merchant());
        charactersPicked.add(new Bishop());
        charactersPicked.add(new Magician());
        charactersPicked.add(new Assassin());
        charactersPicked.add(new Thief());

        Character characterChosen = bot.chooseOneCharacter(charactersPicked);

        assertEquals(new Warlord().getId(), characterChosen.getId());
    }

    @Test
    void chooseOneCharacterWithNoDistrictInPlayerHand() {
        ArrayList<District> handList = new ArrayList<>();
        bot.setHandList(handList);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.MILITARY.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.MILITARY.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.MILITARY.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.NOBLE.getValue()));

        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Architect());
        charactersPicked.add(new Warlord());
        charactersPicked.add(new King());
        charactersPicked.add(new Merchant());
        charactersPicked.add(new Bishop());

        bot.setDistrictPlaced(districtsPlaced);
        Character characterChosen = bot.chooseOneCharacter(charactersPicked);

        assertEquals(new Architect().getId(), characterChosen.getId());
    }

    @Test
    void chooseOneCharacterWithCardAndThreeTradeDistrictPlaced() {
        ArrayList<District> handList = new ArrayList<>();
        handList.add(new District("Test1", 1, 1));
        handList.add(new District("Test2", 1, 1));
        bot.setHandList(handList);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.MILITARY.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.NOBLE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));

        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Architect());
        charactersPicked.add(new Warlord());
        charactersPicked.add(new King());
        charactersPicked.add(new Merchant());
        charactersPicked.add(new Bishop());

        bot.setDistrictPlaced(districtsPlaced);
        Character characterChosen = bot.chooseOneCharacter(charactersPicked);

        assertEquals(new Merchant().getId(), characterChosen.getId());
    }

    @Test
    void chooseOneCharacterWithCardAndThreeTradeDistrictPlacedAndNoGoldCharacter() {
        ArrayList<District> handList = new ArrayList<>();
        handList.add(new District("Test1", 1, 1));
        bot.setHandList(handList);

        bot.setGold(0);
        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.MILITARY.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.NOBLE.getValue()));

        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Architect());
        charactersPicked.add(new Assassin());
        charactersPicked.add(new Magician());
        charactersPicked.add(new Thief());

        bot.setDistrictPlaced(districtsPlaced);
        Character characterChosen = bot.chooseOneCharacter(charactersPicked);

        assertEquals(new Thief().getId(), characterChosen.getId());
    }
}