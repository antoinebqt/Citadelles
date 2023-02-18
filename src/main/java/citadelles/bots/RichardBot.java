package citadelles.bots;

import citadelles.BotView;
import citadelles.GameView;
import citadelles.Hand;
import citadelles.characters.Character;
import citadelles.districts.CategoryEnum;
import citadelles.districts.District;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static citadelles.characters.CharacterEnum.*;

public class RichardBot extends Bot {
    /**
     * Bot's constructor
     *
     * @param id   of the bot that will be created
     * @param hand The hand of the bot containing his cards
     */
    public RichardBot(int id, Hand hand) {
        super(id, hand);
    }

    /**
     * Logic to choose if the bot will draw card or get gold
     *
     * If he has the architect then he takes gold
     * If he has a lot of gold (>5) and less than 2 cards then he draws cards
     * If he has 0 cards, then he draws
     * Otherwise he takes gold
     */
    @Override
    public void chooseCardOrGold() {
        if (getCharacterId() != ARCHITECT.getId() && ((getGold() > 5 && getHandSize() < 2) || getHandSize() == 0)) {
            chooseAddCard();
        } else {
            chooseAddGold();
        }
    }

    /**
     * Method choosing if the bot will construct a district or not
     *
     */
    @Override
    public void eventuallyPlaceADistrict() {
        District placeableDistrict = null;
        if (getGameView().getSizeBiggerCity() == 6) {
            placeableDistrict = getDistrictHandMaxValue();
        } else {
            placeableDistrict = getDistrictHandMinValue();
        }
        if (placeableDistrict != null && getGold() >= placeableDistrict.getValue()) {
            placeCard(getHandList().indexOf(placeableDistrict));
        }
    }

    /**
     * Logic of the bot if he is the Assassin
     *
     * if he is in head : kill warlord
     * if someone has 6 quarters : kill bishop or warlord
     * if someone has 5 quarters : kill king
     * if someone very rich (>= 6) : kill thief
     * if someone rich and >= 5 districts : kill architect
     * else he kill someone randomly
     *
     * @return The id of the character to kill
     */
    @Override
    public int logicAssassin() {
        if (getGameView().getSizeBiggerCity() == getNumberDistrictPlaced() && !isDiscarded(WARLORD.getId()))
            return WARLORD.getId();
        if (getGameView().getSizeBiggerCity() == 6) {
            if (!isDiscarded(BISHOP.getId())) return BISHOP.getId();
            else return WARLORD.getId();
        }
        if (getGameView().getMaxGold() >= 4 && getGameView().getDistrictOfMostRich() >= 5 && !isDiscarded(ARCHITECT.getId()))
            return ARCHITECT.getId();
        if (getGameView().getSizeBiggerCity() == 5 && !isDiscarded(KING.getId())) {
            return KING.getId();
        }
        if (getGameView().getMaxGold() >= 6 && !isDiscarded(THIEF.getId()))
            return THIEF.getId();

        int idRoleKilled;
        int nbCharacters = getGameView().getListAllCharacters().size();
        do {
            idRoleKilled = (int) (Math.random() * nbCharacters);
        } while (idRoleKilled == ASSASSIN.getId() || isDiscarded(idRoleKilled));

        return idRoleKilled;
    }

    /**
     * Return in first the architect if hand size == 0
     * Or try to pick the charactere with a earning gold action wich gain the most of gold possible
     * Otherwise do the default action
     *
     * @param charactersPicked character arraylist
     * @return a character
     */
    @Override
    public Character chooseOneCharacter(ArrayList<Character> charactersPicked) {
        int[] dataDistrictsPlaced = numberOfEachCategoriesPlaced();
        Character choice = null;
        int maxCitySize = 0;
        try{ maxCitySize = getGameView().getSizeBiggerCity();
        }catch (Exception e){}

        if (maxCitySize == 6) {
            choice = containCharacter(charactersPicked, ASSASSIN.getId(), BISHOP.getId(), WARLORD.getId());
        }
        else if (maxCitySize == 5) {
            if (getDistrictPlaced().size() == 5 && getGold() >= 4 && getHandSize() > 0) {
                choice = containCharacter(charactersPicked, ARCHITECT.getId(), KING.getId(), ASSASSIN.getId(), BISHOP.getId(), WARLORD.getId());
            } else {
                choice = containCharacter(charactersPicked, KING.getId(), ASSASSIN.getId(), WARLORD.getId(), BISHOP.getId());
            }
        }

        // if not in endgame so focus on the opportunistic strategy
        if (choice == null) {
            int maxGoldBishop = maxGold(BISHOP.getId(), dataDistrictsPlaced);
            int maxGoldWarlord = maxGold(WARLORD.getId(), dataDistrictsPlaced);
            if (maxGoldBishop >= maxGoldWarlord && maxGoldBishop > 0) {
                choice = containCharacter(charactersPicked, BISHOP.getId(), WARLORD.getId(), THIEF.getId());
            } else if (maxGoldBishop < maxGoldWarlord && maxGoldWarlord > 0) {
                choice = containCharacter(charactersPicked, WARLORD.getId(), BISHOP.getId(), THIEF.getId());
            } else {
                choice = containCharacter(charactersPicked, THIEF.getId(), BISHOP.getId(), WARLORD.getId());
            }
        }

        // if no choice done then choose the default strategy
        if (choice == null) {
            choice = super.chooseOneCharacter(charactersPicked);
        }
        return choice;
    }

    /**
     * return the character with a given id or null
     * the characterIds are checked in passed order
     *
     * @param characterIds
     * @param charactersList
     * @return return the character with a given id or null
     */
    private Character containCharacter(ArrayList<Character> charactersList, int... characterIds) {
        for (int characterId : characterIds) {
            for (Character character : charactersList) {
                if (character.getId() == characterId) return character;
            }
        }
        return null;
    }

    /**
     * Logic of the bot if he is the Warlord
     *
     * Destruct the max value district in the city of the more advanced player
     *
     * @return A tab of int containing the player who will lose a district and the district to destroy
     */
    @Override
    public int[] logicWarlord() {
        BotView player = null;
        District districtChosen = null;
        int citySize = 0;

        for (BotView bot : getGameView().getListAllBots()) {
            if (bot.getID() != getID() && bot.getDistrictsPlaced().size() != 7 && bot.getDistrictsPlaced().size() > citySize && bot.getCharacter().getId() != BISHOP.getId()) {
                int districtChosenValue = 100;
                for (District district : bot.getDistrictsPlaced()) {
                    if (district.getValue() - 1 <= getGold() && district.getValue() < districtChosenValue && district.isCanBeDestruct()) {
                        districtChosen = district;
                        player = bot;
                        districtChosenValue = district.getValue();
                        citySize = bot.getDistrictsPlaced().size();
                    }
                }
            }
        }
        return buildDataWarlord(player, districtChosen);
    }

    /**
     * Logic of the bot if he is the Architect
     *
     * He tries to place his little card to place the maximum possible
     *
     * @return An arrayList of district to place
     */
    @Override
    public ArrayList<District> logicArchitect() {
        int gold = getGold();
        ArrayList<District> districtToPlace = new ArrayList<>();
        ArrayList<District> districtInHand = new ArrayList<>(getHandList());

        for (int i = 0; i < 2; i++) {
            District districtMinValue = null;
            int minValue = 100;
            for (District district : districtInHand) {
                if (districtCanBePlaced(district, gold) && district.getValue() < minValue) {
                    districtMinValue = district;
                    minValue = districtMinValue.getValue();
                }
            }
            // If no district found, exit the loop
            if (districtMinValue == null) break;

            districtToPlace.add(districtMinValue);
            districtInHand.remove(districtMinValue);
            gold -= districtMinValue.getValue();
        }

        return districtToPlace;
    }

    /**
     * Logic if the bot has the Laboratory district
     *
     * If he has more than one card and less than 4 gold, he chooses to exchange
     * a card with a value lower than 3 (if he has one) for a gold
     * or a card that he already place before
     *
     * @return The index of a card in his hand. -1 if he can't
     */
    @Override
    public int logicLaboratory() {
        if (getHandSize() > 0 && getGold() < 4) {
            for (int i = 0; i < getHandSize(); i++){
                if (isDistrictAlreadyPlaced(getHandList().get(i).getName())){
                    return i;
                }
            }
            District minDistrict = getDistrictHandMinValue();
            if (minDistrict != null){
                if (minDistrict.getValue() < 3){
                    return getHandList().indexOf(minDistrict);
                }
            }
        }
        return -1;
    }

    /**
     * Logic if the bot has the Factory district
     *
     * If he has more than 7 gold and less than 4 districts
     * placed, then he decides to exchange 3 golds for 3 cards
     *
     * @return True if he wants to use the Factory power
     */
    @Override
    public boolean logicFactory() {
        return (getGold() > 7 && getNumberDistrictPlaced() < 4);
    }

    /**
     * Logic if the bot has the Graveyard district
     *
     * If the destroyed district has already been placed by the player then he does not buy it.
     * Otherwise, if it is a special district, he buys it.
     * If not, he buys it only if the value of the district is higher than 2.
     *
     * @param destructedDistrict The district that has been destroyed
     * @return True if the bot want to re-buy it
     */
    @Override
    public boolean logicGraveyard(District destructedDistrict) {
        if (isDistrictAlreadyPlaced(destructedDistrict.getName())) {
            return false;
        }
        if (destructedDistrict.getCategory() == CategoryEnum.UNIQUE.getValue()){
            return true;
        }
        return destructedDistrict.getValue() > 2;
    }

        /**
     * Logic of the bot if he is the Thief
     *
     * Steals from a character other than himself and the Assassin and the discarded character
     *
     * @return The character id to steal
     */
    @Override
    public int logicThief() {

        //get a bot that is not him
        AtomicInteger id = new AtomicInteger();
        id.set(-1);

        //if no one is revealed
        if (id.get() == -1) {
            getGameView().getListAllCharacters().forEach(c -> {
                if (c.getId() != getCharacterId() && c.getId() != ASSASSIN.getId()) {
                    boolean cIsDiscarded = false;
                    for (Character discardedChar : getGameView().getVisibleDiscarded()) {
                        if (discardedChar.getId() == c.getId()) {
                            cIsDiscarded = true;
                            break;
                        }
                    }
                    if (!cIsDiscarded) id.set(c.getId());
                }
            });
        }

        //try to steal the bot
        return id.get();
    }

    /**
     * Choose in that order : unique, then military or religious, then juste non-owned district
     *
     * @param districtsPicked The arraylist containing the different districts proposed to the bot
     * @return district chosen
     */
    @Override
    public District chooseOneCard(ArrayList<District> districtsPicked) {
        // first check for unique non-owned district
        for (District district : districtsPicked) {
            if (!isPlayerHaveDistrict(district) && district.getCategory() == CategoryEnum.UNIQUE.getValue())
                return district;
        }
        // then check for religious or military non-owned district
        for (District district : districtsPicked) {
            if (!isPlayerHaveDistrict(district) && (district.getCategory() == CategoryEnum.MILITARY.getValue() || district.getCategory() == CategoryEnum.RELIGIOUS.getValue()))
                return district;
        }
        // then check for non-owned district
        for (District district : districtsPicked) {
            if (!isPlayerHaveDistrict(district))
                return district;
        }
        // otherwise, use the default choice
        return super.chooseOneCard(districtsPicked);
    }

    /**
     * Logic of the bot if he is the Magician
     *
     * First try to exchange the card with the player with the biggest city (if >5 districts placed)
     *
     * Otherwise :
     * If he has less than 3 card in hand, Swap cards with the person who has the most cards.
     * If not, exchange with the deck the cards he already has.
     *
     * Return an int == -1 to exchange all his cards with the deck
     * Return an 0 <= int < nbBot to exchange the hand with bot hand
     */
    @Override
    public int logicMagician() {
        // endGame strategy
        if (getGameView().getSizeBiggerCity() >= 5) {
            BotView botToChoose = null;
            int citySize = 0;
            for (BotView bot : getGameView().getListAllBots()) {
                if (bot.getID() != getID() && bot.getDistrictsPlacedSize() > citySize && bot.getDistrictsPlacedSize() < 7 && bot.getDistrictsPlacedSize() >= 5 && bot.getNbCardHand() > 0) {
                    botToChoose = bot;
                    citySize = bot.getDistrictsPlacedSize();
                }
            }
            if (botToChoose != null)
                return botToChoose.getID();
        }

        // Non-endgame strategy
        AtomicInteger choice = new AtomicInteger(-2);
        AtomicInteger maxNb = new AtomicInteger(getHandSize());
        if (getHandSize() < 3){
            getGameView().getListAllBots().forEach(bot -> {
                if (bot.getNbCardHand() > maxNb.get()) {
                    choice.set(bot.getID());
                    maxNb.set(bot.getNbCardHand());
                }
            });
        }

        if (choice.get() == -2) choice.set(-1);
        return choice.get();
    }

    @Override
    public ArrayList<District> discardList() {
        ArrayList<District> discards = new ArrayList<>();
        for (District district : getHandList()) {
            if (isDistrictAlreadyPlaced(district.getName())) discards.add(district);
        }
        return discards;
    }

    @Override
    public String toString() {
        return "(Richard) ->";
    }
}