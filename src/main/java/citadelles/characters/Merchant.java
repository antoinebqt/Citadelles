package citadelles.characters;

import citadelles.bots.Bot;
import citadelles.districts.CategoryEnum;

import static citadelles.LogManager.defaultColor;
import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.TEXT_COLOR;

/**
 * contain the methods of the merchant
 */
public class Merchant extends Character {

    public Merchant() {
        super(CharacterEnum.MERCHANT);
    }

    /**
     * The merchant gains 1 gold at the start of the turn
     */
    public void startTurnAction() {
        getCurrentBot().addGold(1);
        addLog(Bot.addBotNumberLog(getCurrentBotID())
                + defaultColor(" gains ")
                + colorize("1 gold", TEXT_COLOR(255, 215, 0))
                + defaultColor(" at the start"));
    }

    /**
     *  Win a gold for each Trade district
     */
    @Override
    public void earningsForDistricts() {
        if (getCurrentBot().logicGetGoldPerDistrict())
            getGoldPerDistrictCategory(CategoryEnum.TRADE);
    }
}
