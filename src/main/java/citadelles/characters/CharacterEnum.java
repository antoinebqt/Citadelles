package citadelles.characters;

/**
 * give the game an enum to convert the id to the role of the character
 */
public enum CharacterEnum {
    ASSASSIN(0, "Assassin"),
    THIEF(1, "Thief"),
    MAGICIAN(2, "Magician"),
    KING(3, "King"),
    BISHOP(4, "Bishop"),
    MERCHANT(5, "Merchant"),
    ARCHITECT(6, "Architect"),
    WARLORD(7, "Warlord");

    private final int id;
    private final String role;

    /**
     * CharacterEnum constructor
     *
     * @param id The ID
     * @param role The role
     */
    CharacterEnum(int id, String role) {
        this.id = id;
        this.role = role;
    }

    /**
     * Get the ID of a character
     *
     * @return The id of a character
     */
    public int getId() {
        return id;
    }

    /**
     * Get the role of a character
     *
     * @return The role of the character in string
     */
    public String getRole() {
        return role;
    }
}
