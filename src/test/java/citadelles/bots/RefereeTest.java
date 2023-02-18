package citadelles.bots;

import citadelles.Hand;
import citadelles.characters.*;
import citadelles.characters.Character;
import citadelles.districts.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Ref;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RefereeTest {
    Referee referee;

    @BeforeEach
    void setUp() {
        referee = new Referee();
    }

    @Test
    void killWarlord() {
        Bot botAssassin = new Bot(0, null);
        Bot botWarlord = new Bot(1, null);
        Bot botBishop = new Bot(2, null);
        botAssassin.setCharacter(new Assassin());
        botWarlord.setCharacter(new Warlord());
        botBishop.setCharacter(new Bishop());

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botAssassin);
        botList.add(botWarlord);
        botList.add(botBishop);

        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        referee.kill(botAssassin, CharacterEnum.WARLORD.getId());

        assertTrue(botAssassin.isAlive());
        assertFalse(botWarlord.isAlive());
        assertTrue(botBishop.isAlive());
    }

    @Test
    void killAssassin() {
        Bot botAssassin = new Bot(0, null);
        Bot botWarlord = new Bot(1, null);
        Bot botBishop = new Bot(2, null);
        botAssassin.setCharacter(new Assassin());
        botWarlord.setCharacter(new Warlord());
        botBishop.setCharacter(new Bishop());

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botAssassin);
        botList.add(botWarlord);
        botList.add(botBishop);

        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        referee.kill(botAssassin, CharacterEnum.ASSASSIN.getId());

        assertTrue(botAssassin.isAlive());
        assertTrue(botWarlord.isAlive());
        assertTrue(botBishop.isAlive());
    }

    @Test
    void killRoleNotInList() {
        Bot botAssassin = new Bot(0, null);
        Bot botWarlord = new Bot(1, null);
        Bot botBishop = new Bot(2, null);
        botAssassin.setCharacter(new Assassin());
        botWarlord.setCharacter(new Warlord());
        botBishop.setCharacter(new Bishop());

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botAssassin);
        botList.add(botWarlord);
        botList.add(botBishop);

        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        referee.kill(botAssassin, CharacterEnum.MERCHANT.getId());

        assertTrue(botAssassin.isAlive());
        assertTrue(botWarlord.isAlive());
        assertTrue(botBishop.isAlive());
    }

    @Test
    void addGoldPerDistrictCategoryNoble() {
        Bot bot = new Bot(0, null);
        bot.setCharacter(new King());
        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.NOBLE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.NOBLE.getValue()));
        bot.setDistrictPlaced(districtsPlaced);

        int goldBefore = bot.getGold();
        referee.addGoldPerDistrictCategory(bot, CategoryEnum.NOBLE);
        int goldAfter = bot.getGold();

        assertEquals(goldBefore+2, goldAfter);
    }

    @Test
    void addGoldPerDistrictCategoryNobleWithZeroNoble() {
        Bot bot = new Bot(0, null);
        bot.setCharacter(new King());
        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        bot.setDistrictPlaced(districtsPlaced);

        int goldBefore = bot.getGold();
        referee.addGoldPerDistrictCategory(bot, CategoryEnum.NOBLE);
        int goldAfter = bot.getGold();

        assertEquals(goldBefore, goldAfter);
    }

    @Test
    void destructOneDistrict() {
        DistrictManager dm = new DistrictManager();

        Bot botAssassin = new Bot(0, new Hand(dm));
        Bot botWarlord = new Bot(1, new Hand(dm));
        Bot botBishop = new Bot(2, new Hand(dm));
        botAssassin.setCharacter(new Assassin());
        botWarlord.setCharacter(new Warlord());
        botBishop.setCharacter(new Bishop());

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        botAssassin.setDistrictPlaced(districtsPlaced);

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botAssassin);
        botList.add(botWarlord);
        botList.add(botBishop);

        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        Bot bot = new Bot(10, new Hand(dm));


        int[] data = new int[]{0, 0};

        int sizeCityBefore = botAssassin.getNumberDistrictPlaced();
        referee.destructOneDistrict(bot, data);
        int sizeCityAfter = botAssassin.getNumberDistrictPlaced();


        assertEquals(sizeCityBefore-1, sizeCityAfter);
    }

    @Test
    void destructOneDistrictOnBishop() {
        DistrictManager dm = new DistrictManager();

        Bot botAssassin = new Bot(0, new Hand(dm));
        Bot botWarlord = new Bot(1, new Hand(dm));
        Bot botBishop = new Bot(2, new Hand(dm));
        botAssassin.setCharacter(new Assassin());
        botWarlord.setCharacter(new Warlord());
        botBishop.setCharacter(new Bishop());

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        botBishop.setDistrictPlaced(districtsPlaced);

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botAssassin);
        botList.add(botWarlord);
        botList.add(botBishop);

        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        Bot bot = new Bot(10, new Hand(dm));


        int[] data = new int[]{2, 0};

        int sizeCityBefore = botBishop.getNumberDistrictPlaced();
        referee.destructOneDistrict(bot, data);
        int sizeCityAfter = botBishop.getNumberDistrictPlaced();


        assertEquals(sizeCityBefore, sizeCityAfter);
    }

    @Test
    void destructOneDistrictWithIncorrectDistrictIndex() {
        DistrictManager dm = new DistrictManager();

        Bot botAssassin = new Bot(0, new Hand(dm));
        Bot botWarlord = new Bot(1, new Hand(dm));
        Bot botBishop = new Bot(2, new Hand(dm));
        botAssassin.setCharacter(new Assassin());
        botWarlord.setCharacter(new Warlord());
        botBishop.setCharacter(new Bishop());

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        botAssassin.setDistrictPlaced(districtsPlaced);

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botAssassin);
        botList.add(botWarlord);
        botList.add(botBishop);

        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        Bot bot = new Bot(10, new Hand(dm));


        int[] data = new int[]{0, 6};

        int sizeCityBefore = botAssassin.getNumberDistrictPlaced();
        referee.destructOneDistrict(bot, data);
        int sizeCityAfter = botAssassin.getNumberDistrictPlaced();


        assertEquals(sizeCityBefore, sizeCityAfter);
    }

    @Test
    void destructOneDistrictWithIncorrectBotIndex() {
        DistrictManager dm = new DistrictManager();

        Bot botAssassin = new Bot(0, new Hand(dm));
        Bot botWarlord = new Bot(1, new Hand(dm));
        Bot botBishop = new Bot(2, new Hand(dm));
        botAssassin.setCharacter(new Assassin());
        botWarlord.setCharacter(new Warlord());
        botBishop.setCharacter(new Bishop());

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        botAssassin.setDistrictPlaced(districtsPlaced);

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botAssassin);
        botList.add(botWarlord);
        botList.add(botBishop);

        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        Bot bot = new Bot(10, new Hand(dm));


        int[] data = new int[]{9, 0};

        int sizeCityBefore = botAssassin.getNumberDistrictPlaced();
        referee.destructOneDistrict(bot, data);
        int sizeCityAfter = botAssassin.getNumberDistrictPlaced();


        assertEquals(sizeCityBefore, sizeCityAfter);
    }

    @Test
    void destructOneDistrictWithKeep() {
        DistrictManager dm = new DistrictManager();

        Bot botAssassin = new Bot(0, new Hand(dm));
        Bot botWarlord = new Bot(1, new Hand(dm));
        Bot botBishop = new Bot(2, new Hand(dm));
        botAssassin.setCharacter(new Assassin());
        botWarlord.setCharacter(new Warlord());
        botBishop.setCharacter(new Bishop());

        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new Keep());
        botAssassin.setDistrictPlaced(districtsPlaced);

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botAssassin);
        botList.add(botWarlord);
        botList.add(botBishop);

        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        Bot bot = new Bot(10, new Hand(dm));


        int[] data = new int[]{0, 0};

        int sizeCityBefore = botAssassin.getNumberDistrictPlaced();
        referee.destructOneDistrict(bot, data);
        int sizeCityAfter = botAssassin.getNumberDistrictPlaced();


        assertEquals(sizeCityBefore, sizeCityAfter);
    }

    @Test
    void stealTest(){
        DistrictManager dm = new DistrictManager();

        Bot botThief = new Bot(0, new Hand(dm));
        Bot botWarlord = new Bot(1, new Hand(dm));
        Bot botAssassin = new Bot(2, new Hand(dm));
        Bot botKing = new Bot(3, new Hand(dm));
        botThief.setCharacter(new Thief());
        botWarlord.setCharacter(new Warlord());
        botAssassin.setCharacter(new Assassin());
        botKing.setCharacter(new King());

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botThief);
        botList.add(botWarlord);
        botList.add(botAssassin);
        botList.add(botKing);
        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        int goldInit1 = 50;
        int goldInit2 = 50;
        botWarlord.setGold(goldInit1);
        botThief.setGold(goldInit2);

        referee.steal(botWarlord.getCharacterId(), botThief.getCharacterId());

        assertEquals(goldInit1+goldInit2,botThief.getGold());
        assertEquals(0,botWarlord.getGold());
    }

    @Test
    void stealAssassinTest(){
        DistrictManager dm = new DistrictManager();

        Bot botThief = new Bot(0, new Hand(dm));
        Bot botWarlord = new Bot(1, new Hand(dm));
        Bot botAssassin = new Bot(2, new Hand(dm));
        Bot botKing = new Bot(3, new Hand(dm));
        botThief.setCharacter(new Thief());
        botWarlord.setCharacter(new Warlord());
        botAssassin.setCharacter(new Assassin());
        botKing.setCharacter(new King());

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botThief);
        botList.add(botWarlord);
        botList.add(botAssassin);
        botList.add(botKing);
        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        int goldInit1 = 50;
        int goldInit2 = 50;
        botAssassin.setGold(goldInit1);
        botThief.setGold(goldInit2);

        referee.steal(botAssassin.getCharacterId(), botThief.getCharacterId());

        assertEquals(goldInit2,botThief.getGold());
        assertEquals(goldInit1,botAssassin.getGold());
    }

    @Test
    void stealDeadTest(){
        DistrictManager dm = new DistrictManager();

        Bot botThief = new Bot(0, new Hand(dm));
        Bot botWarlord = new Bot(1, new Hand(dm));
        Bot botAssassin = new Bot(2, new Hand(dm));
        Bot botKing = new Bot(3, new Hand(dm));
        botThief.setCharacter(new Thief());
        botWarlord.setCharacter(new Warlord());
        botAssassin.setCharacter(new Assassin());
        botKing.setCharacter(new King());

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botThief);
        botList.add(botWarlord);
        botList.add(botAssassin);
        botList.add(botKing);
        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        int goldInit1 = 50;
        int goldInit2 = 50;
        botWarlord.setGold(goldInit1);
        botThief.setGold(goldInit2);
        botWarlord.setDead();

        referee.steal(botWarlord.getCharacterId(), botThief.getCharacterId());

        assertEquals(goldInit2,botThief.getGold());
        assertEquals(goldInit1,botWarlord.getGold());
    }

    @Test
    void exchangeCardsTest(){

        DistrictManager dm = new DistrictManager();

        Bot botThief = new Bot(0, new Hand(dm));
        Bot botMagician = new Bot(1, new Hand(dm));
        botThief.setCharacter(new Thief());
        botMagician.setCharacter(new Magician());

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botThief);
        botList.add(botMagician);
        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        ArrayList<District> list = new ArrayList<>();
        list.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        list.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        list.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        list.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));
        list.add(new District("Test", 1, CategoryEnum.TRADE.getValue()));

        botMagician.setHandList(list);
        int nbCard1 = botMagician.getHandSize();
        int nbCard2 = botThief.getHandSize();

        referee.exchangeCards(botThief.getID(),botMagician.getID());

        assertEquals(nbCard2,botMagician.getHandSize());
        assertEquals(nbCard1,botThief.getHandSize());
    }

    @Test
    void setCrownTest(){
        DistrictManager dm = new DistrictManager();

        Bot botThief = new Bot(0, new Hand(dm));
        Bot botMagician = new Bot(1, new Hand(dm));
        botThief.setCharacter(new Thief());
        botMagician.setCharacter(new Magician());

        BotManager botManager = mock(BotManager.class);
        ArrayList<Bot> botList = new ArrayList<>();
        botList.add(botThief);
        botList.add(botMagician);
        when(botManager.getListBot()).thenReturn(botList);
        referee.setBotManager(botManager);

        assertFalse(botThief.doHaveTheCrown());

        referee.setCrown(botThief);

        assertTrue(botThief.doHaveTheCrown());
    }

    @Test
    void get3CardsFor3GoldTestWith0Golds() {
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(new District("Test", 1, 1));
        Hand hand = new Hand(dm);
        Bot bot = new Bot(0, hand);

        bot.setGold(0);

        int sizeHandBefore = bot.getHandSize();
        new Referee().get3CardsFor3Gold(bot);
        int sizeHandAfter = bot.getHandSize();

        assertEquals(sizeHandBefore, sizeHandAfter);
        assertEquals(0,bot.getGold());
    }

    @Test
    void get3CardsFor3GoldTestWith3Golds() {
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(new District("Test", 1, 1));
        Hand hand = new Hand(dm);
        Bot bot = new Bot(0, hand);

        bot.setGold(3);

        int sizeHandBefore = bot.getHandSize();
        new Referee().get3CardsFor3Gold(bot);
        int sizeHandAfter = bot.getHandSize();

        assertEquals(sizeHandBefore+3, sizeHandAfter);
        assertEquals(0,bot.getGold());
    }

    @Test
    void get1GoldFromMagicSchoolTestWithBishop() {
        Bot bot = new Bot(0, null);
        bot.setCharacter(new Bishop());
        bot.setGold(0);

        new Referee().get1GoldFromMagicSchool(bot);

        assertEquals(1, bot.getGold());
    }

    @Test
    void get1GoldFromMagicSchoolTestWithWarlord() {
        Bot bot = new Bot(0, null);
        bot.setCharacter(new Warlord());
        bot.setGold(0);

        new Referee().get1GoldFromMagicSchool(bot);

        assertEquals(1, bot.getGold());
    }

    @Test
    void get1GoldFromMagicSchoolTestWithAssassin() {
        Bot bot = new Bot(0, null);
        bot.setCharacter(new Assassin());
        bot.setGold(0);

        new Referee().get1GoldFromMagicSchool(bot);

        assertEquals(0, bot.getGold());
    }

    @Test
    void get1GoldFor1CardTestGoodIndex() {
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand hand = new Hand(dm);
        Bot bot = new Bot(0, hand);

        ArrayList<District> handList = new ArrayList<>();
        handList.add(new District("Test1", 1, 1));
        handList.add(new District("Test2", 1, 1));
        handList.add(new District("Test3", 1, 1));
        bot.setHandList(handList);

        District districtIndex1 = bot.getHandList().get(1);

        int goldBefore = bot.getGold();
        assertTrue(bot.getHandList().contains(districtIndex1));
        new Referee().get1GoldFor1Card(bot, 1);
        assertFalse(bot.getHandList().contains(districtIndex1));
        assertEquals(goldBefore+1,bot.getGold());
    }

    @Test
    void get1GoldFor1CardTestBadIndex() {
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand hand = new Hand(dm);
        Bot bot = new Bot(0, hand);

        ArrayList<District> handList = new ArrayList<>();
        handList.add(new District("Test1", 1, 1));
        handList.add(new District("Test2", 1, 1));
        handList.add(new District("Test3", 1, 1));
        bot.setHandList(handList);

        District districtIndex1 = bot.getHandList().get(1);

        int goldBefore = bot.getGold();
        assertTrue(bot.getHandList().contains(districtIndex1));
        new Referee().get1GoldFor1Card(bot, 10);
        assertTrue(bot.getHandList().contains(districtIndex1));
        assertEquals(goldBefore,bot.getGold());
    }

    @Test
    void placeCardsTestWith3Golds() {
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand hand = new Hand(dm);
        Bot bot = new Bot(0, hand);

        ArrayList<District> districtsToPlace = new ArrayList<>();
        districtsToPlace.add(new District("Test1", 1, 1));
        districtsToPlace.add(new District("Test2", 1, 1));
        districtsToPlace.add(new District("Test3", 1, 1));
        bot.setHandList(new ArrayList<>());
        bot.getHandList().addAll(districtsToPlace);

        bot.setGold(3);

        int citySizeBefore = bot.getNumberDistrictPlaced();
        new Referee().placeCards(bot, districtsToPlace);
        int citySizeAfter = bot.getNumberDistrictPlaced();

        assertEquals(citySizeBefore+3,citySizeAfter);
        assertEquals(0,bot.getGold());
    }

    @Test
    void placeCardsTestWith0Golds() {
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand hand = new Hand(dm);
        Bot bot = new Bot(0, hand);

        ArrayList<District> districtsToPlace = new ArrayList<>();
        districtsToPlace.add(new District("Test1", 1, 1));
        districtsToPlace.add(new District("Test2", 1, 1));
        districtsToPlace.add(new District("Test3", 1, 1));
        bot.setHandList(new ArrayList<>());
        bot.getHandList().addAll(districtsToPlace);

        bot.setGold(0);

        int citySizeBefore = bot.getNumberDistrictPlaced();
        new Referee().placeCards(bot, districtsToPlace);
        int citySizeAfter = bot.getNumberDistrictPlaced();

        assertEquals(citySizeBefore,citySizeAfter);
        assertEquals(0,bot.getGold());
    }

    @Test
    void placeCardsTestWith3GoldsAndBadCardsGiven() {
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand hand = new Hand(dm);
        Bot bot = new Bot(0, hand);

        ArrayList<District> districtsToPlace = new ArrayList<>();
        districtsToPlace.add(new District("Test1", 1, 1));
        districtsToPlace.add(new District("Test2", 1, 1));
        districtsToPlace.add(new District("Test3", 1, 1));
        bot.setHandList(new ArrayList<>());
        bot.getHandList().addAll(districtsToPlace);

        districtsToPlace.clear();
        districtsToPlace.add(new District("Test4", 1, 1));

        bot.setGold(3);

        int citySizeBefore = bot.getNumberDistrictPlaced();
        new Referee().placeCards(bot, districtsToPlace);
        int citySizeAfter = bot.getNumberDistrictPlaced();

        assertEquals(citySizeBefore,citySizeAfter);
        assertEquals(3,bot.getGold());
    }

    @Test
    void placeCardsTestWith3GoldsAndOneBadCardsGiven() {
        DistrictManager dm = mock(DistrictManager.class);
        when(dm.pickRandomCard()).thenReturn(null);
        Hand hand = new Hand(dm);
        Bot bot = new Bot(0, hand);

        ArrayList<District> districtsToPlace = new ArrayList<>();
        districtsToPlace.add(new District("Test1", 1, 1));
        districtsToPlace.add(new District("Test2", 1, 1));
        districtsToPlace.add(new District("Test3", 1, 1));
        bot.setHandList(new ArrayList<>());
        bot.getHandList().addAll(districtsToPlace);

        districtsToPlace.add(new District("Test4", 1, 1));

        bot.setGold(4);

        int citySizeBefore = bot.getNumberDistrictPlaced();
        new Referee().placeCards(bot, districtsToPlace);
        int citySizeAfter = bot.getNumberDistrictPlaced();

        assertEquals(citySizeBefore+3,citySizeAfter);
        assertEquals(1,bot.getGold());
    }
}