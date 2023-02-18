package citadelles.districts;

import java.util.ArrayList;

/**
 * main class of districts package. Can be seen as a deck of districts
 */
public class DistrictManager {

    private final ArrayList<District> districtsList;

    /**
     * Create a list that will contain all the districts
     */
    public DistrictManager() {
        districtsList = new ArrayList<>();
        initList();
    }

    /**
     * Initialize the district's list from
     * the CSV file using the CSVReader class
     */
    void initList() {
        //add the "classical" districts
        CSVReader csvReader = new CSVReader();
        String FILEPATH_DATA = System.getProperty("user.dir") + "/src/main/data/districtsData.csv";
        csvReader.getCsvToDistricts(districtsList, FILEPATH_DATA);

        //add specials ones
        districtsList.add(new DragonGate());
        districtsList.add(new Factory());
        districtsList.add(new Graveyard());
        districtsList.add(new Keep());
        districtsList.add(new Laboratory());
        districtsList.add(new Library());
        districtsList.add(new MiracleCourtyard());
        districtsList.add(new Observatory());
        districtsList.add(new SchoolOfMagic());
        districtsList.add(new University());
    }

    /**
     * Get a random index for pick a random
     * card in the district's list
     *
     * @return a random int
     */
    private int getRandomIndex() {
        return (int) (Math.random() * (districtsList.size() - 1));
    }

    /**
     * Get a random Card from the Deck
     *
     * @return a District
     */
    public District pickRandomCard() {
        if (districtsList.size() > 0) {
            int index = getRandomIndex();
            District district = districtsList.get(index);
            districtsList.remove(index);
            return district;
        }
        //In case that the district list is empty, return null
        else {
            return null;
        }
    }

    /**
     * Get the list of districts
     *
     * @return the district's list
     */
    public ArrayList<District> getDistrictList() {
        return districtsList;
    }

    /**
     * Add all the districts in the list
     *
     * @param districts List of district
     */
    public void addDistricts(ArrayList<District> districts) {
        districtsList.addAll(districts);
    }

    /**
     * Add the District passed
     *
     * @param district a district
     */
    public void addDistricts(District district) {
        districtsList.add(district);
    }
}
