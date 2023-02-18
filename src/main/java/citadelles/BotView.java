package citadelles;

import citadelles.bots.Bot;
import citadelles.bots.BotManager;
import citadelles.characters.Character;
import citadelles.districts.District;

import java.util.ArrayList;

/**
 * give a view for each bot with all public data
 * Those data are a copy and can't be used to manipulate the game directly
 */
public class BotView {
    private final BotManager botManager;
    private final ArrayList<District> districtsPlaced;
    private final int ID;
    private int Gold;
    private int nbCardHand;
    private Character character;

    /**
     * Bot Viewn constructor
     *
     * @param botManager The bot manager
     * @param id The id of the bot
     */
    BotView(BotManager botManager, int id) {
        this.botManager = botManager;

        character = null;

        districtsPlaced = new ArrayList<>();
        districtsPlaced.addAll(getBot(id).getDistrictPlaced());

        Gold = getBot(id).getGold();
        nbCardHand = getBot(id).getHandSize();
        ID = id;
    }

    /**
     * Get the bot with the ID
     *
     * @param id The ID
     * @return The bot with the given ID
     */
    private Bot getBot(int id) {
        return botManager.getListBot().get(id);
    }

    /**
     * Get the gold of the bot
     *
     * @return The gold
     */
    public int getGold() {
        return Gold;
    }

    /**
     * Get the number of card in hand
     *
     * @return The number of card in hand
     */
    public int getNbCardHand() {
        return nbCardHand;
    }

    /**
     * Get the district placed of the bot
     *
     * @return Arraylist of District containing the district placed
     */
    public ArrayList<District> getDistrictsPlaced() {
        return districtsPlaced;
    }

    /**
     * Get the size of the district placed list of the bot
     *
     * @return int of the districtPlaced list
     */
    public int getDistrictsPlacedSize() {
        return districtsPlaced.size();
    }

    /**
     * Get the bot ID
     *
     * @return The ID
     */
    public int getID() {
        return ID;
    }

    public void update() {
        districtsPlaced.clear();
        districtsPlaced.addAll(getBot(ID).getDistrictPlaced());

        Gold = getBot(ID).getGold();
        nbCardHand = getBot(ID).getHandSize();
        character = getBot(ID).getCharacter();
    }

    /**
     * Get the character of the bot
     *
     * @return The character
     */
    public Character getCharacter() {
        return character;
    }
}
