import java.io.*;
import java.util.*;

		class ColEdge
			{
			int u;
			int v;
			}
		
public class NewCompleteGraphChecker
		{
		
		public final static boolean DEBUG = true;
		
		public final static String COMMENT = "//";
		
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
						if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
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
						
						if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);
				
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

			

		System.out.println(CompleteGraphChecker(n, e));
			
			
			
				
			
		}
		public static boolean CompleteGraphChecker(int n, ColEdge e[]) {
	        //	Assigned to true  
			boolean CompleteGraph = true;
	        // check`s if all the vertices are connected each other.
	        for (int i = 1; i <= n; i++) {
	            for (int j = i + 1; j <= n; j++) {
	                boolean LinkedVertex = false;
	                for (int k = 0; k < e.length; k++) {
	                    if ((e[k].v == i && e[k].u == j) ||(e[k].u == i && e[k].v == j) ) {
	                        LinkedVertex = true;
	                    }
	                }
	                if (!LinkedVertex) {
	                    CompleteGraph = false;
	                    break;
	                }
	                else {
	                	continue;
	                }
	            }
	        }

	        return CompleteGraph;
			// If is true , the cromatic number will be n, the vertices of the graph. 
			
	    }


/*
Or this one ,
public static boolean CompleteChecker(int n, int m) {
	boolean result = false ;
	// The method takes the number of Edge and the number of Vertices.
	int EdgeRequirement = 0;
// if the formula (n*(n-1) / 2 (N is the vertices), equals to m (m is the edge) then our graph is a complete graph !  !	
	EdgeRequirement = (n * (n-1) / 2);
		
		if (EdgeRequirement == m ) {
			// result assigned to true  , , , 
			result = true;
			
		}
		else {
			result = false;
			// not a complete graph :(
			
		}
	return result;
// and done .
*/
}












}
