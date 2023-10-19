# README file for Project1.1 Group 20 Phase 1

## Project Details
This program reads in a graph from a .txt file and can calculate the exact, the lower and the upper bounds for the chromatic number.
1.This code is called from a cmd console, it executes on each text separately. With: 
javac ReadGraph.java
java ReadGraph graphXX_2020.text 
where XX is the number of the graph. 
2. You can choose to change the value of DEBUG to see more details about the code, and track if everything works well. 



## Program Execution
* To calculate the lower bound of the chromatic number, the method calculateLowerChromatic should be called. 
* To calculate the exact chromatic number, the method exactAlgorithm should be called.
* To calculate the upper bound of the chromatic number, the method calculateUpperBound should be called.
(Note that our exactAlgorithm first checks for some Less Trivial Graph Topologies calling different methods, in case any of them are true, it returns imediatelly the chromatic number of the graph.)
