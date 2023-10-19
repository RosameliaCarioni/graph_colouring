import java.io.*;
import java.math.BigInteger;
import java.util.*;

		class ColEdge
			{
			int u;
			int v;
			}

public class TrialLowBound
		{

		public final static boolean DEBUG = false;

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

			//! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
			//! e[1] will be the second edge...
			//! (and so on)
			//! e[m-1] will be the last edge
			//!
			//! there will be n vertices in the graph, numbered 1 to n

			//! INSERT YOUR CODE HERE!

			//System.out.println("Chromatic number (lower bound): " + calculateLowerChromatic(n, m));
			calculateLowerChromatic(n, m);

		}


		  public static void calculateLowerChromatic(int vertices, int edges){

		//If the graph was complete the total possible edges can be calculated using the formula
		double totalPossibleEdges= (vertices*(vertices-1))/2;

		int x = 2;
		double probability =  1;
		int chrom = 2;

		/* The formula to calculate the probability that there exists at least one clique with "x" vertices
		 * as a subset of our graph is: 1-(1-(edges/totalPossibleEdges)^power(xC2))^power(nCx) . 
		 * */

		//We considered cliques of x vertices with a probability higher than 0.9
		while(x<=vertices && probability > 0.9){
			BigInteger power1= combination(x, 2);

			BigInteger power2 =  combination(vertices, x);

			double step1= power (edges/totalPossibleEdges, power1);

			double step2= power (1-step1 , power2);

			probability= 1-step2;

			if(probability > 0.99)
				chrom = x;

			x++;
		}

		System.out.println("With greater than 99% confidence we can say that"
				+ " the graph has at least a clique of size: " + chrom + ". This is our Lower Bound.");
	}




	//This method calculates nCk
	public static BigInteger combination(int n, int k) {

		//Calculate the factorial the numerator divided by the factorial of n-k
		BigInteger numerator = permutation(n, k);
		//While loop is used because calculating the factorial recursively using BigInteger is complex
		while ( k > 0 ) {
			numerator = numerator.divide(BigInteger.valueOf(k));
			k--;
		}
		return numerator;
	}


	public static BigInteger permutation(int n, int k) {
		//Start with result = 1
		BigInteger result = BigInteger.ONE;

		//Simplify n! with (n-k)!, it is only multiplying from n to n-k+1
		for ( int i = n ; i >= n-k+1 ; i-- ) {
			result = result.multiply(BigInteger.valueOf(i));
		}
		return result;
	}



	public static double power(double base, BigInteger exponent) {
		double result=1;

		//While loop stops if the exponent is negative or 0
		while (exponent.signum() > 0) {
			//returns true iff the designated bit is a set
			if (exponent.testBit(0)){
				result = result*base;
			}
			base = base*base;
			//exponent decreases by 1
			exponent = exponent.shiftRight(1);
		}
		return result;
	}

}
