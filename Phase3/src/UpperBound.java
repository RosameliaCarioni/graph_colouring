import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
public class UpperBound {
	
	private int upperBound1;
	
	private int upperBoundRLF;
	private int[] assignedColors;
	
	private int[] coloringOrder;
	
	private int upperBoundGA;
	
	private boolean decomposed;
	
	private int lastUpperBound;
	
	private Graph graph;
	
	public void calculateUpperBoundTournament(int n, ColEdge e[], Graph graph){
		this.graph = graph;
		  int[][] adjacencyMatrix = createAdjacencyMatrix(n, e);
		    coloringOrder = generateOrder(adjacencyMatrix, n);
	    
	    RLFAlgorithm rlf = new RLFAlgorithm(e, n, this);
	    rlf.start();
	    
	    try {
			rlf.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	    printBestUB(upperBoundRLF);
	    
	    GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(n, e.length, e, upperBoundRLF, 2, this);
	    geneticAlgorithm.start();
	    
	    ImprovedUpperBound1 up1 = new ImprovedUpperBound1(e, n, upperBoundRLF, assignedColors, coloringOrder, this, false);
	    up1.start();
	  }

  public int calculateUpperBound(int n, ColEdge e[]){
	  decomposed = true;
	  int[][] adjacencyMatrix = createAdjacencyMatrix(n, e);
	    coloringOrder = generateOrder(adjacencyMatrix, n);
    
    RLFAlgorithm rlf = new RLFAlgorithm(e, n, this);
    rlf.start();
    
    try {
		rlf.join();
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
    
    ImprovedUpperBound1 up1 = new ImprovedUpperBound1(e, n, upperBoundRLF, assignedColors, coloringOrder, this, true);
    up1.start();
    
    try {
		up1.join();
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
    
    return upperBound1;
  }

  //Method that creates an adjacencyMatrix given an array of all edges.
  public int[][] createAdjacencyMatrix(int n, ColEdge edges[]) {
    //We create a new 2-dimensional array of size n, where n is the amount of vertices.
    int adjacencyMatrix[][] = new int[n][n];
    //For all vertices on both 'sides' of the edges, we set the corresponding value in the adjacencyMatrix to 1.
    for (int i = 0; i < edges.length; i++) {
      adjacencyMatrix[edges[i].u-1][edges[i].v-1] = 1;
      adjacencyMatrix[edges[i].v-1][edges[i].u-1] = 1;
    }
    return adjacencyMatrix;
  }

  //Method that returns a list filled with all colorvalues of adjacent vertices of a given node.
  public ArrayList<Integer> findColors(int[][] adjacencyMatrix, int vertexCount, int node, int[] colors) {
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

  //Method that assigns a color to all vertices and performs checks to see if adjacent vertices have different colors.
  public int[] colorFill(int node, int vertexCount, int[] colors, int[][] adjacencyMatrix, int numberOfVerticesDone) {
      //We set the color of the node to color1.
      colors[node] = 1;
      //While we have not found a color for node, we keep increasing it.
      while (findColors(adjacencyMatrix, vertexCount, node, colors).contains(colors[node])) {
        colors[node]++;
      }
      
      numberOfVerticesDone++;
      if (numberOfVerticesDone < vertexCount)
      {
          //When we found a color such that all adjacent vertices of node have a different color, we move on to the next vertex.
          colorFill(coloringOrder[numberOfVerticesDone], vertexCount, colors, adjacencyMatrix, numberOfVerticesDone);
      }
      else
      {
    	    //When we have checked for all vertices, we return the array containing the colorvalues for all vertices.
    	    return colors;
      }
      return colors;
  }
  
  /**
   * This method creates an order the vertices should be colored in, based on their degree.
   * @param adjacencyMatrix This is the adjacency matrix.
   * @param n The amount of vertices.
   * @return int[] Represents the order in which the vertices should be colored in.
   */
  
  public int[] generateOrder(int[][] adjacencyMatrix, int n)
  {
		int[][] degree = new int[n][2];
		for (int i = 0; i < n; i++) 
		{
			degree[i][1] = 0;
			degree[i][0] = i;
			for (int j = 0; j < n; j++) 
			{
				if (adjacencyMatrix[i][j] == 1) 
				{
					degree[i][1]++;
				}
			}
		}
		Arrays.sort(degree, Comparator.comparingInt(o -> o[1]));

		int[] order = new int[n];
		int index = 0;
		for (int i = degree.length - 1; i >= 0; i--)
		{
			order[index] = degree[i][0];
			index++;
		}
		
		return order;
  } 
  
  public void printBestUB(int upperBound)
  {
	  if (!decomposed)
	  {
		  ArrayList<Integer> values = new ArrayList<Integer>();
		  if (upperBound1 != 0)
		  {
			    values.add(upperBound1);
		  }
		  if (upperBoundRLF != 0)
		  {
			  	values.add(upperBoundRLF);
		  }
		  if (upperBoundGA != 0)
		  {
			    values.add(upperBoundGA); 
		  }
		    
		  if (values.size() != 0)
		  {		
			  if (lastUpperBound == 0)
			  {
				  graph.setUpperBound(upperBound);
				  System.out.println("NEW BEST UPPER BOUND = " + upperBound);
				  lastUpperBound = upperBound;
				  
				  if (graph.getLowerBound() != 0 && graph.getLowerBound() == upperBound)
				  {
					  System.out.println("CHROMATIC NUMBER = " + upperBound);
					  System.exit(0);
				  }
			  }
			  else if (upperBound < lastUpperBound)
			  {
				  System.out.println("NEW BEST UPPER BOUND = " + upperBound);
				  lastUpperBound = upperBound;
				  
				  if (graph.getLowerBound() != 0 && graph.getLowerBound() == upperBound)
				  {
					  System.out.println("CHROMATIC NUMBER = " + upperBound);
					  System.exit(0);
				  }
			  } 
		  }
	  }
  }
  
  public void setUpperBound1(int upperBound)
  {
	  this.upperBound1 = upperBound;
	  printBestUB(upperBound);
  }
  public void setUpperBoundRLF(int upperBound, int[] assignedColors)
  {
	  this.upperBoundRLF = upperBound;
	  this.assignedColors = assignedColors;
	  printBestUB(upperBound);
  }
  public void setUpperBoundGA(int upperBound)
  {
	  this.upperBoundGA = upperBound;
	  printBestUB(upperBound);
  }
  
}
