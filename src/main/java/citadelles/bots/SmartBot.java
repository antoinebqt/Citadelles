package citadelles.bots;

import citadelles.BotView;
import citadelles.Hand;
import citadelles.characters.Character;
import citadelles.districts.CategoryEnum;
import citadelles.districts.District;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static citadelles.characters.CharacterEnum.*;

/**
 * Class that contain all the logic of the smart bot
 */
public class SmartBot extends Bot {
    /**
     * Adds the hand of the bot and a list of the placed quarters.
     * initializes the number of golds to 2.
     */
    public SmartBot(int id, Hand hand) {
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
     *
     */
    @Override
    public void eventuallyPlaceADistrict() {
        District maxPlaceableDistrict = getMaxPlaceableDistrict();
        if (maxPlaceableDistrict != null && getGold() >= maxPlaceableDistrict.getValue()) {
            placeCard(getHandList().indexOf(maxPlaceableDistrict));
        }
    }

    /**
     * get the highest placeable district of the hand and if they are multiple choose one of a color that is not already placed on the board.
     * @return the district to place
     */
    private District getMaxPlaceableDistrict() {
        District districtPlaceable = null;
        for(District card : getHand().getListCard())
        {
            if(card.getValue()<=getGold() && !isDistrictAlreadyPlaced(card.getName())){
                if(districtPlaceable==null){
                    districtPlaceable = card;
                }
                else if(card.getValue()>=districtPlaceable.getValue()){
                    if(!isCategoryPlaced(card.getCategory()) && isCategoryPlaced(districtPlaceable.getCategory())){
                        districtPlaceable = card;
                    }
                }
            }
        }
        return districtPlaceable;
    }

    /**
     * Logic of the bot if he is the Architect
     *
     * He tries to place a special district or his best card
     * but check before himself if he has enough golds
     * @return An arrayList of district to place
     */
    @Override
    public ArrayList<District> logicArchitect() {
        ArrayList<District> districtToPlace = new ArrayList<>();
        int firstSpecialDistrictIndex = firstSpecialDistrict();
        if (firstSpecialDistrictIndex != -1 && getGold() >= getHandList().get(firstSpecialDistrictIndex).getValue()){
            districtToPlace.add(getHandList().get(firstSpecialDistrictIndex));
            int value = getHandList().get(firstSpecialDistrictIndex).getValue();
            getHandList().remove(getHandList().get(firstSpecialDistrictIndex));
            District maxDistrict = getDistrictHandMaxValue();
            if (maxDistrict != null && getGold() >= value + maxDistrict.getValue()){
                districtToPlace.add(maxDistrict);
                getHandList().remove(maxDistrict);
            }
        } else {
            District maxDistrict1 = getDistrictHandMaxValue();
            if (maxDistrict1 != null && getGold() >= maxDistrict1.getValue()){
                districtToPlace.add(maxDistrict1);
                getHandList().remove(maxDistrict1);
                District maxDistrict2 = getDistrictHandMaxValue();
                if (maxDistrict2 != null && getGold() >= maxDistrict1.getValue() + maxDistrict2.getValue()){
                    districtToPlace.add(maxDistrict2);
                    getHandList().remove(maxDistrict2);
                }
            }
        }

        districtToPlace.forEach(district -> getHandList().add(district));

        return districtToPlace;
    }


    /**
     * Logic of the bot if he is the Assassin
     *
     * Murders a random character other than himself
     *
     * @return The id of the character to kill
     */
    @Override
    public int logicAssassin() {
        int idRoleKilled;
        int nbCharacters = getGameView().getListAllCharacters().size();
        do {
            idRoleKilled = (int) (Math.random() * nbCharacters);
        } while (idRoleKilled == ASSASSIN.getId() || isDiscarded(idRoleKilled));

        return idRoleKilled;
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
     * Logic of the bot if he is the Thief
     *
     * Steals from a character other than himself and the Assassin
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
                if (c.getId() != getCharacterId() && c.getId() != getGameView().getDeadBotCharacter() && c.getId() != ASSASSIN.getId() && !isDiscarded(c.getId())) {
                    id.set(c.getId());
                }
            });
        }

        //try to steal the bot
        return id.get();
    }

    /**
     * Logic of the bot if he is the Magician
     *
     * If he has less than 3 card in hand, Swap cards with the person who has the most cards.
     * If not, exchange with the deck the cards he already has.
     *
     * Return an int == -1 to exchange all his cards with the deck
     * Return an 0 <= int < nbBot to exchange the hand with bot hand
     */
    @Override
    public int logicMagician() {
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

    /**
     * Logic if the bot has the Laboratory district
     *
     * If he has more than one card and less than 3 gold, he chooses to exchange
     * a card with a value lower than 3 (if he has one) for a gold
     *
     * @return The index of a card in his hand. -1 if he can't
     */
    @Override
    public int logicLaboratory() {
        if (getHandSize() > 0 && getGold() < 3) {
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
     * If he has more than 7 gold and less than 3 cards
     * in his hand, then he decides to exchange 3 golds for 3 cards
     *
     * @return True if he wants to use the Factory power
     */
    @Override
    public boolean logicFactory() {
        return (getGold() > 7 && getHandSize() < 3);
    }

    /**
     * Logic if the bot has the Graveyard district
     *
     * If the destroyed district has already been placed by the player then he does not buy it.
     * Otherwise, if it is a special district, he buys it.
     * If not, he buys it only if the value of the district is higher than 2
     * and the bot has more than 5 golds.
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
        return destructedDistrict.getValue() > 2 && getGold() > 5;
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
        return "(Smart) --->";
    }

    @Override
    public District chooseOneCard(ArrayList<District> districtsPicked) {
        for (District district : districtsPicked) {
            if (!isPlayerHaveDistrict(district)) return district;
        }
        return super.chooseOneCard(districtsPicked);
    }

    /**
     * Return in first the architect if hand size == 0
     * Or try to pick the character with an earning gold action which gain the most of gold possible
     * Otherwise do the default action
     *
     * @param charactersPicked character arraylist
     * @return a character
     */
    @Override
    public Character chooseOneCharacter(ArrayList<Character> charactersPicked) {
        int[] dataDistrictsPlaced = numberOfEachCategoriesPlaced();
        Character choice = null;
        int choiceValue = 0;
        int maxGolds = 3;
        boolean canPlace = canPlace();

        for (Character character : charactersPicked) {
            if (character.getId() == ASSASSIN.getId() && canPlace && getDistrictPlaced().size() == 6 && choiceValue < 10) {
                choice = character;
                choiceValue = 10;
            }

            // if no card and char is architect, take it
            if ((getHandSize() == 0 || !canPlace) && character.getId() == ARCHITECT.getId() && choiceValue < 9) {
                choice = character;
                choiceValue = 9;
            }

            //if the bot has more than 3 districts of the type of the character he takes it
            int goldGains = maxGold(character.getId(), dataDistrictsPlaced);
            if (goldGains >= maxGolds && getGold() <= 10 && choiceValue < 8) {
                choice = character;
                choiceValue = 8;
            }

            if (hasTheLessGolds() && character.getId() == THIEF.getId() && choiceValue < 7) {
                choice = character;
                choiceValue = 7;
            }

            if (hasLessCardsThanSomeone() && character.getId() == MAGICIAN.getId() && choiceValue < 6) {
                choice = character;
                choiceValue = 6;
            }
        }

        //if no choice done then choose the first one
        if (choice == null) {
            choice = charactersPicked.get(0);
        }
        return choice;
    }
}


