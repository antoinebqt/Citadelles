package citadelles.bots;

import citadelles.BotView;
import citadelles.Hand;
import citadelles.districts.District;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static citadelles.characters.CharacterEnum.ASSASSIN;

/**
 * Class that contain all the logic of the stupid bot
 */
public class StupidBot extends Bot {
    /**
     * Adds the hand of the bot and a list of the placed quarters.
     * initializes the number of golds to 2.
     */
    public StupidBot(int id, Hand hand) {
        super(id, hand);
    }

    /**
     * Logic to choose if the bot will draw card or get gold
     *
     * If he has no more cards in his hand, he draws a card
     * Otherwise he takes gold
     */
    @Override
    public void chooseCardOrGold() {
        if (getHandSize() > 0) {
            chooseAddGold();
        } else {
            chooseAddCard();
        }
    }

    /**
     * Method choosing if the bot will construct a district or not
     *
     *
     */
    @Override
    public void eventuallyPlaceADistrict() {
        District districtHandMinValue = getDistrictHandMinValue();
        if (districtHandMinValue != null && getGold() >= districtHandMinValue.getValue()) {
            placeCard(getHandList().indexOf(districtHandMinValue));
        }
    }

    /**
     * Logic of the bot if he is the Architect
     *
     * If he can, he places his lowest card
     * @return An arrayList of district to place
     */
    @Override
    public ArrayList<District> logicArchitect() {
        int canPlace = 2;
        ArrayList<District> districtToPlace = new ArrayList<>();
        for (int i = 0; i < canPlace; i++) {
            District minDistrict = getDistrictHandMinValue();
            if (minDistrict != null){
                districtToPlace.add(minDistrict);
                getHandList().remove(minDistrict);
            }
        }

        districtToPlace.forEach(district -> getHandList().add(district));

        return districtToPlace;
    }

    /**
     * Logic of the bot if he is the Assassin
     *
     * Murders a random character other than himself, he can try to kill a visible discarded character
     *
     * @return The id of the character to kill
     */
    @Override
    public int logicAssassin() {
        int idRoleKilled;
        int nbCharacters = getGameView().getListAllCharacters().size();
        do {
            idRoleKilled = (int) (Math.random() * nbCharacters);
        } while (idRoleKilled == ASSASSIN.getId());

        return idRoleKilled;
    }

    /**
     * Logic of the bot if he is the Warlord
     *
     * Destruct his first district. Completely stupid !
     *
     * @return A tab of int containing the player who will lose a district and the district to destroy
     */
    @Override
    public int[] logicWarlord() {
        BotView player = null;
        District districtChosen = null;

        for (BotView bot : getGameView().getListAllBots()) {
            if (bot.getID() == getID() && bot.getDistrictsPlaced().size() != 7) {
                for (District district : bot.getDistrictsPlaced()) {
                    if (district.getValue() - 1 <= getGold() && district.isCanBeDestruct()) {
                        districtChosen = district;
                        player = bot;
                        break;
                    }
                }
                break;
            }
        }
        return buildDataWarlord(player, districtChosen);
    }

    /**
     * Logic of the bot if he is the Thief
     *
     * Steals from a character other than himself,
     * even a visible discarded character
     *
     * @return The character id to steal
     */
    @Override
    public int logicThief() {
        //get a bot that is not him
        AtomicInteger id = new AtomicInteger();
        id.set(-1);
        getGameView().getListAllCharacters().forEach(c -> {
            if (c.getId() != getCharacterId()) {
                id.set(c.getId());
            }
        });

        //try to steal the bot
        return id.get();
    }

    /**
     * Logic of the bot if he is the Magician
     *
     * He exchanges cards with someone other than himself
     */
    @Override
    public int logicMagician() {
        int random = (int) (Math.random() * (1)) + 1;
        if (random == 1) {
            //get a bot that is not him
            AtomicInteger id = new AtomicInteger();
            id.set(-1);
            getGameView().getListAllBots().forEach(b -> {
                if (b.getID() != getID()) {
                    id.set(b.getID());
                }
            });
            return id.get();
        } else return -1;
    }

    /**
     * Logic if the bot has the Laboratory district
     *
     * If he has more than one card, he always chooses to
     * exchange his first card for a gold
     *
     * @return The index of a card in his hand. -1 if he can't
     */
    @Override
    public int logicLaboratory() {
        if (getHandSize() > 0) {
            return 1;
        } else return -1;
    }

    /**
     * Logic if the bot has the Factory district
     *
     * He always tries to exchange 3 golds for 3 cards
     *
     * @return True if he wants to use the Factory power
     */
    @Override
    public boolean logicFactory() {
        return true;
    }

    /**
     * Logic if the bot has the Graveyard district
     *
     * He is still trying to buy back the destroyed area without thinking
     *
     * @param destructedDistrict The district that has been destroyed
     * @return True if the bot want to re-buy it
     */
    @Override
    public boolean logicGraveyard(District destructedDistrict) {
        return true;
    }

    @Override
    public ArrayList<District> discardList() {
        return getHandList();
    }

    @Override
    public String toString() {
        return "(Stupid) -->";
    }

    /**
     * Logic for choose a card when picking card from deck
     *
     * If he has one of the district picked, in his hand or placed,
     * then he takes it (which prevents him from laying it down).
     * Otherwise he takes the 1st one that is provided to him.
     *
     * @param districtsPicked
     * @return A District object
     */
    @Override
    public District chooseOneCard(ArrayList<District> districtsPicked) {
        for (District district : districtsPicked) {
            if (isPlayerHaveDistrict(district)) return district;
        }
        return super.chooseOneCard(districtsPicked);
    }

}


