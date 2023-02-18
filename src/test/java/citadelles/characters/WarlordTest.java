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

class WarlordTest {
    private Character warlord;
    private Bot bot;

    @BeforeEach
    void setUp() {
        warlord = new Warlord();

        int botQuantity = 1;
        DistrictManager districtManager = new DistrictManager();
        BotManager botManager = new BotManager(0,0,botQuantity, 0,districtManager);
        bot = botManager.getListBot().get(0);
        warlord.setCurrentBot(bot);
        bot.setCharacter(new Warlord());
    }

    @Test
    void earningsForDistrictsZeroMilitary() {
        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("Not military", 1, CategoryEnum.NOBLE.getValue()));
        districts.add(new District("Not military", 1, CategoryEnum.NOBLE.getValue()));
        districts.add(new District("Not military", 1, CategoryEnum.NOBLE.getValue()));
        districts.add(new District("Not military", 1, CategoryEnum.NOBLE.getValue()));
        bot.setDistrictPlaced(districts);

        int GoldBefore = bot.getGold();
        warlord.earningsForDistricts();
        int GoldAfter = bot.getGold();

        assertEquals(GoldBefore, GoldAfter);
    }

    @Test
    void earningsForDistrictsThreeMilitary() {
        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("Not military", 1, CategoryEnum.NOBLE.getValue()));
        districts.add(new District("Not military", 1, CategoryEnum.NOBLE.getValue()));
        districts.add(new District("Not military", 1, CategoryEnum.NOBLE.getValue()));
        districts.add(new District("Military", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Military", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Military", 1, CategoryEnum.MILITARY.getValue()));
        bot.setDistrictPlaced(districts);

        int GoldBefore = bot.getGold();
        warlord.earningsForDistricts();
        int GoldAfter = bot.getGold();

        assertEquals(GoldBefore+3, GoldAfter);
    }
}