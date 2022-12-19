import control.ControllerFag;
import gamifier.control.StageFactory;
import gamifier.view.PaneView;
import gamifier.view.SimpleTextView;
import gamifier.model.Model;
import javafx.application.Application;
import javafx.stage.Stage;
import view.BasicView;

import java.util.Calendar;
import java.util.Random;


/**
 * The game Fox & Geese (Fag) is a game of inequality. It is played upon a cross
 * shaped board consisting of 33 points. The geese cannot capture the fox but
 * aim, through the benefit of numbers, to hem the fox in so that he cannot
 * move. The objective of the fox, on the other hand, is to capture geese until
 * it becomes impossible for them to trap him.
 */
public class Fag extends Application {

    enum Game {
        Single,         // Single: HUMAN against COMPUTER
        DoubleHuman,    // Double: HUMAN against HUMAN
        DoubleComputer, // Double: COMPUTER against COMPUTER (once)
        DoubleProfiler  // Double: COMPUTER against COMPUTER (loop times)
    }

    static Game   gamingMode = Game.Single;
    static Random generator = new Random();

    static long    seed  = -1;  // A seed for the random generator
    static String  name1 = "";  // The name of player #1
    static String  name2 = "";  // The name of player #2
    static String  team1 = "";  // The team of the player #1
    static String  team2 = "";  // The team of the player #2
    static int     algo1 = -1;  // The algorithm strength of computer #1
    static int     algo2 = -1;  // The algorithm strength of computer #2
    static int     loop  =  1;


    /**
     * Main method for the Fag application
     * @param args The supplied command-line arguments. Supported options are:
     *             -m or --mode <Single|DoubleHuman|DoubleComputer>
     *             -n1 or --name1 <NameOfPlayer1> The name of player #1.
     *             -n2 or --name2 <NameOfPlayer2> The name of player #2.
     *             -t1 or --team1 <Fox|Geese>     The team of player #1.
     *             -t2 or --team2 <Fox|Geese>     The team of player #2.
     *             -a1 or --algo1 <0|1|2>         The algorithm strength of computer #1
     *             -a2 or --algo2 <0|1|2>         The algorithm strength of computer #2
     *                 (if specified, sets the DOUBLE_C mode and forces loop=1)
     *             -l or --loop <LONG> The number of games to play
     *                 (if specified, sets the PROFILER  mode)
     *             -s or --seed <LONG> The seed of the random generator
     *                 (0 is currently reserved to force a specific scenario)
     */
    public static void main(String[] args) {

        // PARSE ARGUMENTS
        parseCommandLineArguments(args);

        // HERE WE GO -> LAUNCH THE APPLICATION
        launch(args);
    }

    /**
     * Parse the parameters passed to main
     *  (see the '@param args' of main for a description ot the arguments)
     */
    private static void parseCommandLineArguments(String[] args) {
        int l = 0;
        boolean help = false;
        while (l < args.length) {
            if ((args[l].equals("-s")) || (args[l].equals("--seed"))) {
                l++;
                seed = Long.parseLong(args[l]);
                System.out.println("Initializing the random generator seed to: " + seed);
            }
            else if ((args[l].equals("-m"))  || (args[l].equals("--mode"))) {
                l++;
                if (args[l].equals("Single")) {
                    gamingMode = Game.Single;
                    System.out.println("Setting the gaming mode in HUMAN-COMPUTER mode.");
                }
                else if (args[l].equals("DoubleHuman")) {
                    gamingMode = Game.DoubleHuman;
                    System.out.println("Setting the gaming mode in HUMAN-HUMAN mode.");
                }
                else if (args[l].equals("DoubleComputer")) {
                    gamingMode = Game.DoubleComputer;
                    System.out.println("Setting the gaming mode in COMPUTER-COMPUTER mode.");
                }
                else if (args[l].equals("PROFILER")) {
                    gamingMode = Game.DoubleProfiler;
                    System.out.println("Setting the gaming mode in PROFILER mode.");
                }
                else {
                    System.out.println("ERROR: Unrecognized gaming mode.");
                    help = true;
                }
            }
            else if ((args[l].equals("-n1")) || (args[l].equals("--name1"))) {
                l++;
                if (!args[l].isEmpty() && !args[l].isBlank()) {
                    name1 = args[l];
                }
                else {
                    System.out.println("ERROR: Please provide a name for player #1.");
                    help = true;
                }
            }
            else if ((args[l].equals("-n2")) || (args[l].equals("--name2"))) {
                l++;
                if (!args[l].isEmpty() && !args[l].isBlank()) {
                    name2 = args[l];
                }
                else {
                    System.out.println("ERROR: Please provide a name for player #2.");
                    help = true;
                }
            }
            else if ((args[l].equals("-t1")) || (args[l].equals("--team1"))) {
                l++;
                if (args[l].toLowerCase().equals("fox") ) {
                    team1 = "Fox";
                }
                else if (args[l].toLowerCase().equals("geese")) {
                    team1 = "Geese";
                }
                else {
                    System.out.println("ERROR: Unrecognized team for player #1.");
                    help = true;
                }
            }
            else if ((args[l].equals("-t2")) || (args[l].equals("--team2"))) {
                l++;
                if (args[l].toLowerCase().equals("fox") ) {
                    team2 = "Fox";
                }
                else if (args[l].toLowerCase().equals("geese")) {
                    team2 = "Geese";
                }
                else {
                    System.out.println("ERROR: Unrecognized team for player #2.");
                    help = true;
                }
            }
            else if ((args[l].equals("-a1")) || (args[l].equals("--algo1"))) {
                l++;
                algo1 = Integer.parseInt(args[l]);
            }
            else if ((args[l].equals("-a2")) || (args[l].equals("--algo2"))) {
                l++;
                algo2 = Integer.parseInt(args[l]);
            }
            else if ((args[l].equals("-l"))  || (args[l].equals("--loop")) ) {
                l++;
                loop = Integer.parseInt(args[l]);
                gamingMode = Game.DoubleProfiler;
                // TODO - Disable the graphic animation (if possible)
                // requestedGames = loop;
            }
            else if ((args[l].equals("-h")) || (args[l].equals("--help")) ) {
                help = true;
            }
            l++;
        }

        // SEED
        if(seed < 0) {
            generator = new Random(Calendar.getInstance().getTimeInMillis());
        } else if(seed != 0) {
            generator = new Random(seed);
        } else {
            System.out.println("WARNING-TODO: The seed equals zero. This case is not yet covered/reserved!!!");
            System.exit(1);
        }
        // MODE
        switch (gamingMode) {
            case Single:
                // HUMAN name, team and algo
                if (name1.isEmpty()) name1 = "Lena";
                if (team1.isEmpty()) team1 = "Fox";
                if (algo1 >= 0) {
                    System.out.println("ERROR: The algorithm strength cannot be set for a human player.");
                    help = true;
                }
                System.out.println("Setting player's name to " + name1);
                System.out.println("Setting " + name1 + "'s team to " + team1);
                // COMPUTER name, team and algo
                if (name2.isEmpty())
                    name2 = "Ai-"; // Always assign name #2 to "Ai-"
                if (!name2.toUpperCase().equals("AI-")) {
                    System.out.println("ERROR: In this mode, name #2 must always be set to \"Ai\" or must be left empty.");
                    help = true;
                }
                if (team1.toLowerCase().equals("fox")) {
                    if (team2.isEmpty()) {
                        team2 = "Geese";
                        if (algo2 < 0)
                            algo2 = 1;  // Default if not specified
                    }
                    else if (!team2.toLowerCase().equals("geese")) {
                        System.out.println("ERROR: The team #2 must be set to \"Geese\" or must be left empty.");
                        help = true;
                    }
                }
                else {
                    if (team2.isEmpty()) {
                        team2 = "Fox";
                        if (algo2 < 0)
                            algo2 = 0;  // Default if not specified
                    }
                    else if (!team2.toLowerCase().equals("fox")) {
                        System.out.println("ERROR: The team #2 must be set to \"Fox\" or must be left empty.");
                        help = true;
                    }
                }
                System.out.println("Setting " + name2 + "'s team to " + team2);
                System.out.println("Setting computer's algorithm to strength #" + algo2);
                name2 = name2  + algo2;
                System.out.println("Setting computer's name to " + name2);
                break;
            case DoubleHuman:
                if (name1.isEmpty()) name1 = "Lena";
                if (team1.isEmpty()) team1 = "Fox";
                System.out.println("Setting player-1's name to " + name1);
                System.out.println("Setting " + name1 + "'s team to " + team1);
                if (name2.isEmpty()) name2 = "Samuel";
                if (team2.isEmpty()) team2 = "Geese";
                System.out.println("Setting player-2's name to " + name2);
                if (team1.equals(team2)) {
                    System.out.println("ERROR: Team #1 cannot be the same as team #2.");
                    help = true;
                }
                else {
                    System.out.println("Setting " + name2 + "'s team to " + team2);
                }
                if (algo1 >= 0 || algo2 >= 0)  {
                    System.out.println("ERROR: The algorithm strength cannot be set for a human player.");
                    help = true;
                }
                break;
            case DoubleComputer:
            case DoubleProfiler:
                // Always assign team #1 to the fox
                if (team1.isEmpty())
                    team1 = "Fox";
                if (!team1.toLowerCase().equals("fox")) {
                    System.out.println("ERROR: In this mode, team #1 must always be assigned to the Fox or must be left empty.");
                    help = true;
                }
                // Always assign name #1 to "Ai-"
                if (name1.isEmpty())
                    name1 = "Ai-";
                if (!name1.toUpperCase().equals("AI-")) {
                    System.out.println("ERROR: In this mode, name #1 must always be set to \"Ai-\" or must be left empty.");
                    help = true;
                }
                if (algo1 < 0)  {
                    algo1 = 0; // Default if not specified
                }
                System.out.println("Setting computer-1's algorithm to strength #" + algo1);
                name1 = name1  + algo1;
                System.out.println("Setting computer-1's name to " + name1);

                // Always assign team #2 to the geese
                if (team2.isEmpty())
                    team2 = "Geese";
                if (!team2.toLowerCase().equals("geese")) {
                    System.out.println("ERROR: In this mode, team #2 must always be assigned to the Geese or must be left empty.");
                    help = true;
                }
                // Always assign name #2 to "Ai-"
                if (name2.isEmpty())
                    name2 = "Ai-";
                if (!name2.toUpperCase().equals("AI-")) {
                    System.out.println("ERROR: In this mode, name #2 must always be set to \"Ai-\" or must be left empty.");
                    help = true;
                }
                if (algo2 < 0) {
                    algo2 = 1; // Default if not specified
                }
                System.out.println("Setting computer-2's algorithm to strength #" + algo2);
                name2 = name2 + algo2;
                System.out.println("Setting computer-2's name to " + name2);
                break;
        }
        // HELP
        if (help) {
            printUsage();
            System.exit(0);
        }
    }

    static void printUsage() {
        System.out.println("\nUsage: FoxAndGeese [OPTIONS]");
        System.out.println("List of options:");
        System.out.println(" -m|--mode   <Single|DoubleHuman|DoubleComputer> The gaming mode (HUMAN-COMPUTER, HUMAN-HUMAN, COMPUTER-COMPUTER).");
        System.out.println(" -n1|--name1 <NameOfPlayer1> The name of player #1.");
        System.out.println(" -n2|--name2 <NameOfPlayer2> The name of player #2.");
        System.out.println(" -t1|--team1 <Fox|Geese>     The team of player #1.");
        System.out.println(" -t2|--team2 <Fox|Geese>     The team of player #2.");
        System.out.println(" -a1|--algo1 <0|1|2>         The algorithm strength of computer #1.");
        System.out.println(" -a2|--algo2 <0|1|2>         The algorithm strength of computer #2.");
        // System.out.println(" -l|--loop <LONG> The number of games to " +
        //         "play. If specified, sets the PROFILER mode and " +
        //         "disables the graphical mode.");
        System.out.println(" -s|--seed  <LONG> The seed of the random " +
                "generator. If 0, (TODO-TBD).");
        System.out.println("Enjoy the game...\n");
    }


    @Override
    public void start(Stage stage) throws Exception {

        // Create the global model
        Model model = new Model();

        // Add players.
        //  Note: The Fox always plays first.
        switch (gamingMode) {
            case Single:
                if (team1.equals("Fox")) {
                    model.addHumanPlayer(name1);
                    model.addComputerPlayer(name2);
                } else {
                    model.addComputerPlayer(name2);
                    model.addHumanPlayer(name1);
                }
                break;
            case DoubleHuman:
                if (team1.equals("Fox")) {
                    model.addHumanPlayer(name1);
                    model.addHumanPlayer(name2);
                } else {
                    model.addHumanPlayer(name2);
                    model.addHumanPlayer(name1);
                }
                break;
            case DoubleComputer:
            case DoubleProfiler:
                model.addComputerPlayer(name1);
                model.addComputerPlayer(name2);
                break;
        }

        // Register a single stage model and a view for the 'FoxAndGeese' game
        StageFactory.registerModel("fag", "model.FagStageModel");
        StageFactory.registerView("fag", "view.FagStageView");

        // Create the global view.
        BasicView view = new BasicView(model, stage);
        // Create a pane view for the introduction panel
        PaneView introView = view.createIntro(); // name: "intro"

        PaneView optionView = view.createOption(); // name: "intro"

        // Create a pane view for the game itself
        PaneView paneView = new PaneView("FoxAndGeese");
        // Add pane views to the global view.
        view.addPaneView(introView);
        view.addPaneView(optionView);
        view.addPaneView(paneView);
        // Create the controllers.
        ControllerFag control = new ControllerFag(model, view, generator);
        // Set the name of the first pane view to use when the game is started
        control.setGamePaneViewName("FoxAndGeese");
        // Set the name of the 1st and unique stage to create when the game is started
        control.setFirstStageName("fag");
        // Set the stage title
        stage.setTitle("Fox And Geese");
        // Show the JavaFx main stage
        stage.show();
        // Set the current view to display the intro pane view.
        // In this case, no gamestageview is given since all visual elements are created directly in SimpleTextView.
        view.setView(paneView.getName(), null);
    }

}
