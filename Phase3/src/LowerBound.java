import java.util.ArrayList;

public class LowerBound {


	int[][] A; //Adjacency matrix - 0 if there is no edge between two vertices, else 1
	int n; //number vertices
	int maxClique; //size of the max clique

	boolean decomposed;
	Graph graph;

	/**
	 * Class Constructor
	 * @param n          number of vertices of the graph
	 * @param e          Array of edges of the graph
	 * @param decomposed true if the graph has been decomposed, else false
	 * @param graph
	 */
	public LowerBound(int n, ColEdge[] e, boolean decomposed, Graph graph) {
		this.n = n;
		this.A = creatingMatrix(n, e);
		this.decomposed = decomposed;
		this.graph = graph;
	}

	/**
	 * This method is called from Graph.java
	 * Creates two sets (ArrayLists). "currentClique" will contain the current clique found, thus continuously changes. This set is initially empty
	 * "allOtherVertices" only contains all of the vertices of the graph, from 0 to n-1, where 0 is vertex 1.
	 * @return the size of the largest clique found.
	 */
	public int calculateLowerBound() {

		maxClique = 0;

		ArrayList<Integer> currentClique = new ArrayList<Integer>(); //Current clique - initially all 0s
		ArrayList<Integer> allOtherVertices = new ArrayList<Integer>(); //Candidate set - initially contains all vertices
		//Adding all vertices to allOtherVertices set
		for(int i = 0; i < n; i++) {
			allOtherVertices.add(i);
		}
		//Calling method to expand the clique
		expandClique(currentClique, allOtherVertices);

		return maxClique;

	}

/**
 * This method continuously checks for the largest clique in the graph
 * @param currentClique The clique to expand
 * @param V             all of the "Candidate" vertices of the graph
 */
	public void expandClique(ArrayList<Integer> currentClique, ArrayList<Integer> V) {

		//Iterating through every vertex of the graph, starting from the last one, to the first one. (this is to avoid issues with the arraylist remove() method)
		for(int i = V.size()-1; i >= 0; i--) {

			/**
			 * This is PRUNING
			 * This if statement is able to tell a Candidate vertex is worth searching. This is done by
			 * summing the size of the set containing the current cantidate vertices with the size of the clique that these
			 * have created, and comparing it with the size of the largest clique found and saved until now.
			 * If this sum is smaller than the maxClique, this branch will be abandonded, or "pruned", and the next vertex of the Graph
			 * (and its neighbours) will be searched.
			 */
			if(currentClique.size() + V.size() <= maxClique) {
				return;
			}

			//Add the first vertex in the current candidate set to the clique set.
			int v = V.get(i);
			currentClique.add(v);

			/**
			 * Create a new candidate set (newV) with all of the adjacent vertices, in V, of
			 * the vertex that has been added to the clique set.
			 */
			ArrayList<Integer> newV = new ArrayList<Integer>();
			for(int w : V) {
				//This if statement will look for all of the adjacent vertices, using the adjacency Matrix
				if(A[v][w] == 1){
						newV.add(w);
				}
			}
			/**
			 * If all of the adjacent vertices of the initial candidate vertex have been checked and added to the clique
			 * We check the size of the created clique, and if it results greater than the current maximum clique found
			 * we will save this solution by calling the method: saveSolution(currentClique).
			 * Else, if vertices still have to be checked, we will do a recursive call, to continue checking if there are any other
			 * adjacent vertices that can be added to the clique.
			 */
			if((newV.size() == 0) && (currentClique.size() > maxClique)) {
				saveSolution(currentClique);
			}else if(!(newV.size() == 0)) {
				expandClique(currentClique, newV); //RECURSIVE CALL
			}
			/**
			 * After having searched the "original" candidate vertex from the graph, we will remove it from the set
			 */
			currentClique.remove((Integer) v);
			V.remove((Integer) v);
		}
	}

	/**
	 * Saving the solution: replacing the current largest clique, with a bigger one found.
	 * If the lower bound reaches the found upper bound then the search
	 * will stop and the chromatic number will also have been found
	 * @param currentClique A larger clique than the one previously saved
	 */
	public void saveSolution(ArrayList<Integer> currentClique) {

		maxClique = currentClique.size();
		if (!decomposed)
		{
			System.out.println("NEW BEST LOWER BOUND = "+maxClique);

			if (graph.accessUpperBound() != 0 && graph.accessUpperBound() == maxClique)
			{
				System.out.println("CHROMATIC NUMBER = "+maxClique);
				System.exit(0);
			}
		}
	}

	/**
	 * This method creates an adjacency matrix for the graph passed, when a LowerBound object has been created
	 * @param  n number of vertices
	 * @param  e Array of edges
	 * @return   a two-dimensional integer array- the adjacency matrix.
	 */
	public int[][] creatingMatrix(int n, ColEdge[] e){

		int[][]adjacencyMatrix = new int[n][n];

		int j = 0;
		int k = 0;
		for(int i = 1; i <= n; i ++) {
			for(int y = 0; y < e.length; y++){
				if(e[y].u == i){
					k = e[y].u;
					j = e[y].v;
					adjacencyMatrix[k-1][j-1] = 1;
					adjacencyMatrix[j-1][k-1] = 1;
				}
			}
		}
		return adjacencyMatrix;
	}
}
