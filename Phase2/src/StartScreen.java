import java.io.File;
import java.io.FileInputStream;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class StartScreen extends MainGui {

	static Button startButton;
	static Label titleLabel;
	static VBox startVBox;

	/**
	* Method that is run when the game starts. It creates all the components needed in the StartScreen
	* and adds it to the screen.
	*/
	public static void run() {
		//Create the startbutton and add all characteristics to it.
		startButton = new Button("Start Game!");
		startButton.setFont(new Font("Arial Bold", screenSize.getWidth() / 85));
		startButton.setPrefHeight(screenSize.getHeight() / 22);
		startButton.setPrefWidth(screenSize.getWidth() / 6);
		startVBox = new VBox(50);
		//create all the paths for the logo and the stylesheet and store it in a String variable.
		String home = System.getProperty("user.dir");
		final File f = new File(StartScreen.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String pathLogo = f + File.separator  + File.separator + "GameLogo.png";
		String pathStylesheet = f.toURI().toString() + "Stylesheet.css";
		//Try opening the image and store it in an ImageView.
		try {
			FileInputStream inputstream = new FileInputStream(pathLogo);
			Image image = new Image(inputstream);
			imageView = new ImageView(image);
			imageView.setFitWidth(screenSize.getWidth() / 1.4);
			imageView.setFitHeight(screenSize.getHeight() / 8.5);
		} catch (Exception e) {
			System.out.println("Cannot read file");
		}
		//Add all components to the vBox.
		startVBox.getChildren().addAll(imageView, startButton);
		startVBox.setAlignment(Pos.CENTER);
		//Create a scene and show it on the screen.
		StackPane layout = new StackPane();
		layout.getChildren().add(startVBox);
		scene = new Scene(layout, screenSize.getWidth(), screenSize.getHeight());

		scene.getStylesheets().add(pathStylesheet);

		window.setScene(scene);
		window.show();

		startButton.setOnAction(e -> {
			HomeScreen.run();
		});
	}

	/**
	* Method that is run when the user presses the Go back to main menu button in the escape menu.
	* Creates all the components needed in the StartScreen and adds it to the screen.
	*/
	public static void run2() {
		//Create the startbutton and add all characteristics.
		startButton = new Button("Start Game!");
		startButton.setFont(new Font("Arial Bold", screenSize.getWidth() / 85));
		startButton.setPrefHeight(screenSize.getHeight() / 25);
		startButton.setPrefWidth(screenSize.getWidth() / 8);
		startVBox = new VBox(50);
		//Add all components to the vBox.
		startVBox.getChildren().addAll(imageView, startButton);
		startVBox.setAlignment(Pos.CENTER);

		StackPane layout = new StackPane();
		layout.getChildren().add(startVBox);
		//Set the scene of the window.
		window.getScene().setRoot(layout);

		startButton.setOnAction(e -> {
			HomeScreen.run();
		});
	}

}
