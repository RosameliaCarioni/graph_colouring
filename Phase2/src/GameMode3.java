import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameMode3 extends ColorAssignation {

	static Label titleLabel;
	static HBox topHBox;

	static CheckBox checkBoxShowNumbers;

	static VBox timerVBox;
	static Timer timer;

	/**
	*This method is called if the user starts game mode 3.
	*@return void
	*/
	public static void run() {
		inGame = true;
		//Hint button - is initially disabled until at least one vertex has been colored
		hintButton = new Button("Hint");
		hintButton.setPrefWidth(80);
		titleLabel = new Label("Random Order!");
		checkBoxShowNumbers = new CheckBox("Show the numbers of the vertices");
		checkBoxShowNumbers.setSelected(true);

		timer = new Timer(); // Create the timer.

		timerVBox = new VBox(50);

		topHBox = new HBox(50);

		topHBox.getChildren().addAll(checkBoxShowNumbers, titleLabel, hintButton);
		topHBox.setAlignment(Pos.CENTER);

		hintButton.setDisable(true); // Disable the hint button

		timerVBox.getChildren().add(timeLabel);
		timerVBox.setAlignment(Pos.CENTER);

		// Create a BorderPane. This is used for the left border of the main BorderPane.
		BorderPane pane = new BorderPane();
		pane.setTop(timerVBox);
		pane.setCenter(colorBox);

		ScrollPane scrollPane = new ScrollPane(circlesAndLinesGraph); // Create the scroll pane and add the graph nodes to it.

		gm3Border = new BorderPane(scrollPane);
		gm3Border.setTop(topHBox);
		gm3Border.setLeft(pane);

		scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		window.getScene().setRoot(gm3Border);

		GM3DisplayGraph.createColorOrder();
		GM3DisplayGraph.highlightNextVertex();

		hintButton.setOnAction(e -> {
			Timer.setHintRestrictionGlobal();
			hintButton.setDisable(true);
			Hint.Hints();
		});
		//If the check box is selected the numbers of the vertices will be displayed.
		checkBoxShowNumbers.setOnAction(e -> {
			if (!checkBoxShowNumbers.isSelected()) {
				for (int i = 0; i < labelsDisplayed.size(); i++) {
					labelsDisplayed.get(i).setVisible(false);
				}
			} else {
				for (int i = 0; i < labelsDisplayed.size(); i++) {
					labelsDisplayed.get(i).setVisible(true);
				}
			}
		});
	}
}
