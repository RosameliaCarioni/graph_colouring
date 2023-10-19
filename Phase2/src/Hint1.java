import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Hint1 extends Hint {

	/**
	 * Creates the first type of hint, suitable for each game mode. It is a generic hint that
	 * is mainly used for more complex graphs, when the chromatic number has not yet been calculated.
	 * The hint takes into account a vertex that still has to be colored, checks its adjacent vertices (and their corresponding assigned
	 * colors), and checks if the vertex can be colored (respecting the rules) with a color/s that is already in the comboBox, or
	 * if another color has to be added. Note: Game mode 3 is handled slightly differently, because instead of
	 * taking into account a random vertex that sill has to be colored, the vertex chosen is the vertex that the game
	 * wants the user to color.
	 */
	public static void Hint() {
		int positionOfHint = 0;//Variable that will change based one of the uncolored vertices from the array: assignedColorsVertices
		ArrayList<Integer> colorsUsed = new ArrayList<Integer>();

		if (gameModeChosen != 3) {
			for (int i = 0; i < assignedColorsVertices.length; i++) {
				if (assignedColorsVertices[i] == -1) { //-1 corresponds to the default color black, thus the vertex still has to be colored
					positionOfHint = i;
					System.out.println(positionOfHint);
					break;
				}
			}
		} else
			positionOfHint = GM3DisplayGraph.vertexColorOrder.get(GM3DisplayGraph.currentVertexToColor);

		//Checking the colors assigned to the vertex/vertices (if existent) adjacent to the vertex that the hint is basing on.
		//Looping through all of the edges of the selected vertex.
		for (int j = 0; j < edges.length; j++) {
			int firstVertex = edges[j].u - 1;
			int secondVertex = edges[j].v - 1;
			Circle circleOfHint;
			//Adding the used (and adjacent) colors to the arrayList colorsUsed
			if (positionOfHint == firstVertex) {
				circleOfHint = circlesDisplayed.get(firstVertex);
				if (assignedColorsVertices[secondVertex] != -1) {
					colorsUsed.add(assignedColorsVertices[secondVertex]);
				}

			}
			if (positionOfHint == secondVertex) {
				circleOfHint = circlesDisplayed.get(secondVertex);
				if (assignedColorsVertices[firstVertex] != -1) {
					colorsUsed.add(assignedColorsVertices[firstVertex]);
				}
			}
		}
		//Now compare the colors that have been added to comboBox with the colors that have already been used by the adjacent vertices of the selected vertex
		checkColorsComboBox(colorsUsed, positionOfHint);
	}

	/**
	 * Displays an message with either the colors added in the comboBox that can be used by the selected vertex
	 * Or if a new color has to be added to the comboBox in order to continue
	 * @param array : arrayList of colors used by the adjacent vertices of the "hint vertex"
	 * @param positionOfHint: The vertex that the hint is being based on
	 */
	public static void checkColorsComboBox(ArrayList<Integer> array, int positionOfHint) {

		// Put colors of the comboBox in an arrayList
		ArrayList<Color> currentComboBoxColors = new ArrayList<Color>();

		for (int j = 0; j < theColors.size(); j++) {
			currentComboBoxColors.add(theColors.get(j));
		}
		//DEBUGGING
		System.out.println(currentComboBoxColors.toString());
		System.out.println(array.toString());

		/*
		 * Remove colors that can't be used: i.e colors in arrayList colorsUsed (here
		 * called array). To remove the color from the arraylist (avoiding index problems), replace the color to black
		 * If the color in the currentComboBoxColors arrayList is black, than it means that it can't be used by the vertex
		 * of the hint
		 */
		for (int i = 0; i < array.size(); i++) {
			int index = array.get(i);
			currentComboBoxColors.set(index, Color.BLACK);
		}
		//Loop through all of the colors in the currentComboBoxColors to see which colors can be used
		ArrayList<Color> colorsThatCanBeUsed = new ArrayList<Color>();

		for (int i = 0; i < currentComboBoxColors.size(); i++) {
			if (currentComboBoxColors.get(i) != Color.BLACK) {
				colorsThatCanBeUsed.add(currentComboBoxColors.get(i));
			}
		}
		/*
		 * Different alerts to display:
		 */
		//If the arraylist ended up being empty, there are no more colors to use, so add a color to the comboBox
		if (colorsThatCanBeUsed.size() == 0) {
			Alert addColor = new Alert(AlertType.INFORMATION);
			addColor.initOwner(window);
			addColor.setContentText("Add a color to ComboBox");
			addColor.show();
		} else {
			Stage hintStage = new Stage();
			hintStage.setTitle("Hint Alert: Color Highlighted Vertex");

			VBox alertVBox = new VBox(5);
			Label alertLabel;
			if (Graph.getChromaticNumber() == 0) {
				alertLabel = new Label("Vertex " + (positionOfHint + 1)
						+ " could be colored with these colors."+ "\n" +"But does any of these color lead to"+ "\n" +"the minimum amount of colors?");
			} else {
				alertLabel = new Label("Vertex " + (positionOfHint + 1) + " can be colored with these colors:");
			}
			//Creating an HBox that will display the colors that can be used for the selected vertex
			alertVBox.getChildren().addAll(alertLabel, Hint.createColorHBox(colorsThatCanBeUsed));

			Scene hintScene = new Scene(alertVBox, 300, 100);

			hintStage.setScene(hintScene);
			hintStage.initOwner(window);
			hintStage.show();

		}
	}
}
