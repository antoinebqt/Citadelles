package citadelles.districts;

/**
 * contain the methods of the Keep
 */
public class Keep extends District {
    /**
     * The Keep cannot be destroyed by the Warlord.
     */
    public Keep() {
        super("Keep", 3, 5);
        setCanBeDestruct(false);
    }
}
