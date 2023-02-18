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

class KingTest {
    private Character king;
    private Bot bot;

    @BeforeEach
    void setUp() {
        king = new King();

        int botQuantity = 1;
        DistrictManager districtManager = new DistrictManager();
        CharacterManager characterManager = new CharacterManager(botQuantity);
        BotManager botManager = new BotManager(0,0,botQuantity, 0,districtManager);
        bot = botManager.getListBot().get(0);
        king.setCurrentBot(bot);
        bot.setCharacter(new King());
    }

    @Test
    void specialActionZeroNoble() {
        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("Not noble", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not noble", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not noble", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not noble", 1, CategoryEnum.MILITARY.getValue()));
        bot.setDistrictPlaced(districts);

        int GoldBefore = bot.getGold();
        king.earningsForDistricts();
        int GoldAfter = bot.getGold();

        assertEquals(GoldBefore, GoldAfter);
    }

    @Test
    void specialActionThreeNoble() {
        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("Noble", 1, CategoryEnum.NOBLE.getValue()));
        districts.add(new District("Not noble", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not noble", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Noble", 1, CategoryEnum.NOBLE.getValue()));
        districts.add(new District("Not noble", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Noble", 1, CategoryEnum.NOBLE.getValue()));
        bot.setDistrictPlaced(districts);

        int GoldBefore = bot.getGold();
        king.earningsForDistricts();
        int GoldAfter = bot.getGold();

        assertEquals(GoldBefore+3, GoldAfter);
    }
}