package control;

import gamifier.control.Controller;
import gamifier.control.ControllerKey;
import gamifier.model.Model;
import gamifier.view.View;
import javafx.event.*;
import javafx.scene.input.*;

/**
 * A basic keystrokes handler.
 * Generally useless for board games, but it can still be used if needed
 */
public class ControllerFagKey extends ControllerKey implements EventHandler<KeyEvent> {

    public ControllerFagKey(Model model, View view, Controller control) {
        super(model, view, control);
    }

    public void handle(KeyEvent event) {
        if (!model.isCaptureKeyEvent()) return;

        // if a key is pressed, just prints its code
        System.out.println(event.getCode());
    }
}

