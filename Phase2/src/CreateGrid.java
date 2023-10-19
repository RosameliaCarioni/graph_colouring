/**
 * This class contains methods for creating the grid, and their positions.
 */
public class CreateGrid extends DisplayGraph {
	/**
	 * It checks if the generated positions isn't already taken by an other vertex.
	 * @param randomWidth This is the randomly chosen x position of the vertex.
	 * @param randomHeight This is the randomly chosen y position of the vertex.
	 * @return Boolean If the position is already taken it returns true, otherwise false.
	 */
	public static boolean checkIfDuplicate(int randomWidth, int randomHeight)
	{
		// Check if my random created width and height position is already represented in the array lists that keep track of the used positions.
		for (int j = 0; j < usedWidthPositions.size(); j++)
		{
			if (usedWidthPositions.get(j) == randomWidth)
			{
				if (usedHeightPositions.get(j) == randomHeight)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This method creates the (invisible) grid.
	 * @param none
	 * @return void
	 */
	public static void createGrid()
	{
		int spaceBetweenVertices = (2*CIRCLE_RADIUS) + SPACE_BETWEEN_VERTICES; // The distance/space between two vertices is two times the radius of the circle + the space between the vertices. i.e. 2 * white space in the circle + 1 * white space outside the circle.
		int amountOfVerticesWidth = (int)((SCREEN_WIDTH) / spaceBetweenVertices); // Dividing the width of the screen by the space we want between our circles, we get the amount of circles we can display horizontally.
		int amountOfVerticesHeight = (int)((SCREEN_HEIGHT) / spaceBetweenVertices); // Same story as for the width, but then for the height.

		// We check if the amount of vertices the user wants to display fit on the screen or not
		if (n <= amountOfVerticesWidth * amountOfVerticesHeight)
		{
			widthPositions = new int[amountOfVerticesWidth];
			heightPositions = new int[amountOfVerticesHeight];

			for (int i = 0; i < widthPositions.length; i++)
			{
				widthPositions[i] = i * spaceBetweenVertices; // Add the location in pixels to an array which holds the locations.
			}
			for (int i = 0; i < heightPositions.length; i++)
			{
				heightPositions[i] = i * spaceBetweenVertices;
			}
		}
		else
		{
			/*
			 The vertices don't fit on the screen, so we need to create more positions
			 If we take the root of the amount of vertices for the x-axis positions and for the y-axis positions, we have exact enough positions to display the vertices
			 However, finding an available spot might take some time
			 Therefore, we add an extra amount of spots according to the amount of vertices
			 Such that finding an available spot with big graphs will take much less time
			*/
			widthPositions = new int[(int) Math.sqrt(n) + n/50];
			heightPositions = new int[(int) Math.sqrt(n) + n/100];

			for (int i = 0; i < widthPositions.length; i++)
			{
				widthPositions[i] = (i * spaceBetweenVertices); // Add the location in pixels to an array which holds the locations.
			}
			for (int i = 0; i < heightPositions.length; i++)
			{
				heightPositions[i] = (i * spaceBetweenVertices);
			}
		}
	}
}
