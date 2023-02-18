package citadelles;

import citadelles.bots.Bot;
import citadelles.districts.District;
import citadelles.districts.DragonGate;
import citadelles.districts.MiracleCourtyard;
import citadelles.districts.University;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CitadellesTest {

    private Citadelles citadelles;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(outputStreamCaptor));
        this.citadelles = new Citadelles(0, 0, 5, 0);
    }

    @Test
    void botsAreCreated() {
        assertEquals(citadelles.getBots().size(), 5);
    }

    @Test
    void canUseThePowerOfMiracleCourtyardButHasOnly3CategoryPlaced() {

        ArrayList<District> districts = new ArrayList<>();
        Bot botTest = citadelles.getBots().get(0);

        districts.add(new MiracleCourtyard());
        districts.add(new District("District 2", 0, 2));
        districts.add(new District("District 3", 0, 2));
        districts.add(new District("District 4", 0, 1));
        districts.add(new District("District 5", 0, 2));
        districts.add(new District("District 6", 0, 2));

        botTest.setDistrictPlaced(districts);

        assertFalse(citadelles.doHaveAllCategoriesPlaced(botTest));
    }

    @Test
    void canUseThePowerOfMiracleCourtyardButItHasBeenPlacedDuringLastTurn() {

        ArrayList<District> districts = new ArrayList<>();
        Bot botTest = citadelles.getBots().get(0);

        districts.add(new District("District 2", 0, 2));
        districts.add(new District("District 3", 0, 3));
        districts.add(new District("District 4", 0, 1));
        districts.add(new District("District 5", 0, 2));
        districts.add(new District("District 6", 0, 2));

        District miracleCourtyard = new MiracleCourtyard();
        miracleCourtyard.actionWhenConstruct(8);
        districts.add(miracleCourtyard);

        botTest.setDistrictPlaced(districts);

        citadelles.setFinishTurn(8);
        assertFalse(citadelles.doHaveAllCategoriesPlaced(botTest));
    }

    @Test
    void canUseThePowerOfMiracleCourtyard() {

        ArrayList<District> districts = new ArrayList<>();
        Bot botTest = citadelles.getBots().get(0);

        districts.add(new District("District 2", 0, 2));
        districts.add(new District("District 3", 0, 3));
        districts.add(new District("District 4", 0, 1));
        districts.add(new District("District 5", 0, 2));
        districts.add(new District("District 6", 0, 5));
        District miracleCourtyard = new MiracleCourtyard();
        miracleCourtyard.actionWhenConstruct(3);
        districts.add(miracleCourtyard);

        botTest.setDistrictPlaced(districts);

        citadelles.setFinishTurn(8);
        assertTrue(citadelles.doHaveAllCategoriesPlaced(botTest));
    }

    @Test
    void finalScoreCalculatorWithUniversityAndFirstPositionAndAllCategoriesPlaced() {

        ArrayList<District> districts = new ArrayList<>();

        districts.add(new District("District 1", 0, 1));
        districts.add(new District("District 2", 0, 2));
        districts.add(new District("District 3", 0, 3));
        districts.add(new District("District 4", 0, 4));
        districts.add(new District("District 5", 0, 2));
        districts.add(new University()); //University is category 5

        citadelles.getBots().get(0).setDistrictPlaced(districts);
        citadelles.getBots().get(0).setFinishPosition(1);

        citadelles.finalScoreCalculator();

        assertEquals(15, citadelles.getBots().get(0).getScore());
    }

    @Test
    void finalScoreCalculatorWithUniversityAndFirstPosition() {

        ArrayList<District> universityDistrict = new ArrayList<>();
        universityDistrict.add(new University());

        citadelles.getBots().get(0).setFinishPosition(1);
        citadelles.getBots().get(0).setDistrictPlaced(universityDistrict);

        citadelles.finalScoreCalculator();

        assertEquals(12, citadelles.getBots().get(0).getScore());
    }

    @Test
    void finalScoreCalculatorWithDragonGateAndSecondPosition() {

        ArrayList<District> dragonGateDistrict = new ArrayList<>();
        dragonGateDistrict.add(new DragonGate());

        citadelles.getBots().get(1).setFinishPosition(2);
        citadelles.getBots().get(1).setDistrictPlaced(dragonGateDistrict);

        citadelles.finalScoreCalculator();

        assertEquals(10, citadelles.getBots().get(1).getScore());
    }

    @Test
    void finalScoreCalculatorWithAThirdPosition() {

        citadelles.getBots().get(2).setFinishPosition(3);

        citadelles.finalScoreCalculator();

        assertEquals(2, citadelles.getBots().get(2).getScore());
    }

    @Test
    void finalScoreCalculatorWithARandomDistrict() {

        ArrayList<District> randomDistrict = new ArrayList<>();

        randomDistrict.add(new District("Random District", 1, 2));

        citadelles.getBots().get(3).setDistrictPlaced(randomDistrict);

        citadelles.finalScoreCalculator();

        assertEquals(1, citadelles.getBots().get(3).getScore());
    }

    @Test
    void finalScoreCalculatorWithNothing() {

        citadelles.finalScoreCalculator();

        assertEquals(0, citadelles.getBots().get(4).getScore());
    }

    @Test
    void doHaveAllCategoriesPlacedTestWithTheFiveCategories() {

        ArrayList<District> districts = new ArrayList<>();

        districts.add(new District("District 1", 1, 1));
        districts.add(new District("District 2", 1, 2));
        districts.add(new District("District 3", 1, 3));
        districts.add(new District("District 4", 1, 4));
        districts.add(new District("District 5", 1, 5));
        districts.add(new District("District 6", 1, 2));

        citadelles.getBots().get(0).setDistrictPlaced(districts);

        assertTrue(citadelles.doHaveAllCategoriesPlaced(citadelles.getBots().get(0)));
    }

    @Test
    void doHaveAllCategoriesPlacedTestWithNotAllTheCategories() {

        ArrayList<District> districts = new ArrayList<>();

        districts.add(new District("District 1", 1, 1));
        districts.add(new District("District 2", 1, 2));
        districts.add(new District("District 3", 1, 3));
        districts.add(new District("District 4", 1, 4));
        districts.add(new District("District 5", 1, 3));
        districts.add(new District("District 6", 1, 4));
        districts.add(new District("District 7", 1, 2));

        citadelles.getBots().get(0).setDistrictPlaced(districts);

        assertFalse(citadelles.doHaveAllCategoriesPlaced(citadelles.getBots().get(0)));
    }

    @Test
    void ResultTestWhen7DistrictPlaced() {

        ArrayList<District> districts = new ArrayList<>();

        districts.add(new District("District 1", 1, 1));
        districts.add(new District("District 2", 1, 2));
        districts.add(new District("District 3", 1, 3));
        districts.add(new District("District 4", 1, 4));
        districts.add(new District("District 5", 1, 3));
        districts.add(new District("District 6", 1, 4));
        districts.add(new District("District 7", 1, 2));

        citadelles.getBots().get(0).setDistrictPlaced(districts);

        assertTrue(citadelles.result(citadelles.getBots().get(0), 0));
    }

    @Test
    void ResultTestWhen1DistrictPlaced() {

        ArrayList<District> districts = new ArrayList<>();

        districts.add(new District("District 1", 1, 1));

        citadelles.getBots().get(0).setDistrictPlaced(districts);

        assertFalse(citadelles.result(citadelles.getBots().get(0), 0));
    }

    /*
    @Test
    void showLogTest() {

        LogManager.log = "Test logs";

        citadelles.showLog();
        assertEquals("Test logs", outputStreamCaptor.toString().trim());

    }

    @Test
    public void showEndLogTest() {

        LogManager.endTurnLog = "End test logs";

        citadelles.showEndLog();
        assertEquals("End test logs", outputStreamCaptor.toString().trim());
    }

    @Test
    public void showCharacterLogTest() {

        LogManager.characterLog = "Character test logs";

        citadelles.showCharacters();
        assertEquals("Character test logs", outputStreamCaptor.toString().trim());
    }
     */
}