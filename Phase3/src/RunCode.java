import java.util.ArrayList;
import java.util.*;
public class RunCode {
	/**Main class, rune program from it. 
	@param args: name of the file to open  
	*/
	public static void main(String[] args) {
		boolean DEBUG= false;
		if( args.length < 1 )
		{
			System.out.println("Error! No filename specified.");
			System.exit(0);
		}
		String inputfile = args[0];
		//create a graph object
		Graph graph = new Graph(inputfile);
		// check if the graph is one of the basic types, if so chromatic number, UB and LB are printed  
		ExactAlgorithm checkIfIsBasicType = new ExactAlgorithm();
		int result = checkIfIsBasicType.calculateBasic(graph.getNumberVertices(), graph.getColEdge(),
		graph.getNumberEdges());
		
		if (result != -1) { // is because is a basic type
			System.out.println("NEW BEST LOWER BOUND = " + result);
			System.out.println("NEW BEST UPPER BOUND = " + result);
			System.out.println("CHROMATIC NUMBER = " + result);
			System.exit(0);
		} else {
			
			//start calculating up and low bound for TOURNAMENT
			graph.calculateUpperBoundTournament();
			graph.calculateLowerBoundTournament();
			
			//divide graph into subgraphs, if possible(if there are disconnected parts)
			ArrayList<Graph> subGraphs = DecomposeGraph.disconnect(graph);
			
			ArrayList<Integer> articulationPointsArray = new ArrayList <Integer>();
			ArrayList<ColEdge> bridgesArray = new ArrayList <ColEdge>();
			ArrayList<Graph> newSubGraphs = new ArrayList <Graph>();
			ArrayList<Graph> newSubGraphs2 = new ArrayList <Graph>();
			ArrayList<Graph> test = new ArrayList <Graph>();
			
			if (DEBUG) {System.out.println("After decomposition based on disconnected Elements, size of Subgraphs: " + subGraphs.size());}
			
			//from this subgraphs decompose them based on the bridges
			Bridge findBridge;
			for (int i=0; i<subGraphs.size(); i++){
				//calculate bridges of subgraphs
				findBridge = new Bridge (subGraphs.get(i));
				bridgesArray = findBridge.getBridges(); //finding out bridges
				if (bridgesArray.size()!=0 && subGraphs.get(i).getColEdge().length>1){ //if the graph has at least one bridge  and if the edges are more than 1, then remove them
					//split graph (if possible) based on bridges and store them in  newSubGraphs
					test = BridgeRemoval.remove(subGraphs.get(i), bridgesArray);
					newSubGraphs.addAll(test);
				}else { //if the graph doesnt have a bridge, add it directly into newSubGraphs
					newSubGraphs.add(subGraphs.get(i));
				}
			}
			if (DEBUG){System.out.println("After Bridges removal, size of newSubGraphs: " + newSubGraphs.size());}
			
			//from this subgraphs decompose them based on the articulationPoints
			ArticulationPoint findAP;
			for (int i=0; i<newSubGraphs.size(); i++){
				//calculate articulation point of newsubgraphs
				findAP = new ArticulationPoint (newSubGraphs.get(i));
				articulationPointsArray = findAP.countArticulationPoints(); //finding out articulationPoints
				if (articulationPointsArray.size()!=0){ //if the graph has at least one articulation point then
					//split graph based on articulationPoints and store them in  newSubGraphs2
					VerticeRemovalBasedOnArticulationPoint.remove(newSubGraphs.get(i), articulationPointsArray, newSubGraphs2);
				}else { //if the graph doesnt have an articulation point, add it directly into newSubGraphs2
					newSubGraphs2.add(newSubGraphs.get(i));
				}
			}
			
			if (DEBUG){System.out.println("After ArticulationPoint removal, final size of subGraphs: " + newSubGraphs2.size());}
			
			//from this newSubGraphs, calculate the chromatic number, the highest one will be the chromatic number of the graph.
			if (newSubGraphs2.size() > 1) {
				ArrayList<Integer> chromaticNumbers = new ArrayList<Integer>();
				ArrayList<Integer> lowerBounds = new ArrayList<Integer>();
				ArrayList<Integer> upperBounds = new ArrayList<Integer>();
				for (int i=0; i<newSubGraphs2.size(); i++){
					newSubGraphs2.get(i).calculateChromaticNumber();
					chromaticNumbers.add(newSubGraphs2.get(i).getChromaticNumber());
					lowerBounds.add(newSubGraphs2.get(i).getLowerBound());
					upperBounds.add(newSubGraphs2.get(i).accessUpperBound());
				}
				Collections.sort(chromaticNumbers);
				if (DEBUG){
					System.out.println("Chromatic Numbers:      " +  chromaticNumbers);
					System.out.println("Lower Bounds:      " +  lowerBounds);
					System.out.println("Upper Bounds:      " +  upperBounds);
				}
				int x= chromaticNumbers.get(chromaticNumbers.size()-1);
				System.out.println("NEW BEST LOWER BOUND = " + x);
				System.out.println("NEW BEST UPPER BOUND = " + x);
				System.out.println("CHROMATIC NUMBER = " + x);
				System.exit(0);
			}
			
			else { //if the graph couldnt be decomposed into more than 1 
				newSubGraphs2.get(0).calculateChromaticNumber();
				System.out.println ("NEW BEST LOWER BOUND = " + newSubGraphs2.get(0).getChromaticNumber());
				System.out.println("NEW BEST UPPER BOUND =  " + newSubGraphs2.get(0).getChromaticNumber());
				System.out.println ("CHROMATIC NUMBER = " + newSubGraphs2.get(0).getChromaticNumber());
				System.exit(0);
			}
		}
	}
}
