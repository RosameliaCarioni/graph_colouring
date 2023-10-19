import java.util.ArrayList;

public class ChromaticNumberAlgorithm extends Thread {
	public static int[] assignedColors;
	public static int[] options;

	private static Boolean stopThread = false;

	/**
	* Method to stop the thread that is calculating the chromatic number.
	*/
	public static void stopThread() {
		stopThread = true;
	}

	public static int[] getRelation() {
		System.out.println("assigned colors" + assignedColors[1]);
		return assignedColors;
	}

	/**
	* Method to calculate the chromatic number with a thread, so it can be calculated while the game is played.
	*/
	public void run() {
		stopThread = false;
		Graph.setChromaticNumber(exactAlgorithm(Graph.getEdges(), Graph.getN(), Graph.getAmountOfEdges()));
		System.out.println("Exact: " + Graph.getChromaticNumber());
	}

	/**
	* Method that calculates the exact chromatic number.
	* @param e the array of edges
	* @param n the amount of vertices
	* @param totalNumberOfEdges the number of edges
	*/
	public static int exactAlgorithm(ColEdge[] e, int n, int totalNumberOfEdges) {
		// Array to store the isThereASolution value at assignedColors[i] refers to the
		// color of the vertex
		// this array will only be filled up if the graph is not one of the simple
		// graphs
		assignedColors = new int[n];
		for (int i = 0; i < assignedColors.length; i++) {
			assignedColors[i] = -1;
		}

		final int START_SEARCH_AT = 3;
		// First we perform all the checks for the simple graph algorithms.

		if (e.length == 0) {
			return 1;
		}

		// COMPLETE GRAPH
		if (completeGraphCheck(n, e)) {
			return n;
		}
		// BIPARTITE

		if (bipartite(e, totalNumberOfEdges, n)) {
			return 2;
		}

		int centerVertex = checkForcenterVertex(e, n);
		boolean graphIsEven = checkIfGraphIsEven(n);

		// WHEEL
		if (centerVertex != 0) {
			if (checkIfGraphIsWheel(e, n, centerVertex)) {
				if (graphIsEven) {
					return 4; // because its an even wheel
				} else {
					return 3; // because its an odd wheel
				}
			}
		}

		// CYCLE
		if (checkIfGraphIsCycle(e, n)) {
			if (graphIsEven) {
				return 2; // because its an even cycle
			} else {
				return 3; // because its an odd cycle
			}
		}

		// If the given graph is not one of the simple graphs, we calculate the
		// chromatic number recursively.
		else {
			int exactChromatic = runRecursion(e, n, START_SEARCH_AT);
			return exactChromatic;
		}
	}

	/**
	 * This method is the preparation method of the recursive method. It initializes the arrays with the right amount of colors, and returns the chromatic number if found.
	 * @param allEdges The ColEdge[] that stores all the edges information.
	 * @param n Number of vertices.
	 * @param startSearchAt This represents the lower bound answer. I.e. this is the minimum amount of colors we start trying to find a solution with.
	 * @return int This is the chromatic number.
	 */
	public static int runRecursion(ColEdge[] allEdges, int n, int startSearchAt) {

		// The number of colors to check, starting at the lower bound
		int numberOfOptions = startSearchAt;
		// used for tracking if a solution has been found
		boolean isThereASolution = false;
		while (!isThereASolution && !stopThread) {
			// create a new array of options, this will increase with each iteration
			options = new int[numberOfOptions];
			for (int i = 0; i < numberOfOptions; i++) {
				options[i] = i;
			}
			// call the recursive method to check if the graph can be solved with the given
			// number of options
			isThereASolution = recurse(allEdges);
			// increase number of options
			numberOfOptions += 1;
		}
		if (!stopThread) return numberOfOptions - 1;
		else return 0;

	}

	/**
	 * This method tries if with the given number of colors it is possible to color the graph.
	 * @param e The ColEdge[] that stores all the edges information.
	 * @return boolean True if this number of colors works, false if it doesn't work.
	 */
	public static boolean recurse(ColEdge[] e) {
		if (!stopThread) {
			//System.out.println("RUNNING");
			// Loop through all the edges
			for (int i = 0; i < e.length; i++) {
				// Check if vertexes.u does not have a color yet
				if (assignedColors[e[i].u - 1] == -1) {
					// loop through all the color options
					for (int c : options) {
						// check if we can assign the color to the vertex
						if (isPossible(e[i].u, c, e)) {
							// Assign color to vertex
							assignedColors[e[i].u - 1] = c;
							// recurse to solve another vertex
							if (!recurse(e)) {
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
						if (isPossible(e[i].v, c, e)) {
							assignedColors[e[i].v - 1] = c;
							if (!recurse(e)) {
								assignedColors[e[i].v - 1] = -1;
							}
						}
					}
					if (assignedColors[e[i].v - 1] == -1) {
						return false;
					}
				}
			}
			// If we exit the for loop without returning false, then all vertices are
			// colored
			// And return true
			return true;
		}
		return false;
	}

	/**
	 *  Method to check if it is possible to give a vertex the given color.
	 * @param vertex This is the vertex we want to check the color for.
	 * @param color This is the color we want to check.
	 * @param e The ColEdge[] that stores all the edges information.
	 * @return boolean True if it is possible, false if it's not possible
	 */
	// Iterate trough all the edges and check if any edge contains the vertex and
	// returns false if the other vertex on that edge has the the given color
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

	/**
	 *
	 * @param vertexCount Number of vertices
	 * @param edgesArray We check the length of the array to know the vertices
	 * @return Returns True, if a graph is complete
	 */
	public static boolean completeGraphCheck(int vertexCount, ColEdge[] edgesArray) {

		int edgeRequirement = (vertexCount * (vertexCount - 1) / 2);
		if (edgeRequirement == edgesArray.length) {
			return true;
		} else {
			return false;
		}
	}

	/** METHOD BIPARITE: returns true if method is Bipartite, false if not.
	* Idea: divide the vertices into 2 sets (set 1 and set2). All elements within the
	* same set are disconnected.
	* @param e: relationships of the vertices (ColEdge)
	* @param edges: total number of edges
	* @param n: vertices
	* @return boolean: true if is bipartite, false if not.
	*/
	public static boolean bipartite(ColEdge[] e, int edges, int n) {
		// New arrays 1 and 2 to store my Set 1 & 2
		// Array check keeps track of numbers that I need to check(asign to sets)
		ArrayList<Integer> set1 = new ArrayList<Integer>();
		ArrayList<Integer> set2 = new ArrayList<Integer>();
		ArrayList<Integer> check = new ArrayList<Integer>();

		// Stores the value of the vertice that we are currently cheecking.
		int vertexValue = 0;
		// disconnected vertices
		int disconnectedVertices = disconnected(e, n);

		// Check edges and place them either in set 1 or 2.
		// Returns false if vertice is already placed.
		while (set1.size() + set2.size() + disconnectedVertices < n) {
			// Add first element to check and place it into set1
			// In case the graph has independent set of vertices: One of the elements will
			// also be added at one of the sets.
			for (int i = 0; i < edges; i++) {
				if ((checkIfVertexIsIn(set1, e[i].u) == false) && (checkIfVertexIsIn(set1, e[i].v) == false)) {
					check.add(e[i].u);
					set1.add(e[i].u);
					break;
				}

			}
			while (check.size() > 0) {
				// take last element from check
				vertexValue = check.get(check.size() - 1);
				int vertexIndex = whichSet(set1, set2, vertexValue);
				// delete last element from check, to avoid overcounting
				check.remove(check.size() - 1);

				// Loop thru all edges
				// If an edge contains target vertice
				// We check if the other vertice is in the same set
				// If it is not in the same set we add the second vertice to the other set
				for (int i = 0; i < edges; i++) {

					// if element is in set 1
					if (vertexIndex == 1) {
						if (e[i].u == vertexValue) {
							// if I have the value in set 1 then add the other to set 2, and other way
							// around
							if (checkIfVertexIsIn(set1, e[i].v) == true) {
								return false;
							}

							if (checkIfVertexIsIn(set2, e[i].v) == false) {
								set2.add(e[i].v);
								check.add(e[i].v);

							}
						}
						if (e[i].v == vertexValue) {
							if (checkIfVertexIsIn(set1, e[i].u) == true) {
								return false;
							}
							if (checkIfVertexIsIn(set2, e[i].u) == false) {
								set2.add(e[i].u);
								check.add(e[i].u);
							}
						}
					}

					// if element is in set 2
					if (vertexIndex == 2) {
						if (e[i].u == vertexValue) {
							if (checkIfVertexIsIn(set2, e[i].v) == true) {
								return false;
							}
							if (checkIfVertexIsIn(set1, e[i].v) == false) {
								set1.add(e[i].v);
								check.add(e[i].v);
							}
						}
						if (e[i].v == vertexValue) {
							if (checkIfVertexIsIn(set2, e[i].u) == true) {
								return false;
							}
							if (checkIfVertexIsIn(set1, e[i].u) == false) {
								set1.add(e[i].u);
								check.add(e[i].u);
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**Method disconnected: checks which vertices are disconnected
	* @param e: relationships of the vertices (ColEdge)
	* @param n: vertices
	* @return counter: how many are disconnected
	*/
	public static int disconnected(ColEdge[] e, int n) {
		int counter = 0;
		// go thru vertices
		for (int j = 1; j <= n; j++) {
			// go thru edges
			for (int i = 0; i < e.length; i++) {
				// if vertice is in edge then break
				if (e[i].u == j || e[i].v == j) {
					break;
				}
				if (i == e.length - 1) {
					counter++;
				}
			}
		}
		return counter;
	}

	/**Method checkIfCertexIsIn: checks if element is already in array
	* @param array: to check
	* @param numberToCheck
	* @return boolean: true if it is, false if is not
	*/
	public static boolean checkIfVertexIsIn(ArrayList<Integer> array, int numberToCheck) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) == numberToCheck) {
				return true;
			}
		}
		return false;
	}

	/**Method whichSet: checks in which set a given vertex is
	* @param set1
	* @param set2
	* @param vertexValue: to check
	* @return int: 1 if vertex is in set1, 2 if it is in set 2
	*/
	public static int whichSet(ArrayList<Integer> set1, ArrayList<Integer> set2, int vertexValue) {
		for (int i = 0; i < set1.size(); i++) {
			if (set1.get(i) == vertexValue) {
				return 1;
			}
		}
		return 2;
	}

	/**
	 * This method checks if the graph is even or not.
	 * @param n The number of vertices
	 * @return boolean True if it's even, false if it's odd.
	 */
	public static boolean checkIfGraphIsEven(int n) {
		if (n % 2 == 0) {
			return true;
		} else
			return false;
	}

	/**
	 * This method checks if the graph is a cycle or not.
	 * @param e The ColEdge[] that stores all the edges information.
	 * @param n The number of vertices.
	 * @return boolean Returns true if it's a cycle, false if it's not a cycle.
	 */
	public static boolean checkIfGraphIsCycle(ColEdge[] e, int n) {
		int rightAmountOfVertices = 0; // The vertices that have 2 connected edge

		for (int i = 1; i <= n; i++) // For each vertex
		{
			int amountOfVertices = 0; // Amount of vertices connected to i

			for (int j = 0; j < e.length; j++) // For each edge
			{
				int firstValueOfEdge = e[j].u;
				int secondValueOfEdge = e[j].v;

				if (i == firstValueOfEdge) // If the first value of an edge = i, we want to add + 1 to the
											// amountOfVertices, because the second vertex is connected to i
				{
					amountOfVertices += 1;
				} else if (i == secondValueOfEdge) // If the second value of an edge = i, we want to add + 1 to the
													// amountOfVertices, because the first vertex connected to i
				{
					amountOfVertices += 1;
				}
			}

			if (amountOfVertices == 2) // There are two vertices connected to i. Add + 1 to the rightAmountOfVertices.
			{
				rightAmountOfVertices += 1;
			} else
				return false;
		}

		if (rightAmountOfVertices == n) // So the amount of vertices that have 2 other vertices connected to it needs to
										// be the same as the total amount of vertices; every vertex has 2 other
										// vertices connected to it
		{
			return true;
		} else
			return false;
	}
	/**
	 *  We only want to check if the graph, excluded the center vertex, is a cycle,
	 *  because a cycle + a center vertex is a wheel.
	 * @param e The ColEdge[] that stores all the edges information.
	 * @param n The number of vertices.
	 * @param centerVertex The center vertex.
	 * @return boolean True if it's a wheel, false if it's not.
	 */
	public static boolean checkIfGraphIsWheel(ColEdge[] e, int n, int centerVertex) {
		int rightAmountOfVertices = 0;

		for (int i = 1; i <= n; i++) {
			if (i != centerVertex) // Don't check the centerVertex, because in case if it's a wheel it has n - 1
									// vertices connected to it, and not 2
			{
				int amountOfVertices = 0;

				for (int j = 0; j < e.length; j++) {
					int firstValueOfEdge = e[j].u;
					int secondValueOfEdge = e[j].v;

					if (i == firstValueOfEdge && secondValueOfEdge != centerVertex) // Don't check if i is connected to
																					// the center vertex, because we
																					// already know it is
					{
						amountOfVertices += 1;
					} else if (i == secondValueOfEdge && firstValueOfEdge != centerVertex) // Don't check if i is
																							// connected to the center
																							// vertex, because we
																							// already know it is
					{
						amountOfVertices += 1;
					}
				}
				if (amountOfVertices == 2) {
					rightAmountOfVertices += 1;
				} else
					return false;
			}
		}

		if (rightAmountOfVertices == n - 1) // The amount of vertices that have 2 other vertices connected to it is not
											// the same as the total vertices, because the wheel is excluded: So n - 1
		{
			return true;
		} else
			return false;
	}
	/**
	 * This method checks if there's a vertex that's connected to every other vertex of the graph (center vertex).
	 * @param e The ColEdge[] that stores all the edges information.
	 * @param n The amount of vertices.
	 * @return int This is the center vertex if applicable, else it's 0.
	 */
	public static int checkForcenterVertex(ColEdge[] e, int n) {
		int midVertex = 0;

		for (int i = 1; i <= n; i++) // Check for every vertex
		{
			int amountOfVertices = 0;

			for (int j = 0; j < e.length; j++) // Check for every edge
			{
				int firstValueOfEdge = e[j].u;
				int secondValueOfEdge = e[j].v;

				if (i == firstValueOfEdge || i == secondValueOfEdge) // Check if a vertex is represented in the edge
																		// array. If so, add + 1 to the amountOfVertices
				{
					amountOfVertices += 1;
				}
			}

			if (amountOfVertices == n - 1) // If i is connected to all other vertices, it is connected to n - 1 vertices
			{
				midVertex = i; // i is the center vertex
				return midVertex;
			}
		}

		return 0;
	}

}
