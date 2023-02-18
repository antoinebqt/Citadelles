package citadelles;

import citadelles.bots.Bot;
import citadelles.bots.BotManager;
import citadelles.bots.Referee;
import citadelles.characters.CharacterManager;
import citadelles.districts.CategoryEnum;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.GREEN_TEXT;

/**
 * Main class of the project. Used to create and manipulate all the game
 * is used by the main class
 */
public class Citadelles {
    private static BotManager botManager;
    private final LogManager logManager;
    private final CharacterManager characterManager;
    private static final Logger normalExecution = Logger.getLogger("Normal");
    private static final Logger simulationExecution = Logger.getLogger("Simulation");
    private final int numberOfBots;
    private final int nbSmartBot;
    private final int nbMediumBot;
    private final int nbStupidBot;
    private final int nbTricTracBot;
    private int finishPosition;
    private int finishTurn;

    /**
     * Constructor of the game.
     * Call the botManager class to initialize bots.
     */
    Citadelles(int nbSmartBot, int nbMediumBot, int nbStupidBot, int nbTricTracBot) {
        this.nbSmartBot = nbSmartBot;
        this.nbMediumBot = nbMediumBot;
        this.nbStupidBot = nbStupidBot;
        this.nbTricTracBot = nbTricTracBot;

        numberOfBots = getNumberOfBots();
        characterManager = new CharacterManager(numberOfBots);
        botManager = new BotManager(nbSmartBot, nbMediumBot, nbStupidBot, nbTricTracBot, new DistrictManager());
        logManager = new LogManager(botManager);
        finishPosition = 1;
        finishTurn = 0;
    }

    /**
     * Get the bot manager
     *
     * @return The bot manager
     */
    public static BotManager getBotManager() {
        return botManager;
    }

    /**
     * Get the number of bots
     *
     * @return The number of bot
     */
    public int getNumberOfBots() {
        return nbSmartBot + nbMediumBot + nbStupidBot + nbTricTracBot;
    }

    /**
     * Method who launch the game.
     */
    void launch() {
            int crowned;
            boolean isEnd = false;

            botManager.setTheCrownToARandomBot(numberOfBots);
            GameView gameView = new GameView(botManager, characterManager);

            int i = 1;
            while (!isEnd) {
                //Reset the list of character
                characterManager.init();

                normalExecution.log(Level.INFO, colorize("++++++++++++++++++++++++++++ Turn : " + (i) + " ++++++++++++++++++++++++++++", GREEN_TEXT()));

                crowned = botManager.getTheBotWithTheCrown();
                logManager.showTheCrownedBot(crowned);

                characterTurn(crowned, gameView);
                showCharacters();

                isEnd = gameTurn(gameView, i);

                showEndLog();
                logManager.showResume();

                i++;
                botManager.changeCrownedPlayer(crowned);

                if (i > 100) break;
            }
    }

    /**
     * Initialise the characters of each player
     * the order of choice is defined by the crowned player
     *
     * @param crowned int
     */
    private void characterTurn(int crowned, GameView gameView) {
        for (int j = 0; j < numberOfBots; j++) {
            getBots().get(j).setAlive();
            characterManager.pickPlayerCharacter(getBots().get((j + crowned) % numberOfBots));
        }
        gameView.updateCharacterList();
    }

    /**
     * Check for each role, if a bot has this role. If yes -> the bot play, if no -> continue
     */

    private boolean gameTurn(GameView gameView, int turn) {
        AtomicBoolean isEnd = new AtomicBoolean(false);

        for (int k = 0; k < 8; k++) {
            int finalK = k;
            getBots().forEach(bot -> {
                if (finalK == bot.getCharacterId()) {
                    bot.play(gameView, turn);
                    gameView.updateView();
                    showLog();
                    if (result(bot, turn)) {
                        isEnd.set(true);
                    }
                }
            });
        }
        return isEnd.get();
    }

    /**
     * Check if the game is finish or not
     *
     * @return boolean
     */
    public boolean result(Bot bot, int turn) {

        if (bot.getNumberDistrictPlaced() >= 7) {
            bot.setFinishPosition(finishPosition++);
            setFinishTurn(turn);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Print the result
     */
    void showResult() throws IOException {
        finalScoreCalculator();
        logManager.showResult();
    }

    void showSimulation(){
        logManager.showLogSimulation();
    }

    /**
     * Calculate the final score of each bot by checking
     * the district placed and the finish position
     */
    public void finalScoreCalculator() {
        for (Bot bot : getBots()) {

            if (bot.getFinishPosition() == 1) {
                bot.addScore(4);
            } else if (bot.getFinishPosition() > 1) {
                bot.addScore(2);
            }

            ArrayList<District> districtsPlaced = bot.getDistrictPlaced();
            for (District district : districtsPlaced) {
                bot.addScore(district.getEndGameValue());
            }

            if (doHaveAllCategoriesPlaced(bot)) {
                bot.addScore(3);
            }
        }
    }

    /**
     * Check if a bot has all the categories placed
     *
     * @param bot The bot to check
     * @return True if yes, false otherwise
     */
    public boolean doHaveAllCategoriesPlaced(Bot bot) {
        ArrayList<District> districtPlaced = bot.getDistrictPlaced();

        if (districtPlaced.size() < CategoryEnum.values().length) {
            return false;
        }

        ArrayList<Integer> categoryPlaced = new ArrayList<>();

        for (District district : districtPlaced) {
            if (!(categoryPlaced.contains(district.getCategory()))) {
                categoryPlaced.add(district.getCategory());
            }
        }

        if (categoryPlaced.size() >= CategoryEnum.values().length) return true;

        return canUseThePowerOfMiracleCourtyard(districtPlaced, categoryPlaced.size());
    }

    /**
     * Check if the bot can use the power of the miracle of courtyard
     *
     * @param districtPlaced The district placed by the bot
     * @param numberOfCategory His current number of categories placed
     * @return True if he can, false otherwise
     */
    public boolean canUseThePowerOfMiracleCourtyard(ArrayList<District> districtPlaced, int numberOfCategory) {

        //Check if the bot has the Miracle Courtyard placed
        int indexOfTheMiracleCourtyard = doHaveTheMiracleCourtyard(districtPlaced);
        if (indexOfTheMiracleCourtyard == -1) {
            return false;
        }

        //The bot must has already 4 categories placed for using the Miracle Courtyard
        if (numberOfCategory != 4) {
            return false;
        }

        //Check if the miracle courtyard has been constructed during the last turn
        if (districtPlaced.get(indexOfTheMiracleCourtyard).getPlacedDuringTurn() == finishTurn) {
            return false;
        }

        Referee referee = new Referee();
        //Check if the bot has atleast 2 district of category unique
        return referee.numberDistrictInCategory(districtPlaced, CategoryEnum.UNIQUE.getValue()) > 1;
    }

    /**
     * Check if the miracle of courtyard is present
     * in a given arraylist of District object
     *
     * @param districtPlaced The district placed by the bot
     * @return True if the bot has the miracle of courtyard district, false otherwise
     */
    private int doHaveTheMiracleCourtyard(ArrayList<District> districtPlaced) {
        for (int i = 0; i < districtPlaced.size(); i++) {
            if (Objects.equals(districtPlaced.get(i).getName(), "Miracle Courtyard")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the list of bots
     *
     * @return the bot list
     */
    ArrayList<Bot> getBots() {
        return botManager.getListBot();
    }

    void showLog() {
        LogManager.showLog();
    }

    void showEndLog() {
        LogManager.showEndLog();
    }

    void showCharacters() {
        LogManager.showCharacterLog();
    }

    void setFinishTurn(int turn) {
        finishTurn = turn;
    }
}
