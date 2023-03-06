/*
    This class gives the ability for each tile of the maze to store if it's explored, the cost, heuristic,
    position, and the previous cell that was explored before going in to the current cell.
 */
public class Cell {

    private boolean isExplored = false;
    private double actualCost;
    private double heuristicAndActualCost;
    private int posX;
    private int posY;

    private Cell previousCell;


    // Initial values
    // cost and heuristic have infinity values at first
    Cell(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.actualCost = Double.POSITIVE_INFINITY;
        this.heuristicAndActualCost = Double.POSITIVE_INFINITY;
    }

    Cell(double actualCost, double heuristicAndActualCost) {
        this.actualCost = actualCost;
        this.heuristicAndActualCost = heuristicAndActualCost;
    }

    /*
        Getters and setters
     */

    public double getHeurActCost() {
        return heuristicAndActualCost;
    }

    public double getActualCost() {
        return actualCost;
    }

    public void setExplored(boolean isExplored) {
        this.isExplored = isExplored;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean getExplored() {
        return isExplored;
    }

    public void setPrev(Cell previousCell) {
        this.previousCell = previousCell;
    }

    public void setHeurActCost(double heuristicAndActualCost) {
        this.heuristicAndActualCost = heuristicAndActualCost;
    }

    public void setActualCost(double actualCost) {
        this.actualCost = actualCost;
    }

    public Cell getPreviousCell() {
        return previousCell;
    }
}
