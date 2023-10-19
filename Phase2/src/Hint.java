import java.util.ArrayList;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Hint extends DisplayGraph {
	//Hint button is disabled until a vertex is colored or if all are colored.

	/**Method Hints: it decides which hint to generate, based on:
	* which game mode is been played, how many hints have the user already selected and if in the chromatic number is calculated or not
	*/
	public static void Hints() {
		if (gameModeChosen == 1) {
			if (Graph.getChromaticNumber() != 0) {
				// the graph could be an easy one (trivial): call Hint3
				if (checkIfArrayIsEmpty(ChromaticNumberAlgorithm.getRelation()) && totalHints == 0) {
					Hint3.Hint();
				}
				// if the graph is not a simple one, call hint 2
				else {
					Hint2.Hint();
				}
			}
			// if the chromatic number hasn't been calculated, call hint 1
			else {
				Hint1.Hint();
			}
		} else if (gameModeChosen == 2) {
			//if chromatic number has been calculated and the no hints have been selected then:
			if (Graph.getChromaticNumber() != 0 && totalHints == 0 && checkIfArrayIsEmpty(ChromaticNumberAlgorithm.getRelation())) {
				Hint3.Hint();
			} else {
				Hint1.Hint();
			}
		} else if (gameModeChosen == 3) {
			//if chromatic number has been calculated
			if (Graph.getChromaticNumber() != 0) {
				if (totalHints == 0 && checkIfArrayIsEmpty(ChromaticNumberAlgorithm.getRelation())) {
					Hint3.Hint();
				} else {
					Hint4.Hint();
				}
			} else {
				Hint1.Hint();
			}
		}
		totalHints++;
	}

	/**
	 * Creating an HBox with colored rectangles to show the user which available colors can be used to color the selected vertex (from the hint)
	 * @param colorsThatCanBeUsed ArrayList with the available colors (that have already been added to the comboBox)
	 * @return An HBox containing the rectangles colored with the colors that can be used
	 * by the user for the selected vertex for which the hint is being based on.
	 */
	public static HBox createColorHBox(ArrayList<Color> colorsThatCanBeUsed) {
		//Rectangles spaced by 5 pixels
		HBox colorHBox = new HBox(5);
		//Creating and coloring as many rectangles as the available colors for the vertex
		for (int i = 0; i < colorsThatCanBeUsed.size(); i++) {
			Rectangle rectangle = new Rectangle(50, 40);
			rectangle.setFill(colorsThatCanBeUsed.get(i));
			//Adding rectangle to the HBox that will be returned
			colorHBox.getChildren().add(rectangle);
		}
		return colorHBox;
	}

	/**Method checkIfArrayIsEmpty: checks if an array is empty.
	* @return boolean: true if it is, false if its not.
	*/
	public static boolean checkIfArrayIsEmpty(int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != -1) {
				return false;
			}
		}
		return true;
	}
}
