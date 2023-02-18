package citadelles.districts;

import citadelles.bots.Referee;

/**
 * contain the methods of the Factory
 */
public class Factory extends District {

    /**
     * Once per turn, you can pay three gold
     * coins to draw three cards.
     */
    Factory() {
        super("Factory", 5, 5);
    }

    @Override
    public void action() {
        if (getCurrentBot().logicFactory()) {
            Referee referee = new Referee();
            referee.get3CardsFor3Gold(getCurrentBot());
        }
    }
}
