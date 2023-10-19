import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;

/**
 * Contains all the methods that cause visual alteration to the graph regarding hovering, dragging and highlighting.
 */
public class VisualOutputMethods extends DisplayGraph {
	//when you pass mouse over line, it changes color
		/**
		 * When you hover over a line, or stop hovering over a line, this method is called.
		 * This method changes the color of the line and the stroke of it's adjacent vertices to the right color.
		 * @param Line This is the line where the user is hovering on.
		 * @param circlesAndLinesGroup This group contains all circles, lines and labels (nodes) that are being displayed for the graph.
		 * @param lineColor This is the color the line needs to be colored to.
		 * @return void
		 */
		public static void lineOnHoverEnter(Line line, ColEdge[] edges, Group circlesAndLinesGroup, Color lineColor)
		{
			// Get the index of the line in the group minus the amount of vertices, because they're the first n values, and we want the index from the line starting at 0, because this needs to correspond to the circlesDisplayed array list (which has the same order).
			int index = circlesAndLinesGroup.getChildren().indexOf(line);

			line.setStroke(lineColor);
			line.setStrokeWidth(lineStrokeWidthOnHover);

			Circle circleToColor1 = circlesDisplayed.get(edges[index].u - 1); // Circle we need to color. So the index of the line = index of the edge. We get the first vertex in this edge.
			circleToColor1.setStroke(circleStrokeColorOnHover);
			circleToColor1.setStrokeType(StrokeType.INSIDE);
			circleToColor1.setStrokeWidth(circleStrokeWidthOnHover);

			Circle circleToColor2 = circlesDisplayed.get(edges[index].v - 1); // Same as above, but then for the second vertex in the edge.
			circleToColor2.setStroke(circleStrokeColorOnHover);
			circleToColor2.setStrokeType(StrokeType.INSIDE);
			circleToColor2.setStrokeWidth(circleStrokeWidthOnHover);
		}
		/**
		 * This method is called when the user stops hovering over a line.
		 * @param line This is the line / edge the user stopped hovering on.
		 * @param edges The ColEdge[] containing all the edges data.
		 * @param circlesAndLinesGroup This group contains all circles, lines and labels that are being displayed for the graph.
		 * @param lineColor The color the line needs to be colored to.
		 * @return void
		 */

		public static void lineOnHoverExit(Line line, ColEdge[] edges, Group circlesAndLinesGroup, Color lineColor)
		{
			int index = circlesAndLinesGroup.getChildren().indexOf(line);

			line.setStroke(colorValuesEdges.get(assignedColorsEdges[index]));
			line.setStrokeWidth(strokeWidthValuesEdges.get(assignedColorsEdges[index]));

			// Get the index of the line in the group minus the amount of vertices, because they're the first n values, and we want the index from the line starting at 0, because this needs to correspond to the circlesDisplayed array list (which has the same order).

			Circle circleToColor1 = circlesDisplayed.get(edges[index].u - 1); // Circle we need to color. So the index of the line = index of the edge. We get the first vertex in this edge.
			circleToColor1.setStrokeWidth(0f);

			Circle circleToColor2 = circlesDisplayed.get(edges[index].v - 1); // Same as above, but then for the second vertex in the edge.
			circleToColor2.setStrokeWidth(0f);

			if (gameModeChosen == 3)
			{
				GM3DisplayGraph.highlightNextVertex();
			}
		}

		//when you pass mouse over circle
		/**
		 * When you hover over a circle this method is called.
		 * This method changes the stroke characteristics of the circle, its adjacent vertices and the color of its adjacent lines.
		 * @param circle This is the circle where the user is hovering on.
		 * @param circlesAndLinesGroup This group contains all circles, lines and labels that are being displayed for the graph.
		 * @param lineColor This it he color the adjacent line(s) needs to be colored to.
		 * @return void
		 */
		public static void circleOnHoverEnter(Circle circle, ColEdge[] edges, Group circlesAndLinesGroup, Color lineColor)
		{
			circle.setStroke(circleStrokeColorOnHover);
			circle.setStrokeType(StrokeType.INSIDE);
			circle.setStrokeWidth(circleStrokeWidthOnHover);

			int index = circlesAndLinesGroup.getChildren().indexOf(circle) - edges.length; // Get the index of where the circle is represented in the group. Note that this number + 1 and - the amount of edges is the index, because in the group the lines are first represented. (We want to get the index of the circle starting at 0)
			for (int j = 0; j < edges.length; j++)
			{
				int firstVertex = edges[j].u;
				int secondVertex = edges[j].v;

				if (firstVertex == index + 1) // The first vertex of the edge is the same as my hovered circle, so we want to color the second vertex in this edge.
				{
					Circle circleToColor = circlesDisplayed.get(secondVertex - 1); // We want to get the circle that belongs to this vertex. Note that the second vertex in the edge - 1 is the index of this circle.
					circleToColor.setStroke(circleStrokeColorOnHover);
					circleToColor.setStrokeType(StrokeType.INSIDE);
					circleToColor.setStrokeWidth(circleStrokeWidthOnHover);

					Line lineToColor = linesDisplayed.get(j); // The lines had drawn one after each other, following the order of the edge array. So the first edge represents the first line.
					lineToColor.setStroke(lineColor); // Color this line to the hover color.
					lineToColor.setStrokeWidth(lineStrokeWidthOnHover); // Change the width of the line.
				}
				else if (secondVertex == index + 1) // To the same as above, but then for the other vertex in the edge
				{
					Circle circleToColor = circlesDisplayed.get(firstVertex - 1);
					circleToColor.setStroke(circleStrokeColorOnHover);
					circleToColor.setStrokeType(StrokeType.INSIDE);
					circleToColor.setStrokeWidth(circleStrokeWidthOnHover);

					Line lineToColor = linesDisplayed.get(j);
					lineToColor.setStroke(lineColor);
					lineToColor.setStrokeWidth(lineStrokeWidthOnHover);
				}
			}
		}

		/**
		 * When you stop hovering over a circle, this method is called.
		 * This method changes the stroke characteristics of the circle, its adjacent vertices and the color of its adjacent lines.
		 * @param circle This is the circle where the user stopped hovering on.
		 * @param circlesAndLinesGroup This group contains all circles, lines and labels that are being displayed for the graph.
		 * @param lineColor This it he color the adjacent line(s) needs to be colored to.
		 * @return void
		 */
		public static void circleOnHoverExit(Circle circle, ColEdge[] edges, Group circlesAndLinesGroup, Color lineColor)
		{
			int index = circlesAndLinesGroup.getChildren().indexOf(circle) - edges.length; // Get the index of where the circle is represented in the group. Note that this number + 1 and - the amount of edges is the index, because in the group the lines are first represented. (We want to get the index of the circle starting at 0)

			circle.setStrokeWidth(0f);

			for (int j = 0; j < edges.length; j++)
			{
				int firstVertex = edges[j].u;
				int secondVertex = edges[j].v;

				if (firstVertex == index + 1) // The first vertex of the edge is the same as my hovered circle, so we want to color the second vertex in this edge.
				{
					Circle circleToColor = circlesDisplayed.get(secondVertex - 1); // We want to get the circle that belongs to this vertex. Note that the second vertex in the edge - 1 is the index of this circle.
					circleToColor.setStrokeWidth(0f);


					Line lineToColor = linesDisplayed.get(j); // The lines had drawn one after each other, following the order of the edge array. So the first edge represents the first line.
					lineToColor.setStroke(colorValuesEdges.get(assignedColorsEdges[j])); // Color this line to the hover color.
					lineToColor.setStrokeWidth(strokeWidthValuesEdges.get(assignedColorsEdges[j])); // Change the width of the line.
				}
				else if (secondVertex == index + 1) // To the same as above, but then for the other vertex in the edge
				{
					Circle circleToColor = circlesDisplayed.get(firstVertex - 1);
					circleToColor.setStrokeWidth(0f);

					Line lineToColor = linesDisplayed.get(j);
					lineToColor.setStroke(colorValuesEdges.get(assignedColorsEdges[j]));
					lineToColor.setStrokeWidth(strokeWidthValuesEdges.get(assignedColorsEdges[j]));
				}
			}

			if (gameModeChosen == 3)
			{
				GM3DisplayGraph.highlightNextVertex();
			}
		}

		/**
		 * This method is being called if the user drags a vertex / circle.
		 * @param circle This is the Circle that is being dragged.
		 * @param e This is the MouseEvent.
		 */

		public static void onMouseDragging(Circle circle, MouseEvent e)
		{
			double deltaX = e.getSceneX() - mouseAnchor.get().getX();
			double deltaY = e.getSceneY() - mouseAnchor.get().getY();

			circle.setLayoutX(circle.getLayoutX() + deltaX);
			circle.setLayoutY(circle.getLayoutY() + deltaY);

			// Get the index in the group of the circle that is being dragged. Do it minus the amount of edges, because the group consists of edges(lines) + circles, so now you've the index of the circle in the array list.
			int index = circlesAndLinesGroup.getChildren().indexOf(circle) - edges.length;

			labelsDisplayed.get(index).setTranslateX(labelsDisplayed.get(index).getTranslateX() + deltaX);
			labelsDisplayed.get(index).setTranslateY(labelsDisplayed.get(index).getTranslateY() + deltaY);

			for (int j = 0; j < edges.length; j++) // Loop trough every edge
			{
				int firstVertex = edges[j].u;
				int secondVertex = edges[j].v;

				if (index + 1 == firstVertex) // If the first vertex of the edge = the circle
				{
					Line line = linesDisplayed.get(j); // Get the line of this edge (edge = line, so it has the same index as j)
					line.setStartY(line.getStartY()+deltaY); // Change the starting Y position
					line.setStartX(line.getStartX()+deltaX);
				}
				else if (index + 1 == secondVertex) // If the second vertex of the edge = the circle
				{
					Line line = linesDisplayed.get(j);
					line.setEndY(line.getEndY()+deltaY); // Do the same as above, but then changing the end position of the line, because the vertex we're dragging is the second vertex of the edge.
					line.setEndX(line.getEndX()+deltaX);
				}
			}
			mouseAnchor.set(new Point2D(e.getSceneX(), e.getSceneY()));
		}

		/**
		 * This method highlights the edges of adjacent vertices with the same color, and if needed it also shows an alert.
		 * @param i This is the index of the edge that needs to be changed.
		 * @param edgeColor This is the color the edge needs to be changed to as an integer.
		 * @param showAlert This boolean represents if we want to show an alert.
		 * @param alertBoxShown This boolean represents if the alert is already shown.
		 */

		public static void highlightAdjacentVerticesColors(int i, int edgeColor, boolean showAlert, boolean alertBoxShown)
		{
			assignedColorsEdges[i] = edgeColor; // Change the color of the edge to the edgeColor parameter
			Line lineToColor = linesDisplayed.get(i);
			lineToColor.setStroke(colorValuesEdges.get(assignedColorsEdges[i])); // Set the visual color of the edge
			lineToColor.setStrokeWidth(strokeWidthValuesEdges.get(assignedColorsEdges[i])); // Set the stroke width of the edge
			if (showAlert && !alertBoxShown) showAlertAdjacentColors(); // If the alert for the same colors assigning to adjacent vertices isn't already displayed, display it
		}

		/**
		 * This method removes the highlighted stroke characteristics of the circles.
		 */
		public static void removeCirclesStroke()
		{
			// Loop trough all circles and remove reset their stroke width
			for (int i = 0; i < circlesDisplayed.size(); i++)
			{
				Circle circle = circlesDisplayed.get(i);
				circle.setStrokeWidth(0);
			}
		}
}
