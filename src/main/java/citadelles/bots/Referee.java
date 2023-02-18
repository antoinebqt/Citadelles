package citadelles.bots;

import citadelles.Citadelles;
import citadelles.LogManager;
import citadelles.characters.Character;
import citadelles.characters.CharacterEnum;
import citadelles.districts.CategoryEnum;
import citadelles.districts.District;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static citadelles.LogManager.defaultColor;
import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

/**
 * Class that contain all the methods that manipulate the game after a decision of the bot
 */
public class Referee {
    private BotManager botManager;

    /**
     * Referee constructor
     */
    public Referee() {
        this.botManager = Citadelles.getBotManager();
    }

    /**
     * Set the bot manager
     *
     * @param botManager
     */
    public void setBotManager(BotManager botManager) {
        this.botManager = botManager;
    }

    /**
     * Kill the bot if it's possible
     *
     * @param assassinBot  The bot who made the kill
     * @param roleIdToKill The character ID to kill
     */
    public void kill(Bot assassinBot, int roleIdToKill) {
        Bot botToKill = existRoleToKill(botManager.getListBot(), roleIdToKill);

        addLog(Bot.addBotNumberLog(assassinBot.getID()) + defaultColor(" killed someone"));

        if (botToKill != null && roleIdToKill != CharacterEnum.ASSASSIN.getId()) {
            botToKill.setDead();
        } else {
            addEndLog(defaultColor("Nobody was the character killed by the ")
                    + colorize("Assassin", RED_TEXT()));
        }
    }

    /**
     * Check if a bot has the character to kill
     *
     * @param playerList   The bot list
     * @param roleIDToKill The character ID to kill
     * @return The bot to kill if exists, null otherwise
     */
    private Bot existRoleToKill(ArrayList<Bot> playerList, int roleIDToKill) {
        for (Bot bot : playerList) {
            if (bot.getCharacterId() == roleIDToKill) {
                return bot;
            }
        }
        return null;
    }

    /**
     * Add the number of district placed of a specific Category in Gold
     *
     * @param category The Category to convert district placed to gold
     */
    public void addGoldPerDistrictCategory(Bot bot, CategoryEnum category) {
        int goldBonus = numberDistrictInCategory(bot.getDistrictPlaced(), category.getValue());
        String line = Bot.addBotNumberLog(bot.getID())
                + defaultColor(" use the ")
                + printCharacter(bot.getCharacterRole())
                + defaultColor(" power");
        if (goldBonus > 0) {
            bot.addGold(goldBonus);
            line += defaultColor(" and gains ") + printGold(goldBonus)
                    + defaultColor(" because he has ") + goldBonus
                    + " " + printCategory(category) + defaultColor(goldBonus > 1 ? " districts" : " district");
        } else {
            line += defaultColor(" but has 0 ") + printCategory(category) + defaultColor(" district");
        }

        addLog(line);
    }

    /**
     * Simple formater for print a category
     *
     * @param category The category to print
     * @return The string colored in the right way
     */
    private String printCategory(CategoryEnum category) {
        return colorize(category.getName(), TEXT_COLOR(255, 0, 255));
    }

    /**
     * Simple formater for print a character
     *
     * @param character The character to print
     * @return The string colored in the right way
     */
    private String printCharacter(String character) {
        return colorize(character, RED_TEXT());
    }

    /**
     * Simple formater for print a district
     *
     * @param district The district to print
     * @return The string colored in the right way
     */
    private String printDistrict(String district) {
        return colorize(district, GREEN_TEXT());
    }

    /**
     * Count the number of district placed that are in the given category
     *
     * @param category The category ID to check
     * @return The number of district of the category
     */
    public int numberDistrictInCategory(ArrayList<District> districts, int category) {
        int count = 0;

        for (District district : districts) {
            if (district.getCategory() == category) count++;
        }
        return count;
    }

    /**
     * Check if the data is correct or not
     *
     * @param data The data to check
     * @return True if yes, false otherwise
     */
    private boolean isDataCorrect(int[] data) {
        if (data == null || data.length != 2) return false;
        if (data[0] - 1 <= botManager.getNumberOfBots()) {
            return data[1] - 1 <= botManager.getListBot().get(data[0]).getDistrictPlaced().size();
        }
        return false;
    }

    /**
     * The method to destruct a district if possible
     *
     * @param bot  The bot who is the warlord and who try to destruct a district
     * @param data The data containing the victim bot id and his district to destroy
     */
    public void destructOneDistrict(Bot bot, int[] data) {

        if (!isDataCorrect(data)) return;
        Bot player = botManager.getListBot().get(data[0]);
        District districtChosen = player.getDistrictPlaced().get(data[1]);

        if (bot.getGold() >= districtChosen.getValue() - 1 && player.getCharacter().getId() != CharacterEnum.BISHOP.getId() && districtChosen.isCanBeDestruct()) {
            player.getDistrictPlaced().remove(districtChosen);
            player.getHand().destructOneDistrict(districtChosen);
            bot.removeGold(districtChosen.getValue() - 1);

            addLog(Bot.addBotNumberLog(bot.getID()) + defaultColor(" has destruct the district ")
                    + printDistrict(districtChosen.getName()) + defaultColor(" of ")
                    + Bot.addBotNumberLog(player.getCurrentBotId()) + defaultColor(" for the cost of ")
                    + printGold((districtChosen.getValue() - 1)));

            Bot botWithGraveyard = botWithTheGraveyard();

            if (botWithGraveyard != null) {
                String line = Bot.addBotNumberLog(botWithGraveyard.getID()) + defaultColor(" has the ") + printDistrict("Graveyard");

                if (botWithGraveyard.logicGraveyard(districtChosen)) {

                    if (Objects.equals(botWithGraveyard.getCharacterRole(), CharacterEnum.WARLORD.getRole())) {
                        line += defaultColor(" and try to re-buy the ") + printDistrict(districtChosen.getName())
                                + defaultColor(" but he can't being the ") + printCharacter("Warlord");
                    } else if (botWithGraveyard.getGold() < 1) {
                        line += defaultColor(" and try to re-buy the ") + printDistrict(districtChosen.getName())
                                + defaultColor(" but has ") + printGold(0);
                    } else {
                        botWithGraveyard.getHandList().add(districtChosen);
                        botWithGraveyard.removeGold(1);
                        line += defaultColor(" and re-buy the ") + printDistrict(districtChosen.getName())
                                + defaultColor(" for ") + printGold(1);
                    }
                } else {
                    line += defaultColor(" but didn't choose to re-buy the ") + printDistrict(districtChosen.getName());
                }
                addLog(line);
            }
        }
    }

    /**
     * Check if a bot has the Graveyard district
     *
     * @return The bot who has it if exists, null otherwise
     */
    private Bot botWithTheGraveyard() {

        for (int i = 0; i < botManager.getNumberOfBots(); i++) {
            for (District district : botManager.getListBot().get(i).getDistrictPlaced()) {
                if (district.getName().equals("Graveyard")) {
                    return botManager.getListBot().get(i);
                }
            }
        }
        return null;
    }

    /**
     * The method to make the thief steals someone
     *
     * @param fromID The victim ID
     * @param toID   The thief ID
     */
    public void steal(int fromID, int toID) {

        Bot from = getBotWithCharId(fromID);
        Bot to = getBotWithCharId(toID);
        if (from == null) {
            to.addLog(Bot.addBotNumberLog(to.getID())
                    + defaultColor(" try to steal a character that is not assign so nothing append"));
            return;
        }
        Character deadChar;

        Bot dead = getDead();

        if (dead == null) {
            deadChar = to.getCharacter();
        } else {
            deadChar = dead.getCharacter();
        }

        String log = Bot.addBotNumberLog(to.getID()) + defaultColor(" decide to steal ")
                + colorize(String.valueOf(from.getCharacter().getRole()), RED_TEXT())
                + defaultColor(" that have ") + printGold(from.getGold());

        if (!from.getCharacterRole().equals("Assassin") && !from.getCharacterRole().equals(deadChar.getRole())) {
            to.setGold(to.getGold() + from.getGold());
            from.setGold(0);
            log += defaultColor(", now ") + Bot.addBotNumberLog(to.getID()) + defaultColor(" has ")
                    + printGold(to.getGold()) + defaultColor(" and ") + Bot.addBotNumberLog(from.getID())
                    + defaultColor(" has ") + printGold(0);
        } else if (from.getCharacterRole().equals(deadChar.getRole())) {
            log += defaultColor(", but ") + Bot.addBotNumberLog(to.getID())
                    + defaultColor(" has no succeed because ") + Bot.addBotNumberLog(from.getID())
                    + defaultColor(" is dead");
        } else {
            log += defaultColor(", but ") + Bot.addBotNumberLog(to.getID())
                    + defaultColor(" has no succeed because ") + Bot.addBotNumberLog(from.getID())
                    + defaultColor(" is ") + colorize("Assassin", RED_TEXT());
        }
        to.addLog(log);
    }

    /**
     * Get the bot who is currently dead
     *
     * @return The dead bot
     */
    private Bot getDead() {
        AtomicReference<Bot> bot = new AtomicReference<>();

        botManager.getListBot().forEach(b -> {
            if (!b.isAlive()) bot.set(b);
        });
        return bot.get();
    }

    /**
     * Method who make the magician exchange his hand with someone else
     *
     * @param fromID The victim bot ID
     * @param toID   The Magician bot ID
     */
    public void exchangeCards(int fromID, int toID) {

        Bot from = botManager.getListBot().get(fromID);
        Bot to = botManager.getListBot().get(toID);

        to.addLog(Bot.addBotNumberLog(toID)
                + defaultColor(" exchange his deck that has ") + to.getHandSize()
                + defaultColor(" Cards with the deck of ")
                + Bot.addBotNumberLog(fromID)
                + defaultColor(" that has ") + from.getHandSize()
                + defaultColor(" Cards"));

        ArrayList<District> waiting = new ArrayList<>(from.getHandList());

        from.getHandList().clear();
        from.getHandList().addAll(to.getHandList());

        to.getHandList().clear();
        to.getHandList().addAll(waiting);
    }

    /**
     * Get the bot with a given ID
     *
     * @param id The ID
     * @return the bot with this ID
     */
    private Bot getBotWithCharId(int id) {
        AtomicReference<Bot> bot = new AtomicReference<>();

        botManager.getListBot().forEach(b -> {
            if (b.getCharacterId() == id) bot.set(b);
        });
        return bot.get();
    }

    /**
     * Concatenate the log and the new message
     *
     * @param log String
     */
    void addLog(String log) {
        LogManager.log += log + "\n";
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
     * Simple method to print a number of gold in gold colour
     *
     * @param nbOfGolds number of golds to print
     * @return String with colour
     */
    private String printGold(int nbOfGolds) {
        return colorize(nbOfGolds + (nbOfGolds > 1 ? " golds" : " gold"), TEXT_COLOR(255, 215, 0));
    }

    /**
     * Set the crown to the given bot
     *
     * @param bot The bot to set the crown
     */
    public void setCrown(Bot bot) {
        bot.setCrown();
        addLog(Bot.addBotNumberLog(bot.getID()) + defaultColor(" get the ")
                + colorize("crown", YELLOW_TEXT())
                + defaultColor(" for the next turn"));
    }

    /**
     * Method for the Factory district, exchange 3 golds for 3 cards
     *
     * @param currentBot The bot who has the Factory district placed
     */
    public void get3CardsFor3Gold(Bot currentBot) {
        String line = Bot.addBotNumberLog(currentBot.getID())
                + defaultColor(" used the ") + printDistrict("Factory")
                + defaultColor(" power and ");

        if (currentBot.getGold() >= 3) {
            currentBot.removeGold(3);
            currentBot.pickCard(3);
            line += defaultColor("exchanged ") + printGold(3) + defaultColor(" for 3 cards");
        } else {
            line += defaultColor("tried to exchange ") + printGold(3)
                    + defaultColor(" for 3 cards but only had ") + printGold(currentBot.getGold());
        }
        addLog(line);
    }

    /**
     * Method for the School Of Magic district.
     * Give the bot 1 gold if he is the King, the Merchant, the Warlord or the Bishop
     *
     * @param currentBot The bot who has the School Of Magic district placed
     */
    public void get1GoldFromMagicSchool(Bot currentBot) {
        String line = Bot.addBotNumberLog(currentBot.getID()) + defaultColor(" used the ")
                + printDistrict("School Of Magic") + defaultColor(" power ");

        if (canEarnGoldsFromDistrict(currentBot)) {
            currentBot.addGold(1);
            line += defaultColor("and gained ") + printGold(1)
                    + defaultColor(" because he is the ") + printCharacter(currentBot.getCharacterRole());
        } else {
            line += defaultColor("but can't perceive gold from district being the ")
                    + printCharacter(currentBot.getCharacterRole());
        }
        addLog(line);
    }

    /**
     * Check if the bot can earn gold from district with his character
     *
     * @param currentBot The bot
     * @return True if he can, false otherwise
     */
    private boolean canEarnGoldsFromDistrict(Bot currentBot) {
        String role = currentBot.getCharacterRole();

        return switch (role) {
            case "King", "Bishop", "Merchant", "Warlord" -> true;
            default -> false;
        };
    }

    /**
     * Method for the Laboratory district, exchange a card for 1 gold
     *
     * @param currentBot The bot who has the Laboratory district placed
     */
    public void get1GoldFor1Card(Bot currentBot, int indexOfCard) {
        if (currentBot.getHandList().size() - 1 >= indexOfCard) {
            String line = Bot.addBotNumberLog(currentBot.getID()) + " used the " + printDistrict("Laboratory") + " power and gained " + printGold(1) + " for dropping his " + printDistrict(currentBot.getHandList().get(indexOfCard).getName());

            District districtToRemove = currentBot.getHandList().get(indexOfCard);
            currentBot.getHand().discard(districtToRemove);
            currentBot.getHandList().remove(indexOfCard);

            currentBot.addGold(1);

            addLog(line);
        }
    }

    /**
     * Place the given districts in the districtPlaced list of the bot if he had those districts
     *
     * @param currentBot
     * @param dataDistrictToPlace
     */
    public void placeCards(Bot currentBot, ArrayList<District> dataDistrictToPlace) {
        dataDistrictToPlace.forEach(district -> {
            if (currentBot.getGold() >= district.getValue() && currentBot.getHandList().contains(district)) {
                currentBot.placeCard(currentBot.getHandList().indexOf(district));
            } else {
                addLog(Bot.addBotNumberLog(currentBot.getID()) + " tried to place the "
                        + printDistrict(district.getName()) + " for " + printGold(district.getValue()) + " but has only " + printGold(currentBot.getGold()));
            }
        });
    }
}
