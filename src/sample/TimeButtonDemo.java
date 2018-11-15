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
     * Comments surrounded with <diamonds like this> were in the sample source code.
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

    /*
     * This returns our pane with the clock and buttons in it.
     */
    protected BorderPane getPane() {
        // <pane for containing buttons and clock>

        /*
         * A BorderPane contains "regions" that can be assigned to.
         * We are only interested in its center and bottom regions.
         * This is how we keep our children aligned.
         */
        BorderPane pane = new BorderPane();

        // <pane for containing buttons>

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

        // <clock to be added to pane>

        /*
         * This instantiates a DigitalClock class as defined in the below class.
         * This is essentially a label that updates its text every second with the current time.
         */
        DigitalClock clock = new DigitalClock();

        // <write code for buttons>

        /*
         * An ImageView is a Node that displays an image.
         * It takes an Image as an argument and displays it.
         * Unfortunately the Image constructor does not take a disk path as an argument, as it needs to be able to display images from all kinds of sources.
         * To remedy this, we make a new File() and get that file's URI instead.
         * In this case we are using the US flag image.
         */
        ImageView i12 = new ImageView(new Image(new File("usa.jpg").toURI().toString()));

        /*
         * This sets the height and width of the image.
         * Without them the image will display too big.
         */
        i12.setFitHeight(25);
        i12.setFitWidth(40);

        /*
         * This creates our button that sets the time format to 12 hours.
         * The "12 hr" argument gives the text of the button.
         * The "i12" argument displays an image (US flag) alongside the button.
         */
        Button b12 = new Button("12 hr", i12);

        /*
         * Same process as the above with the EU flag instead.
         */
        ImageView i24 = new ImageView(new Image(new File("eu.jpg").toURI().toString()));
        i24.setFitHeight(25);
        i24.setFitWidth(40);
        Button b24 = new Button("24 hr", i24);

        /*
         * This adds the two buttons we created to the HBox we made.
         * At this point our paneForButtons is essentially a button bar containing our buttons.
         */
        paneForButtons.getChildren().addAll(b12, b24);

        /*
         * This sets the alignment of the HBox's children (buttons) to the center.
         * By default the HBox class puts its children on the left.
         */
        paneForButtons.setAlignment(Pos.CENTER);

        /*
         * This sets the center Node of our master pane to the clock.
         * Note that any nodes added to the center region are automatically centered themselves,
         * so there is no need to set the alignment of clock to Pos.CENTER.
         */
        pane.setCenter(clock);

        /*
         * This sets the bottom Node of our master pane to the HBox button bar we made.
         */
        pane.setBottom(paneForButtons);

        // <handle button clicks with lambdas>

        /*
         * This sets a function to be called when our b12 button is clicked.
         * In this case it is set to a function that calls the "changeFormat12()" method of our clock.
         * A one-time function passed as a parameter is called a "lambda",
         * which is a fancy way of saying an "anonymous function", or a function with no name.
         *
         * This lambda is in the form of an EventHandler<T> interface,
         * which defines a single function "handle(T)" that runs whenever the mouse is clicked.
         *
         * The Java 7 way of doing this would be:
         *
         * b12.setOnMouseClicked(new EventHandler<MouseEvent>(){
         *     @Override
         *     public void handle(MouseEvent e){
         *         clock.changeFormat12();
         *     }
         * });
         *
         * As you can see, this syntax is extremely verbose.
         * As such, in Java 8 they simplified the syntax to below:
         *
         * b12.setOnMouseClicked(e -> {
         *     clock.changeFormat12();
         * });
         *
         * The above code is the equivalent to the Java 7 way, but requires less keystrokes.
         *
         * If your lambda has only one statement, you can simplify it even further to below:
         */
        b12.setOnMouseClicked(e -> clock.changeFormat12());

        /*
         * Ditto above.
         */
        b24.setOnMouseClicked(e -> clock.changeFormat24());

        // <handle keyboard presses with lambdas>

        /*
         * Like above, this is a lambda function.
         * One major difference is that this handles keypresses instead of mouseclicks.
         * The other is that this applies to the entire pane instead of just one button.
         */
        pane.setOnKeyPressed(e -> {
            switch (e.getCode()){
                // If up arrow was pressed.
                case UP:
                    // Set the clock's color to red,
                    clock.setTextFill(Color.RED);
                    break;
                // If down arrow was pressed,
                case DOWN:
                    // Set the text color to cyan.
                    clock.setTextFill(Color.CYAN);
                    break;
                // If enter was pressed,
                case ENTER:
                    // Set the text color to black.
                    clock.setTextFill(Color.BLACK);
                    break;
                // If left arrow was pressed,
                case LEFT:
                    // Change the clock's format to 12h.
                    clock.changeFormat12();
                    break;
                // If right arrow was pressed,
                case RIGHT:
                    // Change the clock's format to 24h.
                    clock.changeFormat24();
                    break;
                // If none of these keys were pressed, don't handle it.
            }
        });


        return pane;
    }

    public void start(Stage primaryStage) {
        // <Create a scene and place it in the stage>
        Scene scene = new Scene(getPane(),250, 150);
        // <Set the stage title>
        primaryStage.setTitle("ClockApplication");
        // <Place the scene in the stage>
        primaryStage.setScene(scene);
        // <Display the stage>
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
 * JavaFX allows us to extend its Node classes.
 * The Label class displays text.
 *
 * We extend it so it displays the current time and updates every second.
 * That means this class is also a Node, because Label extends Node.
 */
class DigitalClock extends Label {

    /*
     * SimpleDateFormat is like printf in the respect that it formats your text by filling in special text sequences.
     * In this case:
     * "HH" = The current hour in 24-hour format.
     * "mm" = The current minute.
     * "ss" = The current second.
     *
     * So on the 15th second of 2:40 PM, this would format as "14:40:15"
     */
    private final SimpleDateFormat dateFormat24 = new SimpleDateFormat("HH:mm:ss");

    /*
     * Like above, except:
     * "hh" = The current hour in 12-hour format.
     * "a"  = AM/PM
     *
     * So on the 15th second of 2:40 PM, this would format as "02:40:15 PM"
     */
    private final SimpleDateFormat dateFormat12 = new SimpleDateFormat("hh:mm:ss a");

    /*
     * This variable is unused, but was in the sample code, so i'm keeping it.
     * See the constructor for details.
     */
    private Timeline animation;

    /*
     * This variable is unused, but was in the sample code, so i'm keeping it.
     * See the constructor for details.
     */
    private Calendar time;

    /*
     * True if 12-hour format is being used, false if not.
     */
    private boolean twelve = true;

    /*
     * This method is responsible for updating this Label's text.
     * Keep in mind that because we extended the Label class,
     * this class is a Label and can use all of Label's protected/public methods.
     */
    private void updateTime() {
        // Choose the correct SimpleDateFormat based on 12/24 hour time.
        SimpleDateFormat df = twelve ? this.dateFormat12 : this.dateFormat24;
        // Set this Label's text to the current time formatted using our selected DateFormat.
        this.setText(df.format(Calendar.getInstance().getTime()));
    }

    /*
     * Constructor for DigitalClock()
     */
    public DigitalClock() {
        /*
         * super() is implicitly called here, so the base Label is automatically constructed
         */

        // <get time and set text with lambda>

        /*
         * The this.time variable is unused, but it was in the sample code.
         * The reason we cannot use one instance of Calendar, is because
         * a different Calendar is returned from Calendar.getInstance() every second.
         *
         * Attempting to use this.time instead of Calendar.getInstance() causes time to freeze in the label.
         */
        this.time = Calendar.getInstance();

        // <change text font here>

        /*
         * Sets the font to 30pt Arial
         */
        this.setFont(new Font("Arial", 30));

        // <set animation here>

        /*
         * The Timeline class denotes an "animation" of sorts.
         * It allows certain actions to happen after certain amounts of time and can be repeated.
         *
         * In this case, our animation updates the label once per second.
         */
        this.animation = new Timeline(
                /*
                 * A KeyFrame is a frame in the Timeline.
                 * The first parameter shows how long it should run for.
                 *
                 * In this case, we want our time to update instantly, so we set the Duration to 0 seconds.
                 */
                new KeyFrame(Duration.seconds(0),
                        /*
                         * The second parameter is a lambda that runs when this KeyFrame is activated.
                         * In this case we call the updateTime() method.
                         *
                         * updateTime() should not be pasted inline here, because that prevents
                         * the label from updating immediately when switching between 12h and 24h formats.
                         */
                        e -> this.updateTime()
                ),

                /*
                 * Our next KeyFrame simply pauses for one second.
                 * Together, these KeyFrames update once every second.
                 */
                new KeyFrame(Duration.seconds(1))
        );

        /*
         * We want our animation to play indefinitely, so we set it to never end here.
         */
        this.animation.setCycleCount(Animation.INDEFINITE);

        /*
         * Finally we start our animation.
         */
        this.animation.play();
    }

    /*
     * Changes the format to 24h.
     */
    public void changeFormat24(){
        this.twelve = false;
        /*
         * We update time now so the changes show immediately instead of after up to a second.
         */
        this.updateTime();
    }

    /*
     * Changes the format to 12h.
     */
    public void changeFormat12(){
        this.twelve = true;
        /*
         * We update time now so the changes show immediately instead of after up to a second.
         */
        this.updateTime();
    }
}
