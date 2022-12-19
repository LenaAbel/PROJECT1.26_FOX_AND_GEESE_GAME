package gamifier.control;

import gamifier.model.*;
import gamifier.model.animation.Animation;
import gamifier.model.action.ActionList;
import gamifier.model.action.GameAction;
import gamifier.view.View;
import javafx.application.Platform;
import java.util.List;


public class ActionPlayer extends Thread {

    protected Controller control;
    protected Model model;
    protected View view;
    protected Decider decider;
    protected ActionList actions;
    protected ActionList preActions;

    public ActionPlayer(Model model, View view, Controller control, ActionList actions) {
        this.model = model;
        this.view = view;
        this.control = control;
        this.actions = actions;
        this.decider = null;
        this.preActions = null;
    }

    public ActionPlayer(Model model, View view, Controller control, Decider decider, ActionList preActions) {
        this.model = model;
        this.view = view;
        this.control = control;
        this.actions = null;
        this.decider = decider;
        this.preActions = preActions;
    }

    public void run() {
        // first disable event capture
        model.setCaptureEvents(false);

        if (preActions != null) {
            playActions(preActions);
        }
        // if there is a decider, decide what to do
        if (decider != null) {
            actions = decider.decide();
        }

        playActions(actions);

        model.setCaptureEvents(true);

        // NB: checking if the game is ended is done in ControllerAnimation
        // now check if the next player must play
        if (actions.mustDoNextPlayer()) {
            Platform.runLater( () -> {control.nextPlayer();});
        }
    }

    private void playActions(ActionList actions) {
        // loop over all action packs
        int idPack = 0;
        for(List<GameAction> actionPack : actions.getActions()) {
            System.out.println("playing pack "+idPack);
            // step 0 : initialize the actions
            for(GameAction action : actionPack) {
                action.initAction();
            }
            // step 1 : start animations from actions.
            Animation[] animations = new Animation[actionPack.size()];
            int idx=0;
            for(GameAction action : actionPack) {
                if (action.getAnimation() != null) {
                    animations[idx++] = action.getAnimation();
                    action.setupAnimation();
                }
            }
            // step 2 : start animations of the same pack
            for(int i=0;i<idx;i++) {
                animations[i].start();

            }
            // step 3 : wait for the end of all animations
            for(int i=0;i<idx;i++) {
                animations[i].getAnimationState().waitStop();
            }
            // step 4 : do the real actions, based on action.type
            for(GameAction action : actionPack) {
                action.start();
            }
            // step 5 : finalize the actions
            for(GameAction action : actionPack) {
                action.finalizeAction();
            }
            // last enable event capture
            idPack++;
        }
    }
}
