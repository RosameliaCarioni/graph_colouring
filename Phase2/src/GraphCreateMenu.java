import java.io.File;
import java.util.Optional;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.scene.input.KeyCode;

public class GraphCreateMenu extends MainGui {

	public static int n;
	public static int m;

	static Button generateRandomGraphButton, readButton;
	static TextField verticesTextField, edgesTextField;
	static Label askVertex, askEdge, orLabel;
	static Rectangle2D screenSize;

	/**
	* Method that is called when the user wants to create a graph. The method creates
	* all components needed and adds it to a scene.
	*/
	public static void display() {
		generateRandomGraphButton = new Button("Generate");
		generateRandomGraphButton.setOnAction(e -> {
			handleCreateButton();
		});
		readButton = new Button("Read");
		readButton.setOnAction(e -> {
			handleReadButton();
		});

		screenSize = Screen.getPrimary().getVisualBounds();

		verticesTextField = new TextField();
		verticesTextField.getStyleClass().add("graphcreate-textfield");
		edgesTextField = new TextField();
		edgesTextField.getStyleClass().add("graphcreate-textfield");

		verticesTextField.setMaxWidth(screenSize.getWidth() / 2);
		edgesTextField.setMaxWidth(screenSize.getWidth() / 2);
		askVertex = new Label("Number of vertices:");
		askVertex.getStyleClass().add("graphcreate-label");
		askEdge = new Label("Number of edges:");
		askEdge.getStyleClass().add("graphcreate-label");
		orLabel = new Label("Or");
		orLabel.getStyleClass().add("graphcreate-label");

		VBox layout = new VBox(20);
		layout.getChildren().addAll(askVertex, verticesTextField, askEdge, edgesTextField, generateRandomGraphButton,
				orLabel, readButton);
		layout.setAlignment(Pos.CENTER);

		verticesTextField.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				e.consume();
			}
		});
		edgesTextField.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				e.consume();
			}
		});

		window.getScene().setRoot(layout);
		window.getScene().getRoot().requestFocus();
	}

	/**
	* Method that handles all actions after the user creates a graph.
	*/
	public static void handleCreateButton() {

		if (!verticesTextField.getText().equals("") && !edgesTextField.getText().equals("")) {
			n = Integer.parseInt(verticesTextField.getText());
			m = Integer.parseInt(edgesTextField.getText());
			long nLong = n;
			long maxEdges = (nLong * (nLong - 1)) / 2;
			Alert b = new Alert(AlertType.CONFIRMATION);

			if ((n > 1500 || m > 45000) && n >= 1 && m <= maxEdges && m >= 0) {
				b.initOwner(window);
				b.setContentText("This might take some time, are you sure that you want to continue ?");

				Optional<ButtonType> result = b.showAndWait();

				if (result.get() == ButtonType.OK && n >= 1 && m <= maxEdges && m >= 0) {

					Graph newGraph = new Graph(n, m);

					n = newGraph.getN();
					edges = newGraph.getEdges();

					circlesAndLinesGraph = DisplayGraph.createSceneDisplayGraph(n, edges);
					ColorPickWindow.createColorMenu(gameModeChosen);
					runGameMode();

				}
				else  {
					if (result.get() == ButtonType.CANCEL) {
						b.close();
					}
				}

			}
			else {

				if (n >= 1 && m <= maxEdges && !(n > 1500 || m  > 45000) && m >= 0) {
					Graph newGraph = new Graph(n, m);

					n = newGraph.getN();
					edges = newGraph.getEdges();

					circlesAndLinesGraph = DisplayGraph.createSceneDisplayGraph(n, edges);
					ColorPickWindow.createColorMenu(gameModeChosen);
					runGameMode();
				}

				else

				{
					Alert a = new Alert(AlertType.ERROR);
					a.initOwner(window);
					a.setContentText("The combination of " + n + " vertices and " + m + " edges is not allowed!");
					a.show();
				}

			}

		} else {
			Alert a = new Alert(AlertType.ERROR);
			a.initOwner(window);
			a.setContentText("Please fill in all fields!");
			a.show();
		}
	}

	/**
	* Method that handles all actions when the user wants to read in a graph.
	*/
	public static void handleReadButton() {
		FileChooser fc = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
		fc.getExtensionFilters().add(extFilter);
		File f = fc.showOpenDialog(window);
		if (f != null) {
			String absolute = f.getAbsolutePath();

			Graph newGraph = new Graph(absolute);
			String filePath = newGraph.getFile();
			ReadFile.read(filePath);

			n = newGraph.getN();
			edges = newGraph.getEdges();

			circlesAndLinesGraph = DisplayGraph.createSceneDisplayGraph(n, edges);
			ColorPickWindow.createColorMenu(gameModeChosen);
			runGameMode();
		}
	}

	/**
	* Method that keeps track of what gamemode is played.
	*/
	public static void runGameMode() {
		if (gameModeChosen == 1) {
			GameMode1.run();
		}
		if (gameModeChosen == 2) {
			GameMode2.run();
		}
		if (gameModeChosen == 3) {
			GameMode3.run();
		}
	}
}
