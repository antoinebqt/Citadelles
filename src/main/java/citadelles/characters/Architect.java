package citadelles.characters;

import citadelles.bots.Referee;
import citadelles.districts.District;

import java.util.ArrayList;

/**
 * contain the methods of the architect
 */
public class Architect extends Character {

    public Architect() {
        super(CharacterEnum.ARCHITECT);
    }

    /**
     * The architect get 2 cards when he picks cards
     */
    @Override
    public void startTurnAction() {
        getCurrentBot().pickCard(2);
    }

    /**
     * Can place from 0 to 3 districts in the turn
     */
    @Override
    public void action() {
        ArrayList<District> dataDistrictToPlace = getCurrentBot().logicArchitect();
        Referee referee = new Referee();
        if (dataDistrictToPlace != null){
            referee.placeCards(getCurrentBot(), dataDistrictToPlace);
        }

    }
}
