package citadelles.districts;

import citadelles.bots.Bot;

/**
 * use as a basis to create all districts
 */
public class District {

    private final String name;
    private final int value;
    private final int category;
    private Bot currentBot;
    private boolean canBeDestruct;
    private int placedDuringTurn;

    /**
     * Create a district card
     *
     * @param name     of the district
     * @param value    of the district
     * @param category of the district
     */
    public District(String name, int value, int category) {
        this.name = name;
        this.value = value;
        this.category = category;
        canBeDestruct = true;
        placedDuringTurn = 0;
    }

    /**
     * Get the name of the district
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value of the district
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the category of the district
     *
     * @return the category's ID
     */
    public int getCategory() {
        return category;
    }

    /**
     * Get the card value at end of the game
     *
     * @return card value
     */
    public int getEndGameValue() {
        return value;
    }

    /**
     * The action of the district
     *
     * NB: is overridden in extended class
     */
    public void action() {}

    /**
     * The action of a district when it's placed
     *
     * @param turn The current turn of the game
     */
    public void actionWhenConstruct(int turn) {
        placedDuringTurn = turn;
    }

    /**
     * Get the turn the district got placed
     *
     * @return The turn
     */
    public int getPlacedDuringTurn() {
        return placedDuringTurn;
    }

    /**
     * Det the current bot
     *
     * @return Bot object
     */
    public Bot getCurrentBot() {
        return currentBot;
    }

    /**
     * Initialise the current bot
     *
     * @param currentBot Bot object
     */
    public void setCurrentBot(Bot currentBot) {
        this.currentBot = currentBot;
    }

    /**
     * check if the district can be destruct or not
     *
     * @return True if it can be destruct, false otherwise
     */
    public boolean isCanBeDestruct() {
        return canBeDestruct;
    }

    /**
     * Change the value of the variable that indicates that the district can be destroyed
     *
     * @param isCanBeDestruct boolean
     */
    public void setCanBeDestruct(Boolean isCanBeDestruct) {
        this.canBeDestruct = isCanBeDestruct;
    }
}
