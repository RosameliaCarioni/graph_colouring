import java.io.*;
import java.util.*;
/**
* LOGIC:
* 1.check if low bound= up bound, if so, return that as chromatic number
* 2. check if is special type graph
* 3. Reduce vertices 
* 4. calculate chromatic
*/ 
public class ExactAlgorithm{
public final boolean DEBUG = false;
private int[] assignedColors;
private int[] options;

  //Code for calculating the exact chromatic number.
  public int calculateChromatic(Graph graph, ColEdge[] e, int n, int m, int lowerBound, int upperBound) {
   //First we perform all the checks for the simple graph algorithms.
   if (lowerBound == upperBound){
     return lowerBound;
   }
   int chromaticNumber = calculateBasic(n, e, m); //it returns -1 if is not one of the basic ones, else the correct chromatic number
    if (chromaticNumber!= -1){
      return chromaticNumber;
      //If the given graph is not one of the simple graphs, we calculate the chromatic number recursively.
    } else {
      return runRecursion(graph, e, n, lowerBound, upperBound);
    }
  }

  public int runRecursion(Graph graph, ColEdge[] allEdges, int n, int lowerBound, int upperBound){
		// The number of colors to check, starting at the lower bound
		int numberOfOptions = upperBound;
		// used for tracking if a solution has been found
		boolean isThereASolution = true;
		while(isThereASolution && numberOfOptions-1 >= lowerBound){
			numberOfOptions -= 1;
			// Array to store the isThereASolution value at assignedColors[i] refers to the color of the vertex
			assignedColors=new int[n];
			// create a new array of options, this will increase with each iteration
			options = new int[numberOfOptions];

    //REDUCE SIZE OF GRAPH BASED ON CHROMATIC NUMBER THAT IS GETTING TESTED: by eliminating the vertices that have less than "numberOfOptions" connections
    Graph newGraph = graph.clone();
    newGraph.removeVertices(numberOfOptions);

  	for(int i =1; i <= numberOfOptions; i++){
				options[i-1]=i;
			}
			// call the recursive method to check if the graph can be solved with the given number of options
			isThereASolution = recurse(newGraph.getColEdge());
			// increase number of options
		}
		if (!isThereASolution)
		{
			return numberOfOptions+1;
		}
		else return numberOfOptions;
	}

	public boolean recurse(ColEdge[] e){
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
	public boolean isPossible(int vertex, int color, ColEdge[] e){
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


    public int calculateBasic (int n, ColEdge[] e, int m){
      SpecialGraphs specialGraph= new SpecialGraphs();
      if(specialGraph.checkEdgelessGraph(e)) {
    	  if (DEBUG) {
    		  System.out.println("The chromatic number is : " + " 1 " + ", because it`s an edgeless graph. " );
    	  return 1;
    	  }
      }
      if (specialGraph.completeGraphCheck(n, e))
       {
         if(DEBUG)
         System.out.println("The chromatic number is: " + n + ", because it's a complete graph.");
       return n;
       }
       if (specialGraph.bipartite(e, m, n))
       {
         if(DEBUG)
         System.out.println("The chromatic number: 2, because it's a bipartite graph.");
       return 2;
       }
         int centerVertex = specialGraph.checkForcenterVertex(e, n);
       boolean graphIsEven = specialGraph.checkIfGraphIsEven(n);

       if (centerVertex != 0)
         {
           if (specialGraph.checkIfGraphIsWheel(e, n, centerVertex))
           {
             if (graphIsEven)
             {
               if(DEBUG)
               System.out.println("The chromatic number is: 4, because it's an even wheel.");
             return 4;
             }
             else{
               if(DEBUG)
               System.out.println("The chromatic number is: 3, because it's an odd wheel.");
             return 3;
           }
           }
         }

       if (specialGraph.checkIfGraphIsCycle(e, n))
       {
         if (graphIsEven)
         {
           if(DEBUG)
           System.out.println("The chromatic number is: 2, because it's an even cycle.");
         return 2;
         }
         else{
           if(DEBUG)
           System.out.println("The chromatic number is: 3, because it's an odd cycle.");
         return 3;
       }
     } else {
       return -1;
     }
   }

}
