public class Graph {
	//an object graph has a number of vertices, edges, chromatic number and upper Bound
	//String file is used to generate the graph
	private static int n;
	public static ColEdge edges[];
	public static int chromaticNumber;
	public static int upperBound;
	private String file;

	static ChromaticNumberAlgorithm exactAlgorithm;
	static UpperBoundAlgorithm upperBoundAlgorithm;

		/**
		* Constructor method for a graph object.
		* @param filePath the filepath to the file where the graphinformation is stored
		*/
    public Graph(String filePath) {
    	file = filePath;
    }

		/**
		* Method that calculates the chromatic numbers for a graph.
		* @param vertices the amount of vertices
		* @param e the array of all edges
		*/
    public static void passingReadGraph(int vertices, ColEdge[] e) {
    	n = vertices;
    	edges = e;

    	//calculate exact and upper Bound -> calling methods from class ChromaticNumberCalc
		upperBoundAlgorithm = new UpperBoundAlgorithm();
		upperBoundAlgorithm.start();

    	exactAlgorithm = new ChromaticNumberAlgorithm();
    	exactAlgorithm.start();
    }

	/**
	* Constructor method for a graph object if the user wants to generate a random graph.
	* @param vertices, the amount of vertices
	* @param totalNumberOfEdges the total number of edges
	*/
	public Graph(int vertices, int totalNumberOfEdges)
	{
		int[][] matrix = GenerateRandomGraph.generateRandomGraph(vertices, totalNumberOfEdges);
		changeToColEdge(matrix);
		n = vertices;

		//calculate exact and upper Bound -> calling methods from class ChromaticNumberCalc
		upperBoundAlgorithm = new UpperBoundAlgorithm();
		upperBoundAlgorithm.start();

		exactAlgorithm = new ChromaticNumberAlgorithm();
		exactAlgorithm.start();
	}

	/**
	* Method that changes a graph stored in a 2-dimensional array so it can be stored in a ColEdge array.
	* @param graph the 2-dimensional array containing the graph
	*/
	public static void changeToColEdge(int[][] graph) {
        edges = new ColEdge[graph.length];

        int row = 0;

        for (int i = 0; i < edges.length; i++)
        {
        	edges[i] = new ColEdge();
        	edges[i].u = graph[row][0];
        	edges[i].v = graph[row][1];

        	row++;
        }
    }

	/**
	* Mtehod to set the upperbound of a graph.
	* @param upperbound the desired upperbound
	*/
	public static void setUpperBound(int upperBound)
	{
		Graph.upperBound = upperBound;
	}

	/**
	* Method to get the upperbound of a graph.
	* @return the upperbound
	*/
	public static int getUpperBound()
	{
		return upperBound;
	}

	/**
	* Method to interupt the threads currently active.
	*/
	public static void interruptThreads()
	{
		if (exactAlgorithm != null && exactAlgorithm.isAlive())
		{
			exactAlgorithm.interrupt();
		}

		if (upperBoundAlgorithm != null && upperBoundAlgorithm.isAlive())
		{
			upperBoundAlgorithm.interrupt();
		}
	}

	/**
	* Method that returns the chromatic number of a graph.
	* @return the chromatic number
	*/
	public static int getChromaticNumber()
	{
		return chromaticNumber;
	}

	/**
	* Method that sets the chromatic number of a graph object.
	* @param chromaticNumber the number we want it to be
	*/
	public static void setChromaticNumber(int chromaticNumber)
	{
		Graph.chromaticNumber = chromaticNumber;
	}

	/**
	* Method that returns the amount of edges of a graph.
	* @return the amount of edges
	*/
	public static int getAmountOfEdges()
	{
		return edges.length;
	}

	/**
	* Method to return the array with all edges.
	* @return the array containing all edges
	*/
	public static ColEdge[] getEdges() {
		return edges;
	}

	/**
	* Method that returns the number of vertices.
	* @return the amount of vertices
	*/
	public static int getN() {
		return n;
	}

	/**
	* Method that returns the file of the graph.
	* @return the file
	*/
	public String getFile() {
    	return file;
	}
}
