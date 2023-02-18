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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RichardBotTest {
    RichardBot richardBot, bot;
    Hand hand;
    ArrayList<Character> listAllCharacters;

    @BeforeEach
    void setUp() {
        hand = mock(Hand.class);
        richardBot = new RichardBot(0, hand);

        DistrictManager districtManager = new DistrictManager();
        bot = new RichardBot(0, new Hand(districtManager));

        CharacterManager cm = new CharacterManager(4);
        listAllCharacters = cm.initList();
    }

    @Test
    void eventuallyPlaceADistrictTest1CardAndEnoughGold() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(2);
        richardBot.setGameView(gv);

        District districtToPlace = new District("Test", 10, 1);
        ArrayList<District> districts = new ArrayList<>();
        districts.add(districtToPlace);
        when(hand.getListCard()).thenReturn(districts);

        richardBot.setGold(10);

        richardBot.eventuallyPlaceADistrict();
        assertTrue(richardBot.getDistrictPlaced().contains(districtToPlace));
        assertFalse(districts.contains(districtToPlace));
        assertEquals(0, richardBot.getGold());
    }

    @Test
    void eventuallyPlaceADistrictTest1CardAndNotEnoughGold() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(2);
        richardBot.setGameView(gv);

        District districtToPlace = new District("Test", 10, 1);
        ArrayList<District> districts = new ArrayList<>();
        districts.add(districtToPlace);
        when(hand.getListCard()).thenReturn(districts);

        richardBot.setGold(9);

        richardBot.eventuallyPlaceADistrict();
        assertFalse(richardBot.getDistrictPlaced().contains(districtToPlace));
        assertTrue(districts.contains(districtToPlace));
        assertEquals(9, richardBot.getGold());
    }

    @Test
    void eventuallyPlaceADistrictTest2CardAndEnoughGoldAndNoEndGame() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(2);
        richardBot.setGameView(gv);

        District districtToPlace = new District("Test1", 5, 1);
        ArrayList<District> districts = new ArrayList<>();
        districts.add(districtToPlace);
        districts.add(new District("Test2", 10, 1));
        when(hand.getListCard()).thenReturn(districts);

        richardBot.setGold(10);

        richardBot.eventuallyPlaceADistrict();
        assertTrue(richardBot.getDistrictPlaced().contains(districtToPlace));
        assertFalse(districts.contains(districtToPlace));
        assertEquals(1, richardBot.getDistrictPlaced().size());
        assertEquals(5, richardBot.getGold());
    }

    @Test
    void eventuallyPlaceADistrictTest2CardAndEnoughGoldAndEndGame() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(6);
        richardBot.setGameView(gv);

        District districtToPlace = new District("Test1", 10, 1);
        ArrayList<District> districts = new ArrayList<>();
        districts.add(districtToPlace);
        districts.add(new District("Test2", 5, 1));
        when(hand.getListCard()).thenReturn(districts);

        richardBot.setGold(10);

        richardBot.eventuallyPlaceADistrict();
        assertTrue(richardBot.getDistrictPlaced().contains(districtToPlace));
        assertFalse(districts.contains(districtToPlace));
        assertEquals(1, richardBot.getDistrictPlaced().size());
        assertEquals(0, richardBot.getGold());
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
    void chooseOneCharacterTest6BiggerCitySizeWithAllChar() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(6);
        richardBot.setGameView(gameView);

        Character characterToChoose = new Assassin();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Warlord());
        charactersPicked.add(new King());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Bishop());
        charactersPicked.add(new Architect());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTest6BiggerCitySizeNoAssassin() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(6);
        richardBot.setGameView(gameView);

        Character characterToChoose = new Bishop();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Warlord());
        charactersPicked.add(new King());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Architect());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTest6BiggerCitySizeWithNoAssassinAndBishop() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(6);
        richardBot.setGameView(gameView);

        Character characterToChoose = new Warlord();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new King());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Architect());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTest6BiggerCitySizeWithNoAssassinAndBishopAndWarlord() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(6);
        richardBot.setGameView(gameView);

        Character characterToChoose = new Thief();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new King());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Architect());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTest5BiggerCitySizeForAnotherPlayerAllChar() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(5);
        richardBot.setGameView(gameView);

        Character characterToChoose = new King();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Warlord());
        charactersPicked.add(new Assassin());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Bishop());
        charactersPicked.add(new Architect());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTest5BiggerCitySizeForAnotherPlayerNoKing() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(5);
        richardBot.setGameView(gameView);

        Character characterToChoose = new Assassin();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Warlord());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Bishop());
        charactersPicked.add(new Architect());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTest5BiggerCitySizeForAnotherPlayerNoKingNoAssassin() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(5);
        richardBot.setGameView(gameView);

        Character characterToChoose = new Warlord();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Bishop());
        charactersPicked.add(new Architect());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTest5BiggerCitySizeForAnotherPlayerNoKingNoAssassinNoWarlord() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(5);
        richardBot.setGameView(gameView);

        Character characterToChoose = new Bishop();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Architect());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTest5BiggerCitySizeForAnotherPlayerNoKingNoAssassinNoWarlordNoBishop() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(5);
        richardBot.setGameView(gameView);

        Character characterToChoose = new Thief();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Architect());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTest5BiggerCitySizeForCurrentPlayer() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(5);
        richardBot.setGameView(gameView);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        richardBot.setDistrictPlaced(districtsPlaced);

        when(hand.getListCardSize()).thenReturn(1);
        richardBot.setGold(4);

        Character characterToChoose = new Architect();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Warlord());
        charactersPicked.add(new Assassin());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Bishop());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTestNotEndGameWithMaxReligious() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(3);
        richardBot.setGameView(gameView);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.RELIGIOUS.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.RELIGIOUS.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.MILITARY.getValue()));
        richardBot.setDistrictPlaced(districtsPlaced);

        when(hand.getListCardSize()).thenReturn(1);
        richardBot.setGold(4);

        Character characterToChoose = new Bishop();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Warlord());
        charactersPicked.add(new Assassin());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Architect());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTestNotEndGameWithMaxMilitary() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(3);
        richardBot.setGameView(gameView);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.RELIGIOUS.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.MILITARY.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.MILITARY.getValue()));
        richardBot.setDistrictPlaced(districtsPlaced);

        when(hand.getListCardSize()).thenReturn(1);
        richardBot.setGold(4);

        Character characterToChoose = new Warlord();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Bishop());
        charactersPicked.add(new Assassin());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Architect());
        charactersPicked.add(new Thief());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void chooseOneCharacterTestNotEndGameWithNoReligousOrMilitary() {
        GameView gameView = mock(GameView.class);
        when(gameView.getSizeBiggerCity()).thenReturn(3);
        richardBot.setGameView(gameView);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        richardBot.setDistrictPlaced(districtsPlaced);

        when(hand.getListCardSize()).thenReturn(1);
        richardBot.setGold(4);

        Character characterToChoose = new Thief();
        ArrayList<Character> charactersPicked = new ArrayList<>();
        charactersPicked.add(new Warlord());
        charactersPicked.add(new Assassin());
        charactersPicked.add(new Magician());
        charactersPicked.add(characterToChoose);
        charactersPicked.add(new Architect());
        charactersPicked.add(new Bishop());

        assertEquals(characterToChoose, richardBot.chooseOneCharacter(charactersPicked));
    }

    @Test
    void logicWarlordTestMaxCityWith0golds(){
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand playerHand = new Hand(dm);

        Bot bot0 = new Bot(0, playerHand);
        Bot bot1 = new Bot(1, playerHand);
        Bot botWarlord = new RichardBot(2, playerHand);

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
        Bot botWarlord = new RichardBot(2, playerHand);

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
        Bot botWarlord = new RichardBot(2, playerHand);

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
        Bot botWarlord = new RichardBot(2, playerHand);

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
        Bot botWarlord = new RichardBot(2, playerHand);

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
        Bot botWarlord = new RichardBot(2, playerHand);

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
    void logicArchitectTestNoHandDistricts(){
        ArrayList<District> handList = new ArrayList<>();
        when(hand.getListCard()).thenReturn(handList);

        richardBot.setGold(5);

        ArrayList<District> districtsChosen = richardBot.logicArchitect();
        assertEquals(0, districtsChosen.size());
    }

    @Test
    void logicArchitectTestEnoughGold(){
        District d1 = new District("Test1", 2, 1);
        District d2 = new District("Test2", 2, 1);
        District d3 = new District("Test3", 2, 1);
        District d4 = new District("Test4", 5, 1);
        ArrayList<District> handList = new ArrayList<>();
        handList.add(d1);
        handList.add(d4);
        handList.add(d2);
        handList.add(d3);
        when(hand.getListCard()).thenReturn(handList);

        richardBot.setGold(5);

        ArrayList<District> districtsChosen = richardBot.logicArchitect();
        assertEquals(2, districtsChosen.size());
        assertTrue(districtsChosen.contains(d1));
        assertTrue(districtsChosen.contains(d2));
        assertFalse(districtsChosen.contains(d3));
        assertFalse(districtsChosen.contains(d4));
    }

    @Test
    void logicArchitectTest2EnoughGoldAnd1DistrictAlreadyPlaced(){
        District d1 = new District("Test1", 2, 1);
        District d2 = new District("Test2", 2, 1);
        District d3 = new District("Test3", 2, 1);
        District d4 = new District("Test4", 5, 1);
        ArrayList<District> handList = new ArrayList<>();
        handList.add(d1);
        handList.add(d4);
        handList.add(d2);
        handList.add(d3);
        when(hand.getListCard()).thenReturn(handList);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(d1);
        richardBot.setDistrictPlaced(districtsPlaced);

        richardBot.setGold(5);

        ArrayList<District> districtsChosen = richardBot.logicArchitect();
        assertEquals(2, districtsChosen.size());
        assertFalse(districtsChosen.contains(d1));
        assertTrue(districtsChosen.contains(d2));
        assertTrue(districtsChosen.contains(d3));
        assertFalse(districtsChosen.contains(d4));
    }

    @Test
    void logicArchitectTest2Gold(){
        District d1 = new District("Test1", 2, 1);
        District d2 = new District("Test2", 2, 1);
        District d3 = new District("Test3", 2, 1);
        District d4 = new District("Test4", 5, 1);
        ArrayList<District> handList = new ArrayList<>();
        handList.add(d1);
        handList.add(d4);
        handList.add(d2);
        handList.add(d3);
        when(hand.getListCard()).thenReturn(handList);

        richardBot.setGold(2);

        ArrayList<District> districtsChosen = richardBot.logicArchitect();
        assertEquals(1, districtsChosen.size());
        assertTrue(districtsChosen.contains(d1));
        assertFalse(districtsChosen.contains(d2));
        assertFalse(districtsChosen.contains(d3));
        assertFalse(districtsChosen.contains(d4));
    }

    @Test
    void logicArchitectTest0Gold(){
        District d1 = new District("Test1", 2, 1);
        District d2 = new District("Test2", 2, 1);
        District d3 = new District("Test3", 2, 1);
        District d4 = new District("Test4", 5, 1);
        ArrayList<District> handList = new ArrayList<>();
        handList.add(d1);
        handList.add(d4);
        handList.add(d2);
        handList.add(d3);
        when(hand.getListCard()).thenReturn(handList);

        richardBot.setGold(0);

        ArrayList<District> districtsChosen = richardBot.logicArchitect();
        assertEquals(0, districtsChosen.size());
        assertFalse(districtsChosen.contains(d1));
        assertFalse(districtsChosen.contains(d2));
        assertFalse(districtsChosen.contains(d3));
        assertFalse(districtsChosen.contains(d4));
    }

    @Test
    void logicAssassinTestWithBiggestCityNoWarlordDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(4);
        richardBot.setGameView(gv);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Bishop());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        richardBot.setDistrictPlaced(districtsPlaced);

        assertEquals(CharacterEnum.WARLORD.getId(), richardBot.logicAssassin());
    }

    @RepeatedTest(10)
    void logicAssassinTestWithBiggestCityWarlordDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(4);
        richardBot.setGameView(gv);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Warlord());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        richardBot.setDistrictPlaced(districtsPlaced);

        assertNotEquals(CharacterEnum.WARLORD.getId(), richardBot.logicAssassin());
    }

    @Test
    void logicAssassinTest6BiggestCityNoBishopDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(6);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Thief());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        richardBot.setGameView(gv);

        assertEquals(CharacterEnum.BISHOP.getId(), richardBot.logicAssassin());
    }

    @Test
    void logicAssassinTest6BiggestCityBishopDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(6);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Bishop());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        richardBot.setGameView(gv);

        assertEquals(CharacterEnum.WARLORD.getId(), richardBot.logicAssassin());
    }

    @Test
    void logicAssassinTest5BiggestCityAnd4MaxGoldAndNoArchitectDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(5);
        when(gv.getDistrictOfMostRich()).thenReturn(5);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Bishop());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        when(gv.getMaxGold()).thenReturn(4);

        richardBot.setGameView(gv);

        assertEquals(CharacterEnum.ARCHITECT.getId(), richardBot.logicAssassin());
    }

    @RepeatedTest(10)
    void logicAssassinTest5BiggestCityAnd4MaxGoldAndArchitectDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(5);
        when(gv.getDistrictOfMostRich()).thenReturn(5);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Architect());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        when(gv.getMaxGold()).thenReturn(4);

        richardBot.setGameView(gv);

        assertNotEquals(CharacterEnum.ARCHITECT.getId(), richardBot.logicAssassin());
    }

    @Test
    void logicAssassinTest5BiggestCityAndNoKingDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(5);
        when(gv.getDistrictOfMostRich()).thenReturn(4);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Merchant());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        when(gv.getMaxGold()).thenReturn(2);

        richardBot.setGameView(gv);

        assertEquals(CharacterEnum.KING.getId(), richardBot.logicAssassin());
    }

    @RepeatedTest(10)
    void logicAssassinTest5BiggestCityAndKingDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(5);
        when(gv.getDistrictOfMostRich()).thenReturn(4);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new King());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        when(gv.getMaxGold()).thenReturn(2);

        richardBot.setGameView(gv);

        assertNotEquals(CharacterEnum.KING.getId(), richardBot.logicAssassin());
    }

    @Test
    void logicAssassinTest6MaxGoldAndNoThiefDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(4);
        when(gv.getDistrictOfMostRich()).thenReturn(4);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Merchant());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        when(gv.getMaxGold()).thenReturn(6);

        richardBot.setGameView(gv);

        assertEquals(CharacterEnum.THIEF.getId(), richardBot.logicAssassin());
    }

    @RepeatedTest(10)
    void logicAssassinTest6MaxGoldAndThiefDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(4);
        when(gv.getDistrictOfMostRich()).thenReturn(4);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Thief());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        when(gv.getMaxGold()).thenReturn(6);

        richardBot.setGameView(gv);

        assertNotEquals(CharacterEnum.THIEF.getId(), richardBot.logicAssassin());
    }

    @RepeatedTest(10)
    void logicAssassinTestRandomWithMerchantDiscarded() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        when(gv.getSizeBiggerCity()).thenReturn(4);
        when(gv.getDistrictOfMostRich()).thenReturn(4);

        ArrayList<Character> visibleDiscarded = new ArrayList<>();
        visibleDiscarded.add(new Merchant());
        when(gv.getVisibleDiscarded()).thenReturn(visibleDiscarded);

        when(gv.getMaxGold()).thenReturn(2);

        richardBot.setGameView(gv);

        assertNotEquals(CharacterEnum.MERCHANT.getId(), richardBot.logicAssassin());
    }

    @Test

    void logicThiefTest() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(listAllCharacters);
        ArrayList<Character> discardedList = new ArrayList<>();
        discardedList.add(new Merchant());
        when(gv.getVisibleDiscarded()).thenReturn(discardedList);

        richardBot.setGameView(gv);
        richardBot.setCharacter(new Thief());

        assertEquals(CharacterEnum.WARLORD.getId(), richardBot.logicThief());
    }

    @Test
    void logicThiefTestEmptyListOfCharacter() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(new ArrayList<>());
        ArrayList<Character> discardedList = new ArrayList<>();
        discardedList.add(new Merchant());
        when(gv.getVisibleDiscarded()).thenReturn(discardedList);

        richardBot.setGameView(gv);
        richardBot.setCharacter(new Thief());

        assertEquals(-1, richardBot.logicThief());
    }

    @Test
    void logicThiefTestDiscardedWarlord() {
        GameView gv = mock(GameView.class);
        when(gv.getListAllCharacters()).thenReturn(new ArrayList<>());
        ArrayList<Character> discardedList = new ArrayList<>();
        discardedList.add(new Warlord());
        when(gv.getVisibleDiscarded()).thenReturn(discardedList);

        richardBot.setGameView(gv);
        richardBot.setCharacter(new Thief());

        assertEquals(-1, richardBot.logicThief());
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
    void logicLaboratoryTestWithACardAlreadyPlaced() {
        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 5, 0));
        bot.setHandList(handTest);
        ArrayList<District> districtPlacedTest = new ArrayList<>();
        districtPlacedTest.add(new District("District 1", 5, 0));
        bot.setDistrictPlaced(districtPlacedTest);
        assertEquals(0, bot.logicLaboratory());
    }

    @Test
    void logicLaboratoryTestWithACardAlreadyPlacedAndAnotherWithAValueLessThan3() {
        ArrayList<District> handTest = new ArrayList<>();
        handTest.add(new District("District 1", 2, 0));
        handTest.add(new District("District 2", 5, 0));
        bot.setHandList(handTest);
        ArrayList<District> districtPlacedTest = new ArrayList<>();
        districtPlacedTest.add(new District("District 2", 5, 0));
        bot.setDistrictPlaced(districtPlacedTest);
        assertEquals(1, bot.logicLaboratory());
    }

    @Test
    void logicFactoryTestWithMoreThan7Gold() {
        bot.setGold(10);
        assertTrue(bot.logicFactory());
    }

    @Test
    void logicFactoryTestWithLessThan7Golds() {
        bot.setGold(5);
        assertFalse(bot.logicFactory());
    }

    @Test
    void logicFactoryTestWithLessThan4DistrictPlacedAndMoreThan7Gold() {
        bot.setGold(10);
        ArrayList<District> districtPlacedTest = new ArrayList<>();
        districtPlacedTest.add(new District("District 1", 4, 0));
        districtPlacedTest.add(new District("District 2", 3, 0));
        districtPlacedTest.add(new District("District 3", 1, 0));
        bot.setDistrictPlaced(districtPlacedTest);
        assertTrue(bot.logicFactory());
    }

    @Test
    void logicFactoryTestWithLessThan4DistrictPlacedAndLessThan7Gold() {
        bot.setGold(6);
        ArrayList<District> districtPlacedTest = new ArrayList<>();
        districtPlacedTest.add(new District("District 1", 4, 0));
        districtPlacedTest.add(new District("District 2", 3, 0));
        districtPlacedTest.add(new District("District 3", 1, 0));
        bot.setDistrictPlaced(districtPlacedTest);
        assertFalse(bot.logicFactory());
    }

    @Test
    void logicFactoryTestWithMoreThan4DistrictPlacedAndLessThan7Gold() {
        bot.setGold(6);
        ArrayList<District> districtPlacedTest = new ArrayList<>();
        districtPlacedTest.add(new District("District 1", 4, 0));
        districtPlacedTest.add(new District("District 2", 3, 0));
        districtPlacedTest.add(new District("District 3", 1, 0));
        districtPlacedTest.add(new District("District 4", 2, 0));
        districtPlacedTest.add(new District("District 5", 3, 0));
        bot.setDistrictPlaced(districtPlacedTest);
        assertFalse(bot.logicFactory());
    }

    @Test
    void logicFactoryTestWithMoreThan4DistrictPlacedAndMoreThan7Gold() {
        bot.setGold(10);
        ArrayList<District> districtPlacedTest = new ArrayList<>();
        districtPlacedTest.add(new District("District 1", 4, 0));
        districtPlacedTest.add(new District("District 2", 3, 0));
        districtPlacedTest.add(new District("District 3", 1, 0));
        districtPlacedTest.add(new District("District 4", 2, 0));
        districtPlacedTest.add(new District("District 5", 3, 0));
        bot.setDistrictPlaced(districtPlacedTest);
        assertFalse(bot.logicFactory());
    }

    @Test
    void logicGraveyardTestWithADistrictOf3Value() {
        assertTrue(bot.logicGraveyard(new District("Random District", 3,0)));
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
        assertEquals("(Richard) ->",bot.toString());
    }

    @Test
    void chooseOneCardTest() {
        District districtUnique = new District("unique", 1, CategoryEnum.UNIQUE.getValue());
        District districtReligious = new District("religious", 1, CategoryEnum.RELIGIOUS.getValue());
        District districtMilitary = new District("military", 1, CategoryEnum.MILITARY.getValue());
        District districtTrade = new District("trade", 1, CategoryEnum.TRADE.getValue());

        ArrayList<District> listOfChoice = new ArrayList<>();
        listOfChoice.add(districtTrade);
        listOfChoice.add(districtMilitary);
        listOfChoice.add(districtReligious);
        listOfChoice.add(districtUnique);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("test1", 1, 1));
        districtsPlaced.add(new District("test2", 1, 1));

        when(hand.getListCard()).thenReturn(new ArrayList<>());
        richardBot.setDistrictPlaced(districtsPlaced);

        assertEquals(districtUnique, richardBot.chooseOneCard(listOfChoice));
    }

    @Test
    void chooseOneCardTestUniqueOwned() {
        District districtUnique = new District("unique", 1, CategoryEnum.UNIQUE.getValue());
        District districtReligious = new District("religious", 1, CategoryEnum.RELIGIOUS.getValue());
        District districtMilitary = new District("military", 1, CategoryEnum.MILITARY.getValue());
        District districtTrade = new District("trade", 1, CategoryEnum.TRADE.getValue());

        ArrayList<District> listOfChoice = new ArrayList<>();
        listOfChoice.add(districtTrade);
        listOfChoice.add(districtMilitary);
        listOfChoice.add(districtReligious);
        listOfChoice.add(districtUnique);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("test1", 1, 1));
        districtsPlaced.add(new District("test2", 1, 1));
        districtsPlaced.add(districtUnique);

        when(hand.getListCard()).thenReturn(new ArrayList<>());
        richardBot.setDistrictPlaced(districtsPlaced);

        assertEquals(districtMilitary, richardBot.chooseOneCard(listOfChoice));
    }

    @Test
    void chooseOneCardTestUniqueAndMilitaryOwned() {
        District districtUnique = new District("unique", 1, CategoryEnum.UNIQUE.getValue());
        District districtReligious = new District("religious", 1, CategoryEnum.RELIGIOUS.getValue());
        District districtMilitary = new District("military", 1, CategoryEnum.MILITARY.getValue());
        District districtTrade = new District("trade", 1, CategoryEnum.TRADE.getValue());

        ArrayList<District> listOfChoice = new ArrayList<>();
        listOfChoice.add(districtTrade);
        listOfChoice.add(districtMilitary);
        listOfChoice.add(districtReligious);
        listOfChoice.add(districtUnique);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("test1", 1, 1));
        districtsPlaced.add(new District("test2", 1, 1));
        districtsPlaced.add(districtUnique);
        districtsPlaced.add(districtMilitary);

        when(hand.getListCard()).thenReturn(new ArrayList<>());
        richardBot.setDistrictPlaced(districtsPlaced);

        assertEquals(districtReligious, richardBot.chooseOneCard(listOfChoice));
    }

    @Test
    void chooseOneCardTestUniqueAndMilitaryAndReligiousOwned() {
        District districtUnique = new District("unique", 1, CategoryEnum.UNIQUE.getValue());
        District districtReligious = new District("religious", 1, CategoryEnum.RELIGIOUS.getValue());
        District districtMilitary = new District("military", 1, CategoryEnum.MILITARY.getValue());
        District districtTrade = new District("trade", 1, CategoryEnum.TRADE.getValue());

        ArrayList<District> listOfChoice = new ArrayList<>();
        listOfChoice.add(districtTrade);
        listOfChoice.add(districtMilitary);
        listOfChoice.add(districtReligious);
        listOfChoice.add(districtUnique);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("test1", 1, 1));
        districtsPlaced.add(new District("test2", 1, 1));
        districtsPlaced.add(districtUnique);
        districtsPlaced.add(districtMilitary);
        districtsPlaced.add(districtReligious);

        when(hand.getListCard()).thenReturn(new ArrayList<>());
        richardBot.setDistrictPlaced(districtsPlaced);

        assertEquals(districtTrade, richardBot.chooseOneCard(listOfChoice));
    }

    @Test
    void chooseOneCardTestAllOwned() {
        District districtUnique = new District("unique", 1, CategoryEnum.UNIQUE.getValue());
        District districtReligious = new District("religious", 1, CategoryEnum.RELIGIOUS.getValue());
        District districtMilitary = new District("military", 1, CategoryEnum.MILITARY.getValue());
        District districtTrade = new District("trade", 1, CategoryEnum.TRADE.getValue());

        ArrayList<District> listOfChoice = new ArrayList<>();
        listOfChoice.add(districtTrade);
        listOfChoice.add(districtMilitary);
        listOfChoice.add(districtReligious);
        listOfChoice.add(districtUnique);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("test1", 1, 1));
        districtsPlaced.add(new District("test2", 1, 1));
        districtsPlaced.add(districtUnique);
        districtsPlaced.add(districtMilitary);
        districtsPlaced.add(districtReligious);
        districtsPlaced.add(districtTrade);

        when(hand.getListCard()).thenReturn(new ArrayList<>());
        richardBot.setDistrictPlaced(districtsPlaced);

        assertEquals(listOfChoice.get(0), richardBot.chooseOneCard(listOfChoice));
    }

    @Test
    void discardListTestNoDiscard() {
        District d1 = new District("d1", 1, 1);
        District d2 = new District("d2", 1, 1);
        District d3 = new District("d3", 1, 1);
        District d4 = new District("d4", 1, 1);

        ArrayList<District> handList = new ArrayList<>();
        handList.add(d1);
        handList.add(d2);
        handList.add(d3);
        handList.add(d4);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("unique", 1, 1));

        when(hand.getListCard()).thenReturn(handList);
        richardBot.setDistrictPlaced(districtsPlaced);

        assertEquals(0, richardBot.discardList().size());
    }

    @Test
    void discardListTestWithDiscard() {
        District d1 = new District("d1", 1, 1);
        District d2 = new District("d2", 1, 1);
        District d3 = new District("d3", 1, 1);
        District d4 = new District("d4", 1, 1);

        ArrayList<District> handList = new ArrayList<>();
        handList.add(d1);
        handList.add(d2);
        handList.add(d3);
        handList.add(d4);

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("unique", 1, 1));
        districtsPlaced.add(d2);
        districtsPlaced.add(d4);

        when(hand.getListCard()).thenReturn(handList);
        richardBot.setDistrictPlaced(districtsPlaced);

        ArrayList<District> discardList = richardBot.discardList();
        assertEquals(2, discardList.size());
        assertFalse(discardList.contains(d1));
        assertTrue(discardList.contains(d2));
        assertFalse(discardList.contains(d3));
        assertTrue(discardList.contains(d4));
    }

    @Test
    void logicMagicianTestEndgameModeBot1() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(5);

        BotView bv1 = mock(BotView.class);
        BotView bv2 = mock(BotView.class);
        BotView bvRichard = mock(BotView.class);

        when(bv1.getDistrictsPlacedSize()).thenReturn(5);
        when(bv1.getNbCardHand()).thenReturn(1);
        when(bv1.getID()).thenReturn(1);

        when(bv2.getDistrictsPlacedSize()).thenReturn(4);
        when(bv2.getNbCardHand()).thenReturn(1);
        when(bv2.getID()).thenReturn(2);

        when(bvRichard.getDistrictsPlacedSize()).thenReturn(5);
        when(bvRichard.getNbCardHand()).thenReturn(1);
        when(bvRichard.getID()).thenReturn(richardBot.getID());

        ArrayList<BotView> bvList = new ArrayList<>();
        bvList.add(bv1);
        bvList.add(bv2);
        bvList.add(bvRichard);

        when(gv.getListAllBots()).thenReturn(bvList);

        richardBot.setGameView(gv);

        assertEquals(bv1.getID(), richardBot.logicMagician());
    }

    @Test
    void logicMagicianTestEndgameModeBot2() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(5);

        BotView bv1 = mock(BotView.class);
        BotView bv2 = mock(BotView.class);
        BotView bvRichard = mock(BotView.class);

        when(bv1.getDistrictsPlacedSize()).thenReturn(4);
        when(bv1.getNbCardHand()).thenReturn(1);
        when(bv1.getID()).thenReturn(1);

        when(bv2.getDistrictsPlacedSize()).thenReturn(5);
        when(bv2.getNbCardHand()).thenReturn(1);
        when(bv2.getID()).thenReturn(2);

        when(bvRichard.getDistrictsPlacedSize()).thenReturn(5);
        when(bvRichard.getNbCardHand()).thenReturn(1);
        when(bvRichard.getID()).thenReturn(richardBot.getID());

        ArrayList<BotView> bvList = new ArrayList<>();
        bvList.add(bv1);
        bvList.add(bv2);
        bvList.add(bvRichard);

        when(gv.getListAllBots()).thenReturn(bvList);

        richardBot.setGameView(gv);

        assertEquals(bv2.getID(), richardBot.logicMagician());
    }

    @Test
    void logicMagicianTestEndgameModeBotRichard() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(5);

        BotView bv1 = mock(BotView.class);
        BotView bv2 = mock(BotView.class);
        BotView bvRichard = mock(BotView.class);

        when(bv1.getDistrictsPlacedSize()).thenReturn(4);
        when(bv1.getNbCardHand()).thenReturn(1);
        when(bv1.getID()).thenReturn(1);

        when(bv2.getDistrictsPlacedSize()).thenReturn(4);
        when(bv2.getNbCardHand()).thenReturn(3);
        when(bv2.getID()).thenReturn(2);

        when(bvRichard.getDistrictsPlacedSize()).thenReturn(5);
        when(bvRichard.getNbCardHand()).thenReturn(1);
        when(bvRichard.getID()).thenReturn(richardBot.getID());

        ArrayList<BotView> bvList = new ArrayList<>();
        bvList.add(bv1);
        bvList.add(bv2);
        bvList.add(bvRichard);

        when(gv.getListAllBots()).thenReturn(bvList);

        richardBot.setGameView(gv);
        when(hand.getListCardSize()).thenReturn(2);

        assertEquals(bv2.getID(), richardBot.logicMagician());
    }

    @Test
    void logicMagicianTestEndgameModeBot1NoHandCard() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(5);

        BotView bv1 = mock(BotView.class);
        BotView bv2 = mock(BotView.class);
        BotView bvRichard = mock(BotView.class);

        when(bv1.getDistrictsPlacedSize()).thenReturn(5);
        when(bv1.getNbCardHand()).thenReturn(0);
        when(bv1.getID()).thenReturn(1);

        when(bv2.getDistrictsPlacedSize()).thenReturn(4);
        when(bv2.getNbCardHand()).thenReturn(1);
        when(bv2.getID()).thenReturn(2);

        when(bvRichard.getDistrictsPlacedSize()).thenReturn(5);
        when(bvRichard.getNbCardHand()).thenReturn(1);
        when(bvRichard.getID()).thenReturn(richardBot.getID());

        ArrayList<BotView> bvList = new ArrayList<>();
        bvList.add(bv1);
        bvList.add(bv2);
        bvList.add(bvRichard);

        when(gv.getListAllBots()).thenReturn(bvList);

        richardBot.setGameView(gv);

        assertEquals(bv2.getID(), richardBot.logicMagician());
    }

    @Test
    void logicMagicianTestNoEndgameModeBot1() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(5);

        BotView bv1 = mock(BotView.class);
        BotView bv2 = mock(BotView.class);
        BotView bvRichard = mock(BotView.class);

        when(bv1.getDistrictsPlacedSize()).thenReturn(3);
        when(bv1.getNbCardHand()).thenReturn(3);
        when(bv1.getID()).thenReturn(1);

        when(bv2.getDistrictsPlacedSize()).thenReturn(4);
        when(bv2.getNbCardHand()).thenReturn(1);
        when(bv2.getID()).thenReturn(2);

        when(bvRichard.getDistrictsPlacedSize()).thenReturn(2);
        when(bvRichard.getNbCardHand()).thenReturn(1);
        when(bvRichard.getID()).thenReturn(richardBot.getID());

        ArrayList<BotView> bvList = new ArrayList<>();
        bvList.add(bv1);
        bvList.add(bv2);
        bvList.add(bvRichard);

        when(gv.getListAllBots()).thenReturn(bvList);

        richardBot.setGameView(gv);

        when(hand.getListCardSize()).thenReturn(2);

        assertEquals(bv1.getID(), richardBot.logicMagician());
    }

    @Test
    void logicMagicianTestNoEndgameModeMoreThan3CardInHand() {
        GameView gv = mock(GameView.class);
        when(gv.getSizeBiggerCity()).thenReturn(5);

        BotView bv1 = mock(BotView.class);
        BotView bv2 = mock(BotView.class);
        BotView bvRichard = mock(BotView.class);

        when(bv1.getDistrictsPlacedSize()).thenReturn(3);
        when(bv1.getNbCardHand()).thenReturn(3);
        when(bv1.getID()).thenReturn(1);

        when(bv2.getDistrictsPlacedSize()).thenReturn(4);
        when(bv2.getNbCardHand()).thenReturn(1);
        when(bv2.getID()).thenReturn(2);

        when(bvRichard.getDistrictsPlacedSize()).thenReturn(2);
        when(bvRichard.getNbCardHand()).thenReturn(1);
        when(bvRichard.getID()).thenReturn(richardBot.getID());

        ArrayList<BotView> bvList = new ArrayList<>();
        bvList.add(bv1);
        bvList.add(bv2);
        bvList.add(bvRichard);

        when(gv.getListAllBots()).thenReturn(bvList);

        richardBot.setGameView(gv);

        when(hand.getListCardSize()).thenReturn(5);

        assertEquals(-1, richardBot.logicMagician());
    }
}