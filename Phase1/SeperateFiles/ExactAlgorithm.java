import java.io.*;
import java.util.*;

class ColEdge
{
	int u;
	int v;
}

public class PossiblyExact
{

	public final static boolean DEBUG = false;

	public final static String COMMENT = "";

	public static int[] assignedColors;

	public static int[] options;

	public static void main( String args[] )
		{
		if( args.length < 1 )
			{
			System.out.println("Error! No filename specified.");
			System.exit(0);
			}

		String inputfile = args[0];

		boolean seen[] = null;

		//! n is the number of vertices in the graph
		int n = -1;

		//! m is the number of edges in the graph
		int m = -1;

		//! e will contain the edges of the graph
		ColEdge e[] = null;

		try 	{
					FileReader fr = new FileReader(inputfile);
						BufferedReader br = new BufferedReader(fr);

						String record = new String();

				//! THe first few lines of the file are allowed to be comments, staring with a // symbol.
				//! These comments are only allowed at the top of the file.

				//! -----------------------------------------
						while ((record = br.readLine()) != null)
					{
					if( record.startsWith("//") ) continue;
					break; // Saw a line that did not start with a comment -- time to start reading the data in!
					}

				if( record.startsWith("VERTICES = ") )
					{
					n = Integer.parseInt( record.substring(11) );
					if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
					}

				seen = new boolean[n+1];

				record = br.readLine();

				if( record.startsWith("EDGES = ") )
					{
					m = Integer.parseInt( record.substring(8) );
					if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
					}

				e = new ColEdge[m];

				for( int d=0; d<m; d++)
					{
					record = br.readLine();
					String data[] = record.split(" ");
					if( data.length != 2 )
							{
							System.out.println("Error! Malformed edge line: "+record);
							System.exit(0);
							}
					e[d] = new ColEdge();

					e[d].u = Integer.parseInt(data[0]);
					e[d].v = Integer.parseInt(data[1]);

					seen[ e[d].u ] = true;
					seen[ e[d].v ] = true;

					}

				String surplus = br.readLine();
				if( surplus != null )
					{
					if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");
					}

				}
		catch (IOException ex)
			{
					// catch possible io errors from readLine()
				System.out.println("Error! Problem reading file "+inputfile);
			System.exit(0);
			}

		for( int x=1; x<=n; x++ )
			{
			if( seen[x] == false )
				{
				if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
				}
			}

		runRecursion(e, n, 2, 4);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void runRecursion(ColEdge[] allEdges, int n, int lowerBound, int upperBound){

		// Array to store the isThereASolution value at assignedColors[i] refers to the color of the vertex
		assignedColors=new int[n];
		// The number of colors to check, starting at the lower bound
		int numberOfOptions = lowerBound;
		// used for tracking if a solution has been found
		boolean isThereASolution = false;
		while(!isThereASolution && numberOfOptions <= upperBound){
			// create a new array of options, this will increase with each iteration
			options = new int[numberOfOptions];
			for(int i =1; i <= numberOfOptions; i++){
				options[i-1]=i;
			}
			// call the recursive method to check if the graph can be solved with the given number of options
			isThereASolution = recurse(allEdges);
			// increase number of options
			numberOfOptions += 1;
		}

		System.out.println("Exact chromatic number: " + (numberOfOptions-1));

	}

	public static boolean recurse(ColEdge[] e){
		// Loop through all the edges
		for(int i = 0; i < e.length; i++){
			// Check if vertexes.u does not have a color yet
			if(assignedColors[e[i].u-1] == 0){
				// loop through all the color options
				for(int c: options){
					// check if we can assign the color to the empty vertex
					if(isPossible(e[i].u, c, e)){
						// Assign color to vertex
						assignedColors[e[i].u-1] = c;
						// recurse to solve another vertex
						if(!recurse(e)){
							// If the recursion fails backtrack and uncolor the vertex
							assignedColors[e[i].u-1] = 0;
						}
					}
				}
				// if there are vertices that are still not colored,
				// return false because there is no possible solution
				if(assignedColors[e[i].u-1] == 0){
					return false;
				}
			}
			// Does the same as previous if statment but for vertex.v
			if(assignedColors[e[i].v-1] == 0){
				for(int c: options){
					if(isPossible(e[i].v, c, e)){
						assignedColors[e[i].v-1] = c;
						if(!recurse(e)){
							assignedColors[e[i].v-1] = 0;
						}
					}
				}
				if(assignedColors[e[i].v-1] == 0){
					return false;
				}
			}
		}
		// If we exit the for loop without returning false, then all vertices are colored
		// And return true
		return true;
	}

	// Method to check if it is possible to give a vertex the given color
	// Iterate trough all the edges and check if any edge contains the vertex and
	// returns false if the other vertex on that edge has the the given color
	public static boolean isPossible(int vertex, int color, ColEdge[] e){
		for(int i = 0; i < e.length; i++){
			if(e[i].u == vertex){
				if(assignedColors[e[i].v-1] == color){
					return false;
				}
			}
			if(e[i].v == vertex){
				if(assignedColors[e[i].u-1] == color){
					return false;
				}
			}
		}
		// If no edge is found which makes this move impossible return true
		return true;
	}
}
