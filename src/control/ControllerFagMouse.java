package control;

import gamifier.control.ActionPlayer;
import gamifier.control.Controller;
import gamifier.control.ControllerMouse;
import gamifier.model.*;
import gamifier.model.action.ActionList;
import gamifier.model.action.GameAction;
import gamifier.model.action.MoveAction;
import gamifier.model.action.RemoveAction;
import gamifier.model.animation.AnimationTypes;
import gamifier.view.GridLook;
import gamifier.view.View;
import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import model.FagPawn;
import model.FagStageModel;

import java.util.List;
import java.util.ListIterator;

/**
 * A basic mouse controller that just grabs the mouse clicks and prints out some informations.
 * It gets the elements of the scene that are at the clicked position and prints them.
 */
public class ControllerFagMouse extends ControllerMouse implements EventHandler<MouseEvent> {

    String className = new Object(){}.getClass().getName();

    public ControllerFagMouse(Model model, View view, Controller control) {
        super(model, view, control);
    }

    public void handle(MouseEvent event) {
        final boolean DEBUG = true;
        String methodName = "[" + new Throwable().getStackTrace()[0].getClassName() + "." +
                new Throwable().getStackTrace()[0].getMethodName() + "()] ";

        String methName = new Object(){}.getClass().getEnclosingMethod().getName();

        // If mouse event capture is disabled in the model, just return
        if (!model.isCaptureMouseEvent()) return;

        // Get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Point2D clic = new Point2D(event.getSceneX(),event.getSceneY());
        // Get elements at that position
        List<GameElement> list = control.elementsAt(clic);

        // For debug, uncomment next instructions to display x,y and elements at that position
        if (DEBUG) System.out.println(methodName + "click in "+event.getSceneX()+","+event.getSceneY());
        for(GameElement element : list) {
            if (DEBUG) System.out.println(methodName + "Selected element is: " + element.toString());
        }

        // Note: To avoid numerous casts, get the single stage in this game.
        // Warning: This method is not advisable for a multi-stage game.
        FagStageModel fagStageModel = (FagStageModel) model.getGameStage();

        if (fagStageModel.getState() == FagStageModel.STATE_SELECTPAWN) {
            for (GameElement element : list) {
                if ((element.getType() == ElementTypes.getType("Goose")) ||
                    (element.getType() == ElementTypes.getType("Fox"))) {
                    FagPawn pawn = (FagPawn)element;
                    if (DEBUG) System.out.println(methodName + "Current player is: " + model.getIdPlayer());
                    // Check if type of the pawn corresponds to the current player id
                    if ((pawn.getType() - 50) == model.getIdPlayer()) {  // TODO-FIXME
                        element.toggleSelected();
                        fagStageModel.setState(FagStageModel.STATE_SELECTDEST);
                        return; // Do not allow another element to be selected
                    }
                }
            }
        }
        else if (fagStageModel.getState() == FagStageModel.STATE_SELECTDEST) {
            // First check if the click is on the current selected pawn
            for (GameElement element : list) {
                if (element.isSelected()) {
                    element.toggleSelected();
                    fagStageModel.setState(FagStageModel.STATE_SELECTPAWN);
                    return;
                }
            }
            // Secondly, search if the board has been clicked
            for (GameElement element : list) {
                if (element == fagStageModel.getBoard()) {
                    // Get the look of the board
                    GridLook look = (GridLook) control.getElementLook(element);
                    // Retrieve the destination cell (i.e. the cell that has been clicked) from the click coordinates
                    int[] dstCell = look.getCellFromSceneLocation(clic);
                    // Retrieve the source cell (i.e. the cell that owns the selected pawn)
                    FagPawn selectedPawn = (FagPawn) model.getSelected().get(0);
                    int[] srcCell = fagStageModel.getElementCell(selectedPawn);
                    if (DEBUG) System.out.println(methodName + "Try to move pawn from " + srcCell[0]+"," + srcCell[1] +
                            " to " + dstCell[0] + "," + dstCell[1]);
                    // If the destination cell can be reached by the selected pawn
                    if (fagStageModel.canReachCell(dstCell[0], dstCell[1])) {
                        // Build the list of actions to do, and pass to the next player when done
                        ActionList actions = new ActionList(true);
                        // Step 1 : move the pawn
                        // Determine the destination point in the root pane
                        Point2D centerDstCell = look.getRootPaneLocationForCellCenter(dstCell[0], dstCell[1]);
                        // Create an action with a linear move animation, with 10 pixel/frame
                        GameAction move = new MoveAction(model, selectedPawn, "board", dstCell[0], dstCell[1], AnimationTypes.MOVELINEARPROP_NAME, centerDstCell.getX(), centerDstCell.getY(), 10);
                        // Add a callback when animation end. NB: just given as an example on how to use such a callback
                        move.onAnimationEnd(() -> {
                            System.out.println("END ANIMATION OF THE PAWN SELECTED BY THE PLAYER");
                        });
                        // Add the action to the action list.
                        actions.addSingleAction(move);
                        // Step 2 : Check if there is a take. In this case remove the pawn from the board.
                        if (selectedPawn.isFox()) {
                            List<GameElement> geList = fagStageModel.getTakenPawns(selectedPawn, dstCell[0], dstCell[1]);
                            ListIterator<GameElement> geListItor = geList.listIterator();
                            while (geListItor.hasNext()) {
                                GameElement p = geListItor.next();
                                if (DEBUG) System.out.println(methodName + "Must remove " + p);
                                actions.addSingleAction(new RemoveAction(model, p));
                            }
                        }
                        fagStageModel.unselectAll();
                        fagStageModel.setState(FagStageModel.STATE_SELECTPAWN);
                        ActionPlayer play = new ActionPlayer(model, view, control, actions);
                        play.start();
                    }
                }
            }
        }

    }
}

