package gamifier.model.action;

import gamifier.model.GameElement;
import gamifier.model.Model;

public class RemoveAction extends GameAction {

    public RemoveAction(Model model, GameElement element) {
        super(model, element, "none");

        createAnimation();
    }

    protected void execute() {
        element.removeFromStage();
    }

    public void createAnimation() {
    }
}
