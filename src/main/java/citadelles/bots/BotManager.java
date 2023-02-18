package citadelles.bots;

import citadelles.Hand;
import citadelles.characters.CharacterEnum;
import citadelles.districts.DistrictManager;

import java.util.ArrayList;

/**
 * The class that create the bot
 */
public class BotManager {

    private final int numberOfBots;
    private final ArrayList<Bot> listBot;
    private final int nbSmartBot;
    private final int nbMediumBot;
    private final int nbStupidBot;
    private final int nbTricTracBot;

    /**
     * Initialize the number of bot and create a list of those bots.
     */
    public BotManager(int nbSmartBot, int nbMediumBot, int nbStupidBot, int nbTricTracBot, DistrictManager districtManager) {

        this.nbSmartBot = nbSmartBot;
        this.nbMediumBot = nbMediumBot;
        this.nbStupidBot = nbStupidBot;
        this.nbTricTracBot = nbTricTracBot;

        numberOfBots = getNumberOfBots();
        listBot = new ArrayList<>();
        int botId = 0;

        for (int i = 0; i < nbSmartBot; i++) {
            listBot.add(new SmartBot(botId++, new Hand(districtManager)));
        }
        for (int i = 0; i < nbMediumBot; i++) {
            listBot.add(new MediumBot(botId++, new Hand(districtManager)));
        }
        for (int i = 0; i < nbStupidBot; i++) {
            listBot.add(new StupidBot(botId++, new Hand(districtManager)));
        }
        for (int i = 0; i < nbTricTracBot; i++) {
            listBot.add(new RichardBot(botId++, new Hand(districtManager)));
        }
    }

    /**
     * Get the number of bots
     *
     * @return The total of bot in the game
     */
    public int getNumberOfBots() {
        return nbSmartBot + nbMediumBot + nbStupidBot + nbTricTracBot;
    }

    /**
     * Get the crowned bot id
     *
     * @return The id of the bot who has the crown
     */
    public int getTheBotWithTheCrown() {

        for (int i = 0; i < numberOfBots; i++) {
            if (getListBot().get(i).doHaveTheCrown()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * If a player was the king, we remove the crown of the current king
     * else the current king keeps the crown for the next turn
     */
    public void changeCrownedPlayer(int oldKingId) {
        if ((someoneWasTheKing() != -1) && (someoneWasTheKing() != oldKingId)) {
            getListBot().get(oldKingId).removeCrown();
        }
    }

    /**
     * Check if a bot was the king during the turn
     *
     * @return a boolean
     */
    private int someoneWasTheKing() {
        for (int i = 0; i < numberOfBots; i++) {
            if (getListBot().get(i).getCharacterId() == CharacterEnum.KING.getId() && getListBot().get(i).isAlive()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * At the start of the game, a random bot get the crown and will start first
     * during the first turn
     *
     * @param numberOfBots The number of bot playing
     */
    public void setTheCrownToARandomBot(int numberOfBots) {
        int rdm = (int) (Math.random() * numberOfBots);
        getListBot().get(rdm).setCrown();
    }

    /**
     * Get the list of bot.
     *
     * @return a List of Object who change corresponding to bots initialize.
     */
    public ArrayList<Bot> getListBot() {
        return listBot;
    }
}
