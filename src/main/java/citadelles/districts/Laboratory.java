package citadelles.districts;

import citadelles.bots.Referee;

/**
 * contain the methods of the Laboratory
 */
public class Laboratory extends District {

    /**
     * Once per turn, you can discard a district card
     * from your hand and receive a gold coin for it
     */
    Laboratory() {
        super("Laboratory", 5, 5);
    }

    @Override
    public void action() {
        int indexOfCard = getCurrentBot().logicLaboratory();
        if (indexOfCard != -1) {
            Referee referee = new Referee();
            referee.get1GoldFor1Card(getCurrentBot(), indexOfCard);
        }
    }

}