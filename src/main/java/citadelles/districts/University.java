package citadelles.districts;

/**
 * contain the methods of the University
 */
public class University extends District {
    public University() {
        super("University", 6, 5);
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
