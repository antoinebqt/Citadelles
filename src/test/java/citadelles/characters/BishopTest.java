package citadelles.characters;

import citadelles.bots.Bot;
import citadelles.bots.BotManager;
import citadelles.districts.CategoryEnum;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    private Character bishop;
    private Bot bot;

    @BeforeEach
    void setUp() {
        bishop = new Bishop();

        int botQuantity = 1;
        DistrictManager districtManager = new DistrictManager();
        CharacterManager characterManager = new CharacterManager(botQuantity);
        BotManager botManager = new BotManager(0,0,botQuantity, 0,districtManager);
        bot = botManager.getListBot().get(0);
        bishop.setCurrentBot(bot);
        bot.setCharacter(new Bishop());
    }

    @Test
    void specialActionZeroReligious() {
        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("Not religious", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not religious", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not religious", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not religious", 1, CategoryEnum.MILITARY.getValue()));
        bot.setDistrictPlaced(districts);

        int GoldBefore = bot.getGold();
        bishop.earningsForDistricts();
        int GoldAfter = bot.getGold();

        assertEquals(GoldBefore, GoldAfter);
    }

    @Test
    void specialActionTwoReligious() {
        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("Not religious", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not religious", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Religious", 1, CategoryEnum.RELIGIOUS.getValue()));
        districts.add(new District("Not religious", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Religious", 1, CategoryEnum.RELIGIOUS.getValue()));
        bot.setDistrictPlaced(districts);

        int GoldBefore = bot.getGold();
        bishop.earningsForDistricts();
        int GoldAfter = bot.getGold();

        assertEquals(GoldBefore+2, GoldAfter);
    }
}