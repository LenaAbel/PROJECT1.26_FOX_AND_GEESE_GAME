package model;

import gamifier.model.GameStageModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FagPawnTest {
    //private FagStageModel stageModel;

    @Test
    void testIsFox() {
        FagStageModel stageModel = (mock(FagStageModel.class));
        FagPawn foxPawn = new FagPawn(FagPawn.FOX_PAWN, stageModel);
        //stageModel.setFox(foxPawn);
        boolean isFox = foxPawn.isFox();
        Assertions.assertTrue(isFox);
    }

    @Test
    void testIsGoose() {
        FagStageModel stageModel = (mock(FagStageModel.class));
        FagPawn goosePawn = new FagPawn(FagPawn.GOOSE_PAWN, stageModel);
        FagPawn[] geeseTab = {goosePawn};
        boolean isGoose = geeseTab[0].isGoose();
        Assertions.assertTrue(isGoose);
    }

    @Test
    void testIsFakePawn() {
        FagStageModel stageModel = (mock(FagStageModel.class));
        FagPawn fakePawn = new FagPawn(FagPawn.FAKE_PAWN, stageModel);
        FagPawn[] fakePawnTab = {fakePawn};
        boolean isFakePawn = fakePawnTab[0].isFakePawn();
        Assertions.assertTrue(isFakePawn);
    }

    @Test
    void testUpdate() {
    }
}