import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomeScreen extends MainGui {

  static Button gm1Button, gm2Button, gm3Button;
  static Label titleLabel;
  static VBox homeVBox;
  static Scene homeScene;
  static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

  /**
  * Method that is being called when the user enteres the homescreen. Method creates
  * all components and adds it to the screen.
  */
  public static void run() {
	Font buttonFont = new Font("Arial Bold", screenSize.getWidth()/100);
    gm1Button = new Button("GameMode 1");
    gm1Button.setPrefWidth(screenSize.getWidth()/8);
    gm1Button.setPrefHeight(screenSize.getHeight()/30);
    gm1Button.setFont(buttonFont);

    gm2Button = new Button("GameMode 2");
    gm2Button.setPrefWidth(screenSize.getWidth()/8);
    gm2Button.setPrefHeight(screenSize.getHeight()/30);
    gm2Button.setFont(buttonFont);


    gm3Button = new Button("GameMode 3");
    gm3Button.setPrefWidth(screenSize.getWidth()/8);
    gm3Button.setPrefHeight(screenSize.getHeight()/30);
    gm3Button.setFont(buttonFont);

    titleLabel = new Label("Choose your game mode");
    titleLabel.setFont(new Font("Arial Bold", 40));
    homeVBox = new VBox(50);

    gm1Button.setOnAction(e -> {
      gameModeChosen = 1;
      GraphCreateMenu.display();
    });

    gm2Button.setOnAction(e -> {
      gameModeChosen = 2;
      GraphCreateMenu.display();
    });

    gm3Button.setOnAction(e -> {
      gameModeChosen = 3;
      GraphCreateMenu.display();
    });

    homeVBox.getChildren().addAll(imageView, titleLabel, gm1Button, gm2Button, gm3Button);
    homeVBox.setAlignment(Pos.CENTER);

    StackPane layout2 = new StackPane();
    layout2.getChildren().add(homeVBox);
    window.getScene().setRoot(layout2);
  }
}
