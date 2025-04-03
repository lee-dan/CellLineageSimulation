package com.celllineage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Simulates the development of a cellular lineage tree, modeling cell division
 * patterns
 * and the emergence of specialized body parts through founder cells.
 */
public class WholeAnimalCellLineage {
    private Cell zygote;
    private final int totalNumberOfCells;
    private int numberOfAliveCells;
    private int currentGeneration;
    private final double presetCellCycleTime;
    private final List<String> founderCells;
    private final double mitoticFractionA;
    private final double mitoticFractionB;
    private final StringBuilder outputFormat;

    /**
     * Creates a new whole animal cell lineage simulation with the specified
     * parameters.
     *
     * @param totalCells       Total number of cells to simulate
     * @param cellCycleTime    Initial uniform cell cycle time
     * @param mitoticA         Parameter 'a' for mitotic fraction calculation
     * @param mitoticB         Parameter 'b' for mitotic fraction calculation
     * @param founderCellsList List of binary names for founder cells
     */
    public WholeAnimalCellLineage(int totalCells, double cellCycleTime, double mitoticA, double mitoticB,
            List<String> founderCellsList) {
        this.totalNumberOfCells = totalCells;
        this.presetCellCycleTime = cellCycleTime;
        this.mitoticFractionA = mitoticA;
        this.mitoticFractionB = mitoticB;
        this.founderCells = new ArrayList<>(founderCellsList);
        this.numberOfAliveCells = 1;
        this.currentGeneration = 1;
        this.outputFormat = new StringBuilder();
    }

    /**
     * Generates the cell lineage tree based on the specified parameters.
     */
    public void generate() {
        // Create the zygote (first cell)
        zygote = new Cell(0b1, 1, presetCellCycleTime);

        // Queue for processing cells in order
        Queue<Cell> divisionQueue = new LinkedList<>();
        divisionQueue.add(zygote);

        // Initialize output format with zygote
        outputFormat.append(zygote.getBinaryCellName())
                .append(":")
                .append(String.format("%.4f", zygote.getCellCycleTime()));

        int newickIndex = -1;

        while (numberOfAliveCells < totalNumberOfCells) {
            Cell currentCell = divisionQueue.remove();

            // Check for new generation
            if (isNewGeneration(currentCell)) {
                currentGeneration++;
                newickIndex = updateNewickIndexForNewGeneration(newickIndex);
            }

            // Process founder cells
            processFounderCell(currentCell);

            // Handle cell division or quiescence
            if (currentCell.isQuiescent()) {
                handleQuiescentCell(currentCell, divisionQueue, newickIndex);
            } else {
                newickIndex = handleCellDivision(currentCell, divisionQueue, newickIndex);
            }

            // Update newick index for next iteration
            newickIndex = updateNewickIndexForNextCell(newickIndex);
        }
    }

    private boolean isNewGeneration(Cell cell) {
        int binaryName = cell.getBinaryCellName();
        return binaryName == 0b1 ||
                (int) (Math.ceil(Math.log(binaryName) / Math.log(2))) == (int) (Math
                        .floor(Math.log(binaryName) / Math.log(2)));
    }

    private int updateNewickIndexForNewGeneration(int index) {
        int newIndex = 0;
        while (outputFormat.charAt(newIndex) == '(') {
            newIndex++;
        }
        return newIndex;
    }

    private void processFounderCell(Cell cell) {
        String cellBinaryName = Integer.toBinaryString(cell.getBinaryCellName());

        for (String founderCell : founderCells) {
            if (cellBinaryName.length() == founderCell.length() &&
                    cellBinaryName.equals(founderCell)) {
                // Set new random cell cycle time for founder cell
                double newCycleTime = presetCellCycleTime * 5 / 6 +
                        Math.random() * presetCellCycleTime * 1 / 3;
                cell.setCellCycleTime(newCycleTime);
                break;
            }
        }
    }

    private void handleQuiescentCell(Cell cell, Queue<Cell> queue, int newickIndex) {
        queue.add(cell);
        cell.setCellCycleTime(cell.getCellCycleTime() * 2);

        // Update output format
        int start = findCellCycleTimeStart(newickIndex);
        int end = findCellCycleTimeEnd(start);
        outputFormat.replace(start, end,
                String.format("%.4f", cell.getCellCycleTime()));
    }

    private int handleCellDivision(Cell cell, Queue<Cell> queue, int newickIndex) {
        // Create left daughter cell
        Cell leftDaughter = new Cell(
                cell.getBinaryCellName() * 0b10,
                currentGeneration,
                cell.getCellCycleTime());

        // Check quiescence for left daughter
        if (Math.random() > calculateMitoticFraction()) {
            leftDaughter.setQuiescent(true);
        }
        cell.setLeft(leftDaughter);
        queue.add(leftDaughter);

        // Create right daughter cell
        Cell rightDaughter = new Cell(
                cell.getBinaryCellName() * 0b10 + 0b1,
                currentGeneration,
                cell.getCellCycleTime());

        // Check quiescence for right daughter
        if (Math.random() > calculateMitoticFraction()) {
            rightDaughter.setQuiescent(true);
        }
        cell.setRight(rightDaughter);
        queue.add(rightDaughter);

        numberOfAliveCells++;

        // Update output format
        return updateOutputFormatForDivision(leftDaughter, rightDaughter, newickIndex);
    }

    private double calculateMitoticFraction() {
        return Math.pow(mitoticFractionA, Math.pow(numberOfAliveCells, mitoticFractionB));
    }

    private int findCellCycleTimeStart(int index) {
        while (outputFormat.charAt(index) != ':') {
            index++;
        }
        return index + 1;
    }

    private int findCellCycleTimeEnd(int start) {
        int end = start;
        while (end < outputFormat.length() &&
                outputFormat.charAt(end) != ',' &&
                outputFormat.charAt(end) != ')') {
            end++;
        }
        return end;
    }

    private int updateOutputFormatForDivision(Cell left, Cell right, int index) {
        String divisionFormat = String.format("(%d:%.4f,%d:%.4f)",
                left.getBinaryCellName(), left.getCellCycleTime(),
                right.getBinaryCellName(), right.getCellCycleTime());
        outputFormat.insert(index, divisionFormat);
        return index + divisionFormat.length();
    }

    private int updateNewickIndexForNextCell(int index) {
        while (index < outputFormat.length() &&
                outputFormat.charAt(index) != ',' &&
                currentGeneration != 1) {
            index++;
        }

        if (index < outputFormat.length()) {
            index++;
            while (index < outputFormat.length() &&
                    outputFormat.charAt(index) == '(') {
                index++;
            }
        }

        return index;
    }

    /**
     * Saves the cell lineage tree in Newick format to a file.
     *
     * @param filename The name of the file to save to
     * @throws IOException If there is an error writing to the file
     */
    public void saveToNewickFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(outputFormat.toString());
            writer.newLine();
        }
    }

    /**
     * @return The current generation number in the lineage
     */
    public int getCurrentGeneration() {
        return currentGeneration;
    }

    /**
     * @return The number of currently alive cells
     */
    public int getNumberOfAliveCells() {
        return numberOfAliveCells;
    }

    /**
     * @return The root (zygote) cell of the lineage
     */
    public Cell getZygote() {
        return zygote;
    }
}