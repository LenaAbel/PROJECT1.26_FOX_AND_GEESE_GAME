package gamifier.control;

import gamifier.model.Model;
import gamifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class ControllerKey implements EventHandler<KeyEvent> {
    protected Model model;
    protected View view;
    protected Controller control;

    public ControllerKey(Model model, View view, Controller control) {
        this.model = model;
        this.view = view;
        this.control = control;
        // Attach KeyEvent listening to a Node that has focus.
        view.getRootNode().setOnKeyPressed(this);
        view.getRootNode().setOnKeyReleased(this);
    }

    // by default, do nothing. Must be overridden in subclasses
    public void handle(KeyEvent event) {}
}
