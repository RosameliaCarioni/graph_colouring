import java.util.*;
public class ChangingVertices{
  /**
  * Method changeVerticesIndex: it changes the number/value of the vertices to start from 1 up to colEdge.length. 
  * @param colEdges
  * @param n: total number of vertices in graph 
  * @return e: new ColEdge  
  */
  public static ColEdge[] changeVerticesIndex(ColEdge[] colEdges, int n){
    //creating a new colEdge array
    ColEdge[] e = new ColEdge[colEdges.length];
    for(int i =0; i < colEdges.length; i++){
      e[i] = new ColEdge();
      e[i].u = colEdges[i].u;
      e[i].v = colEdges[i].v;
    }
    //get vertices into a set, so it doesnt get duplicates
    Set <Integer> setOfvertices = new HashSet <Integer> ();
    for (int i=0; i< e.length; i++){
      setOfvertices.add(e[i].v);
      setOfvertices.add(e[i].u);
    }
    //pass them from set to array, because array has sort method
    int [] arrayOfVertices = new int[setOfvertices.size()];
    int counter=0;
    for (Integer vertice: setOfvertices){
      arrayOfVertices[counter++] = vertice;
    }
    //sort array
    Arrays.sort(arrayOfVertices);
    
    //loop throu colEdge and change the number of vertice with the position in the array
    for (int i=0; i<arrayOfVertices.length; i++){
      int vertice= arrayOfVertices[i];
      for (int j=0; j<e.length; j++){
        if (e[j].u == vertice){
          e[j].u = i+1;
        } else if((e[j].v == vertice)){
          e[j].v = i+1;
        }
      }
    }
    return e;
  }
}
