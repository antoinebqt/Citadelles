package citadelles;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main class
 */
public class Main {

    private final static Logger normalExecution;
    private final static Logger simulationExecution;

    private static final int NUMBER_OF_EXECUTIONS = 1000;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");
        normalExecution = Logger.getLogger("Normal");
        simulationExecution = Logger.getLogger("Simulation");
    }

    public static void main(String... args) throws IllegalArgumentException, IOException {
        int nbExec = 1;
        boolean demoModeOn = false;
        // if args "demo" then run the normal execution
        if (args.length > 0 && isDemoMode(args[0])) {
            demoModeOn = true;
            normalExecution.setLevel(Level.INFO);
            simulationExecution.setLevel(Level.OFF);
            normalExecution.log(Level.INFO, "DEMO RUN");
        }else {
            nbExec = NUMBER_OF_EXECUTIONS;
            normalExecution.setLevel(Level.OFF);
            simulationExecution.setLevel(Level.INFO);
            simulationExecution.log(Level.INFO, "SIMULATION RUN");
        }

        for (int i = 0; i < nbExec; i++){
            //Create a game with X smart bots, Y medium bots and Z stupid bots
            Citadelles citadelles;
            if (args.length > 0 && isSmartSimulationMode(args[0])){
                citadelles = new Citadelles(4, 0, 0,0);
            } else {
                citadelles = new Citadelles(1, 1, 1,1);
            }
            citadelles.launch();

            //Define who won the game and prints the result
            citadelles.showResult();

            if(!demoModeOn && i == nbExec-1) citadelles.showSimulation();
        }


    }

    private static boolean isDemoMode(String arg){
        return arg.toLowerCase(Locale.ROOT).equals("demo");
    }

    private static boolean isSmartSimulationMode(String arg){
        return arg.toLowerCase(Locale.ROOT).equals("smart");
    }
}
