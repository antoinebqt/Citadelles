package citadelles.districts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Create a list with all the district's card from
 * a CSV file
 */
public class CSVReader {

    void getCsvToDistricts(ArrayList<District> districtsList, String filepath) {
        try {
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);
            String line;

            //Avoiding the header line of the file
            br.readLine();

            while ((line = br.readLine()) != null) {
                addDistrictFromLine(districtsList, line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addDistrictFromLine(ArrayList<District> array, String line) {
        String name;
        int value, category, number;

        String[] tempLine = line.split(";");
        name = tempLine[0];
        value = Integer.parseInt(tempLine[1]);
        category = Integer.parseInt(tempLine[2]);
        number = Integer.parseInt(tempLine[3]);

        for (int i = 0; i < number; i++) {
            array.add(new District(name, value, category));
        }
    }
}
