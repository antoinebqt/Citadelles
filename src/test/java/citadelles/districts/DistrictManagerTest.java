package citadelles.districts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DistrictManagerTest {
    private ArrayList<District> districtsList;
    private DistrictManager districtManager;

    @BeforeEach
    void setup() {
        districtsList = new ArrayList<>();
        districtManager = new DistrictManager();

        CSVReader csvReader = new CSVReader();
        String FILEPATH_DATA = System.getProperty("user.dir") + "/src/main/data/districtsData.csv";
        csvReader.getCsvToDistricts(districtsList, FILEPATH_DATA);
    }

    @Test
    void getDistrictListSize() {
        int nbSpecialDistrict = 10;
        assertEquals(districtsList.size() + nbSpecialDistrict, districtManager.getDistrictList().size());
    }

    @Test
    void getDistrictListCardName() {
        assertEquals(districtsList.get(7).getName(), districtManager.getDistrictList().get(7).getName());
    }
}
