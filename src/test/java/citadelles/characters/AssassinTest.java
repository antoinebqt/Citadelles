package citadelles.characters;

import citadelles.bots.Bot;
import citadelles.bots.BotManager;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AssassinTest {
    Character assassin;
    Character king;
    Character bishop;

    Bot assassinBot;
    Bot kingBot;
    Bot bishopBot;

    ArrayList<Bot> botList;
    BotManager botManager;

    @BeforeEach
    void setUp(){
        assassin = new Assassin();
        king = new King();
        bishop = new Bishop();

        int botQuantity = 3;

        DistrictManager districtManager = new DistrictManager();
        CharacterManager characterManager = new CharacterManager(botQuantity);
        botManager = new BotManager(0,0,botQuantity, 0, districtManager);

        assassinBot = botManager.getListBot().get(0);
        kingBot = botManager.getListBot().get(1);
        bishopBot = botManager.getListBot().get(2);

        assassinBot.setCharacter(assassin);
        kingBot.setCharacter(king);
        bishopBot.setCharacter(bishop);

        botList = new ArrayList<>();
        botList.add(assassinBot);
        botList.add(kingBot);
        botList.add(bishopBot);
    }


/*
    @Test
    void assassinWantToKillTheKing(){
        int idToKill = assassin.endTurnAction(botList,3);
        assertEquals(1,idToKill);
    }

    @Test
    void assassinWantToKillTheBishop(){
        int idToKill = assassin.endTurnAction(botList,4);
        assertEquals(2,idToKill);
    }
*/
/*
    @Test
    void verifyWhoIsAliveOrNotWhenAssassinKilledTheKing(){
        int idToKill = assassin.endTurnAction(botList,3);
        botManager.getListBot().get(idToKill).setDead();

        assertTrue(assassinBot.isAlive());
        assertFalse(kingBot.isAlive());
        assertTrue(bishopBot.isAlive());
    }*/

}
