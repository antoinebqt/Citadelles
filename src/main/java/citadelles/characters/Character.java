package citadelles.characters;

import citadelles.LogManager;
import citadelles.bots.Bot;
import citadelles.bots.Referee;
import citadelles.districts.CategoryEnum;

/**
 * abstract class that serve as basis for character classes
 */
public abstract class Character {
    private final int id;
    private final String role;
    private Bot currentBot;

    /**
     * Create a Character
     *
     * @param enumsCharacter list of character
     */
    protected Character(CharacterEnum enumsCharacter) {
        this.id = enumsCharacter.getId();
        this.role = enumsCharacter.getRole();
    }

    /**
     * Action of the character link to a Bot
     *
     * NB: is overridden by extended class
     */
    public void action() {
    }

    /**
     * Action of the character to the very start of his turn
     *
     * NB: is overridden by extended class
     */
    public void startTurnAction() {
    }

    /**
     * Action of the character link to a Bot
     *
     * NB: is overridden by extended class
     */
    public void earningsForDistricts() {
    }

    /**
     * Get the gold for having district in a the category of the character
     *
     * @param category The category
     */
    public void getGoldPerDistrictCategory(CategoryEnum category) {
        Referee referee = new Referee();
        referee.addGoldPerDistrictCategory(getCurrentBot(), category);
    }

    /**
     * get the current bot
     *
     * @return Bot object
     */
    public Bot getCurrentBot() {
        return currentBot;
    }

    /**
     * initialise the current bot
     *
     * @param currentBot Bot object
     */
    public void setCurrentBot(Bot currentBot) {
        this.currentBot = currentBot;
    }

    /**
     * get the ID of the current bot
     *
     * @return int
     */
    public int getCurrentBotID() {
        return currentBot.getID();
    }

    /**
     * get the character of the Bot in the current turn
     *
     * @return String
     */
    public String getRole() {
        return role;
    }

    /**
     * return the id of the character
     *
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Concatenate the log with new log
     *
     * @param log String
     */
    void addLog(String log) {
        LogManager.log += log + "\n";
    }

    public void getCardOrGold() {
        currentBot.chooseCardOrGold();
    }

    public void placeDistrict() {
        currentBot.eventuallyPlaceADistrict();
    }
}
