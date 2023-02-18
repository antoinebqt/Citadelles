package citadelles.characters;

import citadelles.bots.Referee;

/**
 * contain the methods of the thief
 */
public class Thief extends Character {

    public Thief() {
        super(CharacterEnum.THIEF);
    }

    /**
     * The thief can steal all the gold from a character
     */
    @Override
    public void action() {
        int charToSteal = getCurrentBot().logicThief();
        if (charToSteal != -1) {
            Referee referee = new Referee();
            referee.steal(charToSteal, getCurrentBot().getCharacterId());
        }
    }
}
