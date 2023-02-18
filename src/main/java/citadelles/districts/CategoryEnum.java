package citadelles.districts;

/**
 * give the game an enum to convert the value to the name of the category
 */
public enum CategoryEnum {

    RELIGIOUS(1, "Religious"),
    NOBLE(2, "Noble"),
    TRADE(3, "Trade"),
    MILITARY(4, "Military"),
    UNIQUE(5, "Unique");

    private final int value;
    private final String name;

    /**
     * The category enum constructor
     *
     * @param value The value
     * @param name The name
     */
    CategoryEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * Get the value
     *
     * @return The value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the name
     *
     *
     * @return The name
     */
    public String getName() {
        return name;
    }
}
