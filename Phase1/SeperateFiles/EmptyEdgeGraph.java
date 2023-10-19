import java.io.*;
import java.util.*;

public class EmptyEdge {

	public static void main(String[] args){

		System.out.println("Is it an empty edge set: " + checkEmptyEdge(n, m));
		boolean EmptyEdge = checkEmptyEdge(n, m);
		if(EmptyEdge == false){
			System.out.println("Is it a cycle graph: " + /*CHECK ANOTHER GRAPH TOPOLOGY*/);
		}
		else
			System.out.println("Chromatic Number: 1");
		}


		public static boolean checkEmptyEdge(int n, int m){

			if(m == 0){
				return true;
			}
			else 
				return false;
		}	
}
