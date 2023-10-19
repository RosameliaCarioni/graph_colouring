import java.util.Timer;
import java.util.TimerTask;

public class GeneticAlgorithm extends Thread {

  private Individual[] generation, firstParent, secondParent, offspring;
  private int populationSize = 1000;
  private double selectionRate = 0.05;
  private double mutationRate = 0.01;
  private int vertexCount;
  private int edgeCount;
  private ColEdge[] edges;
  private int upperBound;
  private int[][] adjacencyMatrix;
  private boolean solutionFound;

  private UpperBound ub;
  private int lowerBound;

  private int time;

  /**
  Method that keeps running the genetic algorithm until it found the exact chromatic number.
  */
  public void run()
  {
	  while (upperBound > lowerBound)
	  {
		  runGenetic();
	  }
  }

  /**
  Constructor method of the genetic algorithm.
  @param vertexCount the amount of vertices in the graph
  @param edgeCount the amount of edges in the graph
  @param edges the array containing all the edges in the graph
  @param upperBound the current best found upperBound (calculated by RLF algorithm)
  @param lowerBound the current best found lowerBound
  @param ub the variable storing the new upperbounds from the GA
  */
  public GeneticAlgorithm(int vertexCount, int edgeCount, ColEdge[] edges, int upperBound, int lowerBound, UpperBound ub) {
		    this.vertexCount = vertexCount;
		    this.edgeCount = edgeCount;
		    this.edges = edges;
		    this.upperBound = upperBound;
		    this.lowerBound = lowerBound;
		    this.ub = ub;
  }

  /**
  Method that runs the genetic algorithm until a correct colouring with k-1 colours is found.
  */
  public void runGenetic()
  {
	    solutionFound = false;
	    generation = new Individual[populationSize];
	    offspring = new Individual[populationSize];
	    firstParent = new Individual[populationSize];
	    secondParent = new Individual[populationSize];

	    for (int i = 0; i < populationSize; i++) {
	      offspring[i] = new Individual(vertexCount, edgeCount);
	    }

	  	adjacencyMatrix = ub.createAdjacencyMatrix(vertexCount, edges);
	    init();
	    calculateFitness();

	    while (!solutionFound) {
	      HeapSort.sort(generation);
	      selection();
	      crossover();
	      mutation();
	      System.arraycopy(offspring, 0, generation, 0, populationSize);
	      calculateFitness();
	    }

	    ub.setUpperBoundGA(--upperBound);
  }

  /**
  Method that initializes the first generation of individuals and their colourings.
  */
  public void init() {
    for (int k = 0; k < populationSize; k++) {
      generation[k] = new Individual(vertexCount, edgeCount);
      for (int i = 0; i < vertexCount; i++) {
        generation[k].colorList[i] = (int) (Math.random() * (upperBound - 1)) + 1;
      }
    }
  }

  /**
  Method to print the colourings of all individuals in a generation.
  */
  public void printGeneration() {
      for (int i = 0; i < generation.length; i++) {
        System.out.println(generation[i].toString());
      }
  }

  /**
  Method that calculates the fitness of an individual.
  */
  public void calculateFitness() {
    double wrongEdges = 0;
    for (int i = 0; i < populationSize; i++) {
      wrongEdges = 0;
      for (int j = 0; j < edges.length; j++) {
        if (generation[i].colorList[edges[j].u-1] == generation[i].colorList[edges[j].v-1]) {
          wrongEdges++;
        }
      }
      if (wrongEdges == 0) {
        solutionFound = true;
        generation[i].setFitness(1);
        break;
      }
      else if (wrongEdges == 1) {
        generation[i].setFitness(0.75);
      }
      else {
        double fitness = 1/ wrongEdges;
        generation[i].setFitness(fitness);
      }
    }
  }

  /**
  Method that selects the top individuals from a generation using an elitist approach.
  */
  private void selection() {
    double randomBounds = selectionRate * populationSize;
    for (int i = 0; i < populationSize; i++) {
      int topGen = (int) (Math.random() * (randomBounds));
      firstParent[i] = generation[topGen];
    }
    for (int i = 0; i < populationSize; i++) {
      int topGen = (int) (Math.random() * (randomBounds));
      secondParent[i] = generation[topGen];
    }
  }

  /**
  Method that creates new offsprings using uniform crossover.
  */
  private void crossover() {
    for (int i = 0; i < populationSize; i++) {
      for (int j = 0; j < vertexCount; j++) {
        if (Math.random() < 0.5) {
          offspring[i].colorList[j] = firstParent[i].colorList[j];
        }
        else {
          offspring[i].colorList[j] = secondParent[i].colorList[j];
        }
      }
    }
  }

  /**
  Method that has a small chance to change some of the colours in the colouring top increase uniqueness.
  */
  private void mutation() {
    for (Individual individual: offspring) {
      for (int i = 0; i < vertexCount; i++) {
        if (Math.random() < mutationRate) {
          individual.colorList[i] = (int) (Math.random() * (upperBound - 1)) + 1;
        }
      }
    }
  }
}
