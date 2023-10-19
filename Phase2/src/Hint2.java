import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Hint2 extends Hint {
	static int[] assignedColorsVertices2;

	/**Method Hint generates a type of hint, based on the status of the game.
	 * There are 3 types of hints or alert messages that can be generated.
	 */
	public static void Hint() {
		/*First type of hint: check chromatic number and tell user if it has been reached,
		for instance if he/she are ussing Grapmore colours than needed*/
		if (Graph.getChromaticNumber() < totalColorsUsed()) {
			alertHint("You're using more colors than needed!" + "\n" +"Try again!");
			return;
		}
		else {
			/*Because it is possible for the user to use colours in any order or to add new colours to the comboBox
			*and not use them, we need to go trough the colors used and change the value of them in the array AssignedColorsVertices
			*call clean method*/
			assignedColorsVertices2 = clean(assignedColorsVertices);

			/*Second type of hint: give the user an advice of what vertice to colour and in which colour.
			*This will always change, because the decisions of the user will lead to different possible solutions.
			*To have dynamic hints we need to recalculate the colours to use based on relation of vertices by using our code from phase 1,
			*now it will calculate it faster because we already know the chromatic number and assignedColorsVertices2 is already a bit filled by the user.
			Note that: if the decisions of the user do not allow him/her to get to the chromatic number, this will be informed.*/
			int[] relationOfVertices = runRecursion(Graph.getEdges(), assignedColorsVertices2, Graph.getChromaticNumber());

			for (int i = 0; i < assignedColorsVertices2.length; i++) {
				//assignedColorsVertices would be -1 if a colour hasn't been assigned to the vertice
				if (assignedColorsVertices2[i] != -1) {
					int assignedColor = assignedColorsVertices2[i];
					int relationColor = relationOfVertices[i];

					//in the following for loops we are checking if the decisions made by the user are viable and
					//if it could take him/her to the right cromatic number.
					for (int j = 0; j < assignedColorsVertices2.length; j++) {
						if ((assignedColor == assignedColorsVertices2[j]) && (relationColor != relationOfVertices[j])) {
							//Tell the user that assigned the wrong color to vertex
							//i and j shouldn't have the same color
							alertHint("Oh no! It seems like you assigned"+ "\n" +"the wrong color to the vertices " + (i + 1)
									+ " and " + (j + 1) + "! " + "\n" + "They shouln't be the same color.");
							return;
						}
					}
					for (int j = 0; j < assignedColorsVertices2.length; j++) {
						if ((relationColor == relationOfVertices[j]) && (assignedColor != assignedColorsVertices2[j])
								&& (assignedColorsVertices2[j] != -1)) {
							//Tell the user that assigned the wrong color to vertex
							//i and j sould have the same color
							alertHint("Oh no! It seems like you assigned"+"\n" + "the wrong color to the vertices " + (i + 1)
									+ " and " + (j + 1) + "! " + "\n" + "They should have the same color.");
							return;
						}
					}
					for (int j = 0; j < assignedColorsVertices2.length; j++) {
						if ((relationColor == relationOfVertices[j]) && (assignedColorsVertices2[j] == -1)) {
							//Tell user to color j with the same colour as i
							alertHint("Try coloring vertice number " + (j + 1) + " with the same"+ "\n" +"color as vertice "
									+ (i + 1) + "! " + "\n" + " I hope it helps!");
							return;
						}
					}
				}
			}
		}
		/*If the program gets this far, without going in any of the if statements and therefore stopping (because of the return statements)
		*There are 2 options:
		*1. User needs to add a new color because has already coulored all possible vertices that can take one colour with current colours.
		*2. It painted disconnected vertices and should try to paint more with the same color.
		In either case we call Hint method from Hint1 class. */
		Hint1.Hint();
	}

	/**Method totalColorsUsed: it gives how many colours have been used in the game.
	 * @return totalColorsUsed: (type int)
	 */
	public static int totalColorsUsed() {
		ArrayList<Integer> totalColorsUsed = new ArrayList<Integer>();

		for (int i = 0; i < assignedColorsVertices.length; i++) {
			if (assignedColorsVertices[i] != -1) {
				if (!hasThisColorAlreadyBeenChecked(assignedColorsVertices[i], totalColorsUsed)) {
					totalColorsUsed.add(assignedColorsVertices[i]); // you dont add it
				}
			}

		}
		return totalColorsUsed.size();
	}

	/**Method hasThisColorAlreadyBeenChecked: it checks a number is in an array. In our case:
	 * Number represent color and array colours used.
	 * @param color
	 * @param array: type ArrayList.
	 * @return boolean: true or false.
	 */
	public static boolean hasThisColorAlreadyBeenChecked(int color, ArrayList<Integer> array) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) == color) {
				return true;
			}
		}
		return false;
	}

	/**Method alerthHint: it generates the message that is shown to the user when it asks for a hint.
	 * @param message: is a String and contains the information to be displayed.
	 */
	public static void alertHint(String message) {
		Stage hintStage = new Stage();
		hintStage.setTitle("Hint");
		//creating alert box
		VBox alertVBox = new VBox(5);
		Label alertLabel = new Label(message);
		alertVBox.getChildren().addAll(alertLabel);

		Scene hintScene = new Scene(alertVBox, 300, 100);

		hintStage.setScene(hintScene);
		hintStage.initOwner(window);
		hintStage.show();
	}

	/**Method clean: it goes trough array and changes the values (ints) of the numbers to
	 * be the smallest possible, starting from 0.
	 * @param arrayToClean: array that you want to change its values.
	 * @return newArray: new array with smaller values.
	 */
	public static int[] clean(int[] arrayToClean) {
		int[] newArray = arrayToClean.clone();
		int max = newArray[0];
		int missingNumber = -1;
		//Get missing number
		for (int i = 0; i < max; i++) {
			if (!numberIsInArray(i, newArray)) {
				missingNumber = i;
			}
		}
		//Missing number will be -1 when all of the other higher values have been replace by the smallest possible.
		while (missingNumber != -1) {
			// Getting max number in array
			max = newArray[0];
			for (int i = 0; i < newArray.length; i++) {
				if (newArray[i] > max) {
					max = newArray[i];
				}
			}
			// Change the current max in array
			for (int i = 0; i < arrayToClean.length; i++) {
				if (newArray[i] == max) {
					newArray[i] = missingNumber;
				}
			}
			max = newArray[0];
			for (int i = 0; i < newArray.length; i++) {
				if (newArray[i] > max) {
					max = newArray[i];
				}
			}
			// get new missing
			missingNumber = -1;
			for (int i = 0; i < max; i++) {
				if (!numberIsInArray(i, newArray)) {
					missingNumber = i;
				}
			}
		}
		return newArray;
	}

	/**Method numberIsInArray: checks if a number is in an array.
	 * @param number
	 * @param array
	 * @return boolean: true or false
	 */
	public static boolean numberIsInArray(int number, int[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == number) {
				return true;
			}
		}
		return false;
	}

	/**Method runRecursion: slightly edited method from phase 1.
	 * It takes the parameters/information of the game at a certain point and it generates a possible solution, giving the possible colours
	 * for the rest of the vertices that haven't been colored. Note that it uses the chromatic number found earlier, and because of this, if it is
	 * not possible to stick to the chromatic number with the colors/relations already assigned by the user, it returns a correct answer ignoring
	 * the users desicions.
	 * @param allEdges: type ColEdge[]
	 * @param userHalfFilled: array of integers that contains infroamtion about colored vertices.
	 * @param chromaticNumber
	 * @return assignedColors: an array with a possible solution for the rest of the vertices that dont have a color.
	 */
	public static int[] runRecursion(ColEdge[] allEdges, int[] userHalfFilled, int chromaticNumber) {
		int[] assignedColors = userHalfFilled.clone();

		// used for tracking if a solution has been found
		boolean isThereASolution = false;
		// create a new array of options, this will increase with each iteration
		int[] options = new int[chromaticNumber];
		for (int i = 0; i < chromaticNumber; i++) {
			options[i] = i;
			System.out.println("options " + Arrays.toString(options));
		}
		// call the recursive method to check if the graph can be solved with the given
		// number of options
		isThereASolution = recurse(allEdges, assignedColors, options);
		if (isThereASolution == true) {

			// loop thru assignedCOlors and check if any is -1; if so: vertice is disconnected
			// give color 1 if thats the case (any other color will work)
			for (int i = 0; i < assignedColors.length; i++) {
				if (assignedColors[i] == -1) {
					assignedColors[i] = 1;
				}
			}
			return assignedColors;
		} else {
			//if its not possible to solve the game with the user's choices, then calculate the relations of the vertices using the Chromatic Number class.
			return ChromaticNumberAlgorithm.getRelation();
		}
	}

/**Method recurse: it recursively finds a solution to the graph using the options in the options array
 * and returns the solution in assigned colors.
 * @param e: edges
 * @param assignedColors
 * @param options
 * @return
 */
	public static boolean recurse(ColEdge[] e, int[] assignedColors, int[] options) {
		// Loop through all the edges
		for (int i = 0; i < e.length; i++) {
			// Check if vertexes.u does not have a color yet
			if (assignedColors[e[i].u - 1] == -1) {
				// loop through all the color options
				for (int c : options) {
					// check if we can assign the color to the vertex
					if (isPossible(e[i].u, c, e, assignedColors)) {
						// Assign color to vertex
						assignedColors[e[i].u - 1] = c;
						// recurse to solve another vertex
						if (!recurse(e, assignedColors, options)) {
							// If the recursion fails backtrack and uncolor the vertex
							assignedColors[e[i].u - 1] = -1;
						}
					}
				}
				// if there are vertices that are still not colored,
				// return false because there is no possible solution
				if (assignedColors[e[i].u - 1] == -1) {
					return false;
				}
			}
			// Does the same as previous if statement but for vertex.v
			if (assignedColors[e[i].v - 1] == -1) {
				for (int c : options) {
					if (isPossible(e[i].v, c, e, assignedColors)) {
						assignedColors[e[i].v - 1] = c;
						if (!recurse(e, assignedColors, options)) {
							assignedColors[e[i].v - 1] = -1;
						}
					}
				}
				if (assignedColors[e[i].v - 1] == -1) {
					return false;
				}
			}
		}
		// If we exit the for loop without returning false, then all vertices are colored and return true
		return true;
	}

	/**Method isPossible: checks if it is possible to give to a vertex a specific color
	 * Iterates trough all the edges and checks if any edge contains the vertex and
	 * returns false if the other vertex on that edge has the the given color.
	 * @param vertex
	 * @param color
	 * @param e: edges
	 * @param assignedColors
	 * @return boolean: true or false
	 */
	public static boolean isPossible(int vertex, int color, ColEdge[] e, int[] assignedColors) {
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
