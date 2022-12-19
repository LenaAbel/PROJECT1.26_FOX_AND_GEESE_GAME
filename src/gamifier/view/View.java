package gamifier.view;

import gamifier.model.GameElement;
import gamifier.model.GameException;
import gamifier.model.Model;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class View {

    /**
     * The model
     */
    protected Model model;
    /**
     * The list of pane views.
     *
     * Most of the games need only 2: one for the introduction, one for the game itself. It must be noticed that
     * a multi-level game does not need to define a pane view by level. Different levels should be managed by creating
     * different instance of GameStage.
     *
     */
    protected final List<PaneView> paneViews;
    /**
     * The current pane view.
     *
     * Only useful if controllers need to know which is the current one to decide which pane view to show next.
     */
    protected PaneView paneView;
    /**
     * The current game stage view.
     *
     */
    protected GameStageView gameStageView;
    /**
     * The vertical box that contains a menu bar (if needed) and the root pane.
     */
    protected VBox vbox;
    /**
     * The menu bar of the game.
     * It MUST BE created in the createMenuBar() method, which by default does nothing.
     * Thus, dev. must create a subclass of View to override createMenuBar().
     */
    protected MenuBar menuBar;

    /**
     * The root pane where all the node of the current pane view are put.
     *
     * These nodes corresponds to the nodes that are defined in the different element looks associated to
     * the game elements of the current game stage.
     *
     * It must be noticed that the location of this pane in the scene has no effect on the correct management of
     * inner nodes position, mouse click coordinates, ... Indeed, whenever scene coordinates are manipulated, they
     * are translated into root pane coordinates if it is needed. Thus, if a dev. wants to define a subclass of
     * GameElement, he must assume that the x,y coordinates of this element are in the root group space, and NOT the scene space.
     *
     */
    protected final Pane root;
    /**
     * The primary stage.
     *
     * It is used to resize the stage to the current scene size.
     */
    protected Stage stage;
    /**
     * The current scene assigned to the primary stage.
     */
    protected Scene scene;

    FlowPane fl;
    MediaPlayer mp;
    MediaView md;

    public View(Model model, Stage stage) {
        this.model = model;
        this.stage = stage;
        // initialize the list of pane views.
        paneViews = new ArrayList<>();
        // create the vbox that will be the root node of the scene
        vbox = new VBox();
        // create the pane that will contain element of the game for each stage.
        root = new Pane();
        root.setBackground(Background.EMPTY);
        // create the menu bar if needed
        createMenuBar();
        if (menuBar != null) {
            vbox.getChildren().add(menuBar);
        }
        vbox.getChildren().add(root);
        // create a "null" scene. It will be replaced when a pane view is selected.
        Group nullGroup = new Group();
        scene = new Scene(nullGroup, 0,0);
        stage.setScene(scene);
    }

    /**
     * Add a pane view to the list of available pane view that can be used during the game.
     * Most of the time, a game needs only 2 pane views: one for the introduction, and one for the game itself.
     * Indeed, even if the game has several levels/stages/... a single pane view is needed to show them because
     * the content of a pane view can be reset and then fulfilled with the element that are defined in a particular
     * stage.
     * @param paneView A pane view to add to this view.
     */
    public void addPaneView(PaneView paneView) {
        paneViews.add(paneView);
    }

    /**
     * Create the menu bar.
     * By default, a view has no menu bar so this method just set menuBar to null.
     * It should be overridden in subclasses.
     */
    protected void createMenuBar() {
        menuBar = null;
    }

    public PaneView getPaneView() {
        return paneView;
    }

    public GameStageView getGameStageView() {
        return gameStageView;
    }

    /**
     * Setup the whole view, given a pane view and a game stage view
     *
     * This method is called to setup the main window content. It is called when the application is launched, and
     * every time there is a need to change the content, for example when the game starts or when there is a change of stage.
     * It mainly consists in searching the PaneView object with the given name, and if found, to call its init() method to
     * add all nodes associated to the current game stage to the pane view group. Finally, this group
     * is added to the root pane.
     *
     * It must be notice that calling this method twice with the same pane view name will effectively
     * reset the root pane content, as if the game stage was started again.
     *
     * @param name The name of a pane view.
     * @param gameStageView An instance of GameStageView that contains the elements to be added to the pane view.
     * @throws GameException thrown if there is no pane view with the given name
     */
    public void setView(String name, GameStageView gameStageView) throws GameException {
        fl = new FlowPane();
        mp = new MediaPlayer(new Media(getClass().getResource("/fag.mp4").toExternalForm()));
        md = new MediaView(mp);
        for (PaneView paneView : paneViews) {
            if (paneView.name.equals(name)) {

                paneView.init(gameStageView);
                this.paneView = paneView;
                //NB: gameStageView may be null if there is no game stage view to draw (cf. SimpleTextView)
                this.gameStageView = gameStageView;
                root.getChildren().clear();
                root.getChildren().add(this.paneView.getGroup());
                // detach the current vbox as a root node of the current scene
                // so that it can be reused for the new scene.
                scene.setRoot(new Group());
                /* create the new scene with vbox as a root node, and if specified the
                   dimensions.
                 */
                if (!model.isStarted() && !name.equals("intro")  && !name.equals("option")) {
                    fl.getChildren().add(md);
                    vbox.getChildren().add(md);
                    mp.play();
                    mp.setCycleCount(MediaPlayer.INDEFINITE);
                }
                if ((this.gameStageView != null) && (this.gameStageView.getWidth() != -1) && (this.gameStageView.getHeight() != -1)) {
                    double h = 0;
                    if (menuBar != null) h = menuBar.getHeight();
                    scene = new Scene(vbox, this.gameStageView.getWidth(), h+ this.gameStageView.getHeight());
                    // set the clipping area of the root pane with the given size of the stage
                    // So, if there are shape that overlap with these boundaries, they won't show totally.
                    Rectangle r = new Rectangle(this.gameStageView.getWidth(), this.gameStageView.getHeight());
                    root.setClip(r);
                    stage.setScene(scene);
                    stage.sizeToScene();
                }
                else {
                    int w;
                    int h;
                    if (name.equals("intro")){
                        w=727;
                        h=890;
                    }
                    else if (name.equals("option")){
                        w=600;
                        h=300;
                    }
                    else{
                        w=1100;
                        h=750;
                    }
                    scene = new Scene(vbox, w, h);
                    // WARNING: must set the scene and resize the stage BEFORE defining the clipping.
                    // Otherwise, dimensions won't be correct.
                    stage.setScene(scene);
                    stage.sizeToScene();
                    stage.setResizable(false);
                    // set the clipping area with the boundaries of root pane.
                    Rectangle r = new Rectangle(root.getWidth(), root.getHeight());
                    root.setClip(r);
                }
                return;
            }
        }
        // cannot find a pane view => exception
        throw new GameException("cannot find a PaneView instance called "+name);
    }

    /**
     *
     * @return the root pane.
     */
    public Pane getRootNode() {
        return root;
    }

    /**
     *
     * @return the primary javafx stage
     */
    public Stage getStage() {
        return stage;
    }

    public void setPause(){
        mp.setCycleCount(0);
        mp.pause();
        mp.dispose();
    }

    public void removeMediaView(){
        Scene scene = stage.getScene();
        VBox root = (VBox) scene.getRoot();
        root.getChildren().remove(md);
    }

    /**
     * Add a node to the root group, and thus in the current pane view that displays the current game stage.
     * @param node a node to be added to the root group.
     */
    public void addNode(Node node) {
        root.getChildren().add(node);
    }

    /**
     * Remove a node from the root group, and thus in the current pane view that displays the current game stage.
     * @param node a node to be removed from the root group.
     */
    public void removeNode(Node node) {
        root.getChildren().remove(node);
    }

    /* ***************************************
       TRAMPOLINE METHODS
    **************************************** */
    public ElementLook getElementLook(GameElement element) {
        return paneView.getElementLook(element);
    }
    public GridLook getElementGridLook(GameElement element) {
        return (GridLook) paneView.getElementLook(element.getGrid());
    }

    public void update() {
        paneView.update();
    }
}
