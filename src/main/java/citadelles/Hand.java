package citadelles;

import citadelles.districts.District;
import citadelles.districts.DistrictManager;

import java.util.ArrayList;

/**
 * create a hand of district and is given to a bot
 */
public class Hand {

    private ArrayList<District> listCard;
    private final DistrictManager districtManager;

    /**
     * Create the list that will contain the district not
     * already placed of the bot
     *
     * @param districtManager the district manager
     */
    public Hand(DistrictManager districtManager) {
        int numberOfCards = 4;
        listCard = new ArrayList<>();
        this.districtManager = districtManager;
        listCard.addAll(pullDistrict(numberOfCards));
    }

    /**
     * Add number of District Cards in the bot Hand.
     *
     * @param number int
     */
    public ArrayList<District> pullDistrict(int number) {
        ArrayList<District> districtsPicked = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            District district = districtManager.pickRandomCard();
            if (district != null) {
                districtsPicked.add(district);
            }
        }
        return districtsPicked;
    }

    /**
     * Get the list containing the districts card in the hand
     *
     * @return the card's list
     */
    public ArrayList<District> getListCard() {
        return listCard;
    }

    public void setListCard(ArrayList<District> newHandList) {
        listCard = newHandList;
    }

    /**
     * Get the size of the hand
     *
     * @return the hand's size
     */
    public int getListCardSize() {
        return getListCard().size();
    }

    /**
     * Place the chosen district in the hand listCard et add the rest of the list in the Deck
     *
     * @param districtsPicked District list
     * @param districtChosen  District
     */
    public void addCardChosen(ArrayList<District> districtsPicked, District districtChosen) {
        listCard.add(districtChosen);
        districtsPicked.remove(districtChosen);
        districtManager.addDistricts(districtsPicked);
    }

    public void addAllCards(ArrayList<District> districtsPicked) {
        listCard.addAll(districtsPicked);
    }

    public void destructOneDistrict(District district) {
        if (listCard.contains(district)) {
            districtManager.addDistricts(district);
            listCard.remove(district);
        }
    }

    /**
     * Put back in the deck a certain number of district
     *
     * @param toDiscard The arraylist of District object
     */
    public void discard(ArrayList<District> toDiscard) {
        districtManager.addDistricts(toDiscard);
    }

    /**
     * Put back in the deck a District object
     *
     * @param toDiscard The district to put back in the deck
     */
    public void discard(District toDiscard) {
        districtManager.addDistricts(toDiscard);
    }
}
