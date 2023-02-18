package citadelles.characters;

import citadelles.GameView;
import citadelles.Hand;
import citadelles.bots.Bot;
import citadelles.bots.BotManager;
import citadelles.bots.StupidBot;
import citadelles.districts.CategoryEnum;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterTest {
    private CharacterManager cm;
    //private Character character;
    private Bot bot;
    private GameView gameView;

    @BeforeEach
    void setUp() {
        int nbBot = 4;
        DistrictManager dm = new DistrictManager();
        Hand hand = new Hand(dm);
        cm = new CharacterManager(nbBot);
        cm.init();
        gameView = new GameView(new BotManager(0,0,4, 0,dm), cm);
        bot = new Bot(10, hand);
        //character = new Character(10, "Peasant") {};
    }

    @Test
    void getGoldByCategoryWithASingleCategoryPlaced(){
        ArrayList<District> newList = new ArrayList<>();
        newList.add(new District("Temple", 1, 1));
        newList.add(new District("Temple", 1, 1));
        newList.add(new District("Temple", 1, 1));
        bot.setDistrictPlaced(newList);
        int initialGold = bot.getGold();

        Character bishop = new Bishop();
        bishop.setCurrentBot(bot);
        bot.setCharacter(bishop);
        bot.getCharacter().getGoldPerDistrictCategory(CategoryEnum.RELIGIOUS);//Religious

        assertEquals(initialGold+3,bot.getGold());
    }

    @Test
    void getGoldByCategoryWithDifferentCategoryPlaced(){
        ArrayList<District> newList = new ArrayList<>();
        newList.add(new District("University",50,6798));
        newList.add(new District("Temple", 1, 1));
        newList.add(new District("MiddleSchool",50,6));
        newList.add(new District("School",50,6));
        newList.add(new District("Temple", 1, 1));
        newList.add(new District("NotSchool",50,-8));
        bot.setDistrictPlaced(newList);
        int initialGold = bot.getGold();

        Character bishop = new Bishop();
        bishop.setCurrentBot(bot);
        bot.setCharacter(bishop);
        bot.getCharacter().getGoldPerDistrictCategory(CategoryEnum.RELIGIOUS);//Religious

        assertEquals(initialGold+2,bot.getGold());
    }

    @Test
    void getCardOrGoldTest(){
        Character voleur = new Thief();
        voleur.setCurrentBot(bot);
        bot.setCharacter(voleur);
        int cards = bot.getHandSize();
        int golds = bot.getGold();

        bot.getCharacter().getCardOrGold();
        assertEquals(cards, bot.getHandSize());
        assertEquals(golds,bot.getGold());
    }

    @Test
    void placeDistrictTest(){
        Character voleur = new Thief();
        voleur.setCurrentBot(bot);
        bot.setCharacter(voleur);

        int cards = bot.getNumberDistrictPlaced();
        int golds = bot.getGold();

        bot.getCharacter().placeDistrict();
        assertEquals(cards, bot.getNumberDistrictPlaced());
        assertEquals(golds,bot.getGold());
    }
}