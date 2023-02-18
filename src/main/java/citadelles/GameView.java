package citadelles;

import citadelles.bots.Bot;
import citadelles.bots.BotManager;
import citadelles.characters.*;
import citadelles.characters.Character;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * give a view of the all game with all public data
 * Those data are a copy and can't be used to manipulate the game directly
 */
public class GameView {

    private ArrayList<BotView> listAllBots = new ArrayList<>();
    private final BotManager botManager;
    private final CharacterManager characterManager;
    private final ArrayList<Character> listCharacters = new ArrayList<>();

    /**
     * The Game View constructor
     *
     * @param botManager The bot manager
     */
    public GameView(BotManager botManager, CharacterManager characterManager) {
        this.botManager = botManager;
        this.characterManager = characterManager;
        setView();
    }

    public void updateView() {
        listAllBots.forEach(BotView::update);
    }

    public void updateCharacterList() {
        listCharacters.clear();
        listAllBots.forEach(bot -> listCharacters.add(bot.getCharacter()));
    }

    /**
     * Get all the bot
     *
     * @return An arraylist of BotView object
     */
    public ArrayList<BotView> getListAllBots() {
        return listAllBots;
    }

    /**
     * Set the botView List
     *
     * @return An arraylist of BotView object
     */
    public void setListCharacters(ArrayList<BotView> listAllBots) {
        this.listAllBots = listAllBots;
    }

    private void setView() {
        botManager.getListBot().forEach(b -> listAllBots.add(new BotView(botManager, b.getID())));
    }

    public ArrayList<Character> getListCharacters() {
        return listCharacters;
    }

    public ArrayList<Character> getListAllCharacters() {
        return characterManager.initList();
    }

    public ArrayList<Character> getVisibleDiscarded() {
        return characterManager.getVisibleRemovedCharacters();
    }

    /**
     * Get the bot who is dead
     *
     * @return The id of the bot who is dead
     */
    public int getDeadBot() {
        AtomicInteger id = new AtomicInteger();
        id.set(-1);
        botManager.getListBot().forEach(bot -> {
            if (!bot.isAlive()) {
                id.set(bot.getID());
            }
        });
        return id.get();
    }

    /**
     * Get the character of the bot who is dead
     *
     * @return The id of the character of the bot who is dead
     */
    public int getDeadBotCharacter() {
        ArrayList<Bot> botList = botManager.getListBot();
        for (int i = 0; i < botList.size(); i++){
            if (!botList.get(i).isAlive()){
                return i;
            }
        }
        return -1;
    }

    public int getMaxGold(){
        int maxGold = 0;
        for (int i = 0; i < getListAllBots().size(); i++){
            if (getListAllBots().get(i).getGold() > maxGold){
                maxGold = getListAllBots().get(i).getGold();
            }
        }
        return maxGold;
    }

    public int getDistrictOfMostRich(){
        int maxGold = 0;
        int maxGoldID = -1;
        for (int i = 0; i < getListAllBots().size(); i++){
            if (getListAllBots().get(i).getGold() > maxGold){
                maxGold = getListAllBots().get(i).getGold();
                maxGoldID = i;
            }
        }
        return getListAllBots().get(maxGoldID).getDistrictsPlacedSize();
    }

    /**
     * Get the size of the biggest city in the game
     *
     * @return int : city size
     */
    public int getSizeBiggerCity() {
        int maxSize = 0;
        for (BotView bot : listAllBots) {
            if (bot.getDistrictsPlacedSize() > maxSize)
                maxSize = bot.getDistrictsPlacedSize();
        }
        return maxSize;
    }
}


