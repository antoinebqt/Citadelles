package citadelles.districts;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CSVReaderTest {

    @Test
    void getCsvToDistrictsSize() {
        ArrayList<District> districtsMan = new ArrayList<>();
        districtsMan.add(new District("Temple", 1, 1));
        districtsMan.add(new District("Temple", 1, 1));
        districtsMan.add(new District("Church", 2, 1));
        districtsMan.add(new District("Palace", 5, 2));
        districtsMan.add(new District("Town", 2, 3));

        ArrayList<District> districtsAut = new ArrayList<>();
        String filepath = System.getProperty("user.dir") + "/src/test/data/districtsData.csv";
        CSVReader csvReader = new CSVReader();
        csvReader.getCsvToDistricts(districtsAut, filepath);

        assertEquals(districtsMan.size(), districtsAut.size());
    }

    @Test
    void getCsvToDistrictsIndex3() {
        ArrayList<District> districtsMan = new ArrayList<>();
        districtsMan.add(new District("Temple", 1, 1));
        districtsMan.add(new District("Temple", 1, 1));
        districtsMan.add(new District("Church", 2, 1));
        districtsMan.add(new District("Palace", 5, 2));
        districtsMan.add(new District("Town", 2, 3));

        ArrayList<District> districtsAut = new ArrayList<>();
        String filepath = System.getProperty("user.dir") + "/src/test/data/districtsData.csv";
        CSVReader csvReader = new CSVReader();
        csvReader.getCsvToDistricts(districtsAut, filepath);

        assertEquals(districtsMan.get(3).getName(), districtsAut.get(3).getName());
    }

    @Test
    void addDistrictFromLineSoloSize() {
        ArrayList<District> districts = new ArrayList<>();
        String line = "Temple;1;1;1";
        CSVReader csvReader = new CSVReader();
        csvReader.addDistrictFromLine(districts, line);

        assertEquals(1, districts.size());
    }

    @Test
    void addDistrictFromLineDuoSize() {
        ArrayList<District> districts = new ArrayList<>();
        String line = "Temple;1;1;2";
        CSVReader csvReader = new CSVReader();
        csvReader.addDistrictFromLine(districts, line);

        assertEquals(2, districts.size());
    }

    @Test
    void addDistrictFromLineSoloCardName() {
        ArrayList<District> districts = new ArrayList<>();
        String line = "Temple;1;1;1";
        CSVReader csvReader = new CSVReader();
        csvReader.addDistrictFromLine(districts, line);

        assertEquals("Temple", districts.get(0).getName());
    }
}
