package view;

import gamifier.model.GameElement;
import gamifier.view.ElementLook;
import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import model.FagPawn;

import static model.FagPawn.*;

public class FagPawnLook extends ElementLook {
    private static final boolean DEBUG = true;
    private Circle circle;
    public FagPawnLook(int radius, GameElement element) {
        super(element);
        FagPawn pawn = (FagPawn)element;
        circle = new Circle();
        circle.setRadius(radius);
        if (pawn.getColor() == FagPawn.FOX_COLOR) {
            circle.setFill(FOX_COLOR);
        }
        else if (pawn.getColor() == FagPawn.GOOSE_COLOR) {
            if(DEBUG) {
                Text text = new Text(Integer.toString(pawn.getId()));
                text.setBoundsType(TextBoundsType.VISUAL);
                StackPane stack = new StackPane();
                stack.getChildren().addAll(circle, text);
            }
            circle.setFill(GOOSE_COLOR);
        }
        else {
            circle.setFill(FAKE_PAWN_COLOR);
        }
        circle.setCenterX(radius);
        circle.setCenterY(radius);
        addShape(circle);
    }

    @Override
    public void onSelectionChange() {
        FagPawn pawn = (FagPawn)getElement();
        if (pawn.isSelected()) {
            circle.setStrokeWidth(3);
            circle.setStrokeWidth(10);
            circle.setStrokeMiterLimit(10);
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStroke(Color.valueOf("0x333333"));
            circle.setStroke(Color.BLUE);
        }
        else {
            circle.setStrokeWidth(0);
        }
    }
}
