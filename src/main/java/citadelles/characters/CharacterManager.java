package citadelles.characters;

import citadelles.LogManager;
import citadelles.bots.Bot;

import java.util.ArrayList;

import static citadelles.LogManager.defaultColor;
import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.RED_TEXT;

/**
 * Main class of the package characters, it can be seen as a deck of cards
 */
public class CharacterManager {

    private final ArrayList<Character> characters;
    private ArrayList<Character> visibleRemovedCharacters;
    private final int numberOfBots;

    /**
     * Create the arraylist that will contain
     * the different character card
     */
    public CharacterManager(int nbBot) {
        characters = new ArrayList<>();
        visibleRemovedCharacters = new ArrayList<>();
        numberOfBots = nbBot;
    }

    /**
     * Add the different character to the character's list
     */
    public void init() {
        characters.clear();
        visibleRemovedCharacters.clear();
        characters.addAll(initList());

        //Remove card before starting, the king can only be a discarded card face down
        int index;
        int discarded = 0, cardToDiscard;

        cardToDiscard = getDiscard();

        do {
            index = (int) (Math.random() * (characters.size()));
            if (discarded == 0) {
                addCharacterLog(defaultColor("Discarded card face down: ")
                        + colorize(characters.get(index).getRole(), RED_TEXT()));
                discarded++;
                characters.remove(index);
            } else if (characters.get(index).getId() != 3) {
                addCharacterLog(defaultColor("Discarded card face up: ")
                        + colorize(characters.get(index).getRole(), RED_TEXT()));
                discarded++;
                visibleRemovedCharacters.add(characters.get(index));
                characters.remove(index);
            }

        } while (discarded != cardToDiscard);
    }

    /**
     * Iitialize a list with all characters
     *
     * @return list of character
     */
    public ArrayList<Character> initList() {
        ArrayList<Character> newList = new ArrayList<>();

        //Create the liste of character
        newList.add(new Assassin());
        newList.add(new Thief());
        newList.add(new Magician());
        newList.add(new King());
        newList.add(new Bishop());
        newList.add(new Merchant());
        newList.add(new Architect());
        newList.add(new Warlord());

        return newList;
    }

    private int getDiscard() {
        return numberOfBots == 4 ? 3 : numberOfBots == 5 ? 2 : 1;
    }

    /**
     * Remove the given character from le list
     *
     * @param character chosen by the player
     */
    public void removeCharacterChosen(Character character) {
        characters.remove(character);
    }

    /**
     * Get the list of characters
     *
     * @return the character's list
     */
    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public ArrayList<Character> getVisibleRemovedCharacters(){
        return visibleRemovedCharacters;
    }

    private void addCharacterLog(String characterLog) {
        LogManager.characterLog += characterLog + "\n";
    }

    /**
     * The given bot choose a character in the list of character.
     * The chosen character is set to the bot and is remove from the list.
     *
     * @param bot who choose a character
     */
    public void pickPlayerCharacter(Bot bot) {
        Character characterChosen = bot.chooseOneCharacter(characters);

        bot.setCharacter(characterChosen);
        characterChosen.setCurrentBot(bot);

        removeCharacterChosen(characterChosen);

        addCharacterLog(Bot.addBotNumberLog(bot.getID())
                + defaultColor(" has chosen the role ")
                + colorize(characterChosen.getRole(), RED_TEXT()));
    }
}
