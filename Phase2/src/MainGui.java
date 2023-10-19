import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

class ColEdge {
	int u;
	int v;
}

public class MainGui extends Application {

	static int chosenColor;

	static Circle selectedCircle;

	public static ColEdge edges[];

	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	static Stage window;
	static Button newColorButton;
	static BorderPane gm1Border, gm2Border, gm3Border;
	static Group circlesAndLinesGraph;
	static int gameModeChosen;
	static VBox colorBox;
	static Button hintButton;
	static ImageView imageView;


	final static File f = new File(MainGui.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	static String path_music = f + File.separator + "test.mpeg";
	static String path_music2 = f + File.separator + "test2.mp3";

	static MediaPlayer mediaPlayer;
	static MediaPlayer mediaPlayer2;
	static Duration song_Time;
	static Slider slider;

	static Label timeLabel;

	static Scene scene;

	static Boolean inGame;

	static Button finishButton;
	static Label explanationLabel;

	static double SCREEN_WIDTH = (int) screenSize.getWidth();
	static double SCREEN_HEIGHT = (int) screenSize.getHeight();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		inGame = false;
		slider = new Slider();
		slider.setMin(0);
		slider.setMax(100);
		slider.setValue(50);
		slider.setShowTickLabels(false);
		slider.setMajorTickUnit(50);
		slider.setMinorTickCount(5);
		slider.setBlockIncrement(0);

		Media media = new Media(new File(path_music).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(999);

		window = primaryStage;
		window.setTitle("Graph coloring group 20!");
		window.setFullScreen(true);
		window.setResizable(false);
		window.setFullScreenExitHint(""); // Fixes "press escape to exit full screen" on MacOS
		window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		window.initStyle(StageStyle.DECORATED);

		StartScreen.run();

		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				scene.getRoot().setEffect(new GaussianBlur());
				if (inGame) {
					Timer.pauseAnimation();
				}

				ContinueMusic();

				VBox pauseMenu = new VBox(5);
				pauseMenu.setSpacing(50);
				Font labelFont = new Font("Arial Bold", 32);
				Label title = new Label("GAME PAUSED");
				title.setFont(labelFont);
				title.setTextFill(Color.BLACK);

				pauseMenu.getChildren().add(title);
				pauseMenu.setStyle("-fx-background-color: rgba(255,255,255, 0);");
				pauseMenu.setAlignment(Pos.CENTER);

				Font buttonFont = new Font("Arial Bold", 16);

				Button resumeButton = new Button("RESUME");
				Button exitMainMenuButton = new Button("BACK TO MAIN MENU");
				Button exitDesktopButton = new Button("EXIT TO DESKTOP");

				slider.setPrefHeight(screenSize.getHeight() / 15);
				slider.setPrefWidth(screenSize.getWidth() / 10);

				resumeButton.setPrefHeight(screenSize.getHeight() / 24);
				resumeButton.setPrefWidth(screenSize.getWidth() / 7);
				resumeButton.setFont(buttonFont);
				exitMainMenuButton.setPrefHeight(screenSize.getHeight() / 24);
				exitMainMenuButton.setPrefWidth(screenSize.getWidth() / 7);
				exitMainMenuButton.setFont(buttonFont);
				exitDesktopButton.setPrefHeight(screenSize.getHeight() / 24);
				exitDesktopButton.setPrefWidth(screenSize.getWidth() / 7);
				exitDesktopButton.setFont(buttonFont);
				pauseMenu.getChildren().addAll(resumeButton, exitMainMenuButton, exitDesktopButton, slider);

				final File f = new File(MainGui.class.getProtectionDomain().getCodeSource().getLocation().getPath());
				String pathStylesheet = f.toURI().toString() + "Stylesheet.css";

				Stage menuStage = new Stage(StageStyle.TRANSPARENT);
				menuStage.initOwner(window);
				menuStage.initModality(Modality.APPLICATION_MODAL);
				Scene menuScene = new Scene(pauseMenu, Color.TRANSPARENT);
				menuScene.getStylesheets().add(pathStylesheet);
				menuStage.setScene(menuScene);

				menuStage.show();
				slider.setOnMouseDragged(event -> {
					double volume = (slider.getValue()) / 100;
					mediaPlayer.setVolume(volume);
				});
				resumeButton.setOnAction(event -> {
					if (inGame) {
						Timer.resumeAnimation();
					}
					if (gameModeChosen == 2) {
						if (Timer.warningMusic() && inGame) {
							ChangeMusic();
						}
					}
					scene.getRoot().setEffect(null);
					menuStage.hide();
				});
				exitMainMenuButton.setOnAction(event -> {
					Alert a = new Alert(AlertType.CONFIRMATION);
					((Stage) a.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
					a.initOwner(menuStage);
					a.setContentText(
							"Are you sure you want to go back to the main menu? Current game progress will be lost.");

					Optional<ButtonType> result = a.showAndWait();
					if (result.get() == ButtonType.OK) {
						if (inGame) {
							Timer.stopAnimation();
						}
						StartScreen.run2();
						scene.getRoot().setEffect(null);
						menuStage.hide();
						ResetMethods.clearData();
					}
				});
				exitDesktopButton.setOnAction(event -> {
					Alert a = new Alert(AlertType.CONFIRMATION);
					a.initOwner(menuStage);
					a.setContentText("Are you sure you want to exit?");

					Optional<ButtonType> result = a.showAndWait();
					if (result.get() == ButtonType.OK) {
						System.exit(0);
					}
				});
			}
		});

	}

	/**
	* Method that changes the music. Also handles the volume slider.
	*/
	public static void ChangeMusic() {

		mediaPlayer.stop();
		Media media = new Media(new File(path_music2).toURI().toString());
		mediaPlayer2 = new MediaPlayer(media);

		mediaPlayer2.setAutoPlay(true);
		mediaPlayer2.setVolume(slider.getValue() / 100);

	}

	/**
	* Method to continue the music after it has been paused.
	*/
	public static void ContinueMusic() {
		if (mediaPlayer2 != null) {
			mediaPlayer2.stop();
		}
		mediaPlayer.play();
		mediaPlayer.setVolume(slider.getValue() / 100);
		mediaPlayer.setCycleCount(999);

	}
}
