package citadelles.districts;

/**
 * contain the methods of the dragonGate
 */
public class DragonGate extends District {
    /**
     * This prestigious achievement (a dragon has not been seen in
     * the Kingdom for nearly a thousand years)
     * costs six gold coins to build but is worth
     * eight points in the endgame tally.
     */
    public DragonGate() {
        super("Dragon Gate", 6, 5);
    }

    /**
     * Get the card value at end of the game
     *
     * @return The value of the district at the end
     */
    @Override
    public int getEndGameValue() {
        return 8;
    }
}
