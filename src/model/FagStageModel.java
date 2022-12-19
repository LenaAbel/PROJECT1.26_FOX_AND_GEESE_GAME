package model;

import gamifier.model.*;

import java.util.List;

import static model.FagBoard.*;
import static model.FagPawn.FOX_PAWN;

/**
 * This class represents a "level" a.k.a. a "stage" of the game.
 * <p>
 * This class contains all the elements composing the current stage (as lists) and
 * the associated methods to manipulate them. These lists include the GameElements
 * of the stage, the GridElements of the stage and the currently selected elements.
 * It also allows to specify the "callback" methods, which will be called
 * automatically when a selection changes, or when an element is inserted,
 * moved or deleted from the grid.
 * </p>
 * <p>
 * Note: The FoxAndGeese game is a board game which has only one stage.
 * </p>
 */
public class FagStageModel extends GameStageModel {

    // States of the stage
    public final static int STATE_SELECTPAWN = 1; // The player must select a pawn
    public final static int STATE_SELECTDEST = 2; // The player must select a destination

    // Elements of the current stage
    private FagBoard    board;
    private FagPawn     fox;
    private FagPawn[]   geese;
    private FagPawn[]   fakePawns;
    private TextElement playerName;
    private int         geeseToPlay;

    /**
     * Basic constructor.
     * <p>
     *
     * @param name The name of the current game stage.
     * @param model The global model that this stage belongs to.
     */
    public FagStageModel(String name, Model model) {
        super(name, model);
        state = STATE_SELECTPAWN;
        geeseToPlay = GEESE_COUNT; // 13
        setupCallbacks();
    }

    public FagBoard getBoard() {
        return board;
    }
    public void setBoard(FagBoard board) {
        this.board = board;
        // Add the grid of this board to the lists managed by 'GameStageModel'
        addGrid(board);
    }

    public FagPawn getFox() {
        return fox;
    }
    public void setFox(FagPawn foxPawn) {
        this.fox = foxPawn;
        addElement(foxPawn);
    }

    public FagPawn[] getGeese() {
        return geese;
    }
    public void setGeese(FagPawn[] geesePawns) {
        this.geese = geesePawns;
        for(int i=0; i<geesePawns.length; i++) {
            addElement(geesePawns[i]);
        }
    }

    public FagPawn[] getFakePawns() {
        return fakePawns;
    }
    public void setFakePawns(FagPawn[] fakePawns) {
        this.fakePawns = fakePawns;
        for(int i=0; i<fakePawns.length; i++) {
            addElement(fakePawns[i]);
        }
    }

    public TextElement getPlayerName() {
        return playerName;
    }
    public void setPlayerName(TextElement playerName) {
        this.playerName = playerName;
        addElement(playerName);
    }

    /**
     * Define the callbacks which are automatically called when the state of the
     * game needs to be modified.
     * Note: This setup method must be called at the end of the constructor.
     */
    private void setupCallbacks() {
        /**
         * If a pawn is selected, calculate the valid destination squares and
         * change their appearance,
         */
        onSelectionChange( () -> {
            // Get the selected pawn if any
            if (selected.size() == 0) {
                board.resetReachableCells(false);
                return;
            }
            FagPawn pawn = (FagPawn) selected.get(0);
            // Retrieve the row and column of the pawn
            int[] cell = board.getElementCell(pawn);
            // Set the valid destination cells for this pawn
            if (cell != null) {
                board.setReachableCells(cell[0], cell[1]);
            }
            // Check if game is over for the Fox
            if (pawn.isFox() && (board.computeReachableCells(cell[0], cell[1]) == null)) {
                // Set the winner
                model.setIdWinner(GEESE_TEAM);
                // Stop de the game
                model.stopGame();
            }
        });

        /**
         * If a pawn is moved, update the model and change the appearance of the
         * impacted squares.
         */
        onMoveInGrid( (element, gridDest, rowDest, colDest) -> {
            // [FIXME- TODO]
            return;
        });

        /**
         * If a pawn is removed from the board, check if more than 7 geese were
         * taken and then game is over.
         */
        onRemoveFromGrid( (element, gridDest, rowDest, colDest) -> {
            int idWinner = -1;
            FagPawn p = (FagPawn) element;
            if (p.getType() == FOX_PAWN) {
                System.out.println("WARNING: The fox cannot be removed from the board! Instead, we assume that it cannot move anymore!");
                idWinner = GEESE_TEAM;
            }
            else {
                geeseToPlay--;
                // Check if FOX won
                if (geeseToPlay <= GEESE_COUNT-7) {
                    idWinner = FOX_TEAM;
                }
            }
            if (idWinner != -1) {
                // Set the winner
                model.setIdWinner(idWinner);
                // Stop de the game
                model.stopGame();
            }
            return;
        });

        /*** onPutInGrid () - NOT USED (keep as example)
        onPutInGrid( (element, gridDest, rowDest, colDest) -> {
            // just check when pawns are put in 3x3 board
            if (gridDest != board) return;
            FagPawn p = (FagPawn) element;
            if (p.getColor() == 0) {
                blackPawnsToPlay--;
            }
            else {
                redPawnsToPlay--;
            }
            if ((blackPawnsToPlay == 0) && (redPawnsToPlay == 0)) {
                computePartyResult();
            }
        });
        ***/

    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new FagStageFactory(this);
    }

    /**********************************************
     * TRAMPOLINE METHODS
     *  NB: gain direct access to the board
     **********************************************/
    public int[] getElementCell(GameElement gameElement) {
        return board.getElementCell(gameElement);
    }

    public boolean canReachCell(int row, int col) {
        return board.canReachCell(row, col);
    }

    public GameElement getTakenPawn(GameElement element, int rowDest, int colDest) {
        return board.getTakenPawn(element, rowDest, colDest);
    }

    public List<GameElement> getTakenPawns(GameElement foxElement, int rowDest, int colDest) {
        return board.getTakenPawns(foxElement, rowDest, colDest);
    }
}
