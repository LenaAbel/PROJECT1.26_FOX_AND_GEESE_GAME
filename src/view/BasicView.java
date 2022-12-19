package view;

import gamifier.model.Model;
import gamifier.view.PaneView;
import gamifier.view.View;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.concurrent.Flow;

public class BasicView extends View {

    private MenuItem menuStart;
    private MenuItem menuIntro;
    private  MenuItem menuOption;
    private  MenuItem menuQuit;

    public BasicView(Model model, Stage stage) {
        super(model, stage);
    }

    @Override
    protected void createMenuBar() {
        menuBar = new MenuBar();
        Menu menu1 = new Menu("Game");
        menuStart = new MenuItem("New game");
        menuIntro = new MenuItem("Intro");
        menuOption = new MenuItem("Option");
        menuQuit = new MenuItem("Quit");
        menu1.getItems().add(menuStart);
        menu1.getItems().add(menuIntro);
        menu1.getItems().add(menuOption);
        menu1.getItems().add(menuQuit);
        menuBar.getMenus().add(menu1);
    }
    public PaneView createIntro(){
        final URL imageURL = getClass().getResource("img/intro.png");
        final Image image = new Image(imageURL.toExternalForm());
        final ImageView imageView = new ImageView(image);

        PaneView paneView = new PaneView("intro");
        paneView.getGroup().getChildren().add(imageView);

        return paneView;
    }

    public PaneView createOption(){
        VBox v = new VBox();
        FlowPane fl1 = new FlowPane();
        VBox v2 = new VBox();
        FlowPane fl2 = new FlowPane();
        FlowPane fl3 = new FlowPane();
        Label lbPl1 = new Label("Player 1 :");
        Label lbPl2 = new Label("Player 2 :");
        TextField txt1 = new TextField();
        TextField txt2 =  new TextField();

        fl2.getChildren().addAll(lbPl1, txt1);
        fl3.getChildren().addAll(lbPl2, txt2);
        v2.getChildren().addAll(fl2, fl3);
        fl1.getChildren().add(v2);
        v.getChildren().add(fl1);

        HBox hb = new HBox();
        FlowPane fl4 = new FlowPane();
        FlowPane fl5 = new FlowPane();
        Label lbPl3 = new Label("Fox");
        Label lbPl4 = new Label("Geese");
        ToggleGroup group = new ToggleGroup();
        RadioButton button1 = new RadioButton("Human");
        button1.setToggleGroup(group);
        button1.setSelected(true);
        RadioButton button2 = new RadioButton("AI1");
        button2.setToggleGroup(group);
        RadioButton button3 = new RadioButton("AI2");
        button3.setToggleGroup(group);

        ToggleGroup group2 = new ToggleGroup();
        RadioButton button12 = new RadioButton("Human");
        button12.setToggleGroup(group2);
        button12.setSelected(true);
        RadioButton button22 = new RadioButton("AI1");
        button22.setToggleGroup(group2);
        RadioButton button32 = new RadioButton("AI2");
        button32.setToggleGroup(group2);

        VBox v3 = new VBox();
        VBox v4 = new VBox();
        HBox hb4 = new HBox();
        HBox hb5 = new HBox();
        FlowPane fl6 = new FlowPane();
        FlowPane fl7 = new FlowPane();
        FlowPane fl8 = new FlowPane();
        FlowPane fl9 = new FlowPane();

        hb4.getChildren().addAll(button1, button2, button3);
        hb5.getChildren().addAll(button12, button22, button32);

        fl6.getChildren().add(lbPl3);
        fl7.getChildren().add(hb4);
        fl8.getChildren().add(lbPl4);
        fl9.getChildren().add(hb5);

        v3.getChildren().addAll(fl6, fl7);
        v4.getChildren().addAll(fl8, fl9);

        fl4.getChildren().add(v3);
        fl5.getChildren().add(v4);

        hb.getChildren().addAll(fl4, fl5);

        v.getChildren().add(hb);

        FlowPane flp = new FlowPane();

        VBox vbc = new VBox();

        flp.getChildren().add(v);

        vbc.getChildren().add(flp);



        PaneView paneView = new PaneView("option");
        paneView.getGroup().getChildren().add(vbc);

        return paneView;
    }

    public MenuItem getMenuStart() {
        return menuStart;
    }

    public MenuItem getMenuIntro() {
        return menuIntro;
    }

    public MenuItem getMenuOption() {
        return menuOption;
    }

    public MenuItem getMenuQuit() {
        return menuQuit;
    }
}
