import java.util.ArrayList;
import java.util.Arrays;

public class RLFAlgorithm extends Thread {

	/**
	 * This method is used to start the thread.
	 */
	public void run() {
		int upperBound = runRLFAlgorithm();
		ub.setUpperBoundRLF(upperBound, assignedColors);
	}

	/**
	 * Constructor
	 * @param e The ColEdge[] object containing the edges information.
	 * @param n The amount of vertices.
	 * @param up The UpperBound object.
	 */
	public RLFAlgorithm(ColEdge[] e, int n, UpperBound ub) {
		this.e = e;
		this.n = n;
		this.ub = ub;
		assignedColors = new int[n];
	}

	private ColEdge[] e;
	private int n;
	
	private UpperBound ub;
	
	// This is the list that contains all of the colored vertices
	private ArrayList<Integer> C = new ArrayList<Integer>(); 
	// This list contains all of the neighbors of a colored vertex
	private ArrayList<Integer> W = new ArrayList<Integer>();
	// This list contains all the uncolored vertices
	private ArrayList<Integer> U = new ArrayList<Integer>();
	
	private int amountOfColors; // This is the amount of colors used
	private int[] assignedColors;

	/**
	 * This method is called to start the RLF Algorithm.
	 * @return It returns the upper bound it found.
	 */
	public int runRLFAlgorithm() {
		// First we add the list with all the uncolored vertices with every vertex
		for (int i = 0; i < n; i++)
		{
			U.add(i);
		}
		// If after adding everything from W to U, U is empty, we colored everything, so we exit the while loop. Otherwise, we keep going
		while (U.size() > 0)
		{
			amountOfColors++; // Increment the amount of colors used
			int vertex = getVertexMaxDegreeInU(); // This is the first vertex we need to color
			updateNeighbors(vertex); // We update all the lists with the neighbors of this first vertex
			U.remove(U.indexOf(vertex)); // Remove the first vertex from the uncolored list
			C.add(vertex); // Add the first vertex to the colored list
			assignedColors[vertex] = amountOfColors;
			
			while (U.size() > 0) // If there are uncolored vertices left
			{					
				vertex = getVertexMaxDegreeInW(); // Get the vertex we need to color
				updateNeighbors(vertex); // Update all the lists with the neighbors of this vertex
				U.remove(U.indexOf(vertex)); // Remove the vertex from the uncolored list
				C.add(vertex); // Add the first vertex to the colored list
				assignedColors[vertex] = amountOfColors;
			}
			U = new ArrayList<Integer>(W); // Copy everything in list W to list U
			W.clear(); // Clear list W
		}
		return amountOfColors; // If we colored every vertex, return the amount of colors
	}
	
	/**
	 * This method removes all neighbors of a vertex from U, and adds all of these neighbors to W
	 * @param targetVertex This is the vertex we want to get all the neighbors of
	 */
	public void updateNeighbors(int targetVertex)
	{
		for (int j = 0; j < e.length; j++)
		{
			int firstValueOfEdge = e[j].u - 1;
	        int secondValueOfEdge = e[j].v - 1;
	        
	        if (firstValueOfEdge == targetVertex)
	        {
	        	// We check if the neighbor isn't already in W and is uncolored
	        	if (!W.contains(secondValueOfEdge) && U.contains(secondValueOfEdge)) 
	        	{
		        	W.add(secondValueOfEdge); // Add the vertex to W
		        	U.remove(U.indexOf(secondValueOfEdge)); // Remove the vertex from U
	        	}
	        }
	        if (secondValueOfEdge == targetVertex)
	        {
	        	// We check if the neighbor isn't already in W and is uncolored
	        	if (!W.contains(firstValueOfEdge) && U.contains(firstValueOfEdge))
	        	{
		        	W.add(firstValueOfEdge);
		        	U.remove(U.indexOf(firstValueOfEdge));
	        	}
	        }
		}
	}
	
	/**
	 * Get the vertex that is in U and has the highest degree with vertices that are in W.
	 * @return The vertex that has the greatest degree with vertices that are in W.
	 */
	public int getVertexMaxDegreeInW()
	{
		// An array that stores all of the degrees in case there's a tie
		int[] degrees = new int[U.size()]; 
		int maxDegree = 0;
		
		for (int i = 0; i < U.size(); i++)
		{
			int degree = 0;
			for (int j = 0; j < e.length; j++)
			{
				int firstValueOfEdge = e[j].u - 1;
		        int secondValueOfEdge = e[j].v - 1;
		        
		        if (firstValueOfEdge == U.get(i))
		        {
		        	// We check if W contains the other value of the edge, while we only want to count the degree between vertices that are in W.
		        	if (W.contains(secondValueOfEdge))
		        	{
		        		degree++;
		        	}
		        }
		        if (secondValueOfEdge == U.get(i))
		        {
		        	// We check if W contains the other value of the edge, while we only want to count the degree between vertices that are in W.
		        	if (W.contains(firstValueOfEdge))
		        	{
		        		degree++;
		        	}
		        }
			}
			if (degree > maxDegree)
			{
				maxDegree = degree;
			}
			degrees[i] = degree;
		}
	
		// Create a list where we can store the vertices that have a tie
		ArrayList<Integer> ties = new ArrayList<Integer>();
		for (int i = 0; i < degrees.length; i++)
		{
			if (degrees[i] == maxDegree) // If a vertex has the same degree as the maximum degree (it's a tie)
			{
				ties.add(U.get(i)); // Add the vertex to the tie list
			}
		}
		
		// If the size of the tie list is 1, there isn't a tie, while this one value is just the vertex with the maximum value
		if (ties.size() == 1) 
		{
			return ties.get(0);
		}
		else // There is an actual tie, so find the vertex with the highest degree with vertices in U
		{
			int maxDegreeInU = 0;
			int maxDegreeInUVertex = -1;
			for (int i = 0; i < ties.size(); i++)
			{
				int degree = getDegreeInU(ties.get(i));
				if (degree > maxDegreeInU)
				{
					maxDegreeInU = degree; 
					maxDegreeInUVertex = ties.get(i);
				}	
			}
			if (maxDegreeInUVertex != -1) // There is a vertex that has not 0 as degree
			{
				return maxDegreeInUVertex;
			}
			else return ties.get(0); // Otherwise just return a random vertex
		}
	}
	/**
	 * This method gets the vertex that has the maximum degree with uncolored vertices.
	 * @return The vertex which has the maximum degree with uncolored vertices.
	 */
	public int getVertexMaxDegreeInU()
	{
		int maxDegree = 0;
		int maxDegreeVertex = 0;
		for (int i = 0; i < U.size(); i++)
		{
			int degree = getDegree(U.get(i));
			if (degree > maxDegree)
			{
				maxDegreeVertex = U.get(i);
				maxDegree = degree;
			}
		}
		return maxDegreeVertex;
	}
	
	/**
	 * This method gets degree of a vertex.
	 * @param targetVertex This is the vertex we want to know the degree of.
	 * @return It returns the degree of a vertex.
	 */
	public int getDegree(int targetVertex)
	{
		int degree = 0;
		for (int j = 0; j < e.length; j++)
		{
			int firstValueOfEdge = e[j].u - 1;
	        int secondValueOfEdge = e[j].v - 1;
	        
	        if (firstValueOfEdge == targetVertex)
	        {
	        	degree++;
	        }
	        if (secondValueOfEdge == targetVertex)
	        {
	        	degree++;
	        }
		}
		return degree;
	}
	
	/**
	 * This method gets the degree of a vertex with vertices that are uncolored.
	 * @param targetVertex This is the vertex we want to know the degree of.
	 * @return It returns the degree.
	 */
	public int getDegreeInU(int targetVertex)
	{
		int degree = 0;
		for (int j = 0; j < e.length; j++)
		{
			int firstValueOfEdge = e[j].u - 1;
	        int secondValueOfEdge = e[j].v - 1;
	        
	        if (firstValueOfEdge == targetVertex)
	        {
	        	if (U.contains(secondValueOfEdge))
	        	{
		        	degree++;
	        	}
	        }
	        if (secondValueOfEdge == targetVertex)
	        {
	        	if (U.contains(firstValueOfEdge))
	        	{
	        		degree++;
	        	}
	        }
		}
		return degree;
	}
}
