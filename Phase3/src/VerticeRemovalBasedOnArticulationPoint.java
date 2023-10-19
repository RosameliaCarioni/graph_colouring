import java.util.ArrayList;
import java.util.*;

public class VerticeRemovalBasedOnArticulationPoint {
  public static final boolean DEBUG = false;
  /**
  * Method remove: it recursively removes the AP of a graph.
  * @param graph: the graph from which you want to remove the AP
  * @param articulationPoints
  * @param resultAP: ArrayList of graphs that stores the graphs that cant be decompose anymore
  */
  public static  void remove (Graph graph, ArrayList<Integer> articulationPoints, ArrayList<Graph> resultAP){
    ArrayList<Graph> subGraphs= new ArrayList<Graph>();
    ArrayList <ColEdge[]> subColEdges = new ArrayList <ColEdge[]>();
    ArticulationPoint findAP;
    ArrayList<Integer> articulationPointsArray = new ArrayList <Integer>();
    
    subColEdges.addAll(disconnect(graph.getColEdge(), articulationPoints.get(0)));
    //transform the ColEdge array into a graph array
    subGraphs = convertToGraphs(subColEdges);
    
    for (int i=0; i<subGraphs.size(); i++){
      //calculate articulation point of subGraphs
      findAP = new ArticulationPoint (subGraphs.get(i));
      articulationPointsArray = findAP.countArticulationPoints(); //finding out articulationPoints
      if (articulationPointsArray.size()!=0){ //if the graph has at least one articulation point then
        //recursively call remove to split graph based on articulationPoints
        remove(subGraphs.get(i), articulationPointsArray, resultAP);
      }else { //if the graph doesnt have an articulation point, add it directly into resultAP
        resultAP.add(subGraphs.get(i));
      }
    }
    
    
  }
  /**
  * Method disconnect: it  takes the colEdge array you want to disconnect and
  * returns an array with disconnected ColEdges
  * @param e: ColEdge
  * @param verticeToIgnore
  * @return subColEdgeArray
  */
  public static ArrayList<ColEdge[]> disconnect (ColEdge[] e, int verticeToIgnore){
    ArrayList<Integer> checked = new ArrayList<Integer>();
    ArrayList<ColEdge[]> subColEdgeArray = new ArrayList<ColEdge[]>();
    ArrayList<ColEdge> newGraphEdges;
    ArrayList<Integer> toCheckArray;
    int numberVertices= countVerticesFromArray(e);
    
    checked.add(verticeToIgnore);
    
    while (checked.size() != numberVertices){
      toCheckArray = new ArrayList<Integer>();
      newGraphEdges = new ArrayList<ColEdge>();
      
      //checking which vertice hasnt been checked and adding it to be check
      for (int i=0; i<e.length; i++){
        if (!isVerticeIn (e[i].u,checked) || !isVerticeIn (e[i].v,checked) ){
          //newGraphEdges.add(e[i]);
          if (e[i].u != verticeToIgnore){    //NEW CODE
            toCheckArray.add(e[i].u);
          } if (e[i].v != verticeToIgnore){
            toCheckArray.add(e[i].v);
          }
          if (DEBUG){ System.out.println("TO CHECK ARRAY, line 60 " + toCheckArray);}
          break;
        }
      }
      
      
      while (toCheckArray.size()!=0){
        int check = toCheckArray.get(0);
        checked.add(check);
        toCheckArray.remove(0);
        if (DEBUG){ System.out.println("CHECKING, line 70  " + check);}
        
        for (int i=0; i<e.length; i++){
          if (e[i].v == check || e[i].u == check) {
            if (isNotDuplicate(e[i], newGraphEdges)){
              newGraphEdges.add(e[i]);
              if (DEBUG){ System.out.println("NEW GRAPHS EDGES" + newGraphEdges);}
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
        System.out.println("TO CHECK ARRAY " + toCheckArray);
        System.out.println("NEW GRAPH EDGES, line 87 " + newGraphEdges);
        System.out.println("NUMBER OF VERTICES       " + numberVertices + "checked size " + checked.size());
      }
      //add colEdge to array of colEdges
      subColEdgeArray.add(arraylistToArray(newGraphEdges));
    }
    if (DEBUG){ System.out.println("subGraphsArray from disconnect method    " + subColEdgeArray);}
    return subColEdgeArray;
  }
  
  /**
  * Method convertToGraphs: it  takes an ArrayList of ColEdge and converts it into an ArrayList of Graphs
  * @param e
  * @return graphs
  */
  public static ArrayList<Graph> convertToGraphs (ArrayList<ColEdge[]> e ){
    ArrayList<Graph> graphs = new ArrayList<Graph>();
    int n;
    ColEdge[] toSend;
    for (int i=0; i<e.size(); i++){
      n= countVerticesFromArray(e.get(i));
      //change arrayList to Array
      toSend =e.get(i);
      //call method that changes the ColEdge to start vertices from 1
      toSend = ChangingVertices.changeVerticesIndex(toSend, n);
      //create graph and store it in subGraphs array
      Graph subGraph = new Graph(toSend, n);
      //add it to the graphs array
      graphs.add(subGraph);
    }
    return graphs;
  }
  
  /**
  * Method arraylistToArray: it  takes an ArrayList of ColEdge and converts it into a ColEdge array
  * @param original
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
  * Method countVertices: it  takes an ArrayList of ColEdge and counts how many vertices are on it
  * @param graphEdges: ArrayList<ColEdge>
  * @return vertices
  */
  public static int countVertices (ArrayList<ColEdge> graphEdges){
    Set <Integer> vertices = new HashSet <Integer> ();
    for (int i=0; i< graphEdges.size(); i++){
      vertices.add(graphEdges.get(i).v);
      vertices.add(graphEdges.get(i).u);
    }
    return vertices.size();
  }
  
  /**
  * Method countVertices: it  takes a ColEdge Array and counts how many vertices are on it
  * @param graphEdges: ColEdge []
  * @return vertices
  */
  public static int countVerticesFromArray (ColEdge[] graphEdges){
    Set <Integer> vertices = new HashSet <Integer> ();
    for (int i=0; i< graphEdges.length; i++){
      vertices.add(graphEdges[i].v);
      vertices.add(graphEdges[i].u);
    }
    return vertices.size();
  }
  
  /**
  * Method isNotDuplicate: it checks if a ColEdge is in an array of ColEdges
  * @param e
  * @param array
  * @return true or false
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
  * Method isNotDuplicate: it checks if a vertice is in an ArrayList
  * @param vertice
  * @param array
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
  
  /**
  * Method isNotDuplicate: it checks if a number is in a ColEdge array
  * @param e
  * @param checking
  * @return true or false
  */
  public static boolean isNumberIn (int checking, ColEdge[] e){
    for (int i=0; i<e.length; i++){
      if (e[i].u == checking || e[i].v== checking){
        return true;
      }
    }
    return false;
  }
}
