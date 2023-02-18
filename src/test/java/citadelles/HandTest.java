package citadelles;

import citadelles.characters.Character;
import citadelles.characters.CharacterManager;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {
    private Hand hand;
    private DistrictManager districtManager;

    @BeforeEach
    void SetUp() {
        districtManager = new DistrictManager();
        hand = new Hand(districtManager);
    }

    @Test
    void getListCard() {
        Hand handCompare = new Hand(districtManager);
        assertNotEquals(hand.getListCard(), handCompare.getListCard());
    }

    @Test
    void getListCardSize() {
        assertEquals(hand.getListCardSize(), 4);
    }

    @Test
    void pullDistrictOf0() {
        int sizeBefore = hand.getListCardSize();
        assertEquals(0, hand.pullDistrict(0).size());
        assertEquals(sizeBefore, hand.getListCardSize());
    }

    @Test
    void pullDistrictOf10() {
        int sizeBefore = hand.getListCardSize();
        assertEquals(10, hand.pullDistrict(10).size());
        int sizeAfter = hand.getListCardSize();
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void setListCardTest() {
        ArrayList<District> listCard = new ArrayList<>();
        listCard.add(new District("House", 1, 2));
        listCard.add(new District("School", 3, 4));

        hand.setListCard(listCard);
        assertEquals(listCard, hand.getListCard());
    }

    @Test
    void addAllListCardTest() {
        ArrayList<District> listCard = new ArrayList<>();
        ArrayList<District> emptyListCard = new ArrayList<>();
        listCard.add(new District("House", 1, 2));
        listCard.add(new District("School", 3, 4));
        hand.setListCard(emptyListCard);
        hand.addAllCards(listCard);
        assertEquals(listCard, hand.getListCard());
    }

    @Test
    void destructOneDistrictTest() {
        ArrayList<District> listCard = new ArrayList<>();
        District school = new District("School", 3, 4);
        listCard.add(new District("House", 1, 2));
        listCard.add(school);

        hand.addAllCards(listCard);
        hand.destructOneDistrict(school);
        assertFalse(hand.getListCard().contains(school));
    }

    @Test
    void discardsTest(){
        int initSize = districtManager.getDistrictList().size();

        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("random",1,1));
        districts.add(new District("random",1,1));
        districts.add(new District("random",1,1));
        districts.add(new District("random",1,1));

        hand.discard(districts);

        assertEquals(initSize+districts.size(), districtManager.getDistrictList().size());
    }

    @Test
    void discardTest(){
        int initSize = districtManager.getDistrictList().size();

        hand.discard(new District("random",1,1));

        assertEquals(initSize+1, districtManager.getDistrictList().size());
    }

}