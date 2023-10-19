import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Contains the methods for the 4th hint. This hint is used in game mode 3 only.
 */
public class Hint4 extends Hint {

	public static int[] assignedColors;
	public static int[] options;
	private static Boolean addingColor;

	/**
	 * This method generates the hint for game mode 3.
	 * @return void
	 */

	public static void Hint() {
		addingColor = false;

		int[] assignedColors = assignedColorsVertices;

		// Get the vertex we need to give a hint for
		int targetVertex = GM3DisplayGraph.vertexColorOrder.get(GM3DisplayGraph.currentVertexToColor);

		int chromaticNumberToUse;
		// We need to check if the user used more colors than the chromatic number
		// Because if this is the case, we need to use the chromatic number of the user
		// Otherwise, our exact algorithm won't find a solution
		if (ColorAssignation.userChromaticNumber() > Graph.getChromaticNumber()) {
			chromaticNumberToUse = ColorAssignation.userChromaticNumber();
		} else
			chromaticNumberToUse = Graph.getChromaticNumber();

		// This array stores what colors the user already used
		ArrayList<Integer> usedColors = new ArrayList<Integer>();
		for (int i = 0; i < assignedColors.length; i++) {
			if (!usedColors.contains(assignedColors[i]) && assignedColors[i] != -1) {
				usedColors.add(assignedColors[i]);
			}
		}

		chromaticNumberToUse -= 1;

		Color color;
		// We start finding a solution with our chromatic number obtained above
		// If the exact algorithm didn't find any solution we increment the chromatic
		// number, and try again
		do {
			color = Hint4.findRightColor(edges, n, assignedColors, targetVertex, chromaticNumberToUse++);
		} while (color == null && !addingColor);

		if (color != null) {
			// This ArrayList stores which colors we are going to use in the hint
			ArrayList<Color> colorsThatCanBeUsed = new ArrayList<Color>();
			colorsThatCanBeUsed.add(color); // Of course we can add the answer of our exact algorithm, in fact, this
											// color will always work

			// Now we find a random color to also show in our hint
			// First we start searching for an random color in the colors that are already
			// used
			// If there's no other color, we pick a random color from the combo box
			if (usedColors.size() > 1) {
				Color randomColor = null;
				do {
					int randomIndex = new Random().nextInt(usedColors.size());
					randomColor = theColors.get(usedColors.get(randomIndex));
				} while (colorsThatCanBeUsed.contains(randomColor));

				if (randomColor != null) {
					colorsThatCanBeUsed.add(randomColor);
				}
			} else if (theColors.size() > 1) {
				Color randomColor = null;
				do {
					int randomIndex = new Random().nextInt(theColors.size());
					randomColor = theColors.get(randomIndex);
				} while (colorsThatCanBeUsed.contains(randomColor));
				if (randomColor != null) {
					colorsThatCanBeUsed.add(randomColor);
				}
			}
			Stage hintStage = new Stage();
			hintStage.setTitle("Hint Alert: Color Highlighted Vertex");

			VBox alertVBox = new VBox(5);
			Label alertLabel = new Label("Assign one of these colors to vertex " + (targetVertex + 1) + "\n"
					+ " At least one of these colors wil lead to the minimum" + "\n"
					+ "amount of colors, according to your progress.");
			alertVBox.getChildren().addAll(alertLabel, Hint.createColorHBox(colorsThatCanBeUsed));

			Scene hintScene = new Scene(alertVBox, 300, 100);

			hintStage.setScene(hintScene);
			hintStage.initOwner(window);
			hintStage.show();
		}
	}

	/**
	 * @param addingColorBool If the user needs to add a color to the combo box this is true, otherwise false
	 */

	public static void setAddingColor(Boolean addingColorBool) {
		addingColor = addingColorBool;
	}

	/**
	 * This method tries for every color we are allowed to use if there's a solution.
	 * @param allEdges This is the ColEdge[] containing all the data of the edges.
	 * @param n This is the amount of vertices.
	 * @param array This is the assignedColors array representing the colors the user already assigned to the vertices.
	 * @param targetVertex This is the vertex we want to give a hint of.
	 * @param chromaticNumberToUse This is the amount of colors we are allowed to use to find a solution.
	 * @return Color This is the color that will lead to the chromatic number.
	 */

	public static Color findRightColor(ColEdge[] allEdges, int n, int[] array, int targetVertex,
			int chromaticNumberToUse) {
		int[] convertedAssignedColors = new int[n];

		options = new int[chromaticNumberToUse];
		for (int i = 1; i <= chromaticNumberToUse; i++) {
			options[i - 1] = i;
		}

		// This array stores what colors the user already used
		ArrayList<Integer> usedColors = new ArrayList<Integer>();
		for (int i = 0; i < array.length; i++) {
			if (!usedColors.contains(array[i]) && array[i] != -1) {
				usedColors.add(array[i]);
			}
		}
		Collections.sort(usedColors);

		// Because our exact algorithm finds a solution with the colors 1 till the
		// amount of options it has
		// we need to convert the already assigned colors to these numbers
		for (int j = 0; j < usedColors.size(); j++) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == usedColors.get(j)) {
					convertedAssignedColors[i] = j + 1;
				}
			}
		}

		// First we try if the exact algorithm can find a solution with the colors that
		// are already used
		// If the exact algorithm can't find a solution with the colors the user already
		// used
		// wee need to find which color, that isn't used yet, will work
		for (int i = 1; i <= usedColors.size(); i++) {
			Boolean possible = true;
			// First we check if the color is even possible because of adjacency
			for (int j = 0; j < allEdges.length; j++) {
				int firstVertex = allEdges[j].u - 1;
				int secondVertex = allEdges[j].v - 1;

				if (firstVertex == targetVertex) {
					if (convertedAssignedColors[secondVertex] == i) {
						possible = false;
						break;
					}
				}
				if (secondVertex == targetVertex) {
					if (convertedAssignedColors[firstVertex] == i) {
						possible = false;
						break;
					}
				}
			}
			// It is possible
			if (possible) {
				// We assign the color to the our target vertex in the array
				// which the exact algorithm will use to find a solution
				convertedAssignedColors[targetVertex] = i;
				assignedColors = Arrays.copyOf(convertedAssignedColors, convertedAssignedColors.length);
				// Call the exact algorithm to find a solution
				if (recurse(allEdges)) {
					return theColors.get(usedColors.get(i - 1)); // Return the color it found that will work
				}
			}
		}

		// We didn't find any color from the colors the user already used that will work
		// We loop trough colors the user didn't use already
		for (int i = usedColors.size(); i <= chromaticNumberToUse; i++) {
			convertedAssignedColors[targetVertex] = i; // Assign the color
			assignedColors = convertedAssignedColors;
			// Call the exact algorithm
			if (recurse(allEdges)) {
				Alert a = new Alert(AlertType.INFORMATION);
				a.initOwner(window);
				a.setContentText("Add a color to your color list," + "\n" + "and assign this color to vertex "
						+ (targetVertex + 1));
				a.show();
				setAddingColor(true);
				return null;
			}
		}
		// No color found that will work.
		return null;
	}

	/**
	 * This method is the same as {@link ChromaticNumberAlgorithm#recurse(ColEdge[])}.
	 * The only difference is that in this method 0 is used as default color (non color) is used instead of -1.
	 */
	public static boolean recurse(ColEdge[] e) {
		// Loop through all the edges
		for (int i = 0; i < e.length; i++) {
			// Check if vertexes.u does not have a color yet
			if (assignedColors[e[i].u - 1] == 0) {
				// loop through all the color options
				for (int c : options) {
					// check if we can assign the color to the vertex
					if (isPossible(e[i].u, c, e)) {
						// Assign color to vertex
						assignedColors[e[i].u - 1] = c;
						// recurse to solve another vertex
						if (!recurse(e)) {
							// If the recursion fails backtrack and uncolor the vertex
							assignedColors[e[i].u - 1] = 0;
						}
					}
				}
				// if there are vertices that are still not colored,
				// return false because there is no possible solution
				if (assignedColors[e[i].u - 1] == 0) {
					return false;
				}
			}
			// Does the same as previous if statement but for vertex.v
			if (assignedColors[e[i].v - 1] == 0) {
				for (int c : options) {
					if (isPossible(e[i].v, c, e)) {
						assignedColors[e[i].v - 1] = c;
						if (!recurse(e)) {
							assignedColors[e[i].v - 1] = 0;
						}
					}
				}
				if (assignedColors[e[i].v - 1] == 0) {
					return false;
				}
			}
		}
		// If we exit the for loop without returning false, then all vertices are
		// colored
		// And return true
		return true;
	}

	/** Method to check if it is possible to give a vertex the given color.
	* Iterate trough all the edges and check if any edge contains the vertex.
	* @return boolean returns false if the other vertex on that edge has the given color
	*/
	public static boolean isPossible(int vertex, int color, ColEdge[] e) {
		for (int i = 0; i < e.length; i++) {
			if (e[i].u == vertex) {
				if (assignedColors[e[i].v - 1] == color) {
					return false;
				}
			}
			if (e[i].v == vertex) {
				if (assignedColors[e[i].u - 1] == color) {
					return false;
				}
			}
		}
		// If no edge is found which makes this move impossible return true
		return true;
	}
}
