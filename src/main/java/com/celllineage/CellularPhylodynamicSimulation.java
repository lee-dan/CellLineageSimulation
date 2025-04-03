package com.celllineage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for running the Cellular Phylodynamic Lineage Simulation.
 * Provides a command-line interface for configuring and executing the
 * simulation.
 */
public class CellularPhylodynamicSimulation {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            SimulationParameters params = getSimulationParameters();
            runSimulation(params);
            System.out.println(
                    "Simulation completed successfully! The results have been saved to 'WholeAnimalCellLineage.tree'");
            System.out.println("You can visualize the results by uploading the .tree file to https://icytree.org/");
        } catch (Exception e) {
            System.err.println("Error running simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static SimulationParameters getSimulationParameters() {
        System.out.println("Welcome to the Cellular Phylodynamic Lineage Simulation");
        System.out.println("Please enter the following parameters:");

        System.out.print("Total number of cells to simulate: ");
        int totalCells = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Uniform cell cycle time: ");
        double cycleTime = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Mitotic fraction parameter 'a': ");
        double mitoticA = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Mitotic fraction parameter 'b': ");
        double mitoticB = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Founder cell binary names (space-separated): ");
        List<String> founderCells = Arrays.asList(scanner.nextLine().trim().split("\\s+"));

        return new SimulationParameters(totalCells, cycleTime, mitoticA, mitoticB, founderCells);
    }

    private static void runSimulation(SimulationParameters params) throws IOException {
        WholeAnimalCellLineage lineage = new WholeAnimalCellLineage(
                params.totalCells,
                params.cycleTime,
                params.mitoticA,
                params.mitoticB,
                params.founderCells);

        System.out.println("\nGenerating cell lineage...");
        lineage.generate();

        System.out.println("Saving results...");
        lineage.saveToNewickFile("WholeAnimalCellLineage.tree");
    }

    /**
     * Data class to hold simulation parameters
     */
    private static class SimulationParameters {
        final int totalCells;
        final double cycleTime;
        final double mitoticA;
        final double mitoticB;
        final List<String> founderCells;

        SimulationParameters(int totalCells, double cycleTime, double mitoticA,
                double mitoticB, List<String> founderCells) {
            this.totalCells = totalCells;
            this.cycleTime = cycleTime;
            this.mitoticA = mitoticA;
            this.mitoticB = mitoticB;
            this.founderCells = founderCells;
        }
    }
}