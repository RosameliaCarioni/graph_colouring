import java.util.Arrays;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Contains all the reset methods
 * which are called if the user resets a game, or quits a game mode.
 */

public class ResetMethods extends DisplayGraph {
	/**
	 * This method is called if the user resets the game.
	 * In that case the colors, and some other values, need to be reset.
	 * @return void
	 */
	public static void resetAllAssignedColors()
	{
		Arrays.fill(assignedColorsVertices, -1); // Fill the assigned colors of the vertices array with -1 (default color)
		Arrays.fill(assignedColorsEdges, 0); // Fill the assigned colors of the edges array with 0 (default color)
		firstColored = true;
		if (gameModeChosen == 1)
		{
			finishButton.setVisible(false); // Hide the finish button
		}
		if (gameModeChosen == 2)
		{
			timeLabel.setTextFill(Color.BLACK); // Change the font color of the timer label to black (it could be red because of the < 10 seconds warning)
			ContinueMusic();
		}

		// Loop trough all the vertices and edges and change their colors to the default color
		for (int i = 0; i < assignedColorsVertices.length; i++)
		{
			Circle circle = circlesDisplayed.get(i);
			circle.setFill(circleColorDefault);
		}
		for (int i = 0; i < assignedColorsEdges.length; i++)
		{
			Line line = linesDisplayed.get(i);
			line.setStroke(lineColorDefault);
			line.setStrokeWidth(lineStrokeWidthDefault);
		}
	}

	/**
	 * This method is called when the user goes back to the main menu.
	 * In this case all data from that game needs to be cleared.
	 * @return void
	 */

	public static void clearData()
	{
		// Reset every array, list and other values that are different each game
		inGame = false;
		totalHints=0;
		usedWidthPositions.clear();
		usedHeightPositions.clear();
		circlesDisplayed.clear();
		linesDisplayed.clear();
		labelsDisplayed.clear();
		theColors.clear();
		GM3DisplayGraph.vertexColorOrder.clear();
		GM3DisplayGraph.currentVertexToColor = 0;
		Graph.interruptThreads(); // Try to stop the thread that is calculating the exact algorithm
		firstColored = true;
	}
}
