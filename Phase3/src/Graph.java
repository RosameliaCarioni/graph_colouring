public class Graph {
	private ColEdge e[];
	private int n;
	private int m;
	private int lowerBound;
	private int upperBound;
	private int chromaticNumber;
	private int[] degreeVertices;
	
	/**
	* Constructor: takes in the location of the file to generate a Graph
	* @param location (String)
	*/
	public Graph (String location){
		ReadFile file = new ReadFile(location);
		this.e= file.getColEdge();
		this.n= file.getVertices();
		this.m= file.getNumberEdges();
		degreesVertices();
	}
	
	/**
	* Constructor: takes in the edges and the number of vertices of the graph
	* @param e
	* @param n
	*/
	public Graph(ColEdge[] e, int n) {
		this.e = e;
		this.m = e.length;
		this.n = n;
		degreesVertices();
	}
	
	/**
	* Default Constructor
	*/
	public Graph(){
	}
	
	/**
	* Method calculateUpperBoundTournament used to calculate the upper bound including printing the best upper bound found.
	*/
	public void calculateUpperBoundTournament()
	{
		//RUN UPPER BOUND FOR TOURNAMENT
		UpperBound ub = new UpperBound();
		ub.calculateUpperBoundTournament(n, e, this);
	}
	
	/**
	* Method calculateLowerBoundTournament: it calls the method calculateLowerBound with a false as input parameter.
	*/
	public void calculateLowerBoundTournament()
	{
		//RUN LOWER BOUND FOR TOURNAMENT
		System.out.println("NEW BEST LOWER BOUND = "+ calculateLowerBound(false));
	}
	
	/**
	* Method setUpperBound: It sets the UB
	* @param upperBound
	*/
	public void setUpperBound(int upperBound)
	{
		this.upperBound = upperBound;
	}
	
	/**
	* Method calculateLowerBound: takes in a boolean which determinates if results are printed or not and how long the method should run for (if applicable)
	* @param decomposed
	*/
	public int calculateLowerBound(boolean decomposed) {
		LowerBound lb = new LowerBound(n,e, decomposed, this);
		this.lowerBound = lb.calculateLowerBound();
		return lowerBound;
	}
	
	/**
	* Method calculateChromaticNumber
	*/
	public void calculateChromaticNumber (){
		ExactAlgorithm exact = new ExactAlgorithm();
		this.chromaticNumber = exact.calculateChromatic(this,e,n,m,this.calculateLowerBound(true),this.getUpperBound());
	}
	
	/**
	* Method clone: it clones a graph
	*/
	public Graph clone (){
		Graph cloned= new Graph();
		cloned.n= this.n;
		cloned.m= this.m;
		cloned.e = new ColEdge[m];
		for(int i =0;i<m;i++){
			cloned.e[i] = new ColEdge();
			cloned.e[i].u = e[i].u;
			cloned.e[i].v = e[i].v;
		}
		cloned.lowerBound= lowerBound;
		cloned.upperBound = upperBound;
		cloned.degreesVertices();
		return cloned;
	}
	
	/**
	* Method degreesVertices: calculates the degrees(how many connections a vertice has)
	* of the vertices and stores them in an array (degreeVertices)
	*/
	public void degreesVertices(){
		degreeVertices = new int [this.n];
		for (int i=0; i < this.m; i++){
			degreeVertices [this.e[i].u -1] += 1;   //-1 because vertices start at 1 but array starts at 0
			degreeVertices [this.e[i].v -1] += 1;
		}
	}
	
	/**
	* Method removeVertices: From a given graph, this method removes the vertices with
	* less than "degree" connections in each vertex.
	* @param degreee
	*/
	public void removeVertices (int degree){    //if a vertex has less than the degree, we get rid of it
		int reduceN=0;
		for (int i=0; i< n; i++){
			if (degreeVertices[i] < degree) {
				reduceN++;
				this.replace(i+1);
			}
		}
		//after the vertices get replaced with 0, need to copy it across and make that the new graph
		int counter=0;
		//find out size of my new colEdge
		for (int i=0; i<m; i++){
			if (e[i].u != 0 && e[i].v != 0 ){
				counter++;
			}
		}
		ColEdge newE [] = new ColEdge[counter];
		int newCounter=0;
		for (int i=0; i < m ;i++){
			if (e[i].u != 0 && e[i].v != 0 ){
				newE[newCounter] = new ColEdge();
				newE[newCounter].u = e[i].u;
				newE[newCounter].v = e[i].v;
				newCounter++;
			}
		}
		this.e = newE;
		this.m = newE.length;
		this.n = n-reduceN;
	}
	
	/**
	* Method replace: It replaces the vertices input with 0
	* @param vertexToRemove
	*/
	public void replace (int vertexToRemove){
		for (int i=0; i< m ; i++){
			if (e[i].u == vertexToRemove){
				e[i].u = 0;
			} else if (e[i].v == vertexToRemove){
				e[i].v = 0;
			}
		}
	}
	
	/**
	* Method replace: It replaces the vertices input with 0
	* @param vertexToRemove
	*/
	public int getUpperBound(){
		UpperBound up = new UpperBound();
		this.upperBound = up.calculateUpperBound(n, e);
		return upperBound;
	}
	
	/**
	* Accesor method: It returns the UB.
	* @return upperBound
	*/
	public int accessUpperBound(){
		return upperBound;
	}
	
	/**
	* Accesor method: It returns the LB.
	* @return lowerBound
	*/
	public int getLowerBound(){
		return lowerBound;
	}
	/**
	* Accesor method: It returns the chromatic Number
	* @return chromaticNumber
	*/
	public int getChromaticNumber(){
		return chromaticNumber;
	}
	/**
	* Accesor method: It returns the number of vertices.
	* @return n
	*/
	public int getNumberVertices(){
		return n;
	}
	/**
	* Accesor method: It returns the edges of the graph.
	* @return e
	*/
	public ColEdge[] getColEdge(){
		return e;
	}
	
	/**
	* Accesor method: It returns the total number of edges.
	* @return m
	*/
	public int getNumberEdges (){
		return m;
	}
}
