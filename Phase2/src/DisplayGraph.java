import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

/**
 * Contains all the variables, and starting methods for displaying the graph.
 * In other words, this class is the spine of displaying graphs.
 */
public class DisplayGraph extends MainGui {

	// Variables for Graphs + debug
    public final static boolean DEBUG = false;
	public final static String COMMENT = "//";
	public static int totalHints=0;
	public static int n;
	public static int m;

	//variables for scene display graph.

	final static int CIRCLE_RADIUS = 20;
	final static int SPACE_BETWEEN_VERTICES = 30; // White space between two vertices

	// The array lists are keeping track of what positions already are taken. Important to note is that the index of a value in this arraylist + 1 is the actual vertex.
	// Also important is that for example index 2 in both array lists refers to vertex 3.
	static ArrayList<Integer> usedWidthPositions = new ArrayList<Integer>();
	static ArrayList<Integer> usedHeightPositions = new ArrayList<Integer>();

	static ArrayList<Circle> circlesDisplayed = new ArrayList<Circle>();
	static ArrayList<Line> linesDisplayed = new ArrayList<Line>();
	static ArrayList<Label> labelsDisplayed = new ArrayList<Label>();

	static int[] assignedColorsVertices;
	public static ObservableList<Color> theColors = FXCollections.observableArrayList();

	static int[] assignedColorsEdges;
	static ArrayList<Color> colorValuesEdges = new ArrayList<Color>();
	static ArrayList<Float> strokeWidthValuesEdges = new ArrayList<Float>();

	static int[] widthPositions;
	static int[] heightPositions;

	static Color lineColorDefault = Color.BLACK;
	static float lineStrokeWidthDefault = 3f;

	static Color circleColorDefault = Color.BLACK;
	static Color circleColorOnHover = Color.web("#f04747");

	static Color circleStrokeColorOnHover = Color.web("#d69429");
	static float circleStrokeWidthOnHover = 7.25f;

	static Color lineColorOnHover = Color.web("#fff864");
	static float lineStrokeWidthOnHover = 10f;

	static Group circlesAndLinesGroup;

	static final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();

	static Boolean firstColored = true;

	/**
	 * First this method calls the method {@link CreateGrid#createGrid()}, which creates an invisible grid.
	 * Then it starts adding circles, and their corresponding labels, to the group with a random position in the grid.
	 * After that it adds the lines to the group.
	 * Last it changes the positions of all the circles and labels with all the lines, such that the z-order is right.
	 */

	public static Group createSceneDisplayGraph(int n, ColEdge[] edgesPar)
	{
		edges = edgesPar;
		DisplayGraph.n = n;

		initializeVisualizationArrays(); // Initialize all the arrays we need

		circlesAndLinesGroup = new Group(); // In this group all our circles, lines and labels (numbers of vertices) will be stored, such that we can add them to the scene

		CreateGrid.createGrid(); // Create an invisible grid, such that we have positions to place our vertices at

		// In this loop we create our circles with their corresponding number labels
		for (int i = 0; i < n; i++)
		{
			int randomWidth;
			int randomHeight;
			boolean duplicate;

			do {
				randomWidth = (int)(Math.random() * widthPositions.length); // Get a random position on the x axis in the grid
				randomHeight = (int)(Math.random() * heightPositions.length); // Get a random position on the y axis in the grid

				 duplicate = CreateGrid.checkIfDuplicate(randomWidth, randomHeight); // Check if the position isn't already taken
			} while (duplicate);

			Circle circle = new Circle(widthPositions[randomWidth], heightPositions[randomHeight], CIRCLE_RADIUS); // We are allowed to create the circle at this position, so create the circle

			// Add the position to the arrays that keep track of the used positions
			usedWidthPositions.add(randomWidth);
			usedHeightPositions.add(randomHeight);

			// If the user hovers of the circle
			circle.setOnMouseEntered(e -> {
					VisualOutputMethods.circleOnHoverEnter(circle, edges, circlesAndLinesGroup, lineColorOnHover);
			});

			// If the user stops hovering over the circle
			circle.setOnMouseExited(e -> {
					VisualOutputMethods.circleOnHoverExit(circle, edges, circlesAndLinesGroup, lineColorDefault);
			});

			// If the user clicks on the circle
			circle.setOnMouseClicked(e -> {
        	if (e.getButton() == MouseButton.PRIMARY) // If the pressed button is the left mouse button
        	{
        		selectedCircle = circle; // Get the circle that is clicked

                if (gameModeChosen != 3)
                {
                	ColorPickWindow.displayColor(); // Color the vertex
                }
                else // If the user is playing game mode 3, before coloring a vertex we need to check if the clicked circle is the circle that needs to be colored
                {
                	if (GM3DisplayGraph.currentVertexToColor <= n - 1)
                	{
                		if ((circlesAndLinesGroup.getChildren().indexOf(circle) - edges.length) == (GM3DisplayGraph.vertexColorOrder.get(GM3DisplayGraph.currentVertexToColor)))
                		{
                			ColorPickWindow.displayColor(); // Everything is fine, we can color the vertex
                    	}
                	}
                }
        	}
        	if (e.getButton() == MouseButton.SECONDARY) // If the pressed button is the right mouse button
        	{
        		if (gameModeChosen != 3)
        		{
            		selectedCircle = circle;
            		ColorPickWindow.resetColor(); // Reset the color of the selected circle to the default color
        		}
        	}
			});

			circle.setOnMousePressed(e -> {
				mouseAnchor.set(new Point2D(e.getSceneX(), e.getSceneY()));
			});

			// If the user drags a circle
			circle.setOnMouseDragged(e -> {
				VisualOutputMethods.onMouseDragging(circle, e);
			});

			circlesDisplayed.add(circle); // Add the created circle to a list that stores all the circle objects

			// Beneath everything for showing the numbers of the vertices
			// The events are the same as the events for the circles (because the label is inside the circle)

			String text = "" + (circlesDisplayed.indexOf(circle) + 1);
			Label label = new Label(text);

			// Determine what font size the label needs to have according to the the number
			int size;
			if (circlesDisplayed.indexOf(circle) >= 100 && circlesDisplayed.indexOf(circle) < 1000) size = 11;
			else if (circlesDisplayed.indexOf(circle) >= 1000 && circlesDisplayed.indexOf(circle) < 10000) size = 7;
			else if (circlesDisplayed.indexOf(circle) >= 10000 && circlesDisplayed.indexOf(circle) < 100000) size = 5;
			else size = 15;

			label.setFont(new Font("Arial", size));
			label.setTextFill(Color.WHITE);

			label.setPrefWidth(CIRCLE_RADIUS);
			label.setPrefHeight(CIRCLE_RADIUS);

			label.setAlignment(Pos.CENTER);

			// Translate the label to the center of the circle
			label.setTranslateX(widthPositions[randomWidth] - CIRCLE_RADIUS/2);
			label.setTranslateY(heightPositions[randomHeight] - CIRCLE_RADIUS/2);

			// All the events beneath are the same as the events above. This is due to the fact that the label is inside the circle, so clicking the label should have the same result as clicking the circle
			label.setOnMousePressed(e -> {
				mouseAnchor.set(new Point2D(e.getSceneX(), e.getSceneY()));
			});

			label.setOnMouseDragged(e -> {
				VisualOutputMethods.onMouseDragging(circlesDisplayed.get(labelsDisplayed.indexOf(label)), e);
			});

			label.setOnMouseEntered(e -> {
				VisualOutputMethods.circleOnHoverEnter((circlesDisplayed.get(labelsDisplayed.indexOf(label))), edges, circlesAndLinesGroup, lineColorOnHover);
			});

			label.setOnMouseExited(e -> {
				VisualOutputMethods.circleOnHoverExit((circlesDisplayed.get(labelsDisplayed.indexOf(label))), edges, circlesAndLinesGroup, lineColorDefault);
			});

			label.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.PRIMARY)
			{
				selectedCircle = circle;

				if (gameModeChosen != 3)
            	{
					ColorPickWindow.displayColor();
            	}
				else
				{
					if (GM3DisplayGraph.currentVertexToColor <= n - 1)
            		{
						if ((circlesAndLinesGroup.getChildren().indexOf(circlesDisplayed.get(labelsDisplayed.indexOf(label))) - edges.length) == (GM3DisplayGraph.vertexColorOrder.get(GM3DisplayGraph.currentVertexToColor)))
                		{
                			ColorPickWindow.displayColor();
                    	}
            		}
				}
			}
			if (e.getButton() == MouseButton.SECONDARY)
			{
				if (gameModeChosen != 3)
				{
					selectedCircle = circle;
					ColorPickWindow.resetColor();
				}
			}
			});

			// Add the label to the list that stores the label objects
			labelsDisplayed.add(label);
		}

		// Create the lines between the vertices
		for (int i = 0; i < edges.length; i++)
		{
			int firstVertex = edges[i].u;
			int secondVertex = edges[i].v;

			// Draw the line. vertex - 1 is the index of the line, usedWidthPositions gets the position of this vertex as a relative number (so NOT the position in pixels)
			// widthPositions gets the pixel position that corresponds to this relative position. Do this for both vertices, and for both width and height values.
			Line line = new Line(widthPositions[usedWidthPositions.get(firstVertex - 1)], heightPositions[usedHeightPositions.get(firstVertex - 1)], widthPositions[usedWidthPositions.get(secondVertex - 1)], heightPositions[usedHeightPositions.get(secondVertex - 1)]);
			line.setStrokeWidth(lineStrokeWidthDefault);

			// If the user hovers over this line
			line.setOnMouseEntered(e -> {
				VisualOutputMethods.lineOnHoverEnter(line, edges, circlesAndLinesGroup, lineColorOnHover);
			});

			// If the user stops hovering the line. Do the same as above, but then changing the color to the standard color.
			line.setOnMouseExited(e -> {
				VisualOutputMethods.lineOnHoverExit(line, edges, circlesAndLinesGroup, lineColorDefault);
			});

			linesDisplayed.add(line); // Add the line to the list that stores the line objects
		}

		// The code beneath first adds the lines to the group, then the circles and the labels. This will create the right z order between the line, circles and labels.
		for (int i = 0; i < edges.length; i++)
		{
			circlesAndLinesGroup.getChildren().add(linesDisplayed.get(i));
		}
		for (int i = 0; i < n; i++)
		{
			circlesAndLinesGroup.getChildren().add(circlesDisplayed.get(i));
		}
		for (int i = 0; i < n; i++)
		{
			circlesAndLinesGroup.getChildren().add(labelsDisplayed.get(i));
		}

		return circlesAndLinesGroup;
	}

	/**
	 * This method initializes all arrays, that are needed for displaying the graph, with their corresponding values.
	 * @return void
	 */
	public static void initializeVisualizationArrays()
	{
		assignedColorsVertices = new int[n]; // Create an array that holds all the assigned colors to the vertices
		Arrays.fill(assignedColorsVertices, -1); // Fill this array with -1, because -1 represents our default color

		assignedColorsEdges = new int[edges.length]; // Create an array that holds all the assigned colors to the edges
		colorValuesEdges.add(lineColorDefault); // Add our default color to the list that holds the actual colors (these actual colors are represented by integers in the assignedColorsEdges array)
		colorValuesEdges.add(Color.web("#ff0000")); // Add the adjacent highlight color
		strokeWidthValuesEdges.add(lineStrokeWidthDefault); // Add the default line stroke width
		strokeWidthValuesEdges.add(lineStrokeWidthOnHover); // Add the line stroke width for adjacent vertices
	}

	/**
	 * This method shows an alert if the user colors adjacent vertices with the same color.
	 * @return void
	 */

	public static void showAlertAdjacentColors()
	{
		Alert a = new Alert(AlertType.WARNING);
		a.initOwner(window);
		a.setContentText("There is an adjacent vertex that has the same color which is not allowed!");
		a.show();
		if(gameModeChosen == 1) {
			finishButton.setDisable(true);
		}
	}
}
