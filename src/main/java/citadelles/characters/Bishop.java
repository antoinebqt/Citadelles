package citadelles.characters;

import citadelles.districts.CategoryEnum;

/**
 * contain the methods of the bishop
 */
public class Bishop extends Character {

    public Bishop() {
        super(CharacterEnum.BISHOP);
    }

    /**
     * Win a gold for each Religious district
     */
    @Override
    public void earningsForDistricts() {
        if (getCurrentBot().logicGetGoldPerDistrict())
            getGoldPerDistrictCategory(CategoryEnum.RELIGIOUS);
    }
}
