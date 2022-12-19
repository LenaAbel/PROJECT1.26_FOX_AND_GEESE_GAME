package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FagStageModelTest {

    @Test
    void testGetBoard() {
        FagStageModel stageModel = mock(FagStageModel.class);
        FagBoard board = stageModel.getBoard();
        Assertions.assertEquals(stageModel.getBoard(), board);
    }

    @Test
    void testSetBoard() {
        FagStageModel stageModel = mock(FagStageModel.class);
        FagBoard board = new FagBoard(70, 100, stageModel);

        stageModel.setBoard(board);
        when(stageModel.getBoard()).thenReturn(board);

        Assertions.assertEquals(stageModel.getBoard(), board);
    }

    @Test
    void testGetFox() {
        FagStageModel stageModel = mock(FagStageModel.class);
        FagPawn fox = stageModel.getFox();
        Assertions.assertEquals(stageModel.getFox(), fox);
    }

    @Test
    void testSetFox() {
        FagStageModel stageModel = mock(FagStageModel.class);
        FagPawn fox = new FagPawn(FagPawn.FOX_PAWN, stageModel);

        stageModel.setFox(fox);
        when(stageModel.getFox()).thenReturn(fox);

        Assertions.assertEquals(stageModel.getFox(), fox);
    }

    @Test
    void testGetGeese() {
        FagStageModel stageModel = mock(FagStageModel.class);
        FagPawn[] geese = stageModel.getGeese();
        Assertions.assertEquals(stageModel.getGeese(), geese);
    }

    @Test
    void testSetGeese() {
        FagStageModel stageModel = mock(FagStageModel.class);
        FagBoard board = mock(FagBoard.class);
        FagPawn[] geese = new FagPawn[FagBoard.GEESE_COUNT];

        stageModel.setGeese(geese);
        when(stageModel.getGeese()).thenReturn(geese);

        Assertions.assertEquals(stageModel.getGeese(), geese);
    }

    @Test
    void testGetFakePawns() {
        Assertions.assertTrue(true); // a faire
    }

    @Test
    void testSetFakePawns() {
        Assertions.assertTrue(true); // a faire
    }

    @Test
    void testGetPlayerName() {
        Assertions.assertTrue(true); // a faire
    }

    @Test
    void testSetPlayerName() {
        Assertions.assertTrue(true); // a faire
    }

    @Test
    void testCanReachCell() {
        Assertions.assertTrue(true); // a faire
    }

    @Test
    void testGetTakenPawn() {
        Assertions.assertTrue(true); // a faire
    }
}