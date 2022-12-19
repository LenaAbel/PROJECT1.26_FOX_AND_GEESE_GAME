package gamifier.view;

import gamifier.model.CustomButtonElement;
import gamifier.model.GameElement;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CustomButtonLook extends ElementLook {

    protected int fontSize;
    protected String fontColor; // must be provided as a string containing an hex value like "0x123456"
    protected String backgroundColor;
    protected Text text;
    protected Rectangle border;

    public CustomButtonLook(int fontSize, String fontColor, String backgroundColor, GameElement element) {
        super(element);
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.backgroundColor = backgroundColor;
        CustomButtonElement be = (CustomButtonElement) getElement();
        text = new Text(be.getText());
        text.setFont(new Font(fontSize));
        text.setFill(Color.valueOf(fontColor));

        //text.setStroke(Color.valueOf(be.color));
        Bounds bt = text.getBoundsInLocal();
        System.out.println(text.getBaselineOffset()+" "+bt.getHeight());
        border = new Rectangle(bt.getWidth()+10, bt.getHeight()+10, Color.valueOf(backgroundColor));
        border.setArcHeight(10);
        border.setArcWidth(10);
        border.setStrokeWidth(2);
        border.setStrokeMiterLimit(10);
        border.setStrokeType(StrokeType.CENTERED);
        border.setStroke(Color.valueOf(fontColor));
        // position element within this Look
        border.setX(0);
        border.setY(0);
        text.setX(5);
        text.setY(border.getHeight()-5-(bt.getHeight()-text.getBaselineOffset()));
        // add element to the main group of this look.
        addShape(border);
        addShape(text);
    }
}
