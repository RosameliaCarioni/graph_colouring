import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Hint3 extends Hint {
	/**Method Hint generates a simple hint based on the type of graph that the user is playing with, options include:
	* Bipartite, Wheel, Complete, Cycle.
	*/
	public static void Hint() {
		int n= Graph.getN();			//number of vertices
		ColEdge[] e= Graph.getEdges(); 		//edges
		int centerVertex= ChromaticNumberAlgorithm.checkForcenterVertex(e, n); //USED in wheel graph check
		//check for basic types of graphs from ChromaticNumberAlgorithm

		if (ChromaticNumberAlgorithm.completeGraphCheck(n, e))  {
			alertHint("You're dealing with a Complete Graph ;) ");
		}
		else if (ChromaticNumberAlgorithm.checkIfGraphIsCycle(e, n)) {
			alertHint("Oh hello!" + "\n" + "Have you noticed that this graph is a cyle? " + "\n" + "Knowing if its even or odd could help you out ;).");
			//chromatic can be 2=even cycle, or 3= odd cycle
		}
		else if (ChromaticNumberAlgorithm.bipartite(e, e.length, n)) {
			alertHint("Oh hello! Let me give you a hand: " + "\n" + "You're dealing with a Bipartite Graph.");
		}
		else if (centerVertex!=0) {
			if (ChromaticNumberAlgorithm.checkIfGraphIsWheel(e, n, centerVertex)) {
				alertHint("Oh hello! Let me give you a hand:" + "\n" + "You're dealing with wheel graph." + "\n" + "I can't tell you much more about it..."
						+ "\n" + "Just think on the number of vertices ;).");
				//Chromatic number can be:  4=  if wheel is even , 3= if wheel is odd
			}
		}
		}

	/**Method alerthHint: it generates the message that is shown to the user when it asks for a hint.
	 * @param message: is a String and contains the information to be displayed.
	 */
	public static void alertHint (String message){

		Stage hintStage = new Stage();
		hintStage.setTitle("Hint");

		VBox alertVBox = new VBox(5);
		Label alertLabel = new Label(message);
		alertVBox.getChildren().addAll(alertLabel);

		Scene hintScene = new Scene(alertVBox, 300, 100);

		hintStage.setScene(hintScene);
		hintStage.initOwner(window);
		hintStage.show();
	}

}
