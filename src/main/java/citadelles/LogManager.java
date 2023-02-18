package citadelles;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import citadelles.bots.Bot;
import citadelles.bots.BotManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

/**
 * print everything that append in the console
 */
public record LogManager(BotManager botManager) {

    public static String endTurnLog = "";
    public static String log = "";
    public static String characterLog = "";

    private static final Logger normalExecution = Logger.getLogger("Normal");
    private static final Logger simulationExecution = Logger.getLogger("Simulation");

    private static final ArrayList<ArrayList<Integer>> rankings = new ArrayList<>();
    private static final ArrayList<ArrayList<Integer>> scores = new ArrayList<>();

    private static final DecimalFormat df = new DecimalFormat("00.00");

    /**
     * Print the log of each bot for a turn
     */
    public static void showLog() {
        normalExecution.log(Level.INFO, log);

        //Clear the log string for the next turn
        log = "";
    }

    /**
     * Print the special end log at the end of the turn (Like the assassin victim's)
     */
    public static void showEndLog() {
        normalExecution.log(Level.INFO, endTurnLog);

        //Clear the end turn log string for the next turn
        endTurnLog = "";
    }

    /**
     * Print the log of the character
     */
    public static void showCharacterLog() {
        normalExecution.log(Level.INFO, characterLog);
        characterLog = "";
    }

    /**
     * Get all games results and simplify them
     * @param results rankings
     * @param averageScore Scores
     * @param nbBot int
     */
    void getRankingAndScoresCounts(ArrayList<ArrayList<Integer>> results, ArrayList<Integer> averageScore, int nbBot){
        for (int i = 0;i < 4;i++) {
            results.add(new ArrayList<>());
            averageScore.add(0);
        }

        results.forEach(list->{
            for (int i = 0;i < nbBot;i++)list.add(0);
        });

        int size = rankings.size();
        for (int n = 0;n < size; n++){
            ArrayList<Integer> ranking = rankings.get(n);
            for (int position = 0 ; position < nbBot ; position++){
                int idBot = ranking.get(position);
                int newVal = results.get(idBot).get(position)+1;
                results.get(idBot).set(position,newVal);
                int newScore = scores.get(n).get(position) + averageScore.get(idBot);
                averageScore.set(idBot, newScore);
            }
        }
    }

    /**
     * Set a new rankings of games
     * @param newRankings ArrayList
     */
    void setRankings(ArrayList<ArrayList<Integer>> newRankings){
        rankings.clear();
        rankings.addAll(newRankings);
    }

    /**
     * Set a new scores of games
     * @param newScores int
     */
    void setScores(ArrayList<ArrayList<Integer>> newScores){
        scores.clear();
        scores.addAll(newScores);
    }

    /**
     * Create a tab that gave what append during all games
     *
     * bot\rank | 1
     * -------------
     * 0        | 0
     * 1        | 0
     * 2        | 0
     * 3        | 0
     *
     *  results[bot][rank]
     */
    public void showLogSimulation() {
        int nbBot = botManager.getNumberOfBots();
        int nbGames = rankings.size();
        int aggregation;

        String filepathStats = System.getProperty("user.dir") + "/save/result.csv";

        ArrayList<ArrayList<Float>> lastInfo = aggregateResult(filepathStats, nbBot);
        ArrayList<Float> lastResult = lastInfo.get(0);
        ArrayList<Float> lastScore = lastInfo.get(1);
        aggregation = Math.round(lastInfo.get(2).get(0));

        StringBuilder textCSV = new StringBuilder();
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        ArrayList<Integer> averageScore = new ArrayList<>();
        float countGames;

        if(aggregation != (float)1 ) countGames = aggregation;
        else countGames = 0;

        textCSV.append(nbGames + countGames).append("=").append(nbBot).append("=");

        getRankingAndScoresCounts(results, averageScore, nbBot);

        StringBuilder line = new StringBuilder(colorize("*********************** Simulation Result ***********************\n", YELLOW_TEXT(), BOLD()));
        line.append(colorize("Number of games: ", WHITE_TEXT())).append(Math.round(nbGames+countGames)).append("\n");

        line.append(colorize("Percentage of luck that each bot got the place correspond:\n", BRIGHT_GREEN_TEXT()));

        for (int i = 0; i < nbBot; i++){
            line.append(colorize("Bot " + i, WHITE_TEXT())).append(getType(i)).append(" ");
            textCSV.append(getName(i)).append("=");
            float percentage =(results.get(i).get(0)+((lastResult.get(i)/100)*aggregation)) / (nbGames + aggregation)* 100;

            line.append(df.format(percentage)).append("%").append(colorize(" of win and ", BLUE_TEXT())).append(df.format(100-percentage)).append("%").append(colorize(" of loose ", BLUE_TEXT()));
            //percentage of win and loose
            textCSV.append(percentage).append("=").append((float) 100 - percentage).append("=");

            //average score on all games
            float score = (averageScore.get(i)+lastScore.get(i)*aggregation) / (nbGames + aggregation);
            line.append(colorize("with an average score of: ",BLUE_TEXT())).append(score).append(colorize(" points.",BLUE_TEXT()));
            textCSV.append("|=").append(score).append("=");
            line.append("\n");
        }

        simulationExecution.log(Level.INFO, line.toString());

        CSVWriter(textCSV.toString(), filepathStats);
    }

    /**
     * Create a CSV and put all stats in it.
     * @param text string
     * @param path filepath
     */
    void CSVWriter(String text, String  path){

        File directory = new File(System.getProperty("user.dir") + "/save");
        if (! directory.exists()){
            directory.mkdir();
        }

        try {
            File file = new File(path);
            if(file.createNewFile())
                System.out.println("File creation successful");
            else
                System.out.println("File already exists in specified path");
        }
        catch(IOException io) {
            System.out.println("Wrong filepath");
        }

        try{
            CSVWriter writer = new CSVWriter(new FileWriter(path));
            //Create record
            String [] record = text.split("=");
            //Write the record to file
            writer.writeNext(record);

            //close the writer
            writer.close();
        }catch(IOException io) {
            System.out.println("Wrong filepath for the writer");
        }
    }

    /**
     * Create an ArrayList of the correct size with everything set to 0
     * @param botNumber size
     * @return Array
     */
    private ArrayList<ArrayList<Float>> initOldResultArray(int botNumber){
        ArrayList<ArrayList<Float>> oldResult = new ArrayList<>();
        ArrayList<Float> oldResults = new ArrayList<>();
        ArrayList<Float> oldScores = new ArrayList<>();
        ArrayList<Float> oldNbGames = new ArrayList<>();
        oldNbGames.add((float)1);
        for (int i = 0; i < botNumber; i++){
            oldResults.add((float) 0);
            oldScores.add((float) 0);
        }
        oldResult.add(oldResults);
        oldResult.add(oldScores);
        oldResult.add(oldNbGames);
        return oldResult;
    }

    /**
     * Get all result that have already been generated and are in the CSV
     * @param path filepath
     * @param botNumber size of the array
     * @return ArrayList
     */
    ArrayList<ArrayList<Float>> aggregateResult(String path, int botNumber) {
        ArrayList<ArrayList<Float>> oldResult = initOldResultArray(botNumber);

        try{
            CSVReader reader = new CSVReader(new FileReader(path), ',' , '"' , 0);

            //Read CSV line by line and use the string array as you want
            String[] nextLine = reader.readNext();

            if(!sameTypeData(nextLine,botNumber))return oldResult;
            for(int i = 2; i < botNumber+2;i++){
                oldResult.get(0).set(i-2, Float.valueOf(nextLine[3+5*(i-2)]));

                oldResult.get(1).set(i-2, Float.valueOf(nextLine[6+5*(i-2)]));
            }
            oldResult.get(2).set(0,Float.valueOf(nextLine[0]));
        }catch (IOException io){
            System.out.println("File not exist");
        }
        return oldResult;
    }

    /**
     * Check if the new data and the data in CSV have the same type
     * @param nextLine the CSV
     * @param botNumber int
     * @return bool
     */
    private boolean sameTypeData(String[] nextLine, int botNumber) {
        if(nextLine==null) return false;

        if(Integer.parseInt(nextLine[1])!=botNumber) return false;

        ArrayList<String> lastBots = new ArrayList<>();
        for(int i = 0; i< botNumber ; i++){
            lastBots.add(nextLine[2+5*i]);
        }

        //check if it s the same bots
        return lastBots.equals(getNewBotsName(botNumber));
    }

    private List<?> getNewBotsName(int n) {
        ArrayList<String> allNames = new ArrayList<>();
        for(int i = 0; i<n; i++){
            allNames.add(getName(i));
        }
        return allNames;
    }

    /**
     * Print a summary of the game at the end of each turn
     */
    public void showResume() {
        StringBuilder sb = new StringBuilder();
        sb.append("-----------\n| SUMMARY |").append("\n");
        botManager.getListBot().forEach(bot -> sb.append("------------------------"));
        sb.append("-\n");
        botManager.getListBot().forEach(bot -> sb.append("| Bot ").append(bot.getID()).append(" \t\t|"));
        sb.append("\n");
        botManager.getListBot().forEach(bot -> sb.append("------------------------"));
        sb.append("-\n");
        botManager.getListBot().forEach(bot -> sb.append("| Golds : ").append(bot.getGold()).append(" \t\t|"));
        sb.append("\n");
        botManager.getListBot().forEach(bot -> sb.append("| Cards : ").append(bot.getHandSize()).append(" \t\t|"));
        sb.append("\n");
        botManager.getListBot().forEach(bot -> sb.append("| Districts : ").append(bot.getNumberDistrictPlaced()).append(" \t|"));
        sb.append("\n");
        botManager.getListBot().forEach(bot -> sb.append("------------------------"));
        sb.append("-\n");
        normalExecution.log(Level.INFO, colorize(sb.toString(), CYAN_TEXT()));
    }

    /**
     * Print the result with the score at the end of the game
     */
    void showResult() {
        normalExecution.log(Level.INFO, colorize("***************************** Final Result *****************************", YELLOW_TEXT(), BOLD()));

        ArrayList<Integer> ranking = new ArrayList<>();
        ArrayList<Integer> score = new ArrayList<>();

        ArrayList<Bot> botList = getBots();
        sortBotList(botList);

        botList.forEach(bot -> {
            String line;
            if (bot.getFinishPosition() == 0) {
                line = defaultColor("Bot ") + colorize("" + bot.getID(),TEXT_COLOR(255, 165, 0)) + getType(bot) + colorize(" hasn't finished with a score of " + printScore(bot) + " and "
                        + printDistrict(bot) + " placed", BRIGHT_RED_TEXT(), BOLD());
            } else {
                line = defaultColor("Bot ") + colorize("" + bot.getID(),TEXT_COLOR(255, 165, 0)) + getType(bot) + colorize(" has finished in position " + bot.getFinishPosition()
                        + " with a score of " + bot.getScore(), BRIGHT_GREEN_TEXT(), BOLD());
            }
            normalExecution.log(Level.INFO, line);
            ranking.add(bot.getID());
            score.add(bot.getScore());
        });
        scores.add(score);
        rankings.add(ranking);
        normalExecution.log(Level.INFO, colorize("\nBot " + botList.get(0).getID() + " won the game!", BLUE_TEXT(), BOLD()));
    }

    /**
     * Simple method for print the number of district of a bot
     *
     * @param bot The bot
     * @return A string with the right word (plural or not)
     */
    String printDistrict(Bot bot) {
        return bot.getNumberDistrictPlaced() > 1 ? bot.getNumberDistrictPlaced() + " districts" : bot.getNumberDistrictPlaced() + " district ";
    }

    /**
     * Simple method for print the score of a bot
     *
     * @param bot The bot
     * @return The string
     */
    String printScore(Bot bot) {
        return bot.getScore() > 9 ? String.valueOf(bot.getScore()) : bot.getScore() + " ";
    }

    /**
     * Sort the bot on their score and then on their
     * character role during the last turn of game
     *
     * @param botList The list of bot unsorted
     * @return The list of bot sorted
     */
    public ArrayList<Bot> sortBotList(ArrayList<Bot> botList) {
        botList.sort(Comparator.comparing(Bot::getScore).thenComparing(Bot::getCharacterId));
        Collections.reverse(botList);
        return botList;
    }

    /**
     * Simple method for print the type of the bot (Stupid, Medium or Smart)
     *
     * @param bot The bot
     * @return The string with the type of the bot
     */
    String getType(Bot bot) {
        return " " + colorize(String.valueOf(bot), CYAN_TEXT(), ITALIC(), BOLD());
    }

    String getType(int idBot) {
        Bot search = null;
        for (Bot bot : botManager.getListBot()){
            if(bot.getID()==idBot) search = bot;
        }
        return getType(search);
    }

    String getName(Bot bot){
        return String.valueOf(bot);
    }

    String getName(int idBot) {
        Bot search = null;
        for (Bot bot : botManager.getListBot()){
            if(bot.getID()==idBot) search = bot;
        }
        return getName(search);
    }

    /**
     * Get the list containing the bots
     *
     * @return the list's bot
     */
    public ArrayList<Bot> getBots() {
        return botManager.getListBot();
    }

    /**
     * Print the bot who has the crown
     *
     * @param crowned The ID of the bot with the crown
     */
    public void showTheCrownedBot(int crowned) {
        normalExecution.log(Level.INFO, colorize("Bot " + crowned + " has the crown", YELLOW_TEXT()));
    }

    public static String defaultColor(String str) {
        return colorize(str, WHITE_TEXT());
    }

}
