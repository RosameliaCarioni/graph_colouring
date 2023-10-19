import java.io.*;
import java.util.*;
import java.math.BigInteger;

class ColEdge {int u;int v;}
public class FinalCodePhase1
{

  public final static boolean DEBUG = false;
  public final static String COMMENT = "//";
  public static int[] assignedColors;
  public static int[] options;

  public static void main( String args[] )
  {
    if( args.length < 1 )
    {
      System.out.println("Error! No filename specified.");
      System.exit(0);
    }
    String inputfile = args[0];
    boolean seen[] = null;
    //! n is the number of vertices in the graph
    int n = -1;
    //! m is the number of edges in the graph
    int m = -1;
    //! e will contain the edges of the graph
    ColEdge e[] = null;

    try
    {
      FileReader fr = new FileReader(inputfile);
      BufferedReader br = new BufferedReader(fr);
      String record = new String();
      //! THe first few lines of the file are allowed to becomments, staring with a // symbol.
      //! These comments are only allowed at the top of thefile.
      //! -----------------------------------------
      while ((record = br.readLine()) != null)
      {
        if( record.startsWith("//") )
        continue;
        break;
        // Saw a line that did not start with a comment -- time to start reading the data in!
      }
      if( record.startsWith("VERTICES = ") )
      {
        n = Integer.parseInt( record.substring(11) );
        if(DEBUG)
        System.out.println(COMMENT + " Numberof vertices = "+n);
      }
      seen = new boolean[n+1];
      record = br.readLine();
      if( record.startsWith("EDGES = ") )
      {
        m = Integer.parseInt( record.substring(8) );
        if(DEBUG)
        System.out.println(COMMENT + " Expected number of edges = "+m);
      }
      e = new ColEdge[m];
      for( int d=0; d<m; d++)
      {
        if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
        record = br.readLine();
        String data[] = record.split(" ");
        if( data.length != 2 )
        {
          System.out.println("Error! Malformed edge line: "+record);
          System.exit(0);
        }
        e[d] = new ColEdge();
        e[d].u = Integer.parseInt(data[0]);
        e[d].v = Integer.parseInt(data[1]);
        seen[ e[d].u ] = true;
        seen[ e[d].v ] = true;
        if(DEBUG)
        System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);
      }

      String surplus = br.readLine();
      if( surplus != null )
      {
        if( surplus.length() >= 2 )
        if(DEBUG)
        System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");
      }
    }
    catch (IOException ex)
    {         // catch possible io errors from readLine()
      System.out.println("Error! Problem reading file "+inputfile);
      System.exit(0);
    }
    for( int x=1; x<=n; x++ )
    {
      if( seen[x] == false )
      {
        if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
      }
    }

    calculateLowerChromatic(n, m);

    //else System.out.println("With greater than 99% confidence we can say that the graph has at least a clique of size: 2. This is our Lower Bound.");

    //We create an array that can store the colors for every vertex.
    int[] colors = new int[n];
    //We create a 2 dimensional array to store information about adjacing vertices.
    int[][] adjacencyMatrix = createAdjacencyMatrix(n, e);

    //Method that calculates the upperbound.
    int upperBound = calculateUpperBound(colorFill(0, n, colors, adjacencyMatrix));
    System.out.println("The upper bound of the chromatic number is: " + upperBound + ".");

    //We call our exact algorithm.
    exactAlgorithm(e, n, m, 2, upperBound);
  }

  //Method that creates an adjacencyMatrix given an array of all edges.
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

  //Code for calculating the lower bound.
  //----------------------------------------------------------------------------
  public static void calculateLowerChromatic(int vertices, int edges){

    //If the graph was complete the total possible edges can be calculated using the formula
    double totalPossibleEdges= (vertices*(vertices-1))/2;

    int x = 2;
    double probability =  1;
    int chrom = 2;

    /* The formula to calculate the probability that there exists at least one clique with "x" vertices
    * as a subset of our graph is: 1-(1-(edges/totalPossibleEdges)^power(xC2))^power(nCx) .
    * */

    //We considered cliques of x vertices with a probability higher than 0.9
    while(x<=vertices && probability > 0.9){
      BigInteger power1= combination(x, 2);

      BigInteger power2 =  combination(vertices, x);

      double step1= power (edges/totalPossibleEdges, power1);

      double step2= power (1-step1 , power2);

      probability= 1-step2;

      if(probability > 0.99)
      chrom = x;

      x++;
    }

    System.out.println("With greater than 99% confidence we can say that"
    + " the graph has at least a clique of size: " + chrom + ". This is our Lower Bound.");
  }




  //This method calculates nCk
  public static BigInteger combination(int n, int k) {

    //Calculate the factorial the numerator divided by the factorial of n-k
    BigInteger numerator = permutation(n, k);
    //While loop is used because calculating the factorial recursively using BigInteger is complex
    while ( k > 0 ) {
      numerator = numerator.divide(BigInteger.valueOf(k));
      k--;
    }
    return numerator;
  }


  public static BigInteger permutation(int n, int k) {
    //Start with result = 1
    BigInteger result = BigInteger.ONE;

    //Simplify n! with (n-k)!, it is only multiplying from n to n-k+1
    for ( int i = n ; i >= n-k+1 ; i-- ) {
      result = result.multiply(BigInteger.valueOf(i));
    }
    return result;
  }



  public static double power(double base, BigInteger exponent) {
    double result=1;

    //While loop stops if the exponent is negative or 0
    while (exponent.signum() > 0) {
      //returns true iff the designated bit is a set
      if (exponent.testBit(0)){
        result = result*base;
      }
      base = base*base;
      //exponent decreases by 1
      exponent = exponent.shiftRight(1);
    }
    return result;
  }
  //----------------------------------------------------------------------------

  //Code for calculating the exact chromatic number.
  //----------------------------------------------------------------------------
  public static void exactAlgorithm(ColEdge[] e, int n, int m, int lowerBound, int upperBound) {
    //First we perform all the checks for the simple graph algorithms.
    if (completeGraphCheck(n, e))
    {
      System.out.println("The chromatic number is: " + n + ", because it's a complete graph.");
    }

    boolean isBiPartite = false;

    if (bipartite(e, m, n))
    {
      System.out.println("The chromatic number: 2, because it's a bipartite graph.");
      isBiPartite = true;
    }

    int centerVertex = checkForcenterVertex(e, n);
		boolean graphIsEven = checkIfGraphIsEven(n);

    if (centerVertex != 0)
			{
				if (checkIfGraphIsWheel(e, n, centerVertex))
				{
					if (graphIsEven)
					{
						System.out.println("The chromatic number is: 4, because it's an even wheel.");
					}
					else System.out.println("The chromatic number is: 3, because it's an odd wheel.");
        }
      }

		if (checkIfGraphIsCycle(e, n))
		{
			if (graphIsEven)
			{
				System.out.println("The chromatic number is: 2, because it's an even cycle.");
			}
			else System.out.println("The chromatic number is: 3, because it's an odd cycle.");
		}
    //If the given graph is not one of the simple graphs, we calculate the chromatic number recursively.
    else {
      runRecursion(e, n, 2, upperBound);
    }
  }
  public static void runRecursion(ColEdge[] allEdges, int n, int lowerBound, int upperBound){

		// Array to store the isThereASolution value at assignedColors[i] refers to the color of the vertex
		assignedColors=new int[n];
		// The number of colors to check, starting at the lower bound
		int numberOfOptions = lowerBound;
		// used for tracking if a solution has been found
		boolean isThereASolution = false;
		while(!isThereASolution && numberOfOptions <= upperBound){
			// create a new array of options, this will increase with each iteration
			options = new int[numberOfOptions];
			for(int i =1; i <= numberOfOptions; i++){
				options[i-1]=i;
			}
			// call the recursive method to check if the graph can be solved with the given number of options
			isThereASolution = recurse(allEdges);
			// increase number of options
			numberOfOptions += 1;
		}

		System.out.println("The exact chromatic number is: " + (numberOfOptions-1) + ".");

	}

	public static boolean recurse(ColEdge[] e){
		// Loop through all the edges
		for(int i = 0; i < e.length; i++){
			// Check if vertexes.u does not have a color yet
			if(assignedColors[e[i].u-1] == 0){
				// loop through all the color options
				for(int c: options){
					// check if we can assign the color to the empty vertex
					if(isPossible(e[i].u, c, e)){
						// Assign color to vertex
						assignedColors[e[i].u-1] = c;
						// recurse to solve another vertex
						if(!recurse(e)){
							// If the recursion fails backtrack and uncolor the vertex
							assignedColors[e[i].u-1] = 0;
						}
					}
				}
				// if there are vertices that are still not colored,
				// return false because there is no possible solution
				if(assignedColors[e[i].u-1] == 0){
					return false;
				}
			}
			// Does the same as previous if statment but for vertex.v
			if(assignedColors[e[i].v-1] == 0){
				for(int c: options){
					if(isPossible(e[i].v, c, e)){
						assignedColors[e[i].v-1] = c;
						if(!recurse(e)){
							assignedColors[e[i].v-1] = 0;
						}
					}
				}
				if(assignedColors[e[i].v-1] == 0){
					return false;
				}
			}
		}
		// If we exit the for loop without returning false, then all vertices are colored
		// And return true
		return true;
	}

	// Method to check if it is possible to give a vertex the given color
	// Iterate trough all the edges and check if any edge contains the vertex and
	// returns false if the other vertex on that edge has the the given color
	public static boolean isPossible(int vertex, int color, ColEdge[] e){
		for(int i = 0; i < e.length; i++){
			if(e[i].u == vertex){
				if(assignedColors[e[i].v-1] == color){
					return false;
				}
			}
			if(e[i].v == vertex){
				if(assignedColors[e[i].u-1] == color){
					return false;
				}
			}
		}
		// If no edge is found which makes this move impossible return true
		return true;
	}
  //----------------------------------------------------------------------------

  //Code for calculating the upper bound.
  //-------------------------------------------------------------------------
  //Method that returns a list filled with all colorvalues of adjacent vertices of a given node.

  public static int calculateUpperBound(int[] colors) {
    int max = 0;
    for (int i = 0; i < colors.length; i++) {
      if (colors[i] > max) {
        max = colors[i];
      }
    }
    return max;
  }
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

  //Method that assigns a color to all vertices and performs checks to see if adjacent vertices have different colors.
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
  //---------------------------------------------------------------------------





  //Method that checks if a graph is a complete graph, i.e. all vertices are connected to eachother.
  public static boolean completeGraphCheck(int vertexCount, ColEdge[] edgesArray) {
    //If all vertices are connected, we can say that the amount of edges is the same as this formula.
    int edgeRequirement = (vertexCount * (vertexCount-1) / 2);
    if (edgeRequirement == edgesArray.length) {
      return true;
    }
    else {
      return false;
    }
  }

  //METHOD BIPARITE: returns true if method is Bipartite, false if not
  //Idea: divide the vertices into 2 sets(set 1 and set2).All elements within the same set are disconnected.
  public static boolean bipartite (ColEdge [] e, int edges, int n){
    //New arrays 1 and 2 to store my Set 1 & 2
    //Array check keeps track of numbers that I need to check(asign to sets)
    ArrayList <Integer> set1 = new ArrayList <Integer> ();
    ArrayList <Integer> set2 = new ArrayList <Integer> ();
    ArrayList <Integer> check = new ArrayList <Integer> ();

    //Stores the value of the vertice that we are currently cheecking.
    int vertexValue=0;

    //Check edges and place them either in set 1 or 2.
    //Returns false if vertice is already placed.
    while (set1.size() + set2.size() < n){
      //Add first element to check and place it into set1
      //In case the graph has independent set of vertices: One of the elements will also be added at one of the sets.
      for (int i=0; i<edges; i++){
        if ( (checkIfVertexIsIn (set1,e[i].u)== false) && (checkIfVertexIsIn (set1,e[i].v)==false)){
          check.add (e[i].u);
          set1.add (e[i].u);
          break;
        }

      }
      while (check.size()>0){
        //take last element from check
        vertexValue= check.get(check.size()-1);
        int vertexIndex= whichSet (set1,set2,vertexValue);
        //delete last element from check, to avoid overcounting
        check.remove (check.size()-1);

        for (int i=0; i<edges; i++){

          //if element is in set 1
          if (vertexIndex==1){
            if (e[i].u==vertexValue) {
              //if I have the value in set 1 then add the other to set 2, and other way around
              if (checkIfVertexIsIn(set1, e[i].v) ==true){
                return false;
              }

              if (checkIfVertexIsIn(set2, e[i].v) == false){
                set2.add (e[i].v);
                check.add (e[i].v);

              }
            }
            if (e[i].v==vertexValue){
              if (checkIfVertexIsIn(set1, e[i].u) ==true){
                return false;
              }
              if (checkIfVertexIsIn(set2, e[i].u) ==false){
                set2.add (e[i].u);
                check.add (e[i].u);
              }
            }
          }

          //if element is in set 2
          if (vertexIndex==2){
            if (e[i].u==vertexValue){
              if (checkIfVertexIsIn(set2,e[i].v) ==true){
                return false;
              }
              if (checkIfVertexIsIn(set1,e[i].v)==false){
                set1.add (e[i].v);
                check.add (e[i].v);
              }
            }
            if (e[i].v==vertexValue){
              if(checkIfVertexIsIn(set2,e[i].u)==true){
                return false;
              }
              if (checkIfVertexIsIn(set1,e[i].u)==false){
                set1.add (e[i].u);
                check.add (e[i].u);
              }
            }
          }
        }
      }
    }
    return true;
  }

  //METHOD: checks if element is already in array
  public static boolean checkIfVertexIsIn (ArrayList<Integer> array, int numberToCheck){
    for (int i=0; i<array.size(); i++){
      if (array.get(i)==numberToCheck){
        return true;
      }
    }
    return false;
  }

  //METHOD:  checks in which set the vertex is
  public static int whichSet (ArrayList<Integer>  set1, ArrayList<Integer>  set2, int vertexValue){
    for (int i=0; i<set1.size();i++){
      if (set1.get(i)==vertexValue){
        return 1;
      }
    }
    return 2;
  }

  public static boolean checkIfGraphIsEven(int n)
  {
    if (n % 2 == 0)
    {
      return true;
    }
    else return false;
  }

  public static boolean checkIfGraphIsCycle(ColEdge[] e, int n)
  {
    int rightAmountOfVertices = 0; // The vertices that have 2 connected edge

    for (int i = 1; i <= n; i++) // For each vertex
    {
      int amountOfVertices = 0; // Amount of vertices connected to i

      for (int j = 0; j < e.length; j++) // For each edge
      {
        int firstValueOfEdge = e[j].u;
        int secondValueOfEdge = e[j].v;

        if (i == firstValueOfEdge) // If the first value of an edge = i, we want to add + 1 to the amountOfVertices, because the second vertex is connected to i
        {
          amountOfVertices += 1;
        }
        else if (i == secondValueOfEdge) // If the second value of an edge = i, we want to add + 1 to the amountOfVertices, because the first vertex connected to i
        {
          amountOfVertices += 1;
        }
      }

      if (amountOfVertices == 2) // There are two vertices connected to i. Add + 1 to the rightAmountOfVertices.
      {
        rightAmountOfVertices += 1;
      }
      else return false;
    }

    if (rightAmountOfVertices == n) // So the amount of vertices that have 2 other vertices connected to it needs to be the same as the total amount of vertices; every vertex has 2 other vertices connected to it
    {
      return true;
    }
    else return false;
  }

  public static boolean checkIfGraphIsWheel(ColEdge[] e, int n, int centerVertex)
  {
    // We only want to check if the graph excluded the center vertex is a cycle, because a cycle + a center vertex is a wheel
    // So if it's also a cycle: it's a wheel, otherwise it's neither

    int rightAmountOfVertices = 0;

    for (int i = 1; i <= n; i++)
    {
      if (i != centerVertex) // Don't check the centerVertex, because in case if it's a wheel it has n - 1 vertices connected to it, and not 2
      {
        int amountOfVertices = 0;

        for (int j = 0; j < e.length; j++)
        {
          int firstValueOfEdge = e[j].u;
          int secondValueOfEdge = e[j].v;

          if (i == firstValueOfEdge && secondValueOfEdge != centerVertex) // Don't check if i is connected to the center vertex, because we already know it is
          {
            amountOfVertices += 1;
          }
          else if (i == secondValueOfEdge && firstValueOfEdge != centerVertex) // Don't check if i is connected to the center vertex, because we already know it is
          {
            amountOfVertices += 1;
          }
        }
        if (amountOfVertices == 2)
        {
          rightAmountOfVertices += 1;
        }
        else return false;
      }
    }

    if (rightAmountOfVertices == n - 1) // The amount of vertices that have 2 other vertices connected to it is not the same as the total vertices, because the wheel is excluded: So n - 1
    {
      return true;
    }
    else return false;
  }

  public static int checkForcenterVertex(ColEdge[] e, int n)
  {
    int midVertex = 0;

    for (int i = 1; i <= n; i++) // Check for every vertex
    {
      int amountOfVertices = 0;

      for (int j = 0; j < e.length; j++) // Check for every edge
      {
        int firstValueOfEdge = e[j].u;
        int secondValueOfEdge = e[j].v;

        if (i == firstValueOfEdge || i == secondValueOfEdge) // Check if a vertex is represented in the edge array. If so, add + 1 to the amountOfVertices
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

