package com.celllineage;

/**
 * Represents a single cell in the cellular lineage simulation.
 * Each cell has a unique binary name, generation number, and cell cycle time.
 * Cells can divide into two daughter cells (left and right) and can enter a
 * quiescent state.
 */
public class Cell {
    private final int binaryCellName;
    private final int generation;
    private double cellCycleTime;
    private Cell left;
    private Cell right;
    private boolean isQuiescent;

    /**
     * Creates a new Cell with specified characteristics.
     *
     * @param binaryCellName The binary identifier for this cell in the lineage tree
     * @param generation     The generation number (zygote = 1, first division = 2,
     *                       etc.)
     * @param cellCycleTime  The time taken for this cell to complete one cell cycle
     */
    public Cell(int binaryCellName, int generation, double cellCycleTime) {
        this.binaryCellName = binaryCellName;
        this.generation = generation;
        this.cellCycleTime = cellCycleTime;
        this.left = null;
        this.right = null;
        this.isQuiescent = false;
    }

    /**
     * @return The binary name of this cell
     */
    public int getBinaryCellName() {
        return binaryCellName;
    }

    /**
     * @return The generation number of this cell
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * @return The current cell cycle time
     */
    public double getCellCycleTime() {
        return cellCycleTime;
    }

    /**
     * Updates the cell cycle time.
     * 
     * @param cellCycleTime New cell cycle time
     */
    public void setCellCycleTime(double cellCycleTime) {
        this.cellCycleTime = cellCycleTime;
    }

    /**
     * @return The left daughter cell
     */
    public Cell getLeft() {
        return left;
    }

    /**
     * Sets the left daughter cell.
     * 
     * @param left The left daughter cell
     */
    public void setLeft(Cell left) {
        this.left = left;
    }

    /**
     * @return The right daughter cell
     */
    public Cell getRight() {
        return right;
    }

    /**
     * Sets the right daughter cell.
     * 
     * @param right The right daughter cell
     */
    public void setRight(Cell right) {
        this.right = right;
    }

    /**
     * @return Whether the cell is in a quiescent state
     */
    public boolean isQuiescent() {
        return isQuiescent;
    }

    /**
     * Sets the quiescent state of the cell.
     * 
     * @param quiescent The new quiescent state
     */
    public void setQuiescent(boolean quiescent) {
        isQuiescent = quiescent;
    }

    /**
     * @return String representation of the cell
     */
    @Override
    public String toString() {
        return String.format("Cell{name=%s, gen=%d, cycle=%.2f, quiescent=%s}",
                Integer.toBinaryString(binaryCellName), generation, cellCycleTime, isQuiescent);
    }
}