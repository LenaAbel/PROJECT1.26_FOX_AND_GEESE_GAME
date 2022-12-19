package gamifier.model.animation;

import gamifier.model.Model;
import javafx.geometry.Point2D;

public class MoveAnimation extends Animation {
    protected Point2D start;
    protected Point2D end;

    // in order to take into account that an animation duration may be
    // computed, we define 2 constructors with one that do not set duration
    public MoveAnimation(Model model, Point2D start, Point2D end) {
        this(model, start, end, 0);

    }

    public MoveAnimation(Model model, Point2D start, Point2D end, int duration) {
        super(model, duration, AnimationTypes.getType("look/teleport"));
        this.start = start;
        this.end = end;
        // WARNING : the dev will have to explicitly call computeSteps()
    }

    public void computeSteps() {
        AnimationStep step = new AnimationStep();
        step.addData(start.getX());
        step.addData(start.getY());
        steps.add(step);
        step = new AnimationStep();
        step.addData(end.getX());
        step.addData(end.getY());
        steps.add(step);
    }
}
