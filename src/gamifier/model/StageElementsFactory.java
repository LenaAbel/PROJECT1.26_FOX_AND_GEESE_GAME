package gamifier.model;

public abstract class StageElementsFactory {

    protected GameStageModel gameStageModel;

    public StageElementsFactory(GameStageModel gameStageModel) {
        this.gameStageModel = gameStageModel;
    }

    public abstract void setup();
}
