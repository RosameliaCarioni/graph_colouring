import java.util.Arrays;

public class Individual {

  public int[] colorList;
  public double fitness;
  private int vertexCount;
  private int edgeCount;

  /**
  Constructor method for an Individual object. initializes an array for the colouring.
  @param vertexCount the amount of vertices in the graph
  @param edgeCount the amount of edges in the graph
  */
  public Individual(int vertexCount, int edgeCount) {
    this.vertexCount = vertexCount;
    this.edgeCount = edgeCount;
    colorList = new int[vertexCount];
    fitness = 0;
  }

  /**
  Method that returns the fitness value of an individual.
  @return the fitness value
  */
  public double getFitness() {
    return fitness;
  }

  /**
  Method to set the fitness of an individual.
  @param fitness the fitness value that we want to assign to some individual
  */
  public void setFitness(double fitness) {
    this.fitness = fitness;
  }

  /**
  Method to print the colouring of an individual.
  @return the colourlist of the individual
  */
  @Override
  public String toString() {
    return Arrays.toString(colorList);
  }
}
