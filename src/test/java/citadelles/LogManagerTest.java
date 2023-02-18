
package citadelles;

import citadelles.bots.*;
import citadelles.characters.CharacterManager;
import citadelles.characters.Merchant;
import citadelles.characters.Warlord;
import citadelles.districts.District;
import citadelles.districts.DistrictManager;
import com.diogonunes.jcolor.Attribute;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.YELLOW_TEXT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogManagerTest {

    private BotManager botManager;
    private LogManager logManager;
    private DistrictManager districtManager;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        districtManager = new DistrictManager();
        botManager = new BotManager(1, 1, 1,0, districtManager);
        logManager = new LogManager(botManager);
        LogManager.log = "";
        LogManager.endTurnLog = "";
        LogManager.characterLog = "";
    }

    @Test
    public void getBotTest() {
        assertEquals(botManager.getListBot(), logManager.getBots());
    }

    @Test
    void sortBotListWhenExAequo() {

        ArrayList<Bot> botListUnsorted = new ArrayList<>();
        ArrayList<Bot> botListSorted;

        Bot bot1 = botManager.getListBot().get(0);
        Bot bot2 = botManager.getListBot().get(1);

        botListUnsorted.add(bot1);
        botListUnsorted.add(bot2);

        bot1.addScore(10);
        bot2.addScore(10);

        bot1.setCharacter(new Merchant());
        bot2.setCharacter(new Warlord());

        botListSorted = logManager.sortBotList(botListUnsorted);

        assertAll(
                () -> assertEquals(bot2, botListSorted.get(0)),
                () -> assertEquals(bot1, botListSorted.get(1))
        );

    }

    @Test
    void sortBotListTest() {

        ArrayList<Bot> botListUnsorted = new ArrayList<>();
        ArrayList<Bot> botListSorted;

        Bot bot1 = botManager.getListBot().get(0);
        Bot bot2 = botManager.getListBot().get(1);
        Bot bot3 = botManager.getListBot().get(2);

        botListUnsorted.add(bot1);
        botListUnsorted.add(bot2);
        botListUnsorted.add(bot3);

        bot1.addScore(2);
        bot2.addScore(14);
        bot3.addScore(9);

        botListSorted = logManager.sortBotList(botListUnsorted);

        assertAll(
                () -> assertEquals(bot2, botListSorted.get(0)),
                () -> assertEquals(bot3, botListSorted.get(1)),
                () -> assertEquals(bot1, botListSorted.get(2))
        );
    }
    @Test
    void getType(){
        assertEquals(logManager.getType(new SmartBot(0, new Hand(districtManager))),logManager.getType(0));
        assertEquals(logManager.getType(new MediumBot(1, new Hand(districtManager))),logManager.getType(1));
        assertEquals(logManager.getType(new StupidBot(2, new Hand(districtManager))),logManager.getType(2));
    }

    @Test
    void printScore(){
        Bot bot = new SmartBot(0, new Hand(districtManager));
        bot.addScore(5);
        assertEquals(bot.getScore()+" ", logManager.printScore(bot));
        bot.addScore(50);
        assertEquals(String.valueOf(bot.getScore()), logManager.printScore(bot));
    }

    @Test
    void printDistrict(){
        Bot bot = new SmartBot(0, new Hand(districtManager));
        ArrayList<District> list = new ArrayList<>();
        bot.setDistrictPlaced(list);
        assertEquals("0 district ",logManager.printDistrict(bot));
    }

    @Test
    void printDistricts(){
        Bot bot = new SmartBot(0, new Hand(districtManager));
        ArrayList<District> list = new ArrayList<>();
        list.add(new District("random",0,0));
        list.add(new District("random",0,0));
        list.add(new District("random",0,0));
        list.add(new District("random",0,0));
        list.add(new District("random",0,0));
        bot.setDistrictPlaced(list);
        assertEquals("5 districts",logManager.printDistrict(bot));
    }

    @Test
    void getRankingAndScore(){
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(0);
        list1.add(1);
        list1.add(2);
        list1.add(3);
        ArrayList<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(0);
        list2.add(2);
        list2.add(1);
        ArrayList<ArrayList<Integer>> lists = new ArrayList<>();
        lists.add(list1);
        lists.add(list2);

        ArrayList<Integer> score1 = new ArrayList<>();
        score1.add(25);
        score1.add(22);
        score1.add(18);
        score1.add(12);
        ArrayList<Integer> score2 = new ArrayList<>();
        score2.add(22);
        score2.add(15);
        score2.add(12);
        score2.add(2);
        ArrayList<ArrayList<Integer>> scores = new ArrayList<>();
        scores.add(score1);
        scores.add(score2);

        logManager.setRankings(lists);
        logManager.setScores(scores);
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        ArrayList<Integer> averageScore = new ArrayList<>();


        logManager.getRankingAndScoresCounts(results,averageScore,4);

        ArrayList<Integer> score = new ArrayList<>();
        score.add(40);
        score.add(24);
        score.add(30);
        score.add(34);

        assertEquals(score, averageScore);

        ArrayList<Integer> rank1 = new ArrayList<>();
        rank1.add(1);
        rank1.add(1);
        rank1.add(0);
        rank1.add(0);
        ArrayList<Integer> rank2 = new ArrayList<>();
        rank2.add(0);
        rank2.add(1);
        rank2.add(0);
        rank2.add(1);
        ArrayList<Integer> rank3 = new ArrayList<>();
        rank3.add(0);
        rank3.add(0);
        rank3.add(2);
        rank3.add(0);
        ArrayList<Integer> rank4 = new ArrayList<>();
        rank4.add(1);
        rank4.add(0);
        rank4.add(0);
        rank4.add(1);

        ArrayList<ArrayList<Integer>> rank = new ArrayList<>();
        rank.add(rank1);
        rank.add(rank2);
        rank.add(rank3);
        rank.add(rank4);

        assertEquals(rank,results);
    }

    @Test
    void aggregateResult(){
        botManager = new BotManager(1, 1, 1,1, districtManager);
        logManager = new LogManager(botManager);
        LogManager.log = "";
        LogManager.endTurnLog = "";
        LogManager.characterLog = "";

        String filepath = System.getProperty("user.dir") + "/src/test/data/resultTests.csv";

        ArrayList<ArrayList<Float>> lastInfo = logManager.aggregateResult(filepath,4);
        ArrayList<Float> lastResult = lastInfo.get(0);
        ArrayList<Float> lastScore = lastInfo.get(1);
        int aggregation = Math.round(lastInfo.get(2).get(0));

        ArrayList<Float> expectedResult = new ArrayList<>();
        expectedResult.add((float) 59.559483);
        expectedResult.add((float) 20.37023);
        expectedResult.add((float) 9.347827);
        expectedResult.add((float) 10.697478);

        ArrayList<Float> expectedScore = new ArrayList<>();
        expectedScore.add((float) 27.779743);
        expectedScore.add((float) 21.433388);
        expectedScore.add((float) 18.846506);
        expectedScore.add((float) 17.224203);

        int expectedAgg = 4000;

        assertEquals(expectedResult,lastResult);
        assertEquals(expectedScore,lastScore);
        assertEquals(expectedAgg,aggregation);
    }
}