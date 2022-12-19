package gamifier.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SimpleTextView extends PaneView {

    protected String title;
    protected Text text;
    protected Rectangle frame;
    protected int width;
    protected int height;

    public SimpleTextView(int width, int height, String name, String title) {
        super(name);
        this.width = width;
        this.height = height;
        this.title = title;
        frame = new Rectangle(width,height,Color.LIGHTGREY);
        text = new Text(this.title);
        text.setFont(new Font(15));
        text.setFill(Color.BLACK);
        text.setX(10);
        text.setY(50);
        group.getChildren().addAll(frame, text);
    }

    // override init() so that all that is defined in the model won't show.
    public void init(GameStageView gameStageView) {
    }
}
