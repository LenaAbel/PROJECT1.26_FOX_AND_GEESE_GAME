package view;

import gamifier.model.GameStageModel;
import gamifier.view.GameStageView;
import gamifier.view.TextLook;

import model.FagBoard;
import model.FagStageModel;
import model.FagPawn;

import static model.FagBoard.GEESE_COUNT;

public class FagStageView extends GameStageView {
    public FagStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
        width = 800;  // 650;
        height = 800; // 450;
    }

    @Override
    public void createLooks() {
        FagStageModel model = (FagStageModel)gameStageModel;

        // BOARD
        FagBoard fagBoard = model.getBoard();
        addLook(new FagBoardLook(650, fagBoard)); // 650=7*90+20

        // FOX
        FagPawn foxPawn = model.getFox();
        addLook(new FagPawnLook(25, foxPawn));

        // GEESE
        FagPawn[] geesePawns = model.getGeese();
        for (int i=0; i<geesePawns.length; i++) {
            addLook(new FagPawnLook(25, geesePawns[i]));
        }

        // FAKE PAWNS
        FagPawn[] fakePawns = model.getFakePawns();
        for (int i=0; i<fakePawns.length; i++) {
            if (fakePawns[i].isVisible()) {
                addLook(new FagPawnLook(2, fakePawns[i]));
            }
        }

        // TEXT
        addLook(new TextLook(24, "0x000000", model.getPlayerName()));
    }
}
