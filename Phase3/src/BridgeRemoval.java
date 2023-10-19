import java.util.ArrayList;
import java.util.*;

public class BridgeRemoval {
  public static final boolean DEBUG = false;
  
  /**
  * Method remove: it returns the graph or subgraphs (in case decomposition is possible) after removing the bridges from it. 
  * @param: graphs
  * @param: bridge (ArrayList of ColEdge)
  * @return: subGraphs (ArrayList of Graphs)
  */
  public static ArrayList<Graph>  remove (Graph graph, ArrayList<ColEdge> bridge){
    ArrayList<Graph> subGraphs= new ArrayList<Graph>(); //creating an ArrayList of graphs, where the subgraphs will be stored (if possible)
    ColEdge[] e= graph.getColEdge();
    
    if (DEBUG){ System.out.println("e " + Arrays.toString(e));}
    //go thru colEdge of graph and replace bridges with 0
    int counter=0;
    for (int i=0; i<bridge.size(); i++){
      ColEdge checking = bridge.get(i);
      for (int j=0; j<e.length; j++){
        if (checking.u==e[j].u && checking.v==e[j].v){
          e[j].u=0;
          e[j].v=0;
          counter++;
        } else if (checking.v==e[j].u && checking.u==e[j].v){
          e[j].u=0;
          e[j].v=0;
          counter++;
        }
      }
    }
    ColEdge[] newE= new ColEdge[e.length-counter]; //create a new ColEdge with the size of e minus the times that the bridge was found. 
    int counter2= 0;
    
    if (DEBUG){ System.out.println("e " + Arrays.toString(e));}
    
    //loop tru e and copy to newE the ColEdges different from 0 
    for(int i =0;i< e.length; i++){
      if (e[i].u!=0 && e[i].v!=0){
        newE[counter2] = new ColEdge();
        newE[counter2].u = e[i].u;
        newE[counter2].v = e[i].v;
        counter2++;
      }
    }
    if (DEBUG){
      System.out.println("NEW E");
      System.out.println(Arrays.toString(newE));
    }
    
    int n = countVerticesFromArray(newE); //count total number of vertices 
    //call method that changes the ColEdge to start vertices from 1 up to maximum 
    newE = ChangingVertices.changeVerticesIndex(newE, n);
    Graph newGraph= new Graph(newE,n);  //create a new graph 
    
    if (DEBUG){
      System.out.println("NEWGRAPH COLEGE: ");
      System.out.println(Arrays.toString(newGraph.getColEdge()));
    }
    subGraphs = DecomposeGraph.disconnect(newGraph);  //decompose new graph if disconnected components are found 
    return subGraphs;
  }
  /**
  * Method countVerticesFromArray: it counts the total number of vertices from a ColEdge array 
  * @param: graphEdges
  * @return: total number of vertices  
  */
  public static int countVerticesFromArray (ColEdge[] graphEdges){
    Set <Integer> vertices = new HashSet <Integer> ();
    for (int i=0; i< graphEdges.length; i++){
      vertices.add(graphEdges[i].v);
      vertices.add(graphEdges[i].u);
    }
    return vertices.size();
  }
}
