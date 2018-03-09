package main.java.gof.gui;

import main.java.gof.core.Board;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller implements Initializable {
    
    private final int    DEFAULT_SIZE = 15;
    private final double DEFAULT_PROB = 0.3;

    @FXML
    private FlowPane base;
    @FXML
    private Label countLabel;
    @FXML
    private Slider countSlider;
    @FXML
    private HBox presetBox;
    @FXML
    private Button openButton, saveButton, openPresetBtn;
    @FXML
    private Button runButton, stopButton, randomizeButton, clearButton;
    @FXML
    private HBox rootBox;

    private Board board;

    private JavaFXDisplayDriver display;

    private Timeline loop = null;
    
    private int windowWidth = 750;
    private int cellSizePx = 30;

    private PresetHandler presetHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        presetHandler = new PresetHandler();
        AnchorPane anchor = presetHandler.loadPresets(base);
        presetBox.getChildren().add(anchor);

        createBoard(DEFAULT_SIZE, DEFAULT_PROB);
        
        attachResizeListener();
    }

    @FXML
    private void onRun(Event evt) {

        loop = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            board.update();
            display.displayBoard(board);
        }));

        loop.setCycleCount(100);
        loop.play();
    }

    @FXML
    private void onStop(Event evt) {
        loop.stop();
    }

    @FXML
    private void onClear(Event evt) {
        createBoard(DEFAULT_SIZE, 0);
    }


    private void createBoard(int size, double prob) {
        board = new Board(size, size, prob);
        createDisplay();
    }
    
    private void createDisplay() {
        display = new JavaFXDisplayDriver(board.getSize(), cellSizePx, board);

        base.getChildren().clear();
        base.getChildren().add(new Group(display.getPane()));        
    }
    
    private void attachResizeListener() {
        ChangeListener<Number> sizeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int newWidth = newValue.intValue();
                if (newWidth > 250 && Math.abs(newWidth - windowWidth) >= 50) {
                    windowWidth = newWidth;
                    cellSizePx = newWidth / 25;
                    createDisplay();
                }
            }
        };
        rootBox.widthProperty().addListener(sizeListener);
    }
}