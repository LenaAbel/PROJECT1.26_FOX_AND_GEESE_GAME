package gamifier.model.action;

import gamifier.model.*;
import gamifier.model.animation.AnimationTypes;
import gamifier.model.animation.LinearMoveAnimation;
import gamifier.model.animation.MoveAnimation;
import javafx.geometry.Point2D;

public class MoveAction extends GameAction {

    protected GridElement gridSrc;
    protected GridElement gridDest;
    protected int rowDest;
    protected int colDest;
    protected double xDest;
    protected double yDest;
    protected double factor; // a speed in pixel/ms or the whole duration, see LinearMoveAnimation

    public MoveAction(Model model, GameElement element, String gridDest, int rowDest, int colDest, String animationName, double xDest, double yDest, double factor) {
        this(model, element, model.getGrid(gridDest), rowDest, colDest, animationName, xDest, yDest, factor);
    }

    public MoveAction(Model model, GameElement element, GridElement gridDest, int rowDest, int colDest, String animationName, double xDest, double yDest, double factor) {
        super(model, element, animationName);

        gridSrc = element.getGrid();
        this.gridDest = gridDest;
        this.rowDest = rowDest;
        this.colDest = colDest;
        this.xDest = xDest;
        this.yDest = yDest;
        this.factor = factor;

        createAnimation();
    }

    public int getRowDest() {
        return rowDest;
    }

    public int getColDest() {
        return colDest;
    }

    protected void execute() {
        if ((gridSrc == null) || (gridDest == null)) return;
        if (gridSrc == gridDest) {
            gridDest.moveElement(element, rowDest, colDest);
        }
        else {
            gridSrc.removeElement(element);
            gridDest.putElement(element, rowDest, colDest);
        }
    }

    protected void createAnimation() {
        animation = null;
        if (animationName.startsWith("move")) {
            if (gridDest == null) return;
            Point2D endLoc = new Point2D(xDest, yDest);
            // create animation to visualize this movement
            if (animationType == AnimationTypes.MOVETELEPORT_VALUE) {
                animation = new MoveAnimation(model, element.getLocation(), endLoc);
            } else if ((animationType == AnimationTypes.MOVELINEARPROP_VALUE) ||
                    (animationType == AnimationTypes.MOVELINEARCST_VALUE)) {
                animation = new LinearMoveAnimation(model, element.getLocation(), endLoc, animationType, factor);
            }
        }
    }
}
