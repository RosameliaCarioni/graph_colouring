import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

public class ImprovedUpperBound1 extends Thread {
	
	/**
	 * This method is used to start the thread.
	 */
	public void run()
	{
		// Run the Reverse backtracking
		int returnedValue = advancedBacktrack(e, n, upperBound, upperBoundValues, coloringOrder);
		
		// Set the value of the reversed backtracking upper bound in the upper bound object
		obj.setUpperBound1(returnedValue);
	}
	
	/**
	 * The constructor of the reversed backtracking class.
	 * @param e ColEdge[] which contains the edge information.
	 * @param n The number of vertices.
	 * @param upperBound The current best upper bound found.
	 * @param upperBoundValues The current best coloring scheme found.
	 * @param coloringOrder The order in which we should backtrack vertices, based on their degree.
	 * @param obj This it the upperBound object.
	 * @param decomposed True if it is a subgraph, i.e. decomposed. False if it is not a subgraph.
	 */
	public ImprovedUpperBound1(ColEdge[] e, int n, int upperBound, int[] upperBoundValues, int[] coloringOrder, UpperBound obj, boolean decomposed)
	{     
		this.e = e;
		this.n = n;
		this.upperBound = upperBound;
		this.upperBoundValues = upperBoundValues;
		this.coloringOrder = coloringOrder;
		this.obj = obj;
		this.decomposed = decomposed;
	}
	
	private int[] assignedColors;
	private int[] options;
	private boolean timeUp = false;
	
	private ColEdge[] e;
	private int n;
	private int upperBound;
	private int[] upperBoundValues;
	private int[] coloringOrder;
	private UpperBound obj;
	
	private boolean decomposed;
	
	/**
	   * This is the starting method for improving the upper bound.
	   * @param e The ColEdge[] that stores all the edges information.
	   * @param n The number of vertices.
	   * @param upperBound The current best upper bound.
	   * @param upperBoundValues The array that contains all the vertices with their assigned colors by the upper bound algorithm.
	   * @param coloringOrder This array contains the order the vertices should be colored in, based on their degree.
	   * @return int The improved upper bound.
	   */
	  
	  public int advancedBacktrack(ColEdge[] e, int n, int upperBound, int[] upperBoundValues, int[] coloringOrder)
		{	      
		  	int currentUpperBound = upperBound;

		  	// We create a timer, that makes sure that reverse backtracking doesn't run for longer than 15 seconds.
			TimerTask completeImprovement = new TimerTask() {
		        public void run() {
		        	 timeUp = true;
		        }
		    };
		    Timer timer = new Timer();
		    long delay;
		    if (decomposed) delay = 15000;
		    else delay = 240000;
		   
		    timer.schedule(completeImprovement, delay);
		    
		    // We are copy the array that contains the colouring scheme to assignedColors
			assignedColors = Arrays.copyOf(upperBoundValues, upperBoundValues.length);

			int maxColor = upperBound; // The maximum color currently used is the upper bound
			boolean possible = true;
			while (possible && !timeUp) // Checks if the tried upper bound works, so it can try upper bound - 1
			{
				currentUpperBound = maxColor; // The currentUpperBound is the last known upper bound that works
				obj.setUpperBound1(currentUpperBound);
				
				options = new int[maxColor-1]; // The amount of colors we are allowed to use, which is maxColor - 1
				
				// We fill the array which contains the colors
				for(int i =1; i <= maxColor-1; i++){
					options[i-1]=i;
				}
				
				// This list contains the vertices that currently have the maxColor assigned
				ArrayList<Integer> list = getMaxValueVertices(maxColor);
				// This matrix will contain the vertices numbers with their degrees
				int[][] order = new int[list.size()][2];
				
				for (int i = 0; i < list.size(); i++)
				{
					for (int j = 0; j < coloringOrder.length; j++)
					{
						if (list.get(i) == coloringOrder[j])
						{
							order[i][0] = list.get(i);
							order[i][1] = j;
							break;
						}
					}
				}
				
				// Sort the array based on their degree
				Arrays.sort(order, Comparator.comparingInt(o -> o[1]));
				
				// We create an array that contains the numbers of the vertices in the right order based on their degree
				int[] coloringOrderFinal = new int[list.size()];
				int index = 0;
				for (int i = order.length - 1; i >= 0; i--)
				{
					coloringOrderFinal[index] = order[i][0];
					index++;
				}
			
				// We loop trough all of the vertices that have the maxColor assigned
				for (int i = coloringOrderFinal.length - 1; i >= 0; i--)
				{
					// We check if this vertex still has the maxColor assigned, while it could be that it is already lowered
					if (assignedColors[coloringOrderFinal[i]] == maxColor && !timeUp)
					{
						// We check if it is possible to assign a lower color to this vertex
						if (!isOtherColorPossible(e, n, coloringOrderFinal[i], maxColor))
						{
							// It is not possible, so we stop trying
							possible = false;
							break;
						}
					}
				}	
				maxColor -= 1;
			}
			timer.cancel();
			
			// If it is here because the time expired, it didn't finish lowering every maxColor assigned, so the last known upper bound is the currentUpperBound
			if (timeUp) { 
				return currentUpperBound;
			}
			// If it came here, the time didn't expired, but it couldn't lower a vertex with maxColor assigned, so the last known upper bound is maxColor + 1, while at the end of the while loop it does maxColor -= 1
			else
			{
				return maxColor+1;	
			}
		}
		
		/**
		 * This method checks if we can change the color of an vertex that has a maximum color assigned, such that it gets a color lower than this maximum value.
		 * @param e The ColEdge[] that stores all the edges information.
		 * @param n The number of vertices
		 * @param targetVertex This is a vertex with the maximum color assigned.
		 * @param maxColor This is the maximum color.
		 * @return boolean. True if an other color is possible, false if not.
		 */
		public boolean isOtherColorPossible(ColEdge[] e, int n, int targetVertex, int maxColor)
		{
			assignedColors[targetVertex] = 0; // Reset the assigned color of the target vertex
			int[] colorBackup = Arrays.copyOf(assignedColors, assignedColors.length); // Create a backup of the original assignedColors
			ArrayList<Integer> list = createList(e, n, targetVertex, 1); // This list contains the neighbors  of a target vertex
			
			// First we try to find a solution with one layer, if that doesn't work we try with 2 layers
			for (int numberOfLayers = 1; numberOfLayers <= 2; numberOfLayers++) 
			{
				assignedColors = Arrays.copyOf(colorBackup, colorBackup.length);
				
				if (numberOfLayers == 1 && !timeUp) 
				{
					// Loop trough every neighbor of the target vertex, and reset their color
					for (int x = 0; x < list.size(); x++) 
					{
						assignedColors[list.get(x)] = 0;
					}	
					// Now we use our exact algorithm to find a solution
					if (recurse(e)) 
					{
						// There is a solution, return true
						return true;
					} 
				} 
				else if (numberOfLayers == 2 && !timeUp)
				{
					// First loop trough every neighbor of the target vertex
					for (int i = 0; i < list.size(); i++)
					{
						// From the neighbor of this target vertex, we also create a list that contains the neighbors of this neighbor
						ArrayList<Integer> secondList = createList(e, n, list.get(i), 1);

						// Reset the color of the neighbor of the target vertex
						assignedColors[list.get(i)] = 0;
			
						// Loop trough all of the neighbors of the neighbor, and reset all of their colors
						for (int j = 0; j < secondList.size(); j++)
						{
							assignedColors[secondList.get(j)] = 0;
						}
					}
					// Everything that needed to be reset is reset, so we run the exact algorithm to check if there's a solution
					if (recurse(e)) 
					{
						// There is a solution return true
						return true;
					}
				}
			}
			// We tried every layer, and there isn't a solution. Return false
			return false;
		}
		
		/**
	  	 * This method gets all the vertices that have the maximum color value assigned.
	  	 * @param maxColor This is the current maximum color.
	  	 * @return An ArrayList<Integer> containing the vertices that have the maximum color value assigned.
	  	 */
		
		public ArrayList<Integer> getMaxValueVertices(int maxColor)
		{
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < assignedColors.length; i++)
			{
				if (assignedColors[i] == maxColor)
				{
					list.add(i);
				}
			}
			return list;
		}
		
		/**
		 * This method creates an ArrayList which contains all the adjacent vertices of a vertex.
		 * @param e The ColEdge[] that stores all the edges information.
		 * @param n The number of vertices.
		 * @param targetVertex This is the vertex we want to get all the adjacent vertices of.
		 * @param numberOfLayers This is the number of adjacent layers we want to get. So layer 2 gets the adjacent vertices of a vertex, and gets the adjacent vertices of these adjacent vertices.
		 * @return ArrayList<Integer> This contains the adjacent vertices.
		 */
		public ArrayList<Integer> createList(ColEdge[] e, int n, int targetVertex, int numberOfLayers)
		{
			int[][] matrix = new int[n][numberOfLayers];
			for (int x = 0; x < numberOfLayers; x++)
			{
				if (x != 0)
				{
					for (int j = 0; j < matrix.length; j++)
					{
						if (matrix[j][x-1] == 1)
						{
							for (int i = 0; i < e.length; i++)
							{
								int firstValueOfEdge = e[i].u;
						        int secondValueOfEdge = e[i].v;
						        
						        if (firstValueOfEdge == j+1 && secondValueOfEdge != targetVertex+1)
						        {
						        	matrix[secondValueOfEdge - 1][x] = 1;
						        }
						        if (secondValueOfEdge == j+1 && firstValueOfEdge != targetVertex+1)
						        {
						        	matrix[firstValueOfEdge - 1][x] = 1;
						        }
							}
						}
					}
				}
				else
				{
					for (int i = 0; i < e.length; i++)
					{
						int firstValueOfEdge = e[i].u;
				        int secondValueOfEdge = e[i].v;
				        
				        if (firstValueOfEdge == targetVertex+1)
				        {
				        	matrix[secondValueOfEdge - 1][x] = 1;
				        }
				        if (secondValueOfEdge == targetVertex+1)
				        {
				        	matrix[firstValueOfEdge - 1][x] = 1;
				        }
					}
				}	
			}
			ArrayList<Integer> list = new ArrayList<Integer>();
			
			for (int i = 0; i < matrix.length; i++)
			{
				for (int j = 0; j < matrix[0].length; j++)
				{
					if (matrix[i][j] == 1)
					{
						if (!list.contains(i))
						{
							list.add(i);
						}
					}
				}
			}
			
			return list;
		}
		
		/**
		 * This method tries if with the given number of colors it is possible to color the graph.
		 * @param e The ColEdge[] that stores all the edges information.
		 * @return boolean True if this number of colors works, false if it doesn't work.
		 */
		public boolean recurse(ColEdge[] e) {
			if (!timeUp)
			{
				// Loop through all the edges
				for (int i = 0; i < e.length; i++) {
					// Check if vertexes.u does not have a color yet
					if (assignedColors[e[i].u - 1] == 0) {
						// loop through all the color options
						for (int c : options) {
							// check if we can assign the color to the vertex
							if (isPossible(e[i].u, c, e)) {
								// Assign color to vertex
								assignedColors[e[i].u - 1] = c;
								// recurse to solve another vertex
								if (!recurse(e)) {
									// If the recursion fails backtrack and uncolor the vertex
									assignedColors[e[i].u - 1] = 0;
								}
							}
						}
						// if there are vertices that are still not colored,
						// return false because there is no possible solution
						if (assignedColors[e[i].u - 1] == 0) {
							return false;
						}
					}
					// Does the same as previous if statement but for vertex.v
					if (assignedColors[e[i].v - 1] == 0) {
						for (int c : options) {  
							if (isPossible(e[i].v, c, e)) {
								assignedColors[e[i].v - 1] = c;
								if (!recurse(e)) {
									assignedColors[e[i].v - 1] = 0;
								}
							}
						}
						if (assignedColors[e[i].v - 1] == 0) {
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
		public boolean isPossible(int vertex, int color, ColEdge[] e) {
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
