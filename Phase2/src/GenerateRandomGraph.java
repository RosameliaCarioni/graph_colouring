public class GenerateRandomGraph {

	/**
	 *This method, returns a two dimensional array (matrix) with a unique random graph.
	 *For additional information, please check the source code !
	 * @param vertices The number of Vertices in a graph.
	 * @param edges    The number of Edges in a graph.
	 * @return Returns a totally random graph, in a two dimensional array.
	 */
	public static int[][] generateRandomGraph(int vertices, int edges) {

        int[][] matrix = new int[edges][2];

        int randomVertex1;
        int randomVertex2;
        boolean isItPossible;

        // Loop trough all the edges, so we can assign vertices to them
        for (int i = 0; i < edges; i++)
        {

        		do
        		{
            		randomVertex1 = (int)(Math.random() * vertices + 1); // Get a completely random vertex
            		randomVertex2 = (int)(Math.random() * vertices + 1);

            		isItPossible = doesEdgeAlreadyExist(randomVertex1, randomVertex2, matrix);
        		} while ((randomVertex1 == randomVertex2) || !isItPossible);

            matrix[i][0] = randomVertex1; // Assign the vertices to the edge
            matrix[i][1] = randomVertex2;
        }

        return matrix;
    }
 /**
  * important method, to check if the edge already exist
  * @param randomVertex1 this first vertex is created in generateRandomGraph method
  * @param randomVertex2 this second vertex is created in generateRandomGraph method
  * @param matrix Is the random graph
  * @return Returns a boolean value, if the edge`s already exist or not (used in generateRandomGraph )
  */
    public static boolean doesEdgeAlreadyExist(int randomVertex1, int randomVertex2, int[][] matrix)
    {
    	for (int i = 0; i < matrix.length; i++)
    	{
    		if ((matrix[i][0] == randomVertex1 && matrix[i][1] == randomVertex2) || (matrix[i][0] == randomVertex2 && matrix[i][1] == randomVertex1))
    		{
    			return false;
    		}
    	}
    	return true;
    }


}
