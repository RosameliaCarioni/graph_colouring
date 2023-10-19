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
