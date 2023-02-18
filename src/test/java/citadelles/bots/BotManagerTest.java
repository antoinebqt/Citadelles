package citadelles.bots;

import citadelles.Hand;
import citadelles.characters.CharacterManager;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BotManagerTest {

    private int nombreBots;
    private BotManager botManager;
    ArrayList<StupidBot> list;
    @BeforeEach
    void setUp(){
        DistrictManager districtManager = new DistrictManager();
        nombreBots = 4;
        CharacterManager characterManager = new CharacterManager(nombreBots);
        characterManager.init();
        botManager = new BotManager(0,0, nombreBots, 0,districtManager);

        list = new ArrayList<>();
        list.add(new StupidBot(0, new Hand(districtManager)));
        list.add(new StupidBot(1, new Hand(districtManager)));
    }

    @Test
    void getNombreBot(){
        assertEquals(nombreBots,botManager.getNumberOfBots());
    }

    @Test
    void getListBot(){
        assertEquals(list.get(0).getID(),botManager.getListBot().get(0).getID());
        assertEquals(list.get(1).getID(),botManager.getListBot().get(1).getID());
    }

    /*
    @Test
    void crownManipulation(){
        botManager.getListBot().forEach(Bot::chooseCharacter);
        assertEquals(-1,botManager.getTheBotWithTheCrown());
        botManager.getListBot().get(0).setCrown();
        assertEquals(0,botManager.getTheBotWithTheCrown());

        botManager.getListBot().get(1).setCharacter(new Character(3,"King") {});
        botManager.getListBot().get(2).setCharacter(new Character(6,"ChefKing") {});
        botManager.getListBot().get(3).setCharacter(new Character(4,"KingKong") {});
        botManager.getListBot().get(0).setCharacter(new Character(5,"NotKing") {});

        assertEquals(0,botManager.changeCrownedPlayer(0));
        assertEquals(-1,botManager.getTheBotWithTheCrown());
    }
*/
    @Test
    void crownToARandomBot(){
        assertEquals(-1,botManager.getTheBotWithTheCrown());
        botManager.setTheCrownToARandomBot(4);
        assertNotEquals(-1,botManager.getTheBotWithTheCrown());
    }

}