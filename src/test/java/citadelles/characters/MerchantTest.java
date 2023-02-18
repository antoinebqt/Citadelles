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

class MerchantTest {
    private Character merchant;
    private Bot bot;

    @BeforeEach
    void setUp() {
        merchant = new Merchant();

        int botQuantity = 1;
        DistrictManager districtManager = new DistrictManager();
        CharacterManager characterManager = new CharacterManager(botQuantity);
        BotManager botManager = new BotManager(0,0,botQuantity, 0,districtManager);
        bot = botManager.getListBot().get(0);
        merchant.setCurrentBot(bot);
        bot.setCharacter(new Merchant());
    }

    @Test
    void specialActionZeroTrade() {
        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("Not Trade", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not Trade", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not Trade", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not Trade", 1, CategoryEnum.MILITARY.getValue()));
        bot.setDistrictPlaced(districts);

        int GoldBefore = bot.getGold();
        merchant.earningsForDistricts();
        int GoldAfter = bot.getGold();

        assertEquals(GoldBefore, GoldAfter);
    }

    @Test
    void specialActionThreeTrade() {
        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("Not Trade", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not Trade", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Not Trade", 1, CategoryEnum.MILITARY.getValue()));
        districts.add(new District("Trade", 1, CategoryEnum.TRADE.getValue()));
        districts.add(new District("Trade", 1, CategoryEnum.TRADE.getValue()));
        districts.add(new District("Trade", 1, CategoryEnum.TRADE.getValue()));

        bot.setDistrictPlaced(districts);

        int GoldBefore = bot.getGold();
        merchant.earningsForDistricts();
        int GoldAfter = bot.getGold();

        assertEquals(GoldBefore+3, GoldAfter);
    }
}