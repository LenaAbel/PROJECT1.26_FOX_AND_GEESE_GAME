package gamifier.model;

public class WallElement extends GameElement {

    public WallElement(GameStageModel gameStageModel) {
        super(gameStageModel, ElementTypes.getType("wall"));
        // in order to have an easier positioning, set the location type to 1
        locationAnchor = LOCATION_TOPLEFT;
    }
}
