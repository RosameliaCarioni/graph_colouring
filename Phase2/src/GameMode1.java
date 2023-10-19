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
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

public class GameMode1 extends ColorAssignation {

	static Button resetButton, searchButton;
	static CheckBox checkBoxShowNumbers;
	static Label titleLabel;
	static HBox topHBox;
	static TextField searchVertex;

	static VBox timerVBox;
	static Timer timer;

	static ScrollPane scrollPane;

	/**
	* Method that is called when the user wants to play gamemode 1. This method creates all the components
	* needed for gamemode 1 and adds it to a screen.
	*/
	public static void run() {
		inGame = true;
		//Hint button - is initially disabled until at least one vertex has been colored
		hintButton = new Button("Hint");
		hintButton.setPrefWidth(80);
		hintButton.setDisable(true);
		//Reset Button
		resetButton = new Button("Reset");
		//Option to show the number of vertices
		checkBoxShowNumbers = new CheckBox("Show the numbers of the vertices");
		checkBoxShowNumbers.setSelected(true);
		/*
		 * With gm1, the user will not be able to finish the game unless all vertices have
		 * been colored correctly. Thus when the game mode is first run, the finish button is invisible.
		 */
		finishButton = new Button("Finish");
		finishButton.setVisible(false);

		explanationLabel = new Label("");

		timer = new Timer();

		timerVBox = new VBox(50);
		titleLabel = new Label("To the bitter end!");

		/*
		 * The user will be able to search a vertex (for example as a consequence of one of the hints) if the graph
		 * is big or to save up time. The vertex searched in the TextField will be taken into account and highlighted in the graph
		 * when the search button is clicked. The TextField also has a listener, thus when the vertex searched is deleted from the
		 * TextField, the highlighting in the graph will stop.
		 */
		searchVertex = new TextField();
	   	searchVertex.setPromptText("Search a vertex");
	    	searchVertex.setEditable(true);
	    	searchButton = new Button("Search");

		topHBox = new HBox(50);

		topHBox.getChildren().addAll(checkBoxShowNumbers, titleLabel, hintButton, resetButton, finishButton, explanationLabel, searchVertex, searchButton);
		topHBox.setAlignment(Pos.CENTER);



		timerVBox.getChildren().add(timeLabel);
		timerVBox.setAlignment(Pos.CENTER);

	    	BorderPane pane = new BorderPane();
	   	 pane.setTop(timerVBox);
	    	pane.setCenter(colorBox);

		scrollPane = new ScrollPane(circlesAndLinesGraph);

		gm1Border = new BorderPane(scrollPane);
		gm1Border.setTop(topHBox);
		gm1Border.setLeft(pane);

		scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);


		/*
		 * This is the listener of the textField, when nothing is inputed in the text field, the vertex that
		 * has previously been searched, will not be highlighted anymore.
		 */
		 searchVertex.textProperty().addListener((observable, oldValue, newValue) -> {
			 int vert;

		    	if(newValue.equals("")) {
		    		vert = 0;
		    		for(int i = 0; i < circlesDisplayed.size(); i++) {
		        		circlesDisplayed.get(i).setStroke(null);
		        	}
		    	}
		 });

		 /*
		  * ActionEvent of the search button. Takes the vertex that has been inputed in the textField and highlights
		  * the corresponding vertex in the graph. This is done with a yellow stroke inside the circle (vertex).
		  */
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


		window.getScene().setRoot(gm1Border);
		//Reset Button, connects to class ResetMethods.java
		resetButton.setOnAction(e -> {
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.initOwner(window);
			a.setContentText("Are you sure you want to reset the colors of the vertices?");

			Optional<ButtonType> result = a.showAndWait();// Wait for the input of the user for further execution of the code
			if (result.get() == ButtonType.OK) {
				ResetMethods.resetAllAssignedColors();//Reset the colors that have been assigned to the vertices

				a = new Alert(AlertType.INFORMATION);
				a.initOwner(window);
				a.setContentText("The colors have been reset succesfully!");
				a.show();
			}
		});

		/*
		 * The message displayed by the alert of this button will tell the user the amount of colors used (with will be the chromatic number
		 * of the graph) and the time it took. Moreover the user will have two different options after finishing: they can either restart or
		 * go back to the home screen.
		 */
		finishButton.setOnAction(e -> {
			Timer.pauseAnimation();
			Alert a = new Alert(AlertType.CONFIRMATION);
			ButtonType restartType = new ButtonType("Try again");
			ButtonType homeType = new ButtonType("Main menu");

			a.getButtonTypes().setAll(restartType, homeType);
			a.initOwner(window);
			a.setHeaderText("You won!");
			int seconds = Timer.getTimeItTook();
			int minutes = 0;
			while (seconds - 60 >= 0)
			{
				seconds = seconds - 60;
				minutes++;
			}
			String timeText = new String(minutes + " minutes and " + seconds + " seconds.");
			a.setContentText(
					"CONGRATS! You colored the graph with " + userChromaticNumber() + " colors, which is the chromatic number."+  "\n"+ "And you did it in " + timeText);

			/*The reply of the user after the alert is displayed
			 * If the user decides to replay - resets all the colors assigned to the vertices, these will also be changed back to the
			 * default color, which is black. Resets also the timer.
			 * If the user decides to go back to home screen, the method run2() of the class StartScreen is executed.
			 */
			Optional<ButtonType> result = a.showAndWait();
			if (result.get() == restartType) {
				ResetMethods.resetAllAssignedColors();
				Timer.resetAnimation();
				firstColored = true;
			} else if (result.get() == homeType) {
				ResetMethods.clearData();
				StartScreen.run2();
				Timer.stopAnimation();
			}
		});

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
