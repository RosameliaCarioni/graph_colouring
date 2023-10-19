class ColEdge {
  //variables
  int u;
  int v;
  
  /**
  * Default Constructor
  */
  public ColEdge(){
    this.u=0;
    this.v=0;
  }
  
  /**
  * Constructor 2
  * @param u
  * @param v
  */
  public ColEdge(int u, int v){
    this.u=u;
    this.v=v;
  }
  
  /**
  * Method toString: used for DEBUG
  * @return: a string with information about u and v 
  */
  public String toString (){
    return ("u " + u + " " + " v " + v);
  }
}
