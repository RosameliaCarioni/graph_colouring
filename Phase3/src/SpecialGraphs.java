import java.util.ArrayList;

public class SpecialGraphs {
	 /**
	 * A graph is Complete if every pair of distinct vertices is connected by a unique edge. These graphs have n(n-1)2 edges. The Chromatic number is n
	 *The implemented algorithm checks whether a graph is complete or not by seeing if the graph has the same number of edges as the formula previously shown.    
	 * @param vertexCount The amount of vertices 
	 * @param edgesArray The edges of the graph
	 * @return Returns value True if  graph is complete
	 */
	  public  boolean completeGraphCheck(int vertexCount, ColEdge[] edgesArray) {
	    //If all vertices are connected, we can say that the amount of edges is the same as this formula.
	    int edgeRequirement = (vertexCount * (vertexCount-1) / 2);
	    if (edgeRequirement == edgesArray.length) {
	      return true;
	    }
	    else {
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
	  public  boolean bipartite(ColEdge[] e, int edges, int n) {
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
    public int disconnected(ColEdge[] e, int n) {
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
    private  boolean checkIfVertexIsIn(ArrayList<Integer> array, int numberToCheck) {
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
    public int whichSet(ArrayList<Integer> set1, ArrayList<Integer> set2, int vertexValue) {
      for (int i = 0; i < set1.size(); i++) {
        if (set1.get(i) == vertexValue) {
          return 1;
        }
      }
      return 2;
    }


    /**
     * This method checks if a given graph is a WHEEL
     * @param e ColEdge[] which contains the edges information.
     * @param n This is the amount of vertices.
     * @param centerVertex This is the center vertex found.
     * @return True if it is a wheel, false if it's not.
     */
	  public  boolean checkIfGraphIsWheel(ColEdge[] e, int n, int centerVertex)
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

	  /**
	   * This method checks if a given graph is a cycle.
	   * @param e ColEdge[] Which contains the edges information.
	   * @param n The amount of vertices.
	   * @return True if it is a cycle, false if it isn't.
	   */
	  public  boolean checkIfGraphIsCycle(ColEdge[] e, int n)
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
	
	  /**
	   * This method checks if the graph has a center vertex. If it does, it could be a wheel.
	   * @param e ColEdge[] which contains the edges information.
	   * @param n The amount of vertices.
	   * @return The vertex that is the center vertex. If there isn't a center vertex it returns 0.
	   */
		 public  int checkForcenterVertex(ColEdge[] e, int n)
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

		 /**
		  * This method checks if the graph is even.
		  * @param n The amount of edges
		  * @return True if it is even, false if it isn't.
		  */
		 public  boolean checkIfGraphIsEven(int n)
		  {
		    if (n % 2 == 0)
		    {
		      return true;
		    }
		    else return false;
		  }
	
				/** 
		 		  * Method CHECK EDGELESS GRAPH: it check if a graph has no edges
		 		  * @param e: the edges of the graph
		 		  * @return true or false: if a graph has 0 edges
		 		  */
		 		public boolean checkEdgelessGraph (ColEdge[] e) {
		 			boolean result = false;
		 			if (e.length == 0) {
		 				result = true;
		 			}
		 			return result;
		 		}


}
