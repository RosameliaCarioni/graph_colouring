import java.util.ArrayList;

public class UpperBoundAlgorithm extends Thread {

	/**
	* Method that calculates the upperbound while the game is running.
	*/
	public void run() {
		Graph.setUpperBound(calculateUpperBound(Graph.getEdges(), Graph.getN()));
		System.out.println("Upper bound: " + Graph.getUpperBound());
	}

	/**
	* Method to claculate the upperBound of a graph.
	* @param edges the array containing all edges
	* @param vertices the amount of vertices
	*/
	public static int calculateUpperBound (ColEdge[] edges, int vertices) {
		//We create an array that can store the colors for every vertex.
		int[] colors = new int[vertices];
		//We create a 2 dimensional array to store information about adjacing vertices.
		int[][] adjacencyMatrix = createAdjacencyMatrix(vertices, edges);

		int upperBound = maximumNumberOfArray(colorFill(0, vertices, colors, adjacencyMatrix));
		return upperBound;
	}

	/**
	* Method that returns the number of colors used.
	* @param colors the array of colors used to color the graph
	* @return the number of colors used in the solution
	*/
	public static int maximumNumberOfArray(int[] colors) {
		int max = 0;
		for (int i = 0; i < colors.length; i++) {
			if (colors[i] > max) {
				max = colors[i];
			}
		}
		return max;
	}

	/**
	* Method that checks if the color we want to color the vertex with is already used by one of its adjacent vertices.
	* @param adjacencyMatrix the matrix containing all information about adjacent vertices
	* @param vertexCount the amount of vertices
	* @param node, the number of the current vertex
	* @param colors, the array where we store all the colors of the vertices
	*/
	public static ArrayList<Integer> findColors(int[][] adjacencyMatrix, int vertexCount, int node, int[] colors) {
		ArrayList<Integer> cols = new ArrayList<Integer>();
		//For all vertices that are adjacent to node, if the colorvalue is not in the list yet, we add it.
		for (int i = 0; i < vertexCount; i++) {
			if (adjacencyMatrix[node][i] == 1) {
				if (!cols.contains(colors[i])) {
					cols.add(colors[i]);
				}
			}
		}
		//Once we added all the colors to the list, we return it.
		return cols;
	}

	/**
	* Method that assigns a color to all vertices and performs checks to see if adjacent vertices have different colors.
	* @param node number of the current vertex
	* @param vertexCount the amount of vertices
	* @param colors the array containing all colorvalues.
	* @param adjacencyMatrix matrix containing all information about adjacent vertices
	* @return the array containing all colorvalues
	*/
	public static int[] colorFill(int node, int vertexCount, int[] colors, int[][] adjacencyMatrix) {
		//Check if we reached the last vertex.
		if (node < vertexCount) {
			//We set the color of the node to color1.
			colors[node] = 1;
			//While we have not found a color for node, we keep increasing it.
			while (findColors(adjacencyMatrix, vertexCount, node, colors).contains(colors[node])) {
				colors[node]++;
			}
			//When we found a color such that all adjacent vertices of node have a different color, we move on to the next vertex.
			colorFill(node+1, vertexCount, colors, adjacencyMatrix);
		}
		//When we have checked for all vertices, we return the array containing the colorvalues for all vertices.
		return colors;
	}

	//Method that creates an adjacencyMatrix given an array of all edges.
	/**
	* Method that creates an adjacencyMatrix given an array of all edges.
	* @param n the amount of vertices
	* @param edges the array containing all edges
	* @return the adjacencyMatrix
	*/
	public static int[][] createAdjacencyMatrix(int n, ColEdge edges[]) {
		//We create a new 2-dimensional array of size n, where n is the amount of vertices.
		int adjacencyMatrix[][] = new int[n][n];
		//For all vertices on both 'sides' of the edges, we set the corresponding value in the adjacencyMatrix to 1.
		for (int i = 0; i < edges.length; i++) {
			adjacencyMatrix[edges[i].u-1][edges[i].v-1] = 1;
			adjacencyMatrix[edges[i].v-1][edges[i].u-1] = 1;
		}
		return adjacencyMatrix;
	}
}
