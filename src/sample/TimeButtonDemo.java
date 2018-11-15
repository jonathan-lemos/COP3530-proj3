package sample;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.io.File;
import java.util.Calendar;
import javafx.scene.control.Label;
import java.text.SimpleDateFormat;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TimeButtonDemo extends Application {

    /*
     * Because i know people will be looking at this source, I will try to explain it to the best of my ability.
     *
     * Basically JavaFX has a sort of hierarchy like this:
     *
     *                     Stage
     *                       |
     *                     Scene
     *                       |
     *                   Root Node
     *                       |
     *                  Child node(s)
     *
     * Stage: The current window (container) of the application.
     * Scene: Container of the contents of the window. These contents are in the form of a root node.
     * Node : An object that is displayed.
     *        Note that nodes can contain other nodes (children).
     *        The children of a node can be arranged into a graph called a "scene graph".
     *        The scene graph for this application looks like this:
     *
     *                    pane(BorderPane)
     *                   /               \
     *          clock(DigitalClock)    paneForButtons(HBox)
     *                                     /          \
     *                              b12(Button)     b24(Button)
     *                                   |               |
     *                             i12(ImageView)  i24(ImageView)
     *
     * This is a data structure called a "tree", which makes this an easy structure for the computer to traverse.
     * The root of the tree (pane) is called our "root node", and is the argument passed to our Scene.
     * The Scene then draws our application based on the above scene graph.
     * The drawing is done post-order, because the positioning of parents depends on their children.
     */

    protected BorderPane getPane() {
        // pane for containing buttons and clock

        /*
         * A BorderPane contains "regions" that can be assigned to.
         * We are only interested in its center and bottom regions.
         * This is how we keep our children aligned.
         */
        BorderPane pane = new BorderPane();

        // pane for containing buttons

        /*
         * An HBox is a "horizontal box".
         * It arranges its children horizontally, which is perfect for the button bar we are creating.
         *
         * The parameter (50) determines the space between each child of the HBox.
         */
        HBox paneForButtons = new HBox(50);

        /*
         * We need to set a border for this HBox.
         *
         * This sets the HBox's border to a new Border with a green, solid stroke with right angles at its corners and the default width.
         */
        paneForButtons.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // clock to be added to pane

        /*
         * This instantiates a DigitalClock class as defined in the below class.
         * This is essentially a label that updates its text every second with the current time.
         */
        DigitalClock clock = new DigitalClock();

        /*
         * This causes our clock to be aligned in the center of our parent's region.
         */
        clock.setAlignment(Pos.CENTER);

        // write code for buttons
        ImageView i12 = new ImageView(new Image(new File("usa.jpg").toURI().toString()));
        i12.setFitHeight(25);
        i12.setFitWidth(40);
        Button b12 = new Button("12 hr", i12);

        ImageView i24 = new ImageView(new Image(new File("eu.jpg").toURI().toString()));
        i24.setFitHeight(25);
        i24.setFitWidth(40);
        Button b24 = new Button("24 hr", i24);

        paneForButtons.getChildren().addAll(b12, b24);
        paneForButtons.setAlignment(Pos.BOTTOM_CENTER);

        pane.setCenter(clock);
        pane.setBottom(paneForButtons);

        // handle button clicks with lambdas
        b12.setOnMouseClicked(e -> {
            clock.changeFormat12();
        });
        b24.setOnMouseClicked(e -> {
            clock.changeFormat24();
        });

        // handle keyboard presses with lambdas
        pane.setOnKeyPressed(e -> {
            switch (e.getCode()){
                case UP:
                    clock.setTextFill(Color.RED);
                    break;
                case DOWN:
                    clock.setTextFill(Color.CYAN);
                    break;
                case ENTER:
                    clock.setTextFill(Color.BLACK);
                    break;
                case LEFT:
                    clock.changeFormat12();
                    break;
                case RIGHT:
                    clock.changeFormat24();
                    break;
            }
        });
        return pane;
    }

    public void start(Stage primaryStage) {
        // Create a scene and place it in the stage
        Scene scene = new Scene(getPane(),250, 150);
        // Set the stage title
        primaryStage.setTitle("ClockApplication");
        // Place the scene in the stage
        primaryStage.setScene(scene);
        // Display the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class DigitalClock extends Label {
    private final SimpleDateFormat dateFormat24 = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat dateFormat12 = new SimpleDateFormat("hh:mm:ss a");
    private Timeline animation;
    private Calendar time;
    private boolean twelve = true;

    private void updateTime() {
        SimpleDateFormat df = twelve ? this.dateFormat12 : this.dateFormat24;
        this.setText(df.format(Calendar.getInstance().getTime()));
    }

    public DigitalClock() {
        // get time and set text with lambda
        this.time = Calendar.getInstance();
        // change text font here
        this.setFont(new Font("Arial", 30));
        // set animation here
        this.animation = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        e -> {
                            this.updateTime();
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        this.animation.setCycleCount(Animation.INDEFINITE);
        this.animation.play();
    }

    public void changeFormat24(){
        this.twelve = false;
        this.updateTime();
    }

    public void changeFormat12(){
        this.twelve = true;
        this.updateTime();
    }
}
