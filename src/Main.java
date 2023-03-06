import java.util.*;
import java.io.*;

//isang file muna lahat kasi di nagana github sa comlabs

/*
    This program finds the optimal path for a maze using A* Search, if the path to the goal exists.
 */
public class Main {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_WHITE_BG = "\u001B[47,";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws InterruptedException, IOException {

        // scanning from input
        List<Object> fileOut = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        fileOut = readFile();

        // from the input, take the size and the layout of the maze
        int size = (int) fileOut.get(0);
        char[][] arrMaze = (char[][]) fileOut.get(1);

        Cell[][] cellVisited = new Cell[size][size];


        // for each tile, we create a cell so we can keep track of the tile's status
        for (int i = 0; i < cellVisited.length; i++) {
            for (int j = 0; j < cellVisited.length; j++) {
                cellVisited[i][j] = new Cell(i, j);
            }
        }

        searchForGoal(arrMaze, cellVisited);

    }

    /*
        Gets the size and the maze's layout from the inputted file
     */
    public static List<Object> readFile() {

        List<Object> objectList = new ArrayList<>();

        char[][] arrMaze = null;
        int size = -1;
        int ctr = -1;

        File file = new File("maze//maze.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {

                // input: size
                if (ctr == -1) {
                    //parse int
                    size = Integer.parseInt(line);
                    arrMaze = new char[size][size];

                // input: maze
                } else {
                    arrMaze[ctr] = line.toCharArray();
                }

                ctr++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        objectList.add(size);
        objectList.add(arrMaze);

        return objectList;
    }

    /*
        Finds the position of either the start or the goal, depending on the passed parameter for id
     */
    public static int[] findStartOrGoal(char id, char[][] arrMaze) {

        int[] pos = new int[2];

        for (int i = 0; i < arrMaze.length; i++) {
            for (int j = 0; j < arrMaze.length; j++) {

                if (arrMaze[i][j] == id) {
                    pos[0] = i;
                    pos[1] = j;
                }

            }
        }


        return pos;
    }

    /*
        Searches for the optimal path if possible using A* Search
     */
    public static void searchForGoal(char[][] arrMaze, Cell[][] cellVisited) throws InterruptedException, IOException {
        int[] start;
        int[] goal;
        int numOfExploredStates = 0;
        double cost = 0d;
        Cell currCell;
        Cell nextCell = null;
        Cell goalCell = null;
        char[][] displayMaze = new char[arrMaze.length][arrMaze.length];

        // displays the maze
        for (int i = 0; i < arrMaze.length; i++) {
            for (int j = 0; j < arrMaze.length; j++) {
                if(arrMaze[i][j] == '.') {
                    displayMaze[i][j] = ' ';
                } else {
                    displayMaze[i][j] = arrMaze[i][j];
                }
            }
        }

        //state should contain:
        //  position in grid
        //  previous state
        //  total cost of state

        /*
         * add sstart to frontier with priority h(s start)
         *
         * while frontier is not empty do:
         *   remove s with smallest priority p from frontier
         *   let c be the total cost up to s
         *    if isEnd(s) then
         *     return solution
         *    add s to explored
         *    for each action in Actions(s) do:
         *       Get successr s' <- Succ(s,a)
         *        If s' already explored then continue
         *        Update frontier with s' and priority c + Cost(s,a) + h(s')
         * */

        //get the starting and goal cell
        start = findStartOrGoal('S', arrMaze);
        goal = findStartOrGoal('G', arrMaze);

        System.out.println("=====================");
        System.out.println(start[0] + " " + start[1]);
        System.out.println(goal[0] + " " + goal[1]);
        System.out.println("======================");

        // make priority queue as frontier
        PriorityQueue<Cell> frontierPQ = new PriorityQueue<>(Comparator.comparing(Cell::getHeurActCost).thenComparing(Cell::getActualCost));

        Scanner scanner = new Scanner(System.in);
        String choice = null;

        //add start priority to the queue
        cellVisited[start[0]][start[1]].setActualCost(0);
        cellVisited[start[0]][start[1]].setHeurActCost(cellVisited[start[0]][start[1]].getActualCost() + heuristicFunc(start[1], goal[1], start[0], goal[0]));
        frontierPQ.add(cellVisited[start[0]][start[1]]);

        //while queue is not empty
        while (!frontierPQ.isEmpty()) {

            //set current cell to th
            currCell = frontierPQ.peek();

            // remove s with the smallest priority p
            // from the frontier
            // and set to true
            frontierPQ.poll().setExplored(true);
            numOfExploredStates++;

            // current cell is not yet the goal
            if (displayMaze[currCell.getPosX()][currCell.getPosY()] != 'G') {
                displayMaze[currCell.getPosX()][currCell.getPosY()] = '.';
            }

            // get the cost to reach the current cell from the start
            cost = currCell.getActualCost();

            //if it is end then return solution
            if (arrMaze[currCell.getPosX()][currCell.getPosY()] == 'G') {
                goalCell = currCell;
                break;
            } else if (arrMaze[currCell.getPosX()][currCell.getPosY()] != 'G') {
                //for each action in Action(s)
                //check up, down, left, right, if it's still within the maze
                //and if no wall

                //check up
                if ((currCell.getPosX() - 1 <= arrMaze.length - 1 && currCell.getPosX() - 1 >= 0) &&
                        (arrMaze[currCell.getPosX() - 1][currCell.getPosY()] != '#')) {

                    //get s' or the next cell
                    nextCell = cellVisited[currCell.getPosX() - 1][currCell.getPosY()];

                    // If s' already explored then continue
                    if (!nextCell.getExplored()) {
                        //Update frontier with s' and priority c + Cost(s,a) + h(s')
                        double heurActCost = cost + 1 + heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        // if nextCell is already in the priority queue
                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev
                            // if so, update the value
                            if (nextCell.getActualCost() > cost + 1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost + 1);
                                nextCell.setHeurActCost(heurActCost);
                            }
                        // else, add nextCell to priority queue and take note the previous cell
                        } else {
                            nextCell.setActualCost(cost + 1);
                            nextCell.setHeurActCost(heurActCost);
                            frontierPQ.add(nextCell);
                            nextCell.setPrev(currCell);
                        }

                    }

                }

                //check down
                if ((currCell.getPosX() + 1 <= arrMaze.length - 1 && currCell.getPosX() + 1 >= 0) &&
                        (arrMaze[currCell.getPosX() + 1][currCell.getPosY()] != '#')) {

                    // get s' or the next cell
                    nextCell = cellVisited[currCell.getPosX() + 1][currCell.getPosY()];

                    // If s' already explored then continue
                    if (!nextCell.getExplored()) {
                        //Update frontier with s' and priority c + Cost(s,a) + h(s')
                        double heurActCost = cost + 1 + heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        System.out.println(heurActCost);

                        // if nextCell is already in the priority queue
                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev
                            // if so, update the value
                            if (nextCell.getActualCost() > cost + 1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost + 1);
                                nextCell.setHeurActCost(heurActCost);
                            }
                        // else, add nextCell to priority queue and take note the previous cell
                        } else {
                            nextCell.setActualCost(cost + 1);
                            nextCell.setHeurActCost(heurActCost);
                            frontierPQ.add(nextCell);
                            nextCell.setPrev(currCell);
                        }

                    }


                }

                //check left
                if ((currCell.getPosY() - 1 <= arrMaze.length - 1 && currCell.getPosY() - 1 >= 0) &&
                        (arrMaze[currCell.getPosX()][currCell.getPosY() - 1] != '#')) {

                    //get s' or the next cell
                    nextCell = cellVisited[currCell.getPosX()][currCell.getPosY() - 1];

                    // If s' already explored then continue
                    if (!nextCell.getExplored()) {
                        //Update frontier with s' and priority c + Cost(s,a) + h(s')
                        double heurActCost = cost + 1 + heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        System.out.println(heurActCost);

                        // if nextCell is already in the priority queue
                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev
                            // if so, update the value
                            if (nextCell.getActualCost() > cost + 1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost + 1);
                                nextCell.setHeurActCost(heurActCost);
                            }
                        // else, add nextCell to priority queue and take note the previous cell
                        } else {
                            nextCell.setActualCost(cost + 1);
                            nextCell.setHeurActCost(heurActCost);
                            frontierPQ.add(nextCell);
                            nextCell.setPrev(currCell);
                        }
                    }


                }

                //check right
                if ((currCell.getPosY() + 1 <= arrMaze.length - 1 && currCell.getPosY() + 1 >= 0) &&
                        (arrMaze[currCell.getPosX()][currCell.getPosY() + 1] != '#')) {

                    //get s'
                    nextCell = cellVisited[currCell.getPosX()][currCell.getPosY() + 1];

                    // If s' already explored then continue
                    if (!nextCell.getExplored()) {
                        //Update frontier with s' and priority c + Cost(s,a) + h(s')
                        double heurActCost = cost + 1 + heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        // if nextCell is already in the priority queue
                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev
                            // if so, update the value
                            if (nextCell.getActualCost() > cost + 1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost + 1);
                                nextCell.setHeurActCost(heurActCost);
                            }
                        // else, add nextCell to priority queue and take note the previous cell
                        } else {
                            nextCell.setActualCost(cost + 1);
                            nextCell.setHeurActCost(heurActCost);
                            frontierPQ.add(nextCell);
                            nextCell.setPrev(currCell);
                        }

                    }


                }

            }
            // display graphics
            System.out.println(displayMaze(currCell, start, goal, displayMaze));
            System.out.print("\033[H\033[2J");
            System.out.flush();
            Thread.sleep(1500);
            //System.out.println("Enter any key to continue.");
            //choice = scanner.nextLine();
        }

        //printing the most optimal path in x
        if (goalCell != null) {
            System.out.println("Solution found!");
            System.out.println(goalCell.getPosX() + " " + goalCell.getPosY() + "\n");

            // from goal cell using previous cells, backtrack to find the optimal path
            while (goalCell.getPreviousCell() != null) {
                displayMaze[goalCell.getPreviousCell().getPosX()][goalCell.getPreviousCell().getPosY()] = 'x';
                goalCell = cellVisited[goalCell.getPreviousCell().getPosX()][goalCell.getPreviousCell().getPosY()];
            }

            System.out.println(displayMaze(goalCell, start, goal, displayMaze));

        // no path to the goal exists
        } else {
            System.out.println("Solution not found.");
        }

        System.out.println("Number of explored states: " + numOfExploredStates);

        System.out.println("Legends: ");
        System.out.println(ANSI_RED + "o" + ANSI_RESET + " - Bot");
        System.out.println(ANSI_YELLOW + "." + ANSI_RESET + " - Visited Locations");
        System.out.println(ANSI_GREEN + "x" + ANSI_RESET + " - Final Path");
        System.out.println(ANSI_GREEN + "E" + ANSI_RESET + " - End");

    }
    /*
        Compute for the heuristic of the cell
     */
    public static int heuristicFunc(int xPos, int xGoal, int yPos, int yGoal) {
        //manhattan distance
        return Math.abs(xPos - xGoal) + Math.abs(yPos - yGoal);
    }

    /*
        Display for the maze
     */
    public static StringBuilder displayMaze(Cell cell, int[] start, int[] goal, char[][] arrMaze) {

        StringBuilder maze = new StringBuilder("   ");
        StringBuilder mazeUnder = new StringBuilder();

        for (int i = 0; i < arrMaze.length; i++) {

            if (i < 10) {
                maze.append("  ").append(i).append(" ");
            } else {
                maze.append("  ").append(i);
            }
        }
        maze.append("\n   ╔");

        for (int i = 1; i < arrMaze.length; i++) {

            char temp = arrMaze[0][i - 1];
            char tempNextCol = arrMaze[0][i];

            if (temp == '#') {
                maze.append("═══╤");
            } else {
                if (tempNextCol == '#') {

                    maze.append("═══╤");
                } else {

                    maze.append("════");
                }
            }
        }
        maze.append("═══╗\n");

        char tempPos = '0';
        char tempEast = '0';
        char tempSouth = '0';
        char tempLowerRight = '0';

        for (int row = 0; row < arrMaze.length; row++) {

            mazeUnder = new StringBuilder();

            for (int col = 0; col < arrMaze.length; col++) {

                tempPos = arrMaze[row][col]; // = A -> Current square

                if (col + 1 == arrMaze.length){// = B -> Square at the right of temp
                    tempEast = 0;
                } else {
                    tempEast = arrMaze[row][col + 1];
                }

                if (row + 1 == arrMaze.length){// = C -> Square below temp

                    tempSouth = 0;
                } else {

                    tempSouth = arrMaze[row + 1][col];
                }

                if (row < arrMaze.length - 1 && col < arrMaze.length - 1) {

                    tempLowerRight = arrMaze[row + 1][col + 1]; // = D -> Square in the temp lower right-hand diagonal
                }

                if (col == 0) {

                    if (row < 10) {

                        maze.append(row).append("  ║");
                    } else {

                        maze.append(row).append(" ║");
                    }

                    if (tempSouth != 0) {

                        if (tempPos == '#' || tempSouth == '#') {

                            mazeUnder.append("   ╟");
                        } else {
                            mazeUnder.append("   ║");
                        }
                    }
                }

                if (tempPos == '#') {

                    maze.append("   ");
                    mazeUnder.append("───");
                } else {

                    if (row == cell.getPosX() && col == cell.getPosY()){

                        maze.append(" ").append(ANSI_RED).append("o").append(ANSI_RESET).append(" ");
                    }
                    else if (row == start[0] && col == start[1]){

                        maze.append(" ").append(ANSI_GREEN).append("S").append(ANSI_RESET).append(" ");
                    }
                    else if (row == goal[0] && col == goal[1]){

                        maze.append(" ").append(ANSI_GREEN).append("E").append(ANSI_RESET).append(" ");
                    } else if (arrMaze[row][col] == 'x'){

                        maze.append(" ").append(ANSI_GREEN).append(arrMaze[row][col]).append(ANSI_RESET).append(" ");
                    } else {

                        maze.append(" ").append(ANSI_YELLOW).append(arrMaze[row][col]).append(ANSI_RESET).append(" ");
                    }

                
                    if (row < arrMaze.length - 1) {
                        if (tempSouth == '#'){

                            mazeUnder.append("───");
                        } else {

                            mazeUnder.append("   ");
                        }
                    }
                }

                //Maze right edge
                if (col == arrMaze.length - 1) {
                    
                    maze.append("║");
                    
                    if (tempPos != 0 && tempSouth != 0) {
                        
                        if (tempPos == '#' || tempSouth == '#'){

                            mazeUnder.append("╢");
                        } else {

                            mazeUnder.append("║");
                        }
                    }
                } else {
                    //Squares corners.
                    // two cases : wall square or not
                    if (tempPos == '#') {

                        maze.append("│");
                        if (tempSouth != 0 && tempLowerRight != 0 && tempEast != 0) {
                            //"┼" = (B + D).(C + D) -> The most reccurent corner to write
                            if ((tempEast == '#' || tempLowerRight == '#') && (tempSouth == '#' || tempLowerRight == '#')){

                                mazeUnder.append("┼");
                            } else {

                                if (tempSouth != '#' && tempEast != '#') //Wall on top left only

                                    mazeUnder.append("┘");
                                else if (tempSouth != '#') // Walls on top

                                    mazeUnder.append("┴");
                                else

                                    mazeUnder.append("┤");
                            }
                        }
                    } else {
                        if (tempEast != 0) {
                            if (tempEast == '#')
                                maze.append("│");
                            else
                                maze.append(" ");

                            if (tempSouth != 0 && tempLowerRight != 0) {
                                //"┼" = (C).(D) -> The most reccurent corner to write
                                if (tempSouth == '#' && tempEast == '#')
                                    mazeUnder.append("┼");
                                else {
                                    if (tempSouth != '#' && tempLowerRight != '#' && tempEast != '#') //No wall

                                        mazeUnder.append(" ");
                                    else if (tempSouth != '#' && tempLowerRight == '#' && tempEast != '#') //Wall on right below

                                        mazeUnder.append("┌");
                                    else if (tempSouth == '#' && tempLowerRight != '#') //Wall on left below

                                        mazeUnder.append("┐");
                                    else if (tempSouth != '#' && tempLowerRight != '#') //Wall on top right

                                        mazeUnder.append("└");
                                    else if (tempSouth != '#') //Walls on right

                                        mazeUnder.append("├");
                                    else

                                        mazeUnder.append("┬");
                                }
                            }
                        }
                    }
                }
            }//<- for each column
            if (row < arrMaze.length - 1)
                maze.append("\n").append(mazeUnder).append("\n"); //Concatenate res + mazeUnder
            else {
                //Maze bottom edge
                maze.append("\n   ╚");
                for (int i = 1; i < arrMaze.length; i++) {
                    tempPos = arrMaze[row][i - 1];
                    if (tempEast == '#' || tempPos == '#')
                        maze.append("═══╧");
                    else
                        maze.append("════");
                }
                maze.append("═══╝\n");
            }
        }
        return maze;
    }
}