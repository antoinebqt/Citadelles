package citadelles.characters;

import citadelles.bots.Referee;
import citadelles.districts.CategoryEnum;


/**
 * contain the methods of the king
 */
public class King extends Character {

    public King() {
        super(CharacterEnum.KING);
    }

    /**
     * The king get the crown for the next turn
     */
    public void startTurnAction() {
        Referee referee = new Referee();
        referee.setCrown(getCurrentBot());
    }

    /**
     *  Win a gold for each Noble district
     */
    @Override
    public void earningsForDistricts() {
        if (getCurrentBot().logicGetGoldPerDistrict())
            getGoldPerDistrictCategory(CategoryEnum.NOBLE);
    }
}
