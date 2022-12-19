package model;

import gamifier.model.ElementTypes;
import gamifier.model.GameElement;
import gamifier.model.GameStageModel;
import gamifier.model.animation.AnimationStep;
import gamifier.view.GridGeometry;
import javafx.scene.paint.Color;

/**
 * This class represents a pawn which is either a "Fox" or a "Goose".
 * <p>
 * Note: In Gamifier, each game element (pawns, boards, texts, buttons, ...)
 * must be represented by a subclass of GameElement.
 * </p>
 */
public class FagPawn extends GameElement {

    public static final int FOX_PAWN   = 0;
    public static final int GOOSE_PAWN = 1;
    public static final int FAKE_PAWN  = 2;

    public static final Color FOX_COLOR       = Color.RED;
    public static final Color GOOSE_COLOR     = Color.YELLOW;
    public static final Color FAKE_PAWN_COLOR = Color.BLACK;  // Invisible (for debug)

    private Color color;
    private int id;
    private static int count = 0; // A pawn number to ease the debug


    /**
     * Basic constructor.
     *
     * @param pawnType       The type of pawn (Fox=0, Geese=1)
     * @param gameStageModel The game stage that owns this pawn.
     */
    public FagPawn(int pawnType, GameStageModel gameStageModel) {
        super(gameStageModel);
        String elementName;
        if (pawnType == FOX_PAWN) {
            elementName = "Fox";
            this.color = FOX_COLOR;
        }
        else if (pawnType == GOOSE_PAWN) {
            elementName = "Goose";
            this.color = GOOSE_COLOR;
        }
        else {
            elementName = "FakePawn";
            this.color = FAKE_PAWN_COLOR;
            this.visible = false;
            this.clickable = false;
        }
        // Register a corresponding element type/value for this pawn.
        // Note: By convention, the value must be >=50 to avoid overwriting
        // those already defined by Gamifier.
        ElementTypes.register(elementName, 50 + pawnType);
        this.type = ElementTypes.getType(elementName);

        // Set the pawn ID
        id = count;
        // Increment counter class variable
        count++;
    }

    public boolean isFox() {
        return (this.getType() == (FOX_PAWN + 50));
    }
    public boolean isGoose() {
        return (this.getType() == (GOOSE_PAWN + 50));
    }
    public boolean isFakePawn() {
        return (this.getType() == (FAKE_PAWN + 50));
    }
    public Color getColor() {
        return color;
    }

    public String toString() {
        String typeStr = "";
        switch(this.getType()) {
            case 50: typeStr = "Fox  "; break;
            case 51: typeStr = "Goose"; break;
            case 52: typeStr = "FakePawn"; break;
            default: typeStr = "Unknown type"; break;
        }
        return this.getClass().getName() + "(T=" + typeStr + "," +
                                           " X=" + this.getX() + "," +
                                            "Y=" + this.getY() + ")";
    }

    public int getId() {
        return id;
    }

    /*
     * Update the Pawn.
     * This method will be called at each frame. It is used to update the
     * location of the Pawn, its state, or any other attribute that is defined
     * in its class.
     * IMPORTANT: this method overwrites that of the 'GameElement' subclasses.
     * In some cases, notably when the element must move, it must have access to
     * its bounding box or to the grid geometry that owns it, in order to change
     * its state. Since the model has no access to the view, the controller
     * provides the bounding box dimensions and the grid geometry as parameters.
     *
     * @param width        The current width of the element bounding box, as provided by the controller
     * @param height       The current height of the element bounding box, as provided by the controller
     * @param gridGeometry The grid geometry
     */
    public void update(double width, double height, GridGeometry gridGeometry) {
        // If the pawn must be animated, move it
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
