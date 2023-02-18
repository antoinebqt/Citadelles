package citadelles.bots;

import citadelles.*;
import citadelles.characters.Character;
import citadelles.districts.CategoryEnum;
import citadelles.districts.District;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static citadelles.LogManager.defaultColor;
import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

/**
 * The class above the other bots containing the methods useful for the proper functioning of the bots
 */
public class Bot {

    private final int id;
    private final Hand hand;
    private final ArrayList<District> districtPlaced;
    private int gold;
    private int score;
    private int finishPosition;
    private Character character;
    private boolean haveTheCrown;
    private boolean alive;
    private GameView gameView;
    private int currentTurn;

    /**
     * Bot's constructor
     *
     * @param id of the bot that will be created
     * @param hand The hand of the bot containing his cards
     */
    public Bot(int id, Hand hand) {
        this.hand = hand;
        this.districtPlaced = new ArrayList<>();
        this.gold = 2;
        this.id = id;
        this.score = 0;
        this.finishPosition = 0;
        this.character = null;
        this.haveTheCrown = false;
        this.alive = true;
        this.currentTurn = 0;
    }

    /**
     * The method who make the bot play
     *
     * @param gameView object that gave the global aspect of the game
     */
    public void play(GameView gameView, int turn) {

        this.gameView = gameView;
        this.currentTurn = turn;

        if (isAlive()) {
            addLog(addBotNumberLog(getID())
                    + defaultColor(" is the ")
                    + colorize(getCharacterRole(), RED_TEXT()));
            getCharacter().startTurnAction();
            getCharacter().getCardOrGold();
            getCharacter().placeDistrict();
            getCharacter().earningsForDistricts();
            getCharacter().action();
            districtAction();
        } else {
            addEndLog(addBotNumberLog(getID())
                    + defaultColor(" was killed by the ")
                    + colorize("Assassin", RED_TEXT())
                    + defaultColor(" he was the ")
                    + colorize(getCharacterRole(), RED_TEXT()));
        }
    }

    /**
     * Check if a given character ID is a character who has been
     * discarded at the start of the turn
     */
    boolean isDiscarded(int id){
        ArrayList<Character> discardedCharacterList = getGameView().getVisibleDiscarded();
        for (Character character : discardedCharacterList) {
            if (id == character.getId()) return true;
        }
        return false;
    }

    /**
     * Make the action related to the district placed by the bot
     */
    private void districtAction() {
        getDistrictPlaced().forEach((district) -> {
            district.setCurrentBot(this);
            district.action();
        });
    }

    /**
     * Logic to choose if the bot will draw card or get gold
     *
     * NB: overridden by the current bot
     */
    public void chooseCardOrGold() {}

    /**
     * Method choosing if the bot will construct a district or not
     *
     * NB: overridden by the current bot
     */
    public void eventuallyPlaceADistrict() {}

    /**
     * Prevent district to be placed multiple time
     *
     * @param name Name of the district
     * @return A boolean, true if district is already placed, false otherwise
     */
    boolean isDistrictAlreadyPlaced(String name) {

        if (districtPlaced.size() > 0) {
            for (District district : districtPlaced) {
                if (name.equals(district.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Add the card index in the list of district placed by the bot
     * and remove it from his hand
     *
     * @param index the index of a Card in the hand list.
     */
    public void placeCard(int index) {

        if (!(isDistrictAlreadyPlaced(getHandList().get(index).getName()))) {
            addLog(addBotNumberLog(id)
                    + defaultColor(" place the district: ")
                    + colorize(String.valueOf(getHandList().get(index).getName()), GREEN_TEXT())
                    + defaultColor(" for a cost of ")
                    + printGold(getHandList().get(index).getValue()));
            districtPlaced.add(getHandList().get(index));
            districtPlaced.get(districtPlaced.size() - 1).actionWhenConstruct(getCurrentTurn());
            gold -= getHandList().get(index).getValue();
            getHandList().remove(index);
        }
    }

    /**
     * Get the current turn of the game
     *
     * @return An int, the current turn
     */
    private int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Add a certain number of card to the hand list.
     *
     * @param quantity The quantity of card to pick from the deck
     */
    public void pickCard(int quantity) {

        ArrayList<District> districtsPicked = getHand().pullDistrict(quantity);
        getHand().addAllCards(districtsPicked);
        addLog(addBotNumberLog(id) + defaultColor(" pick " + districtsPicked.size()
                + (districtsPicked.size() > 1 ? " cards" : " card")));
    }

    /**
     * Add card to the hand of the bot, but check before if he has the "Observatory" or the "Library"
     * district placed that change the way of getting card.
     *
     * If the bot has the Observatory, he will choose 1 card among 3.
     * If the bot has the Library, he will pick 2 card.
     * If he has both the Observatory and the Library, he will act like he has only the Observatory.
     * If he has neither the Observatory nor the Library, he will choose 1 card among 2.
     */
    public void chooseAddCard() {

        int cardToDraw;

        if (isDistrictAlreadyPlaced("Observatory")) {

            cardToDraw = 3;
            ArrayList<District> districtsPicked = getHand().pullDistrict(cardToDraw);

            if (districtsPicked.size() > 0) {
                District districtChosen = chooseOneCard(districtsPicked);
                getHand().addCardChosen(districtsPicked, districtChosen);
                addLog(addBotNumberLog(id)
                        + defaultColor(" use the power of the ")
                        + colorize("Observatory", GREEN_TEXT())
                        + defaultColor(" and pick 1 card"));
            }
        } else {
            cardToDraw = 2;
            ArrayList<District> districtsPicked = getHand().pullDistrict(cardToDraw);

            if (districtsPicked.size() > 0) {
                if (isDistrictAlreadyPlaced("Library")) {
                    getHand().addAllCards(districtsPicked);
                    addLog(addBotNumberLog(id)
                            + defaultColor(" use the power of the ")
                            + colorize("Library", GREEN_TEXT())
                            + defaultColor(" and get 2 cards"));
                } else {
                    District districtChosen = chooseOneCard(districtsPicked);
                    getHand().addCardChosen(districtsPicked, districtChosen);
                    addLog(addBotNumberLog(id) + defaultColor(" pick 1 card"));
                }
            }
        }
    }

    /**
     * Choose one district in a list
     *
     * @param districtsPicked The arraylist containing the different districts proposed to the bot
     * @return An object District, the chosen one
     *
     * NB: overridden by the current bot
     */
    public District chooseOneCard(ArrayList<District> districtsPicked) {
        return districtsPicked.get(0);
    }

    /**
     * If the bot chose to get gold instead of card, 2 gold will be added to his wallet
     */
    void chooseAddGold() {
        addLog(addBotNumberLog(id) + defaultColor(" get ") + printGold(2));
        gold += 2;
    }

    /**
     * Add a certain int value to the current score of the bot
     *
     * @param value The score to add
     */
    public void addScore(int value) {
        score += value;
    }

    /**
     * Add a certain amount of Gold to the bot wallet
     *
     * @param amount How many counts should be added
     */
    public void addGold(int amount) {
        if (amount > 0 && amount < 100) this.gold += amount;
    }

    /**
     * Get the size of the bot hand
     *
     * @return the size of the hand
     */
    public int getHandSize() {
        return getHand().getListCardSize();
    }

    /**
     * Get the hand of the bot
     *
     * @return an object Hand containing a list with the cards in the bot hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Get the number of golds of the bot
     *
     * @return the golds
     */
    public int getGold() {
        return gold;
    }

    /**
     * Set the Gold of the bot to a certain value
     *
     * @param value The value to set to the bot wallet
     */
    void setGold(int value) {
        if (value >= 0 && value <= 100)  gold = value;
    }

    /**
     * Get the bot id
     *
     * @return The bot it
     */
    public int getID() {
        return id;
    }

    /**
     * Get the bot score
     *
     * @return The score of the bot
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the district already build by the bot
     *
     * @return The list of district placed by the bot
     */
    public ArrayList<District> getDistrictPlaced() {
        return districtPlaced;
    }

    /**
     * Set a new list of district placed like it was build by the bot
     *
     * @param newDistrictPlaced the arraylist containing the districts to set as placed to the bot
     */
    public void setDistrictPlaced(ArrayList<District> newDistrictPlaced) {
        districtPlaced.addAll(newDistrictPlaced);
    }

    /**
     * Get the list from the hand of the bot
     *
     * @return An arraylist of district card not yet placed by the bot
     */
    public ArrayList<District> getHandList() {
        return hand.getListCard();
    }

    /**
     * Set a new list of district in the hand of the bot
     *
     * @param newHand the arraylist containing the districts to set in the hand of the bot
     */
    public void setHandList(ArrayList<District> newHand) {
        hand.setListCard(newHand);
    }

    /**
     * Get the number of district already build by the bot
     *
     * @return The number of district placed by the bot
     */
    public int getNumberDistrictPlaced() {
        return districtPlaced.size();
    }

    /**
     * Get the finish position of the bot
     *
     * @return The finish position of the bot, start at 1
     */
    public int getFinishPosition() {
        return finishPosition;
    }

    /**
     * Set the finish position of the bot
     *
     * @param position The finish position to set
     */
    public void setFinishPosition(int position) {
        finishPosition = position;
    }

    /**
     * Get the character's id of the bot
     *
     * @return the role's id
     */
    public int getCharacterId() {
        return character.getId();
    }

    /**
     * Get the character's role name
     *
     * @return A string of the character's role name
     */
    public String getCharacterRole() {
        return character.getRole();
    }

    /**
     * Return the district with the highest value of the hand
     *
     * @return The district with the highest value of the hand
     */
    District getDistrictHandMaxValue() {
        District districtMaxValue = null;
        for (District district : hand.getListCard()) {
            if (districtMaxValue != null) {
                if (district.getValue() > districtMaxValue.getValue()) {
                    boolean alreadyPlaced = false;
                    for (District districtPlaced : districtPlaced) {
                        if (district.getName().equals(districtPlaced.getName())) {
                            alreadyPlaced = true;
                            break;
                        }
                    }
                    if (!alreadyPlaced) districtMaxValue = district;
                }
            } else {
                boolean alreadyPlaced = false;
                for (District districtPlaced : districtPlaced) {
                    if (district.getName().equals(districtPlaced.getName())) {
                        alreadyPlaced = true;
                        break;
                    }
                }
                if (!alreadyPlaced) districtMaxValue = district;
            }
        }
        return districtMaxValue;
    }

    /**
     * Return the district with the lowest value of the hand
     *
     * @return The district with the lowest value of the hand
     */
    District getDistrictHandMinValue() {
        District districtMinValue = null;
        for (District district : hand.getListCard()) {
            if (districtMinValue != null) {
                if (district.getValue() < districtMinValue.getValue()) {
                    boolean alreadyPlaced = false;
                    for (District districtPlaced : districtPlaced) {
                        if (district.getName().equals(districtPlaced.getName())) {
                            alreadyPlaced = true;
                            break;
                        }
                    }
                    if (!alreadyPlaced) districtMinValue = district;
                }
            } else {
                boolean alreadyPlaced = false;
                for (District districtPlaced : districtPlaced) {
                    if (district.getName().equals(districtPlaced.getName())) {
                        alreadyPlaced = true;
                        break;
                    }
                }
                if (!alreadyPlaced) districtMinValue = district;
            }
        }
        return districtMinValue;
    }

    /**
     * Check if the bot has a special district in his hand
     * If yes, return the card index
     *If no, return -1
     *
     * @return The index of the special card or -1
     */
    int firstSpecialDistrict() {
        for (int i = 0; i < getHandSize(); i++) {
            if (getHandList().get(i).getCategory() == CategoryEnum.UNIQUE.getValue()) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Return true if the bot has the crown and false otherwise
     *
     * @return True if the bot has currently the crown
     */
    public boolean doHaveTheCrown() {
        return haveTheCrown;
    }

    /**
     * set the crown to the current bot
     */
    public void setCrown() {
        haveTheCrown = true;
    }

    /**
     * Remove the crown from the current bot
     */
    public void removeCrown() {
        haveTheCrown = false;
    }

    /**
     * Get the curent character of the Bot
     *
     * @return A Character object
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * Set the Character of the Bot
     *
     * @param newCharacter The Character Object to set to the bot
     */
    public void setCharacter(Character newCharacter) {
        character = newCharacter;
    }

    /**
     * Remove a certain quantity of gold from the current bot
     *
     * @param quantity The quantity of gold to remove from the bot wallet
     */
    public void removeGold(int quantity) {
        if (gold - quantity < 0) gold = 0;
        else gold -= quantity;
    }

    /**
     * Get the ID of the current bot
     *
     * @return The id of the current bot
     */
    public int getCurrentBotId() {
        return id;
    }

    /**
     * Concatenate the log and the new message
     *
     * @param log The string to add to the log
     */
    void addLog(String log) {
        LogManager.log += log + "\n";
    }

    /**
     * Return the Character chosen
     *
     * NB: overridden by the current bot
     * @return The Character object of the character chosen by the bot
     */
    public Character chooseOneCharacter(ArrayList<Character> charactersPicked) {
        //Pick the first character
        return charactersPicked.get(0);
    }

    /**
     * Concatenate a log for the end of the turn
     *
     * @param endLog String
     */
    void addEndLog(String endLog) {
        LogManager.endTurnLog += endLog + "\n";
    }

    /**
     * Set the Bot dead. So he can't play for the current turn
     */
    public void setDead() {
        alive = false;
    }

    /**
     * Set the Bot alive before starting the turn
     */
    public void setAlive() {
        alive = true;
    }

    /**
     * Method to know if the current bot is alive or not
     *
     * @return True if the bot is alive, false otherwise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Get the game view
     *
     * @return A GameView object
     */
    public GameView getGameView() {
        return gameView;
    }

    /**
     * Set a certain game view to the bot
     *
     * @param gv The GameView object
     */
    public void setGameView(GameView gv) {
        gameView = gv;
    }

    /**
     * The logic for the action of the Architect
     *
     * NB: overridden by the current bot
     * @return
     */
    public ArrayList<District> logicArchitect() {
        return null;
    }

    /**
     * The logic for the action of the Assassin
     *
     * NB: overridden by the current bot
     */
    public int logicAssassin() {
        return 1;
    }

    /**
     * The logic for the action of the Warlord
     *
     * NB: overridden by the current bot
     */
    public int[] logicWarlord() {
        return null;
    }

    /**
     * The logic for the action of the Thief
     *
     * NB: overridden by the current bot
     */
    public int logicThief() {
        return -1;
    }

    /**
     * The logic for the action of the Magician
     *
     * NB: overridden by the current bot
     */
    public int logicMagician() {
        return -1;
    }

    /**
     * The logic for choosing if the bot want to get the gold from his district
     * if he is the King, the Bishop, the Merchant or the Warlord
     *
     * NB: always True in fact
     */
    public boolean logicGetGoldPerDistrict() {
        return true;
    }

    public ArrayList<District> discardList() {
        return getHandList();
    }

    /**
     * The logic for the action of the Factory district
     *
     * NB: overridden by the current bot
     */
    public boolean logicFactory() {
        return true;
    }

    /**
     * The logic for the action of the Laboratory district
     *
     * NB: overridden by the current bot
     */
    public int logicLaboratory() {
        return -1;
    }

    /**
     * The logic for the action of the Graveyard district
     *
     * NB: overridden by the current bot
     */
    public boolean logicGraveyard(District destructedDistrict) {
        return true;
    }

    /**
     * Build the data about the player and the district that he want to destruct, needed by the warlord
     *
     * @param player The data of a bot
     * @param districtChosen The index of a district placed
     * @return An array with playerIndex in index 0 and districtIndex in index 1
     */
    int[] buildDataWarlord(BotView player, District districtChosen) {
        if (player != null) {
            int[] result = new int[2];
            result[0] = getGameView().getListAllBots().indexOf(player);
            result[1] = player.getDistrictsPlaced().indexOf(districtChosen);
            return result;
        }
        return null;
    }

    /**
     * Apply the right format of printing a bot
     *
     * @param botID int
     * @return The string formatted in the right way
     */
    public static String addBotNumberLog(int botID) {
        return colorize("Bot ", WHITE_TEXT()) + colorize(String.valueOf(botID), TEXT_COLOR(255, 165, 0));
    }

    /**
     * Simple method to print a number of gold in gold color
     *
     * @param nbOfGolds The number of golds to print
     * @return The string with the number of golds colored
     */
    private String printGold(int nbOfGolds) {
        return colorize(nbOfGolds + (nbOfGolds > 1 ? " golds" : " gold"), TEXT_COLOR(255, 215, 0));
    }

    boolean isPlayerHaveDistrict(District district) {
        ArrayList<District> allCards = new ArrayList<>();
        allCards.addAll(getDistrictPlaced());
        allCards.addAll(getHandList());

        for (District districtsPossessed : allCards) {
            if (districtsPossessed.getName().equals(district.getName())) return true;
        }
        return false;
    }

    /**
     * Return the number of occurrences of categories in the districtsPlaced
     *
     * @return A tab[DistrictCategory] = number of occurrences
     */
    int[] numberOfEachCategoriesPlaced() {
        int[] result = {0, 0, 0, 0, 0, 0};

        for (District district : getDistrictPlaced()) {
            result[district.getCategory()]++;
        }

        return result;
    }

    int maxGold(int characterId, int[] data) {
        int maxGold = 0;
        switch (characterId) {
            case 3 -> maxGold = data[CategoryEnum.NOBLE.getValue()];
            case 4 -> maxGold = data[CategoryEnum.RELIGIOUS.getValue()];
            case 5 -> maxGold = data[CategoryEnum.TRADE.getValue()];
            case 7 -> maxGold = data[CategoryEnum.MILITARY.getValue()];
            default -> {
            }
        }
        return maxGold;
    }

    /**
     * Check if the current bot has the less gold
     *
     * @return True if the current bot is the poorest in the game
     */
    boolean hasTheLessGolds() {
        if (gameView == null) return false;

        AtomicBoolean hasLessGolds = new AtomicBoolean(true);
        gameView.getListAllBots().forEach(bot -> {
            if (bot.getGold() < getGold()) hasLessGolds.set(false);
        });
        return hasLessGolds.get();
    }

    /**
     * Check if a bot has more card in his hand than the current bot
     *
     * @return True if a bot in the game has more card in his hand than the current bot
     */
    boolean hasLessCardsThanSomeone() {
        if (gameView == null) return false;

        AtomicBoolean hasLessCards = new AtomicBoolean(false);

        gameView.getListAllBots().forEach(bot -> {
            if (bot.getNbCardHand() > getHandSize()) hasLessCards.set(true);
        });
        return hasLessCards.get();
    }

    /**
     * Check if the bot can place a card
     *
     * @return a boolean
     */
    boolean canPlace() {
        if (gameView == null) return true;

        AtomicBoolean hasLessCards = new AtomicBoolean(false);
        hand.getListCard().forEach(card -> {
            if (!isDistrictAlreadyPlaced(card.getName())) hasLessCards.set(true);
        });
        return hasLessCards.get();
    }

    /**
     * check if there is already a district of the given category place
     * @param cat int
     * @return boolean
     */
    boolean isCategoryPlaced(int cat) {
        for (District district:getDistrictPlaced()){
            if(district.getCategory()==cat) return true;
        }
        return false;
    }

    /**
     * Check if the given district can be placed by the player (check if enough gold and if district not already placed)
     * @param district
     * @param maxGold the number of gold that can be used
     * @return true if the district can be placed, false otherwise
     */
    boolean districtCanBePlaced(District district, int maxGold) {
        if (maxGold >= district.getValue()) {
            for (District districtPlaced : getDistrictPlaced()) {
                if (districtPlaced.getName().equals(district.getName()))
                    return false;
            }
            return true;
        }
        return false;
    }
}
