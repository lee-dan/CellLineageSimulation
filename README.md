# Cellular Phylodynamic Lineage Simulation

A Java-based simulation tool for modeling cell lineage development and body part formation in organisms. This software simulates cell division patterns, quiescence states, and the emergence of specialized body parts through founder cells.

## Overview

This simulation models cellular development using a binary tree structure where:

- Each cell has a unique binary name that represents its position in the lineage
- Cells can divide into two daughter cells or enter a quiescent (non-dividing) state
- Founder cells mark the beginning of specialized body parts with distinct cell cycle characteristics
- The simulation outputs a Newick format tree file that can be visualized using [IcyTree](https://icytree.org/)

## Features

- Binary tree-based cell lineage modeling
- Configurable cell cycle times
- Founder cell specification for body part development
- Quiescence state modeling based on mitotic fraction
- Newick format output for visualization
- Command-line interface for parameter input

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Installation

1. Clone the repository:

```bash
git clone https://github.com/lee-dan/CellLineageSimulation.git
cd CellLineageSimulation
```

2. Build the project:

```bash
mvn clean package
```

## Usage

Run the simulation using:

```bash
java -jar target/cell-lineage-simulation-1.0-SNAPSHOT.jar
```

The program will prompt you for the following parameters:

1. **Total number of cells**: The target number of cells to simulate
2. **Uniform cell cycle time**: The base cell cycle duration for non-founder cells
3. **Mitotic fraction parameters**:
   - Parameter 'a': Base probability component
   - Parameter 'b': Population scaling component
4. **Founder cell binary names**: Space-separated list of binary names for cells that will initiate new body parts

Example input:

```
Total number of cells to simulate: 100
Uniform cell cycle time: 1.0
Mitotic fraction parameter 'a': 0.95
Mitotic fraction parameter 'b': 0.1
Founder cell binary names: 10 11 100 101
```

## Output

The simulation generates a file named `WholeAnimalCellLineage.tree` in Newick format. This file can be visualized using [IcyTree](https://icytree.org/):

1. Visit https://icytree.org/
2. Upload your .tree file
3. Explore the generated cell lineage tree

## Technical Details

### Cell Naming Convention

Each cell in the lineage is assigned a unique binary name that represents its position in the tree:

- The zygote (first cell) is named "1"
- For any cell with binary name n:
  - Left daughter cell: n \* 2
  - Right daughter cell: (n \* 2) + 1

### Mitotic Fraction

The probability of a cell entering quiescence is calculated using:

```
P(quiescence) = 1 - a^(N^b)
```

where:

- N is the current number of cells
- a and b are user-specified parameters

### Founder Cells

Founder cells mark the beginning of specialized body parts and have modified cell cycle times:

```
new_cycle_time = base_time * (5/6 + random * 1/3)
```

## Author

**Daniel Lee** - [GitHub](https://github.com/lee-dan)

## Acknowledgments

This project was developed to study cell lineage development and body part formation through computational simulation.
