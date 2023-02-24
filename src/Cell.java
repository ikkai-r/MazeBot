public class Cell {

    private boolean isExplored = false;
    private double actualCost;
    private double heuristicAndActualCost;
    private int posX;
    private int posY;

    private Cell previousCell;


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
