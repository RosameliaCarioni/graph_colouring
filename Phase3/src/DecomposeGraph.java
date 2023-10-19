import java.util.ArrayList;
import java.util.*;

public class DecomposeGraph {
  public static final boolean DEBUG = false;
  /**
  * Method disconnect:  takes the graph you want to disconnect and returns an ArrayList with subgraphs
  * @param graph
  * @return subGraphs
  */
  public static ArrayList<Graph> disconnect (Graph graph){
    ColEdge[] e = graph.getColEdge();
    ArrayList<Integer> checked = new ArrayList<Integer>();
    ArrayList<Graph> subGraphsArray = new ArrayList<Graph>();
    ArrayList<Integer> toCheckArray;
    ArrayList<ColEdge> newGraphEdges;
    int[] connectedVertices= new int [graph.getNumberVertices()];
    
    //first check disconnected vertices that are not in the colEdge.
    //loop adds in connectedVertices 1 if the vertice is in colEdge, leaves 0 if is not.
    for (int i=0; i<graph.getNumberEdges(); i++){
      connectedVertices[e[i].v -1]= 1;
      connectedVertices[e[i].u -1]= 1;
    }
    //add in checked the vertices that are disconnected (represented by 0 in connectedVertices)
    for (int i=0; i<connectedVertices.length; i++){
      if (connectedVertices[i] == 0){
        checked.add(i+1);
      }
    }
    
    while (checked.size() != graph.getNumberVertices()){
      toCheckArray = new ArrayList<Integer>();
      newGraphEdges = new ArrayList<ColEdge>();
      
      //checking which vertice hasnt been checked and add it to be check
      for (int i=0; i<graph.getNumberEdges(); i++){
        if (!isVerticeIn (e[i].u,checked) || !isVerticeIn (e[i].v,checked) ){
          toCheckArray.add(e[i].u);
          toCheckArray.add(e[i].v);
          if (DEBUG){
            System.out.println("TO CHECK ARRAY " + toCheckArray);
          }
          break;
        }
      }
      
      while (toCheckArray.size()!=0){
        int check = toCheckArray.get(0);
        checked.add(check);
        toCheckArray.remove(0);
        
        for (int i=0; i<graph.getNumberEdges(); i++){
          if (e[i].v == check || e[i].u == check) {
            if (isNotDuplicate(e[i], newGraphEdges)){
              newGraphEdges.add(e[i]);
              if ((!isVerticeIn(e[i].v, checked)) && (!isVerticeIn (e[i].v, toCheckArray))){
                toCheckArray.add(e[i].v);
              } if ((!isVerticeIn (e[i].u, checked)) && (!isVerticeIn (e[i].u, toCheckArray))){
                toCheckArray.add(e[i].u);
              }
            }
          }
        }
      }
      if (DEBUG){
        System.out.println("NEW GRAPH EDGES " + newGraphEdges);
      }
      //create a new graph with the edges
      int n= countVertices(newGraphEdges);
      ColEdge[] toSend = arraylistToArray(newGraphEdges);
      //call method that changes the ColEdge to start vertices from 1
      toSend = ChangingVertices.changeVerticesIndex(toSend, n);
      //create graph and store it in subGraphs array
      Graph subGraph = new Graph(toSend, n);
      subGraphsArray.add(subGraph);
    }
    return subGraphsArray;
  }
  
  /**
  * Method arraylistToArray:  takes an ArrayList of ColEdge and returns a ColEdge array.
  * @param original (ArrayList)
  * @return newArray
  */
  public static ColEdge[] arraylistToArray (ArrayList<ColEdge> original){
    ColEdge [] newArray = new ColEdge[original.size()];
    for (int i =0; i<original.size(); i++){
      newArray[i]= new ColEdge();
      newArray[i].u= original.get(i).u;
      newArray[i].v= original.get(i).v;
    }
    return newArray;
  }
  
  /**
  * Method countVertices: counts the total number of vertices in a the ColEdge of a graph
  * @param graphEdges
  * @return number of vertices
  */
  public static int countVertices (ArrayList<ColEdge> graphEdges){
    //using sets because they dont add repeated items
    Set <Integer> vertices = new HashSet <Integer> ();
    for (int i=0; i< graphEdges.size(); i++){
      vertices.add(graphEdges.get(i).v);
      vertices.add(graphEdges.get(i).u);
    }
    return vertices.size();
  }
  
  /**
  * Method isNotDuplicate: checks wether
  * @param graphEdges
  * @return number of vertices
  */
  public static boolean isNotDuplicate (ColEdge e, ArrayList<ColEdge> array){
    for (int i=0; i<array.size(); i++){
      if ((e.u == array.get(i).u) && (e.v == array.get(i).v)){     //not sure about this
        return false;
      }
    }
    return true;
  }
  
  /**
  * Method isVerticeIn: checks wether a vertice is in an array of vertices (Integer)
  * @param array
  * @param vertice
  * @return true or false
  */
  public static boolean isVerticeIn (int vertice, ArrayList<Integer> array){
    for (int i=0; i< array.size(); i++){
      if (vertice == array.get(i)){
        return true;
      }
    }
    return false;
  }
}
