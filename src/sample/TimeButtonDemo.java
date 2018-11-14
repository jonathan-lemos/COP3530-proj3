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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.io.File;
import java.util.Calendar;
import javafx.scene.control.Label;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TimeButtonDemo extends Application {

    protected BorderPane getPane() {
        // pane for containing buttons and clock
        BorderPane pane = new BorderPane();

        // pane for containing buttons
        HBox paneForButtons = new HBox(50);
        paneForButtons.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        // clock to be added to pane
        DigitalClock clock = new DigitalClock();
        clock.setAlignment(Pos.CENTER);

        // write code for buttons
        ImageView i12 = new ImageView(new Image("file:usa.jpg"));
        Button b12 = new Button("12 hr", i12);
        i12.setFitHeight(25);
        i12.setFitWidth(40);
        b12.setOnMouseClicked(e -> {
            clock.changeFormat12();
        });

        ImageView i24 = new ImageView(new Image("file:eu.jpg"));
        Button b24 = new Button("24 hr", i24);
        i24.setFitHeight(25);
        i24.setFitWidth(40);
        b24.setOnMouseClicked(e -> {
            clock.changeFormat24();
        });
        paneForButtons.getChildren().addAll(b12, b24);
        paneForButtons.setAlignment(Pos.BOTTOM_CENTER);

        pane.setCenter(clock);
        pane.setBottom(paneForButtons);

        // handle button clicks with lambdas

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
