package control;

import gamifier.control.ActionPlayer;
import gamifier.control.Controller;
import gamifier.model.Model;
import gamifier.model.Player;
import gamifier.view.View;
import model.FagStageModel;

import java.util.List;
import java.util.Random;

public class ControllerFag extends Controller {

    public static Random generator = new Random();

    /**
     * Global controller of the FoxAndGeese game. This class contains most of the
     * methods needed for the management of the game.
     * @param model     A ref. to the global model.
     * @param view      A ref. to the global view.
     * @param generator A ref. to a centralized random generator.
     */
    public ControllerFag(Model model, View view, Random generator) {
        super(model, view);
        setControlKey(new ControllerFagKey(model, view, this));
        setControlMouse(new ControllerFagMouse(model, view, this));
        setControlAction (new ControllerFagAction(model, view, this));
        this.generator = generator;
    }

    public void nextPlayer() {
        /* What must be done :
          - compute the id of the next player
          - set the model to this id
          - if this id corresponds to a computer :
             - instantiate a Decider,
             - instantiate an ActionPlayer to play what the Decider guesses.
         */

        // Use the default method to compute next player
        model.setNextPlayer();
        // Get the current player
        Player p = model.getCurrentPlayer();
        // Change the text of the TextElement
        FagStageModel stageModel = (FagStageModel) model.getGameStage();
        String team = (model.getIdPlayer() == 0) ? "Fox" : "Geese";
        stageModel.getPlayerName().setText(p.getName() + " (" + team + ")");

        List<Player> players = model.getPlayers();
        Player currPlayer = players.get(model.getIdPlayer());
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER " + currPlayer.getName() + " PLAYS " + team + " (IdPlayer=" + model.getIdPlayer() + ")");
            FagDecider decider = new FagDecider(model,this);
            ActionPlayer play = new ActionPlayer(model, view, this, decider, null);
            play.start();
        }
        else {
            System.out.println("HUMAN " + currPlayer.getName() + " PLAYS " + team + " (IdPlayer=" + model.getIdPlayer() + ")");
        }
    }
}
