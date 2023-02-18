package citadelles.bots;

import citadelles.BotView;
import citadelles.Hand;
import citadelles.characters.Character;
import citadelles.districts.District;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static citadelles.characters.CharacterEnum.*;

/**
 * Class that contain all the logic of the medium bot
 */
public class MediumBot extends Bot {

    /**
     * Adds the hand of the bot and a list of the placed quarters.
     * Initializes the number of golds to 2.
     */
    public MediumBot(int id, Hand hand) {
        super(id, hand);
    }


    /**
     * Logic to choose if the bot will draw card or get gold
     *
     * If he has a lot of gold (>5) and less than 2 cards then he draws cards
     * If he has 0 cards, then he draws
     * Otherwise he takes gold
     */
    @Override
    public void chooseCardOrGold() {
        if ((getGold() > 5 && getHandSize() < 2) || getHandSize() == 0) {
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
     * He tries to place the first two card he can place
     * @return An arrayList of district to place
     */
    @Override
    public ArrayList<District> logicArchitect() {
        ArrayList<District> districtToPlace = new ArrayList<>();

        if (getHandSize() >= 2) {
            districtToPlace.add(getHandList().get(0));
            districtToPlace.add(getHandList().get(1));
        }
        else if (getHandSize() == 1) districtToPlace.add(getHandList().get(0));
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
     * Destroys a district of the player who has the most buildings placed
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
                for (District district : bot.getDistrictsPlaced()) {
                    if (district.getValue() - 1 <= getGold() && district.isCanBeDestruct()) {
                        districtChosen = district;
                        player = bot;
                        citySize = bot.getDistrictsPlaced().size();
                        break;
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

        //Get a bot that is not him
        AtomicInteger id = new AtomicInteger();
        id.set(-1);

        //if no one is revealed
        if (id.get() == -1) {
            getGameView().getListAllCharacters().forEach(c -> {
                if (c.getId() != getCharacterId() && c.getId() != ASSASSIN.getId() && !isDiscarded(c.getId())) {
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
     * Swap cards with the person who has the most cards.
     * If he's the player with the most cards, he exchanges with the deck all his cards
     *
     * Return an int == -1 to exchange all his cards with the deck
     * Return an 0 <= int < nbBot to exchange the hand with bot hand
     */
    @Override
    public int logicMagician() {
        AtomicInteger choice = new AtomicInteger(-2);
        AtomicInteger maxNb = new AtomicInteger(getHandSize());
        getGameView().getListAllBots().forEach(bot -> {
            if (bot.getNbCardHand() > maxNb.get()) {
                choice.set(bot.getID());
                maxNb.set(bot.getNbCardHand());
            }
        });

        if (choice.get() == -2) choice.set(-1);
        return choice.get();
    }

    /**
     * Logic if the bot has the Laboratory district
     *
     * If he has more than one card, he chooses to exchange a card with a value lower
     * than 3 (if he has one) for a gold
     *
     * @return The index of a card in his hand. -1 if he can't
     */
    @Override
    public int logicLaboratory() {
        if (getHandSize() > 0) {
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
     * If he has more than 7 gold, he decides to exchange 3 golds for 3 cards
     *
     * @return True if he wants to use the Factory power
     */
    @Override
    public boolean logicFactory() {
        return getGold() > 7;
    }

    /**
     * Logic if the bot has the Graveyard district
     *
     * If the destroyed district has already been built by the bot, then he does not buy it. Otherwise he always buys it.
     *
     * @param destructedDistrict The district that has been destroyed
     * @return True if the bot want to re-buy it
     */
    @Override
    public boolean logicGraveyard(District destructedDistrict) {
        return !isDistrictAlreadyPlaced(destructedDistrict.getName());
    }

    @Override
    public ArrayList<District> discardList() {
        return getHandList();
    }

    /**
     * Logic for choosing a character
     *
     * Choose a character who brings him money in order to be able to place buildings more easily.
     *
     * @param charactersPicked
     * @return
     */
    @Override
    public Character chooseOneCharacter(ArrayList<Character> charactersPicked) {
        for (Character character : charactersPicked) {
            if (character.getId() == MERCHANT.getId() || character.getId() == BISHOP.getId() || character.getId() == KING.getId() || character.getId() == WARLORD.getId())
                return character;
        }
        return charactersPicked.get(0);
    }

    @Override
    public String toString() {
        return "(Medium) -->";
    }

}


