package gamifier.model;

import gamifier.model.animation.AnimationStep;
import gamifier.view.GridGeometry;

public class TextElement extends GameElement {

    protected String text;

    public TextElement(String text, GameStageModel gameStageModel) {
        super(gameStageModel);
        setText(text);
        locationAnchor = LOCATION_TOPLEFT;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        lookChanged = true;
    }

    public void update(double width, double height, GridGeometry gridGeometry) {
        // if must be animated, move the text
        if (animation != null) {
            AnimationStep step = animation.next();
            if (step != null) {
                setLocation(step.getInt(0), step.getInt(1));
            }
            else {
                animation = null;
            }
        }
    }
}
