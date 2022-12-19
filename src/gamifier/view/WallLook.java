package gamifier.view;

import gamifier.model.GameElement;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class WallLook extends ElementLook {

    protected Rectangle frame;
    protected int width;
    protected int height;
    protected double angle;
    protected String color;

    public WallLook(int width, int height, double angle, String color, GameElement element) {
        super(element);
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.color = color;
        frame = new Rectangle(width, height, Color.valueOf(color));
        frame.setRotate(angle);
        addShape(frame);
    }
}
