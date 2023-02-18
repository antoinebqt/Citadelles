package citadelles.characters;

import citadelles.bots.Bot;
import citadelles.bots.Referee;
import citadelles.districts.District;

import java.util.ArrayList;

import static citadelles.LogManager.defaultColor;

/**
 * contain the methods of the magician
 */
public class Magician extends Character {

    public Magician() {
        super(CharacterEnum.MAGICIAN);
    }

    /**
     * The magician can exchange his hand with
     * another player or exchange his card with the deck
     */
    @Override
    public void action() {
        int exchange = getCurrentBot().logicMagician();
        Referee referee = new Referee();

        if (exchange >= 0) {
            referee.exchangeCards(exchange, getCurrentBotID());
        } else if (exchange == -1) {
            ArrayList<District> toDiscard = getCurrentBot().discardList();
            int size = toDiscard.size();

            getCurrentBot().getHand().discard(toDiscard);

            getCurrentBot().pickCard(size);
            addLog(Bot.addBotNumberLog(getCurrentBotID()) + defaultColor(" discards ") + size
                    + defaultColor(" districts and takes ") + size + defaultColor(" new districts"));
        }
    }
}
