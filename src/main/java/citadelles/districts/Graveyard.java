package citadelles.districts;

/**
 * contain the methods of the Graveyard
 */
public class Graveyard extends District {

    /**
     * When the Warlord destroys a district, you can pay a gold coin
     * to take it back into your hand. You cannot do
     * this if you are a warlord yourself.
     */
    Graveyard() {
        super("Graveyard", 5, 5);
    }
}
