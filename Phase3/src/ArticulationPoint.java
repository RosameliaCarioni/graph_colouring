import java.util.*;
public class ArticulationPoint {
  //variables
  int[] parent;
  int[] timeDiscovered;
  int[] timeFinished;
  int[] status; //0 initial, 1 if discovered, 2 if finished
  boolean [] articulationPoint;
  int time;
  int [] low;
  Graph graph;
  final boolean DEBUG = false;  
  
  /**
  * Constructor: it initializes the variables and calls visit method for each vertex in the graph, and  it changes their status. 
  * @param graph: the graph from which you want to calculate its articulationPoints
  */
  public ArticulationPoint (Graph graph){
    parent= new int [graph.getNumberVertices()]; //initial 0
    timeDiscovered= new int [graph.getNumberVertices()];
    timeFinished= new int [graph.getNumberVertices()];
    status =new int [graph.getNumberVertices()]; 
    articulationPoint = new boolean [graph.getNumberVertices()];
    low = new int [graph.getNumberVertices()];
    time=0;
    this.graph= graph;
    
    //for each vertex in the graph
    for (int i=0; i<graph.getNumberVertices(); i++){
      if (status[i]==0){
        visit(i+1);
      }
    }
  }
  
  /**
  * Method Visit: 
  * @param vertice: the vertice to which we want to find out if its an articulationPoint. 
  * Note that because we are using ArraysLists, we use vertice-1 to access to the vertice position and change the status in a specific array
  */
  public void visit (int vertice){
    time++;   //time increases 
    timeDiscovered[vertice-1]= time;  //the vertex is discovered at the current value of time 
    status[vertice-1]= 1;   //status is discovered
    low[vertice-1]= timeDiscovered[vertice-1]; 
    int children=0;
    
    //for eachadjacent vertex of "vertice" in the graph
    int otherVertice;
    for (int i=0; i<graph.getColEdge().length; i++){
      if (graph.getColEdge()[i].u == vertice){    //if the vertice is in u 
        otherVertice= graph.getColEdge()[i].v;
        if (status[otherVertice-1]==0){ //if it hasnt been discovered, we select the parent to be the vertice
          children++;
          parent[otherVertice-1]= vertice;
          visit(otherVertice);
          
          low[vertice-1]= Math.min (low[vertice-1], low[otherVertice-1]);
          if (parent[vertice-1]==0 && children>1){ //CASE 1: if it is the root (it does not have a parent)  and it has at least 2 children
            articulationPoint[vertice-1]= true;
          } if (parent[vertice-1]!=0 && (low[otherVertice-1]>= timeDiscovered[vertice-1])){ //CASE 2: LOW (d) ≥ TimeDiscovered(v)
            articulationPoint[vertice-1]= true;
          }
        }
        else if(otherVertice != parent[vertice-1]){
          low[vertice-1]= Math.min(low[vertice-1], timeDiscovered[otherVertice-1]);
        }
      }
      else if (graph.getColEdge()[i].v == vertice){   //if the vertice is in v
        otherVertice = graph.getColEdge()[i].u;
        if (status[otherVertice-1]==0){ //if it hasnt been discovered, we select the parent to be the vertice
          children++;
          parent[otherVertice-1]= vertice;
          visit(otherVertice);    
          
          low[vertice-1]= Math.min (low[vertice-1], low[otherVertice-1]);
          if (parent[vertice-1]==0 && children>1){ //CASE 1: if it is the root (it does not have a parent)  and it has at least 2 children
            articulationPoint[vertice-1]= true;
          } if (parent[vertice-1]!=0 && (low[otherVertice-1]>= timeDiscovered[vertice-1])){ //CASE 2: LOW (d) ≥ TimeDiscovered(v)
            articulationPoint[vertice-1]= true;
          }
        }
        else if(graph.getColEdge()[i].u != parent[vertice-1]){
          low[vertice-1]= Math.min(low[vertice-1], timeDiscovered[otherVertice-1]);
        }
      }
      status[vertice-1]= 2; //status finished 
      time++; //time increases 
      timeFinished[vertice-1]= time;    //the time the search finishes for the vertex 
    }
  }
  
  /**
  * Accessor method: it retursn the vertices that are ArticulationPoints 
  * @return: vertices (ArrayList of Integers)
  */
  public ArrayList<Integer> countArticulationPoints(){
    ArrayList<Integer>vertices = new ArrayList<Integer>();
    for (int i=0; i<articulationPoint.length; i++){
      if (articulationPoint[i]==true){
        vertices.add(i+1);
      }
    }
    if (DEBUG){
      System.out.println("VERTICES of articulation POINT " + vertices);
    }
    return vertices;
  }
}
