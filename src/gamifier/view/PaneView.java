package gamifier.view;

import gamifier.model.GameElement;
import gamifier.model.Model;
import javafx.scene.Group;

import java.util.Collections;

public class PaneView {

    protected GameStageView gameStageView;
    protected String name;
    protected Group group;

    /**
     * Instantiate a game view with the given name.
     * The model attribute is set to null. It will be automatically set by the
     * global view when this game view is added to it.
     * @param name The name of the game view
     */
    public PaneView(String name) {
        this.name = name;
        this.gameStageView = null;
        group = new Group();
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }

    public GameStageView getGameStageView() {
        return gameStageView;
    }

    /**
     * Initialize the content of the group.
     * It takes the elements of the model, which are initialized when starting a game stage.
     * It sorts them so that the element with the highest depth are put in first in the group.
     * So they will be hidden by elements with a lower depth.
     */
    public void init(GameStageView gameStageView) {
        if (gameStageView != null) {
            this.gameStageView = gameStageView;
            // first sort element by their depth
            Collections.sort(gameStageView.getLooks(), (a, b) -> a.getDepth() - b.getDepth());
            // remove existing children
            group.getChildren().clear();
            for (ElementLook look : gameStageView.getLooks()) {
                Group group = look.getGroup();
                this.group.getChildren().add(group);
            }
        }
    }

    public void removeLook(GameElement element) {
        for (ElementLook look : gameStageView.getLooks()) {
            if (look.getElement() == element) {
                this.group.getChildren().remove(group);
            }
        }
    }

    /* ***************************************
       TRAMPOLINE METHODS
    **************************************** */

    public ElementLook getElementLook(GameElement element) {
        if (gameStageView == null) return null;
        return gameStageView.getElementLook(element);
    }
    public void update() {
        if (gameStageView == null) return;
        gameStageView.update();
    }
}
