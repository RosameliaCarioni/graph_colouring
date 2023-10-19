import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Circle;

/**
 * Contains all the methods that are needed for coloring vertices.
 */
public class ColorAssignation extends DisplayGraph {
	/**
	 * This method is the last check before finally assigning a color to a vertex.
	 * Goal of this method is creating different execution paths for the different game modes.
	 * Besides that, checking if the user finished the game is also happening in this method.
	 * @param color This is the value of the color represented as integer.
	 * @param circle This is the selected circle that needs to be colored.
	 * @param edges This is the ColEdge[] that contains the edges data.
	 * @return void
	 */
	public static void colorChosen(int color, Circle circle, ColEdge[] edges)
	{
		if (firstColored  && color != -1) // If it is the first vertex the user colors, we enable the hint button
		{
			hintButton.setDisable(false);
			firstColored = false;
		}
		boolean vertexIsAlreadyColored = false; // This boolean belongs to game mode 3. If the vertex is already colored, we don't want to change the color

		// Check if the assigned color of our clicked circle is not the default color
		if (gameModeChosen == 3 && assignedColorsVertices[circlesAndLinesGroup.getChildren().indexOf(circle) - edges.length] != -1)
		{
			vertexIsAlreadyColored = true;
		}

		if (!checkAdjacentColors(color, circle))
		{
			// In game mode 1 and 2 the user is able to color adjacent vertices the same color, however, in game mode 3 they shouldn't able to do this
			if (gameModeChosen != 3)
			{
				colorVertex(color, circle);
			}
			else
			{
				Alert a = new Alert(AlertType.ERROR);
				a.initOwner(window);
				a.setContentText("The selected color can't be assigned to this vertex, because at least one adjacent vertex has the same color as the selected color!");
				a.show();
			}
		}
		else
		{
			if (gameModeChosen != 3)
			{
				colorVertex(color, circle);

				if (gameModeChosen == 1)
				{
					//GM1 Logic
					if(allVerticesAreColored())
					{
						if(lastAdjacencyCheck() == false)
						{
							if(checkChromaticNumReached())
							{
								//You can finish;
								System.out.println("Reached");
								finishButton.setVisible(true);
								finishButton.setDisable(false);
								explanationLabel.setText("");
							}
							else if (Graph.getChromaticNumber() == 0)
							{
								System.out.println("Not Reached");
								finishButton.setVisible(true);
								finishButton.setDisable(true);
								explanationLabel.setText("WARNNING: The chromatic number hasn't been calculated (yet)!");
							}
							else
							{
								//You have used too many colors;
								System.out.println("Not Reached");
								finishButton.setVisible(true);
								finishButton.setDisable(true);
								explanationLabel.setText("You are not using the same amount of colors as the chromatic number");
							}
						}
						else
						{
							explanationLabel.setText("Some adjacent vertices have the same color");
						}
					}
				}
				else if (gameModeChosen == 2) {
					if (allVerticesAreColored() && !lastAdjacencyCheck()) {
						System.out.println(allVerticesAreColored());
						GM2Complete();
					}
				}
			}
			else
			{
				if (!vertexIsAlreadyColored) // The selected circle isn't colored yet
				{
					// First we change the assigned color value (integer) in the assignedColorsVertices array
					assignedColorsVertices[circlesAndLinesGroup.getChildren().indexOf(circle) - edges.length] = color;
					VisualOutputMethods.removeCirclesStroke(); // Remove the highlight stroke
					circle.setFill(theColors.get(color)); // Change the visual color of the circle
					GM3DisplayGraph.currentVertexToColor++; // The user colored the vertex that needs to be colored, so we continue to the next vertex
					GM3DisplayGraph.highlightNextVertex(); // Highlight the next vertex that needs to be colored

					if(allVerticesAreColored()) // Check if all vertices are colored
					{
						GM3Complete();
					}
				}
			}
		}
    }
	/**
	 * This is the method that finally assigns the color to the vertex.
	 * @param color This is the value of the color represented as integer.
	 * @param circle This is the selected circle that needs to be colored.
	 * @return void
	 */
	public static void colorVertex(int color, Circle circle)
	{
		// First we change the assigned color value (integer) in the assignedColorsVertices array
		assignedColorsVertices[circlesAndLinesGroup.getChildren().indexOf(circle) - edges.length] = color;
		VisualOutputMethods.removeCirclesStroke(); // Remove the highlight stroke
		if (color != -1)
		{
			circle.setFill(theColors.get(color)); // Change the visual color of the circle
		}
		else circle.setFill(circleColorDefault); // The user wants to color the circle back to the default color
	}
	/**
	 * This method checks if the color the user wants to assign to an vertex is already assigned to adjacent vertices.
	 * @param color color This is the value of the color represented as integer.
	 * @param circle This is the selected circle that needs to be colored.
	 * @return Boolean It returns true if there are adjacent vertices with the same color and returns false if there aren't.
	 */
	public static Boolean checkAdjacentColors(int color, Circle circle)
	{
		boolean allowed = true;
		boolean alertBoxShown = false;
		int index = circlesAndLinesGroup.getChildren().indexOf(circle) - edges.length;

		for (int i = 0; i < edges.length; i++)
		{
			int firstVertex = edges[i].u;
			int secondVertex = edges[i].v;

			if (firstVertex == index + 1) // The first vertex of the edge is equals to our selected circle
			{
				int indexOtherCircle = secondVertex - 1;
				int colorOfOtherCircle = assignedColorsVertices[indexOtherCircle];

				if (color == colorOfOtherCircle && color != -1) // Check if the color the user wants to assign to the selected circle is the same as the color of the adjacent vertex
				{
					if (gameModeChosen != 3)
					{
						VisualOutputMethods.highlightAdjacentVerticesColors(i, 1, true, alertBoxShown);
						alertBoxShown = true;
					}
					allowed = false;
				}
				else
				{
					if (gameModeChosen != 3)
					{
						VisualOutputMethods.highlightAdjacentVerticesColors(i, 0, false, alertBoxShown);
					}
				}
			}
			if (secondVertex == index + 1) // The second vertex of the edge is equals to our selected circle
			{
				int indexOtherCircle = firstVertex - 1;
				int colorOfOtherCircle = assignedColorsVertices[indexOtherCircle];

				if (color == colorOfOtherCircle && color != -1)
				{
					if (gameModeChosen != 3)
					{
						VisualOutputMethods.highlightAdjacentVerticesColors(i, 1, true, alertBoxShown);
						alertBoxShown = true;
					}
					allowed = false;
				}
				else
				{
					if (gameModeChosen != 3)
					{
						VisualOutputMethods.highlightAdjacentVerticesColors(i, 0, false, alertBoxShown);
					}
				}
			}
		}
		return allowed;
	}

	/**
	* Method that checks if all vertices are colored.
	* @return true or false
	*/
	public static boolean allVerticesAreColored() {
		for(int i = 0; i < assignedColorsVertices.length; i++) {
			if(assignedColorsVertices[i] == -1)
			{
				return false;
			}
		}
		return true;
	}

	/**
	* Method that checks if the used amount of colours is the same as the exact chromatic number.
	* @return true or false
	*/
	public static boolean checkChromaticNumReached() {

		ArrayList<Integer> checkColorsUsed = new ArrayList<Integer>();

		for(int i = 0; i < assignedColorsVertices.length; i++)
		{
			if(!check(assignedColorsVertices[i], checkColorsUsed))
			{
				checkColorsUsed.add(assignedColorsVertices[i]);
			}
		}

		if(checkColorsUsed.size() == Graph.chromaticNumber) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean check(int color, ArrayList<Integer> array)
	{
		for(int i = 0; i < array.size(); i++)
		{
			if(array.get(i)== color)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean lastAdjacencyCheck()
	{
		for (int j = 0; j < edges.length; j++)
		{
			int firstVertex = edges[j].u;
			int secondVertex = edges[j].v;

			int firstVColor = assignedColorsVertices[firstVertex - 1];
			int secondVColor = assignedColorsVertices[secondVertex - 1];
			if(firstVColor == secondVColor)
			{
				return true;
			}
		}
		return false;
	}

	/**
	* Method that is called when the user completes gamemode 2.
	* Method will stop the timer and showe an alert with all needed information.
	*/
	public static void GM2Complete() {
		Timer.pauseAnimation();
		Timer.ContinueMusic();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(window);
		int greedyChromatic = Graph.getUpperBound();
		int userChromatic = userChromaticNumber();
		alert.setTitle("You Won!");
		alert.setHeaderText("Well Done! You colored the graph within the time limit.");
		int seconds = Timer.getTimeItTook();
		int minutes = 0;
		while (seconds - 60 >= 0)
		{
			seconds = seconds - 60;
			minutes++;
		}
		String timeText = new String(minutes + " minutes and " + seconds + " seconds.");
		alert.setContentText("You colored the graph in " + timeText + "\nYou colored it with: " + userChromatic + " colors."+ "\nThe upper bound is: " + greedyChromatic + " colors.");
		ButtonType restartType = new ButtonType("Retry");
		ButtonType homeType = new ButtonType("Home");
		alert.getButtonTypes().setAll(restartType, homeType);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == restartType) {
			ResetMethods.resetAllAssignedColors();
			Timer.resetAnimation();
			firstColored = true;
		}
		else if (result.get() == homeType) {
			ResetMethods.clearData();
			StartScreen.run2();
			Timer.stopAnimation();
		}
	}

	/**
	 * This method is being called if the user completes game mode 3.
	 * @return void
	 */
	public static void GM3Complete()
	{
		Timer.pauseAnimation(); // Pause the timer
		GM3DisplayGraph.currentVertexToColor = 0;
		Alert a = new Alert(AlertType.CONFIRMATION);
		ButtonType restartType = new ButtonType("Try again");
	    ButtonType homeType = new ButtonType("Main menu");

	    a.getButtonTypes().setAll(restartType, homeType);
	    a.initOwner(window);
	    a.setHeaderText("Your results");

	    // This transforms the seconds it took to minutes and seconds
	    int seconds = Timer.getTimeItTook();
		int minutes = 0;
		while (seconds - 60 >= 0)
		{
			seconds = seconds - 60;
			minutes++;
		}
		String timeText = new String(minutes + " minutes and " + seconds + " seconds.");
	    a.setContentText("You colored the graph with " + userChromaticNumber() + " colors. \nThe least number of colors needed to color this graph is " + Graph.getChromaticNumber() + ".\nThe upper bound of this graph is " + Graph.getUpperBound() + ". \nThe time it took to color the graph is " + timeText);

	    Optional<ButtonType> result = a.showAndWait(); // Wait with further execution of the code for the input of the user
	    if (result.get() == restartType) {
		  GM3DisplayGraph.createColorOrder(); // Create a new order the vertices need to be colored in
		  GM3DisplayGraph.highlightNextVertex(); // Highlight the first vertex that needs to be colored
	      ResetMethods.resetAllAssignedColors(); // Reset the colors that are assigned to the circles
	      Timer.resetAnimation(); // Reset the timer
	      firstColored = true;
	    }
	    else if (result.get() == homeType) {
	      ResetMethods.clearData(); // Clear game data
	      StartScreen.run2(); // Run the start screen
	      Timer.stopAnimation(); // Stop the timer
	    }
	}

	/**
	* Method that checks the amount of colors that the user needed to color the graph.
	*/
	public static int userChromaticNumber() {
		ArrayList<Integer> colorsUsed = new ArrayList<Integer>();
		for (int i = 0; i < assignedColorsVertices.length; i++) {
			if (!newColorCheck(assignedColorsVertices[i], colorsUsed)) {
				colorsUsed.add(assignedColorsVertices[i]);
			}
		}
		return colorsUsed.size();
	}

	/**
	* Method that checks if the chosen color is already in the list.
	*/
	public static boolean newColorCheck(int color, ArrayList<Integer> colorList) {
		return colorList.contains(color);
	}
}
