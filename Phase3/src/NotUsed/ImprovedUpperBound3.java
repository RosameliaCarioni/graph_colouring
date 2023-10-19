import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

public class ImprovedUpperBound3 extends Thread {
	
	public void run()
	{
		int returnedValue = advancedBacktrack(e, n, upperBound, upperBoundValues, coloringOrder);
		obj.setUpperBound3(returnedValue);
	}
	
	public ImprovedUpperBound3(ColEdge[] e, int n, int upperBound, int[] upperBoundValues, int[] coloringOrder, UpperBound obj)
	{
		this.e = e;
		this.n = n;
		this.upperBound = upperBound;
		this.upperBoundValues = upperBoundValues;
		this.coloringOrder = coloringOrder;
		this.obj = obj;
	}
	
	private static int[] assignedColors;
	private static int[] options;
	private static boolean timeUp = false;
	
	private ColEdge[] e;
	private int n;
	private int upperBound;
	private int[] upperBoundValues;
	private int[] coloringOrder;
	private UpperBound obj;
	
	/**
	   * This is the starting method for improving the upper bound.
	   * @param e The ColEdge[] that stores all the edges information.
	   * @param n The number of vertices.
	   * @param upperBound The upper bound.
	   * @param upperBoundValues The array that contains all the vertices with their assigned colors by the upper bound algorithm.
	   * @param coloringOrder This array contains the order the vertices should be colored in, based on their degree.
	   * @return int The improved upper bound.
	   */
	  
	  public static int advancedBacktrack(ColEdge[] e, int n, int upperBound, int[] upperBoundValues, int[] coloringOrder)
		{
		  	int currentUpperBound = upperBound;
			TimerTask completeImprovement = new TimerTask() {
		        public void run() {
		        	 timeUp = true;
		        }
		    };
		    Timer timer = new Timer();
		    long delay = 15000;
		    timer.schedule(completeImprovement, delay);
		    
			assignedColors = Arrays.copyOf(upperBoundValues, upperBoundValues.length);
			int maxColor = upperBound;
			boolean possible = true;
			
			while (possible && !timeUp)
			{
				currentUpperBound = maxColor;
				
				options = new int[maxColor-1];
				
				for(int i =1; i <= maxColor-1; i++){
					options[i-1]=i;
				}
				
				ArrayList<Integer> list = getMaxValueVertices(maxColor);
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
				
				Arrays.sort(order, Comparator.comparingInt(o -> o[1]));
				
				int[] coloringOrderFinal = new int[list.size()];
				int index = 0;
				for (int i = order.length - 1; i >= 0; i--)
				{
					coloringOrderFinal[index] = order[i][0];
					index++;
				}
			
				for (int i = coloringOrderFinal.length - 1; i >= 0; i--)
				{
					if (assignedColors[coloringOrderFinal[i]] == maxColor && !timeUp)
					{
						if (!isOtherColorPossible(e, n, coloringOrderFinal[i], maxColor))
						{
							possible = false;
							break;
						}	
					}
				}	
				maxColor -= 1;
			}
			timer.cancel();
			if (timeUp) return currentUpperBound;
			else return maxColor+1;
		}
	  
	  	/**
	  	 * This method gets all the vertices that have the maximum color value assigned.
	  	 * @param maxColor This is the current maximum color.
	  	 * @return An ArrayList<Integer> containing the vertices that have the maximum color value assigned.
	  	 */
		
		public static ArrayList<Integer> getMaxValueVertices(int maxColor)
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
		 * This method checks if we can change the color of an vertex that has a maximum color assigned, such that it gets a color lower than this maximum value.
		 * @param e The ColEdge[] that stores all the edges information.
		 * @param n The number of vertices
		 * @param targetVertex This is a vertex with the maximum color assigned.
		 * @param maxColor This is the maximum color.
		 * @return boolean. True if an other color is possible, false if not.
		 */
		public static boolean isOtherColorPossible(ColEdge[] e, int n, int targetVertex, int maxColor)
		{
			assignedColors[targetVertex] = 0;
			int[] colorBackup = Arrays.copyOf(assignedColors, assignedColors.length);
			ArrayList<Integer> list = createList(e, n, targetVertex, 1);
			//System.out.println((targetVertex+1)+", "+Arrays.toString(list.toArray()));
			//System.out.println("Size: "+list.size());
			
			for (int numberOfLayers = 1; numberOfLayers <= 2; numberOfLayers++) 
			{
				assignedColors = Arrays.copyOf(colorBackup, colorBackup.length);
				//System.out.println("AMOUNT OF LAYERS ============ " + numberOfLayers);
				
				if (numberOfLayers == 1 && !timeUp) 
				{
					for (int x = 0; x < list.size(); x++) 
					{
						assignedColors[list.get(x)] = 0;		
					}	
					if (recurse(e)) 
					{
						return true;
					} 
				} 
				else if (numberOfLayers == 2 && !timeUp)
				{
					//System.out.println("Adjacent ones: "+Arrays.toString(secondList.toArray()));
					for (int i = 0; i < list.size(); i++)
					{
						ArrayList<Integer> secondList = createList(e, n, list.get(i), 1);

						assignedColors[list.get(i)] = 0;
			
						for (int j = 0; j < secondList.size(); j++)
						{
							assignedColors[secondList.get(j)] = 0;
						}
						
						if (recurse(e)) 
						{
							return true;
						}
						
						//System.out.println("Amount of backtracking " + secondList.size());
						//System.out.println(Arrays.toString(secondList.toArray()));
						//System.out.println(Arrays.toString(assignedColors));
					}
				}
			}
			return false;
		}
		
		/**
		 * This method creates an ArrayList which contains all the adjacent vertices of a vertex.
		 * @param e The ColEdge[] that stores all the edges information.
		 * @param n The number of vertices.
		 * @param targetVertex This is the vertex we want to get all the adjacent vertices of.
		 * @param numberOfLayers This is the number of adjacent layers we want to get. So layer 2 gets the adjacent vertices of a vertex, and gets the adjacent vertices of these adjacent vertices.
		 * @return ArrayList<Integer> This contains the adjacent vertices.
		 */
		public static ArrayList<Integer> createList(ColEdge[] e, int n, int targetVertex, int numberOfLayers)
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
			//System.out.println(Arrays.deepToString(matrix));
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
			
			//System.out.println(Arrays.toString(list.toArray()));
			
			return list;
		}
		
		/**
		 * This method tries if with the given number of colors it is possible to color the graph.
		 * @param e The ColEdge[] that stores all the edges information.
		 * @return boolean True if this number of colors works, false if it doesn't work.
		 */
		public static boolean recurse(ColEdge[] e) {
			if (!timeUp)
			{
				//System.out.println(Arrays.toString(assignedColors));
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
}
