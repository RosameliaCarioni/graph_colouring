import java.util.Optional;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.control.TextField;


public class GameMode2 extends ColorAssignation {

	static Button resetButton, searchButton;
	static Label titleLabel;
	static HBox topHBox;
	static TextField searchVertex;

	static CheckBox checkBoxShowNumbers;

	static VBox timerVBox;
	static Timer timer;

	static ScrollPane scrollPane;

	/**
	* Method that is run when the user wants to play gamemode 2. This method
	* creates all the components needed for the gamemode and adds it to the screen.
	*/ 
	public static void run() {
		inGame = true;
		hintButton = new Button("Hint");
		hintButton.setPrefWidth(80);
		resetButton = new Button("Reset");
		titleLabel = new Label("Best upperbound!");
		checkBoxShowNumbers = new CheckBox("Show the numbers of the vertices");
		checkBoxShowNumbers.setSelected(true);


		searchVertex = new TextField();
	    	searchVertex.setPromptText("Search a vertex");
	    	searchVertex.setEditable(true);
	    	searchButton = new Button("Search");

		topHBox = new HBox(50);

		timerVBox = new VBox(0);
		timer = new Timer((n*2)+(edges.length*3));

		topHBox.getChildren().addAll(checkBoxShowNumbers, titleLabel, hintButton, resetButton, searchVertex, searchButton);
		topHBox.setAlignment(Pos.CENTER);

		hintButton.setDisable(true);

		timerVBox.getChildren().add(timeLabel);
		timerVBox.setAlignment(Pos.CENTER);

		BorderPane pane = new BorderPane();
		pane.setTop(timerVBox);
		pane.setCenter(colorBox);

		scrollPane = new ScrollPane(circlesAndLinesGraph);

		gm2Border = new BorderPane(scrollPane);
		gm2Border.setTop(topHBox);
		gm2Border.setLeft(pane);

		scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);

		window.getScene().setRoot(gm2Border);


		 searchVertex.textProperty().addListener((observable, oldValue, newValue) -> {
			 int vert;

		    	if(newValue.equals("")) {
		    		vert = 0;
		    		for(int i = 0; i < circlesDisplayed.size(); i++) {
		        		circlesDisplayed.get(i).setStroke(null);
		        	}
		    	}
		 });

		 searchButton.setOnAction(e -> {
		    	Circle circleOfSearched;
		    	Circle oldCircleSearched;
		    	int vert;

		    	if(searchVertex.getText().isEmpty()) {
		    		vert = 0;
		    		for(int i = 0; i < circlesDisplayed.size(); i++) {
		        		circlesDisplayed.get(i).setStroke(null);
		        	}
		    	}
		    	else {
		    		vert = Integer.valueOf(searchVertex.getText());
			        if(vert < circlesDisplayed.size()+1 && vert > 0) {
					circleOfSearched = circlesDisplayed.get(vert-1);
					circleOfSearched.setStrokeType(StrokeType.INSIDE);
					circleOfSearched.setStroke(Color.YELLOW);
					circleOfSearched.setStrokeWidth(7.25f);
					if (scrollPane != null)
			       		 {
				       		scrollPane.setVvalue(circleOfSearched.getCenterY() / scrollPane.getContent().getBoundsInLocal().getHeight());
						scrollPane.setHvalue(circleOfSearched.getCenterX() / scrollPane.getContent().getBoundsInLocal().getWidth());
			        	}
				}
		    	}

		    });

		hintButton.setOnAction(e -> {
			Timer.setHintRestrictionGM2();
			hintButton.setDisable(true);
			Hint.Hints();
		});

		resetButton.setOnAction(e -> {
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.initOwner(window);
			a.setContentText("Are you sure you want to reset the colors of the vertices?");

			Optional<ButtonType> result = a.showAndWait();
			if (result.get() == ButtonType.OK) {
				ResetMethods.resetAllAssignedColors();

				a = new Alert(AlertType.INFORMATION);
				a.initOwner(window);
				a.setContentText("The colors have been reset succesfully!");
				a.show();
				Timer.resetAnimation();
			}
		});

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
