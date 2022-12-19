package gamifier.model;

public class CustomButtonElement extends StaticElement {
    protected String text;

    public CustomButtonElement(int x, int y, String text, GameStageModel gameStageModel) {
        super(x, y, gameStageModel, ElementTypes.getType("custombutton"));
        this.text = text;

    }
    public String getText() {
        return text;
    }
}
