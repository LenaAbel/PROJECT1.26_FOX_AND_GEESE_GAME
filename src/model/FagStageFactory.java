package model;

import gamifier.model.GameStageModel;
import gamifier.model.StageElementsFactory;
import gamifier.model.TextElement;

//import static model.FagBoard.GEESE_COUNT;

/**
 * This class is used to create the elements of a "level" a.k.a a "stage".
 * There is one such class for every "FagStageModel" class.
 * <p>
 * Note: The FoxAndGeese game is a board game which has only one stage.
 * </p>
 */
public class FagStageFactory extends StageElementsFactory {

    private FagStageModel fagStageModel;

    /**
     * Basic constructor.
     *
     * @param gameStageModel The game-stage-model for which to create elements.
     */
    public FagStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        fagStageModel = (FagStageModel) gameStageModel;
    }

    @Override
    public void setup() {

        //-- Create the board ---------------------------------------
        FagBoard fagBoard = new FagBoard(70, 100, fagStageModel);
        fagStageModel.setBoard(fagBoard);

        //-- Create the Fox pawn ------------------------------------
        FagPawn foxPawn = new FagPawn(FagPawn.FOX_PAWN, fagStageModel);
        // Add the fox to the list of GameElement
        fagStageModel.setFox(foxPawn);
        // Assign the fox to the board cell
        fagBoard.putElement(foxPawn, 5,3);

        //-- Create the Goose pawns ---------------------------------
        FagPawn[] geesePawns = new FagPawn[FagBoard.GEESE_COUNT];
        for (int i=0; i<FagBoard.GEESE_COUNT; i++) {
            geesePawns[i] = new FagPawn(FagPawn.GOOSE_PAWN, fagStageModel);
        }
        fagStageModel.setGeese(geesePawns);

        //-- Create the Fake pawns ----------------------------------
        //  Note: These are static and invisible pawns. We use this trick to
        //  simplify the setting and the management of the board-game's shape
        //  by locating these pawns at the unplayable grid cell locations.
        final int FAKE_PAWN_COUNT = (FagBoard.GRID_SIZE*FagBoard.GRID_SIZE) - FagBoard.BOARD_CELLS;
        FagPawn[] fakePawns = new FagPawn[FAKE_PAWN_COUNT];
        for (int i=0; i<FAKE_PAWN_COUNT; i++) {
            fakePawns[i] = new FagPawn(FagPawn.FAKE_PAWN, fagStageModel);
        }
        fagStageModel.setFakePawns(fakePawns);

	    // Assigns the geese and fake pawns to the board
        int f = 0;
        int g = 0;
        for(int row = 0; row < FagBoard.GRID_SIZE; row++) {
            if((row == 0) || (row == 1)) {
                fagBoard.putElement(fakePawns[f],    row, 0);
                fagBoard.putElement(fakePawns[f+1],  row, 1);
                fagBoard.putElement(geesePawns[g],   row, 2);
                fagBoard.putElement(geesePawns[g+1], row, 3);
                fagBoard.putElement(geesePawns[g+2], row, 4);
                fagBoard.putElement(fakePawns[f+2],  row, 5);
                fagBoard.putElement(fakePawns[f+3],  row, 6);
                f += 4;
                g += 3;
            }
            else if (row == 2) {
                for(int col = 0; col < FagBoard.GRID_SIZE; col++) {
                    fagBoard.putElement(geesePawns[g+col], row, col);
                }
             }
            else if ((row == FagBoard.GRID_SIZE-2) || (row == FagBoard.GRID_SIZE-1)) {
                fagBoard.putElement(fakePawns[f],    row, 0);
                fagBoard.putElement(fakePawns[f+1],  row, 1);
                fagBoard.putElement(fakePawns[f+2],  row, 5);
                fagBoard.putElement(fakePawns[f+3],  row, 6);
                f += 4;
            }
        }

        //-- Create the text ----------------------------------------
        TextElement text = new TextElement(fagStageModel.getCurrentPlayerName(), fagStageModel);
        text.setLocation(10,10);
        fagStageModel.setPlayerName(text);
    }

}
