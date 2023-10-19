# Graph Colouring Project

## Objective:
The primary aim of this project is to enhance the calculation speeds for determining the chromatic number of a graph, which holds significant potential in addressing real-world challenges. Ascertaining the chromatic number of a graph is classified as an NP-complete problem. Consequently, this research is centered on identifying the smallest interval of the chromatic number through lower and upper bounds. The problem was tackled using a variety of methods:

- Graph Decomposition
- Recognizing Simple Graph Topologies
- Solving the Max-Clique Problem (for Lower Bound calculation)
- Algorithms for Upper Bound:
  - Reverse Backtracking
  - Greedy Algorithm
  - RLF (Recursive Largest First)
  - Genetic Algorithm

For graphs that are relatively small or possess specific characteristics, the chromatic number is computed instantaneously. On the other hand, larger graphs, up to a certain size, are processed within a reasonable timeframe. Given the current computational limitations in solving NP-complete problems (including those faced by Quantum computers), this research demonstrates that by providing precise instructions to computers, employing a multi-algorithmic strategy, and optimizing the code, it is feasible to obtain results even for graphs with high complexity.

## Tournament Achievement:
This code was competed in a tournament at Maastricht University, and we are proud to announce that we secured the 4th position out of 27 participating groups.

## Essay:
For a detailed understanding of the research and methodologies employed in this project, you can refer to the [essay](https://drive.google.com/file/d/1Ung9Gr5SY_6gV8dDxByq9o8QWv_XysIR/view?usp=share_link).

## Getting Started:
To get started with this project, follow the instructions provided in each phase's respective README.
