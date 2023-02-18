package citadelles.bots;

import citadelles.Hand;
import citadelles.characters.*;
import citadelles.characters.Character;
import citadelles.districts.CategoryEnum;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BotTest {
    private Bot bot;

    @BeforeEach
    void setUp() {
        DistrictManager districtManager = new DistrictManager();
        bot = new Bot(0, new Hand(districtManager));
    }

    @Test
    void scoreTest(){
        int score = bot.getScore();
        int add = 10;
        bot.addScore(10);
        assertEquals(score + add,bot.getScore());
    }

    @Test
    void add10Gold(){
        int base = bot.getGold();
        bot.addGold(10);
        assertEquals(base + 10,bot.getGold());
    }
    @Test
    void add10000Gold(){
        int base = bot.getGold();
        bot.addGold(10000);
        assertEquals(base,bot.getGold());
    }
    @Test
    void addNegativeGold(){
        int base = bot.getGold();
        bot.addGold(-5);
        assertEquals(base,bot.getGold());
    }

    @Test
    void setGold(){
        bot.setGold(50);
        assertEquals(50, bot.getGold());
    }

    @Test
    void setGoldNegative(){
        bot.setGold(-50);
        assertEquals(2, bot.getGold());
    }

    @Test
    void setGoldAbusivelyHigh(){
        bot.setGold(1000);
        assertEquals(2, bot.getGold());
    }

    @Test
    void getHandSize(){
        assertEquals(4, bot.getHandSize());
        bot.chooseAddCard();
        assertEquals(5, bot.getHandSize());
    }

    @Test
    void chooseAddCardsObservatory(){
        int initSize = bot.getHandSize();

        ArrayList<District> placed = new ArrayList<>();
        placed.add(new District("test1",1,1));
        placed.add(new District("test2",1,2));
        placed.add(new District("test3",1,2));
        placed.add(new District("test4",1,1));
        placed.add(new District("Observatory",1,1));

        bot.setDistrictPlaced(placed);

        bot.chooseAddCard();

        assertEquals(initSize + 1, bot.getHandSize());
    }

    @Test
    void chooseAddCardsLibrary(){
        int initSize = bot.getHandSize();

        ArrayList<District> placed = new ArrayList<>();
        placed.add(new District("test1",1,1));
        placed.add(new District("test2",1,2));
        placed.add(new District("test3",1,2));
        placed.add(new District("test4",1,1));
        placed.add(new District("Library",1,1));

        bot.setDistrictPlaced(placed);

        bot.chooseAddCard();

        assertEquals(initSize + 2, bot.getHandSize());
    }

    @Test
    void getID(){
        assertEquals(0, bot.getID());
    }

    @Test
    void getScoreAlreadyPlaced() {
        bot.getDistrictPlaced().add(new District("Temple", 1, 1)); // default is Temple
        assertTrue(bot.isDistrictAlreadyPlaced("Temple"));
        assertFalse(bot.isDistrictAlreadyPlaced("Patate"));
    }

    @Test
    void getDistrictHandMinValue(){
        bot.getHandList().clear();
        bot.getHandList().add(new District("Building",7,2));
        bot.getHandList().add(new District("Apartment",5,5));
        bot.getHandList().add(new District("House",10,1));

        assertEquals("Apartment",bot.getDistrictHandMinValue().getName());
    }

    @Test
    void getDistrictHandMinValueWithDistrictsPlaced(){
        bot.getHandList().clear();
        bot.getHandList().add(new District("Building",7,2));
        bot.getHandList().add(new District("Apartment",5,5));
        bot.getHandList().add(new District("House",10,1));

        ArrayList<District> placed = new ArrayList<>();
        placed.add(new District("dogHouse",5,5));
        placed.add(new District("Apartment",5,5));
        placed.add(new District("catHouse",5,5));
        bot.setDistrictPlaced(placed);

        assertEquals("Building",bot.getDistrictHandMinValue().getName());
    }



    @Test
    void CheckFinishedPosition(){
        bot.setFinishPosition(5);
        assertEquals(5,bot.getFinishPosition());
    }

    @Test
    void crownManipulation(){
        assertFalse(bot.doHaveTheCrown());
        bot.setCrown();
        assertTrue(bot.doHaveTheCrown());
    }

    @Test
    void aliveManipulation() {
        assertTrue(bot.isAlive());
        bot.setDead();
        assertFalse(bot.isAlive());
        bot.setAlive();
        assertTrue(bot.isAlive());
    }

    @Test
    void isPlayerHaveDistrict() {
        ArrayList<District> districtsPlaced = new ArrayList<>();
        districtsPlaced.add(new District("d1", 1, 1));
        districtsPlaced.add(new District("d2", 1, 1));

        ArrayList<District> handCards = new ArrayList<>();
        handCards.add(new District("d3", 1, 1));
        handCards.add(new District("d4", 1, 1));
        handCards.add(new District("d5", 1, 1));

        bot.setDistrictPlaced(districtsPlaced);
        bot.setHandList(handCards);

        assertTrue(bot.isPlayerHaveDistrict(new District("d2", 1, 1)));
        assertTrue(bot.isPlayerHaveDistrict(new District("d4", 1, 1)));
        assertFalse(bot.isPlayerHaveDistrict(new District("d10", 1, 1)));
    }

    @Test
    void chooseOneCard() {
        ArrayList<District> districtsList = new ArrayList<>();
        District d1 = new District("d1", 1, 1);
        District d2 = new District("d2", 1, 1);
        districtsList.add(d1);
        districtsList.add(d2);

        assertEquals(d1, bot.chooseOneCard(districtsList));
    }

    @Test
    void numberOfEachCategoriesPlacedNoDistrictsPlaced() {
        ArrayList<District> districtsPlaced = new ArrayList<>();
        bot.setDistrictPlaced(districtsPlaced);

        int[] dataOC = bot.numberOfEachCategoriesPlaced();

        assertEquals(0, dataOC[CategoryEnum.RELIGIOUS.getValue()]);
        assertEquals(0, dataOC[CategoryEnum.NOBLE.getValue()]);
        assertEquals(0, dataOC[CategoryEnum.TRADE.getValue()]);
        assertEquals(0, dataOC[CategoryEnum.MILITARY.getValue()]);
        assertEquals(0, dataOC[CategoryEnum.UNIQUE.getValue()]);
    }

    @Test
    void numberOfEachCategoriesPlacedDistrictsPlaced() {
        ArrayList<District> districtsPlaced = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            for (int j = 0; j < i; j++) {
                districtsPlaced.add(new District("Test",1,i));
            }
        }

        bot.setDistrictPlaced(districtsPlaced);

        int[] dataOC = bot.numberOfEachCategoriesPlaced();

        assertEquals(1, dataOC[CategoryEnum.RELIGIOUS.getValue()]);
        assertEquals(2, dataOC[CategoryEnum.NOBLE.getValue()]);
        assertEquals(3, dataOC[CategoryEnum.TRADE.getValue()]);
        assertEquals(4, dataOC[CategoryEnum.MILITARY.getValue()]);
        assertEquals(5, dataOC[CategoryEnum.UNIQUE.getValue()]);
    }

    @Test
    void chooseOneCharacter() {
        ArrayList<Character> characterArrayList = new ArrayList<>();
        characterArrayList.add(new Assassin());
        characterArrayList.add(new Warlord());
        characterArrayList.add(new Bishop());
        characterArrayList.add(new Merchant());

        assertEquals(characterArrayList.get(0), bot.chooseOneCharacter(characterArrayList));
    }

    @Test
    void isAlreadyPlacedTest(){
        ArrayList<District> placed = new ArrayList<>();
        placed.add(new District("test",1,1));
        placed.add(new District("test",1,2));
        placed.add(new District("test",1,2));
        placed.add(new District("test",1,1));

        bot.setDistrictPlaced(placed);

        assertTrue(bot.isCategoryPlaced(1));
        assertFalse(bot.isCategoryPlaced(3));

    }

    @Test
    void districtCanBePlacedTestEnoughGoldAndNotPlaced() {
        ArrayList<District> placed = new ArrayList<>();
        placed.add(new District("test1",1,1));
        placed.add(new District("test2",1,2));
        placed.add(new District("test3",1,2));
        placed.add(new District("test4",1,1));
        bot.setDistrictPlaced(placed);

        District districtToPlace = new District("test5", 3, 1);

        assertTrue(bot.districtCanBePlaced(districtToPlace,3));
    }

    @Test
    void districtCanBePlacedTestEnoughGoldAndPlaced() {
        ArrayList<District> placed = new ArrayList<>();
        placed.add(new District("test1",1,1));
        placed.add(new District("test2",1,2));
        placed.add(new District("test3",1,2));
        placed.add(new District("test4",1,1));
        bot.setDistrictPlaced(placed);

        District districtToPlace = new District("test3", 3, 1);

        assertFalse(bot.districtCanBePlaced(districtToPlace,3));
    }

    @Test
    void districtCanBePlacedTestNoEnoughGoldAndNotPlaced() {
        ArrayList<District> placed = new ArrayList<>();
        placed.add(new District("test1",1,1));
        placed.add(new District("test2",1,2));
        placed.add(new District("test3",1,2));
        placed.add(new District("test4",1,1));
        bot.setDistrictPlaced(placed);

        District districtToPlace = new District("test5", 3, 1);

        assertFalse(bot.districtCanBePlaced(districtToPlace,2));
    }

    @Test
    void districtCanBePlacedTestNoEnoughGoldAndPlaced() {
        ArrayList<District> placed = new ArrayList<>();
        placed.add(new District("test1",1,1));
        placed.add(new District("test2",1,2));
        placed.add(new District("test3",1,2));
        placed.add(new District("test4",1,1));
        bot.setDistrictPlaced(placed);

        District districtToPlace = new District("test3", 3, 1);

        assertFalse(bot.districtCanBePlaced(districtToPlace,2));
    }
}