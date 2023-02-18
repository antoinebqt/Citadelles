package citadelles.characters;

import citadelles.bots.Referee;
import citadelles.districts.CategoryEnum;

/**
 * contain the methods of the warlord
 */
public class Warlord extends Character {

    public Warlord() {
        super(CharacterEnum.WARLORD);
    }

    /**
     *  Win a gold for each Military district
     */
    @Override
    public void earningsForDistricts() {
        if (getCurrentBot().logicGetGoldPerDistrict())
            getGoldPerDistrictCategory(CategoryEnum.MILITARY);
    }

    /**
     * The warlord can destruct a district of another player
     */
    @Override
    public void action() {
        int[] dataDestruction = getCurrentBot().logicWarlord();
        if (dataDestruction != null) {
            Referee referee = new Referee();
            referee.destructOneDistrict(getCurrentBot(), dataDestruction);
        }
    }
}
