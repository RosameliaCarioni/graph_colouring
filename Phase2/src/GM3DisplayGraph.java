import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class GM3DisplayGraph extends DisplayGraph {

	static ArrayList<Integer> vertexColorOrder = new ArrayList<Integer>();
	static int currentVertexToColor = 0;

	/**
	 * This method creates the random the vertices need to be colored in.
	 * @return void
	 */
	public static void createColorOrder()
	{
		// This holds all vertices that still need to get a position in the order.
		ArrayList<Integer> availableVertices = new ArrayList<Integer>();

		// Add every vertex to the list.
		for (int i = 0; i < n; i++)
		{
			availableVertices.add(i);
		}

		// Loop trough the vertices
		for (int i = 0; i < n; i++)
		{
			int chosenIndex = new Random().nextInt(availableVertices.size()); // Get a random vertex from the available vertices
			vertexColorOrder.add(availableVertices.get(chosenIndex)); // Add this vertex to the list that contains the order
			availableVertices.remove(chosenIndex); // Remove the chosen vertex from the available list
		}
	}
	/**
	 * This method highlights the next vertex that needs to be colored, according to the random generated order.
	 * @return void
	 */

	public static void highlightNextVertex()
	{
		if (currentVertexToColor <= n - 1)
		{
			Circle circle = circlesDisplayed.get(vertexColorOrder.get(currentVertexToColor)); // Get the circle that needs to be highlighted
			circle.setRadius(30f);
			circle.setStroke(Color.GOLD);
			circle.setStrokeType(StrokeType.INSIDE);
			circle.setStrokeWidth(circleStrokeWidthOnHover);
		}

		if (currentVertexToColor > 0)
		{
			Circle circle = circlesDisplayed.get(vertexColorOrder.get(currentVertexToColor - 1));
			circle.setRadius(CIRCLE_RADIUS);
		}
	}
}
