package control;

import gamifier.control.Controller;
import gamifier.control.Decider;
import gamifier.model.GameElement;
import gamifier.model.Model;
import gamifier.model.action.ActionList;
import gamifier.model.action.GameAction;
import gamifier.model.action.MoveAction;
import gamifier.model.action.RemoveAction;
import gamifier.model.animation.AnimationTypes;
import gamifier.view.GridLook;
import javafx.geometry.Point2D;
import model.FagBoard;
import model.FagStageModel;
import model.FagPawn;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.lang.Integer.MAX_VALUE;
import static model.FagBoard.GRID_SIZE;

public class FagDecider extends Decider {
    final static String boardName = "board";

    public FagDecider(Model model, Controller control) {
        super(model, control);
    }

    /**
     * Computes the center of mass of a group of cells.
     *
     * @param cells A list of cell.
     * @return The coordinates of the center of mass in [X,Y] coordinates.
     *
     * @note 'X' corresponds to the 'col' direction and 'Y' to the 'row' direction.
     */
    private Point2D centerOfMass(List<FagBoard.Cell> cells) {
        double totalMass = 1* cells.size();
        double xCM = 0.0;
        double yCM = 0.0;
        for (int c=0; c<cells.size(); c++) {
            xCM += cells.get(c).getCol();
            yCM += cells.get(c).getRow();
        }
        xCM = xCM / totalMass;
        yCM = yCM / totalMass;

        return new Point2D(xCM, yCM);
    }

    /**
     * Computes the  Euclidean distance between two Point2D
     * @param p1 The first point2D
     * @param p2 The second point2D
     * @return The euclidean distance
     *
     * @note 'X' corresponds to the 'col' direction and 'Y' to the 'row' direction.
     */
    public double euclideanDistance(Point2D p1, Point2D p2) {
        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();
        double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        return distance;
    }

    /**
     * Retrieve the cell which is the closest to another cell
     * @param cell      The reference cell
     * @param neighbors A list of neighbor locations.
     * @return The neighbor which distance to cell is the smallest.
     */
    public FagBoard.Cell getClosestCell(FagBoard.Cell cell, List<FagBoard.Cell> neighbors) {
        FagBoard.Cell closestCell = new FagBoard.Cell();
        double smallestDistance = MAX_VALUE;
        Point2D srcPoint = new Point2D(cell.getCol(), cell.getRow());

        for (FagBoard.Cell neighbor : neighbors) {
            Point2D dstPoint = new Point2D(neighbor.getCol(), neighbor.getRow());
            if (euclideanDistance(srcPoint, dstPoint) < smallestDistance) {
                smallestDistance = euclideanDistance(srcPoint, dstPoint);
                closestCell.row = neighbor.getRow();
                closestCell.col = neighbor.getCol();
            }
        }
        return closestCell;
    }

    /**
     * Retrieve the cell which is the farthest from another cell
     * @param cell      The reference cell
     * @param neighbors A list of neighbor locations.
     * @return The neighbor which distance to cell is the smallest.
     */
    public FagBoard.Cell getFarthestCell(FagBoard.Cell cell, List<FagBoard.Cell> neighbors) {
        FagBoard.Cell farthestCell = new FagBoard.Cell();
        double biggestDistance = 0;
        Point2D srcPoint = new Point2D(cell.getCol(), cell.getRow());

        for (FagBoard.Cell neighbor : neighbors) {
            Point2D dstPoint = new Point2D(neighbor.getCol(), neighbor.getRow());
            if (euclideanDistance(srcPoint, dstPoint) > biggestDistance) {
                biggestDistance = euclideanDistance(srcPoint, dstPoint);
                farthestCell.row = neighbor.getRow();
                farthestCell.col = neighbor.getCol();
            }
        }
        return farthestCell;
    }

    /**
     * AI algorithm #0 for the Geese team.
     * @param fagBoard   A ref. to the board.
     * @param actions A ref. to the action list to be executed after this AI.
     *
     * @Note: This is a naive algorithm. It takes the first available goose and
     *  moves it to a random valid place.
     */
    void GeeseAi0(FagBoard fagBoard, ActionList actions) {
        GameElement goosePawn = null; // The goosePawn that is moved
        int rowDest = 0; // The destination row in board
        int colDest = 0; // The destination column in board
        final boolean DEBUG = true;
        String methodName = "[" + new Throwable().getStackTrace()[0].getClassName() + "." +
                new Throwable().getStackTrace()[0].getMethodName() + "()] ";

        // Collect the movable goose pawns
        List<FagBoard.Cell> movableGeese = fagBoard.getMovableGeese();

        // Choose a movable goose at random
        if (movableGeese.size() != 0) {
            if (DEBUG) System.out.println(methodName + "movableGeese.size() = " + movableGeese.size());
            int gooseId = ControllerFag.generator.nextInt(movableGeese.size());
            if (DEBUG) System.out.println(methodName + "gooseId = " + gooseId);
            int row = movableGeese.get(gooseId).row;
            int col = movableGeese.get(gooseId).col;
            if (DEBUG) System.out.println(methodName + "Row=" + row + " Col=" + col);
            FagPawn boardElement = (FagPawn) fagBoard.getElement(row, col);
            List<FagBoard.Cell> reachable = fagBoard.computeReachableCells(row, col);
            int dirId = ControllerFag.generator.nextInt(reachable.size());

            if (DEBUG) System.out.println(methodName + "dirId=" + dirId);
            goosePawn = boardElement;
            rowDest = reachable.get(dirId).row;
            colDest = reachable.get(dirId).col;
        }

        createMoveAction(fagBoard, rowDest, colDest, goosePawn, actions);
    }

    /**
     * AI algorithm #1 for the Geese team.
     * @param fagBoard A ref. to the board.
     * @param actions  A ref. to the action list to be executed after this AI.
     *
     * @Note: This algorithm tries to win on the long run by continuously
     *  avoiding any goose to be eaten by the fox.
     */
    void GeeseAi1(FagBoard fagBoard, ActionList actions) {

        FagBoard.Cell gooseSrc = new FagBoard.Cell();
        FagBoard.Cell gooseDst = new FagBoard.Cell();
        final boolean DEBUG = true;
        String methodName = "[" + new Throwable().getStackTrace()[0].getClassName() + "." +
                new Throwable().getStackTrace()[0].getMethodName() + "()] ";

        // Retrieve grid location of the Fox
        FagBoard.Cell fox = fagBoard.getFoxLocation();

        // Collect the movable goose pawns
        List<FagBoard.Cell> movableGeese = fagBoard.getMovableGeese();

        // Collect the geese which are at risk of being eaten
        List<FagBoard.Cell> geeseAtRisk = fagBoard.getGeeseAtRisk(fox);

        if (!geeseAtRisk.isEmpty()) {
            // -- Play defense strategy ----------------------------------------
            //  Try to protect the geese at risk by moving them around
            if (DEBUG) System.out.println(methodName + "Found " + geeseAtRisk.size() + " geese at risk: " + geeseAtRisk.toString());

            // Step-1: Find the goose that is the closest to the fox
            gooseSrc = getClosestCell(fox, geeseAtRisk);

            if (movableGeese.contains(gooseSrc)) {
                // Step-2: This goose is movable. Select a random direction and move the goose away from the fox.
                List<FagBoard.Cell> reachableForGoose = fagBoard.reachableForGoose(gooseSrc);
                if (reachableForGoose.size() == 0) {
                    System.out.println("Houston, we have a problem. The code should never end up here !!!");
                }
                // Step-3: Select the direction that brings that goose the farthest away from fox.
                gooseDst = getFarthestCell(fox, reachableForGoose);
            }
            else {
                // Step-2: This goose is not movable. Select the closest goose among the movable ones.
                gooseSrc = getClosestCell(fox, movableGeese);
                // Step-3: Select the direction that brings that goose closer to the fox.
                List<FagBoard.Cell> reachableForGoose = fagBoard.reachableForGoose(gooseSrc);
                if (reachableForGoose.size() == 0) {
                    System.out.println("Houston, we have a problem. The code should never end up here !!!");
                }
                gooseDst = getClosestCell(fox, reachableForGoose);
            }
        }
        else {
            //-- Play attack strategy ------------------------------------------
            if (DEBUG) System.out.println(methodName + "Found " + movableGeese.size() + " movable geese.");

            // Play the movable geese that is the further away from the fox
            gooseSrc = fagBoard.maxDistance(fox, movableGeese);

            List<FagBoard.Cell> reachableForGoose = fagBoard.reachableForGoose(gooseSrc);

            // Play the direction that maintains the smallest center of mass
            Point2D centerOfMass = this.centerOfMass(reachableForGoose);
            double smallestDistance = Double.MAX_VALUE;

            for (int g=0; g<reachableForGoose.size(); g++) {
                Point2D destPoint = new Point2D(reachableForGoose.get(g).getCol(), reachableForGoose.get(g).getRow());
                if (DEBUG) System.out.println(methodName + "Distance " + g + " = " + euclideanDistance(centerOfMass, destPoint));
                if (euclideanDistance(centerOfMass, destPoint) < smallestDistance) {
                    smallestDistance = euclideanDistance(centerOfMass, destPoint);
                    gooseDst.row = reachableForGoose.get(g).row;
                    gooseDst.col = reachableForGoose.get(g).col;
                }
            }
        }

        // Play this direction
        FagPawn gooseElement = (FagPawn) fagBoard.getElement(gooseSrc.row, gooseSrc.col);

        createMoveAction(fagBoard, gooseDst.row, gooseDst.col, gooseElement, actions);
    }

    /**
     * AI algorithm #0 for the Fox team.
     * @param fagBoard   A ref. to the board.
     * @param actions A ref. to the action list to be executed after this AI.
     *
     * @Note: This is a naive algorithm. It always moves the fox to a random
     *  place unless there is a goose to be eaten.
     */
    void FoxAi0(FagBoard fagBoard, ActionList actions) {
        final boolean DEBUG = true;
        String methodName = "[" + new Throwable().getStackTrace()[0].getClassName() + "." +
                new Throwable().getStackTrace()[0].getMethodName() + "()] ";

        FagBoard.Cell foxSrc = fagBoard.getFoxLocation();
        FagBoard.Cell foxDst = new FagBoard.Cell();

        List<FagBoard.Cell> reachableForFox = fagBoard.reachableForFox(foxSrc.row, foxSrc.col);

        // Collect the geese which are at risk of being eaten
        List<FagBoard.Cell> geeseAtRisk = fagBoard.getGeeseAtRisk(foxSrc);

        if (!geeseAtRisk.isEmpty()) {
            if (DEBUG) System.out.println(methodName + "Found " + geeseAtRisk.size() + " geese at risk: " + geeseAtRisk.toString());
            // Eat a random number of geese
            //  TODO - Should eat a maximum number of geese
            List<FagBoard.Cell> reachableForJump = fagBoard.reachableForJump(foxSrc, new boolean[GRID_SIZE][GRID_SIZE]);
            // Move to a random jump
            int jumpId = ControllerFag.generator.nextInt(reachableForJump.size());
            foxDst.row = reachableForFox.get(jumpId).row;
            foxDst.col = reachableForFox.get(jumpId).col;
        }
        else {
            // Move to a random position
            int dirId = ControllerFag.generator.nextInt(reachableForFox.size());
            foxDst.row = reachableForFox.get(dirId).row;
            foxDst.col = reachableForFox.get(dirId).col;
        }

        FagPawn foxPawnElement = (FagPawn) fagBoard.getElement(foxSrc.row, foxSrc.col);

        // Remove eaten geese from the board
        List<GameElement> geese = fagBoard.getTakenPawns(foxPawnElement, foxDst.row, foxDst.col);
        ListIterator<GameElement> gooseItor = geese.listIterator();
        while (gooseItor.hasNext()) {
            GameElement p = gooseItor.next();
            if (DEBUG) System.out.println(methodName + "Must remove goose:" + p);
            actions.addSingleAction(new RemoveAction(model, p));
        }

        createMoveAction(fagBoard, foxDst.row, foxDst.col, foxPawnElement, actions);
    }

    /**
     * AI algorithm #1 for the Fox team.
     * @param fagBoard   A ref. to the board.
     * @param actions A ref. to the action list to be executed after this AI.
     *
     * @Note: This algorithm builds a decision tree and walks through it with a MinMax
     * algorithm which goal is to find the highest gain that a player can be sure to
     * get without knowing the actions of the other player.
     */
    void FoxAi1(FagBoard fagBoard, ActionList actions) {
        final boolean DEBUG = true;
        String methodName = "[" + new Throwable().getStackTrace()[0].getClassName() + "." +
                new Throwable().getStackTrace()[0].getMethodName() + "()] ";

        FagBoard.Cell foxSrc = fagBoard.getFoxLocation();
        FagBoard.Cell foxDst = new FagBoard.Cell();

        List<FagBoard.Cell> reachableForFox = fagBoard.reachableForFox(foxSrc.row, foxSrc.col);

        // Collect the geese which are at risk of being eaten
        List<FagBoard.Cell> geeseAtRisk = fagBoard.getGeeseAtRisk(foxSrc);

        /*** TODO
        try{
            FagBoard clone = (FagBoard)fagBoard.clone();
        }
        catch(CloneNotSupportedException c){}
        ***/

        if (!geeseAtRisk.isEmpty()) {
            if (DEBUG) System.out.println(methodName + "Found " + geeseAtRisk.size() + " geese at risk: " + geeseAtRisk.toString());
            // Eat a random number of geese
            //  TODO - Should try to eat a maximum number of geese
            List<FagBoard.Cell> reachableForJump = fagBoard.reachableForJump(foxSrc, new boolean[GRID_SIZE][GRID_SIZE]);
            // Move to a random jump
            int jumpId = ControllerFag.generator.nextInt(reachableForJump.size());
            foxDst.row = reachableForFox.get(jumpId).row;
            foxDst.col = reachableForFox.get(jumpId).col;
        }
        else {
            // Move to a random position
            int dirId = ControllerFag.generator.nextInt(reachableForFox.size());
            foxDst.row = reachableForFox.get(dirId).row;
            foxDst.col = reachableForFox.get(dirId).col;
        }

        FagPawn foxPawnElement = (FagPawn) fagBoard.getElement(foxSrc.row, foxSrc.col);
        createMoveAction(fagBoard, foxDst.row, foxDst.col, foxPawnElement, actions);

        // Remove eaten geese from the board
        List<GameElement> geese = fagBoard.getTakenPawns(foxPawnElement, foxDst.row, foxDst.col);
        ListIterator<GameElement> gooseItor = geese.listIterator();
        while (gooseItor.hasNext()) {
            GameElement p = gooseItor.next();
            if (DEBUG) System.out.println(methodName + "Must remove goose:" + p);
            actions.addSingleAction(new RemoveAction(model, p));
        }
    }

    /**
     * Generate a game action that moves an item in a FagBoard grid.
     *  The possible animations are teleportation or a linear movement at
     *  constant time or speed.
     * @param fagBoard A ref. to the board.
     * @param rowDest  The destination row of the item.
     * @param colDest  The destination column of the item.
     * @param pawn     A pawn game element.
     * @param actions  A list of actions to perform.
     */
    private void createMoveAction(FagBoard fagBoard, int rowDest, int colDest, GameElement pawn, ActionList actions) {
        // Get the dest. cell center in space.
        GridLook look = (GridLook) control.getElementLook(fagBoard);
        Point2D center = look.getRootPaneLocationForCellCenter(rowDest, colDest);
        // Create the move action
        GameAction move = new MoveAction(model, pawn, boardName, rowDest, colDest, AnimationTypes.MOVELINEARPROP_NAME, center.getX(), center.getY(), 10);
        actions.addSingleAction(move);
    }

    @Override
    public ActionList decide() {
        // Get the stage. Note the cast which gets us a variable of the real
        //  type for accessing the attributes of FagStageModel
        FagStageModel stage = (FagStageModel) model.getGameStage();
        // Get the board
        FagBoard board = stage.getBoard();

        // Retrieve the TEAM and AI-# of this computer
        String team = (model.getIdPlayer() == 0) ? "Fox" : "Geese";
        String name = model.getPlayers().get(model.getIdPlayer()).getName();

        // Create action list. After the last action, it is next player's turn.
        ActionList actions = new ActionList(true);

        if (team.equals("Fox")) {
            if(name.contains("Ai-0")) {
                FoxAi0(board, actions);
            } else if(name.contains("Ai-1")) {
                FoxAi1(board, actions);
            } else {
                System.out.println("WARNING: This AI method (" + name + ") is not yet implemented!");
                System.exit(1);
            }
        } else {  // team.equals("Geese")
            if (name.contains("Ai-0")) {
                GeeseAi0(board, actions);
            } else if(name.contains("Ai-1")) {
                GeeseAi1(board, actions);
            } else {
                System.out.println("WARNING: This AI method (" + name + ") is not yet implemented!");
                System.exit(1);
            }
        }
        return actions;
    }
}
