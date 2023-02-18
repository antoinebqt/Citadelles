package citadelles.characters;

import citadelles.bots.Referee;

/**
 * contain the methods of the assassin
 */
public class Assassin extends Character {

    public Assassin() {
        super(CharacterEnum.ASSASSIN);
    }

    /**
     * Kill a Bot in the turn
     */
    @Override
    public void action() {
        Referee referee = new Referee();
        int idRoleToKill = getCurrentBot().logicAssassin();
        referee.kill(getCurrentBot(), idRoleToKill);
    }


}
