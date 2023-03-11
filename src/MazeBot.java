import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
    This program finds the optimal path for a maze using A* Search, if the path to the goal exists.
 */
public class MazeBot {

    /**
     * Called by the main method
     *
     * @param choice if timed animation or prompt input
     * @param arrMaze the char array containing the maze
     * @param cellVisited the array of Cell objects
     * @param mazeBot the current mazeBot
     * @throws InterruptedException
     */
    public void commenceSearch(String choice, char[][] arrMaze, Cell[][] cellVisited, MazeBot mazeBot) throws InterruptedException {

        searchForGoal(choice, arrMaze, cellVisited, mazeBot);

    }

    /**
     * Searches for the optimal path if possible using A* Search
     *
     * @param choice if timed animation or prompt input
     * @param arrMaze the char array containing the maze
     * @param cellVisited the array of Cell objects
     * @param mazeBot the current mazeBot
     * @throws InterruptedException
     */

    public void searchForGoal(String choice, char[][] arrMaze, Cell[][] cellVisited, MazeBot mazeBot) throws InterruptedException {
        int[] start;
        int[] goal;
        int numOfExploredStates = 0;
        double cost;
        Cell currCell;
        Cell nextCell;
        Cell goalCell = null;
        char[][] displayMaze = new char[arrMaze.length][arrMaze.length];

        for (int i = 0; i < arrMaze.length; i++) {
            for (int j = 0; j < arrMaze.length; j++) {
                if(arrMaze[i][j] == '.') {
                    displayMaze[i][j] = ' ';
                } else {
                    displayMaze[i][j] = arrMaze[i][j];
                }
            }
        }

        /*state should contain:
        //  position in grid
        //  previous state
        //  total cost of state
        */

        /*
         * A* Search Pseudocode Algorithm from CSINTSY Slides
         * add sstart to frontier with priority h(s start)
         *
         * while frontier is not empty do:
         *   remove s with the smallest priority p from frontier
         *   let c be the total cost up to s
         *    if isEnd(s) then
         *     return solution
         *    add s to explored
         *    for each action in Actions(s) do:
         *       Get successor s' <- Succ(s,a)
         *        If s' already explored then continue
         *        Update frontier with s' and priority c + Cost(s,a) + h(s')
         * */

        //get the starting and goal cell
        start = mazeBot.findStartOrGoal('S', arrMaze);
        goal = mazeBot.findStartOrGoal('G', arrMaze);

        System.out.println("==========================");
        System.out.println("Start cell at: " + start[0] + " " + start[1]);
        System.out.println("Goal cell at: " + goal[0] + " " + goal[1]);
        System.out.println("==========================");

        //make priority queue
        PriorityQueue<Cell> frontierPQ = new PriorityQueue<>(Comparator.comparing(Cell::getHeurActCost).thenComparing(Cell::getActualCost));

        Scanner scanner = new Scanner(System.in);

        //add start priority to the queue
        cellVisited[start[0]][start[1]].setActualCost(0);
        cellVisited[start[0]][start[1]].setHeurActCost(cellVisited[start[0]][start[1]].getActualCost() + heuristicFunc(start[1], goal[1], start[0], goal[0]));
        frontierPQ.add(cellVisited[start[0]][start[1]]);

        //while queue is not empty
        while (!frontierPQ.isEmpty()) {

            //set current cell to th
            currCell = frontierPQ.peek();

            // remove s with smallest priority p
            // from the frontier
            // and set to true
            frontierPQ.poll().setExplored(true);
            numOfExploredStates++;


            if (displayMaze[currCell.getPosX()][currCell.getPosY()] != 'G') {
                displayMaze[currCell.getPosX()][currCell.getPosY()] = '.';
            }

            //let c be the total cost up to s
            cost = currCell.getActualCost();

            //if it is end then return solution
            if (arrMaze[currCell.getPosX()][currCell.getPosY()] == 'G') {
                goalCell = currCell;
                break;
            } else if (arrMaze[currCell.getPosX()][currCell.getPosY()] != 'G') {
                //for each action in Action(s)
                //check up, down, left, right, if valid
                //and if no wall

                //check up
                if ((currCell.getPosX() - 1 <= arrMaze.length - 1 && currCell.getPosX() - 1 >= 0) &&
                        (arrMaze[currCell.getPosX() - 1][currCell.getPosY()] != '#')) {

                    //get s'
                    nextCell = cellVisited[currCell.getPosX() - 1][currCell.getPosY()];

                    // If s' already explored then continue
                    //else


                    if (!nextCell.getExplored()) {
                        //Update frontier with s' and priority c + Cost(s,a) + h(s')
                        double heurActCost = cost + 1 + heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev

                            if (nextCell.getActualCost() > cost + 1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost + 1);
                                nextCell.setHeurActCost(heurActCost);
                            }
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

                    //get s'
                    nextCell = cellVisited[currCell.getPosX() + 1][currCell.getPosY()];

                    // If s' already explored then continue
                    //else

                    if (!nextCell.getExplored()) {
                        //Update frontier with s' and priority c + Cost(s,a) + h(s')
                        double heurActCost = cost + 1 + heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev

                            if (nextCell.getActualCost() > cost + 1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost + 1);
                                nextCell.setHeurActCost(heurActCost);
                            }
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

                    //get s'
                    nextCell = cellVisited[currCell.getPosX()][currCell.getPosY() - 1];

                    // If s' already explored then continue
                    //else

                    if (!nextCell.getExplored()) {
                        double heurActCost = cost + 1 + heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        //Update frontier with s' and priority c + Cost(s,a) + h(s')
                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev

                            if (nextCell.getActualCost() > cost + 1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost + 1);
                                nextCell.setHeurActCost(heurActCost);
                            }
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
                    //else

                    if (!nextCell.getExplored()) {
                        //Update frontier with s' and priority c + Cost(s,a) + h(s')
                        double heurActCost = cost + 1 + heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev

                            if (nextCell.getActualCost() > cost + 1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost + 1);
                                nextCell.setHeurActCost(heurActCost);
                            }
                        } else {
                            nextCell.setActualCost(cost + 1);
                            nextCell.setHeurActCost(heurActCost);
                            frontierPQ.add(nextCell);
                            nextCell.setPrev(currCell);
                        }

                    }


                }

            }

            //display graphics
            System.out.println(displayMaze(currCell, start, goal, displayMaze));

            if(choice.equals("1")) {
                Thread.sleep(1500);
            } else {
                System.out.println("Enter any key to continue.");
                scanner.nextLine();
            }
        }

        //printing the most optimal path in x
        if (goalCell != null) {
            System.out.println("Solution found!");
            System.out.println(goalCell.getPosX() + " " + goalCell.getPosY() + "\n");

            //from goal cell using previous cells, backtrack to find the optimal path
            while (goalCell.getPreviousCell() != null) {
                displayMaze[goalCell.getPreviousCell().getPosX()][goalCell.getPreviousCell().getPosY()] = 'x';
                goalCell = cellVisited[goalCell.getPreviousCell().getPosX()][goalCell.getPreviousCell().getPosY()];
            }

            System.out.println(displayMaze(goalCell, start, goal, displayMaze));
        } else {
            //no path to the goal exist
            System.out.println("Solution not found.");
        }

        System.out.println("Number of explored states: " + numOfExploredStates);

        System.out.println("Legends: ");
        System.out.println("o - Bot");
        System.out.println(". - Visited Locations");
        System.out.println("x - Final Path");
        System.out.println("E - End");

    }

    /**
     * Computes for the cell's heuristic
     *
     * @param xGoal x position of the goal
     * @param xPos x position of the current cell
     * @param yGoal y position of the goal
     * @param yPos y position of the current cell
     * @return manhattan distance between goal cell and current cell
     */

    public static int heuristicFunc(int xPos, int xGoal, int yPos, int yGoal) {
        //manhattan distance
        return Math.abs(xPos - xGoal) + Math.abs(yPos - yGoal);
    }

    /**
     * Reads and processes the file's content into a char array.
     *
     * @return a list of object that contains the processed maze from the maze.txt file
     */
    public List<Object> readFile() {

        List<Object> objectList = new ArrayList<>();

        char[][] arrMaze = null;
        int size = -1;
        int ctr = -1;

        File file = new File("C:\\Users\\Brian Gabini\\OneDrive - De La Salle University - Manila\\Personal\\work_files\\Projects\\GITHUB\\DLSU\\Y2T2\\CSINTSY\\MCO1\\MazeBot\\src\\maze1\\maze-31.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (ctr == -1) {
                    //parse int
                    size = Integer.parseInt(line);
                    arrMaze = new char[size][size];

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

    /**
     * Finds the position of the start and goal
     *
     * @param id whether it should find the start or the goal
     * @param arrMaze the array of the char maze
     * @return an int position array of the start and the goal
     */
    public int[] findStartOrGoal(char id, char[][] arrMaze) {

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

    /**
     * Display for the maze
     *
     * @param cell the current cell object being displayed
     * @param start  the position of the start cell
     * @param goal the position of the goal cell
     * @param arrMaze the char array containing the maze
     * @return StringBuilder of the maze display
     */

    public static StringBuilder displayMaze(Cell cell, int[] start, int[] goal, char[][] arrMaze) {

        StringBuilder maze = new StringBuilder("   ");
        StringBuilder mazeUnder;

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

                        maze.append(" ").append("o").append(" ");
                    }
                    else if (row == start[0] && col == start[1]){

                        maze.append(" ").append("S").append(" ");
                    }
                    else if (row == goal[0] && col == goal[1]){

                        maze.append(" ").append("E").append(" ");
                    } else if (arrMaze[row][col] == 'x'){

                        maze.append(" ").append(arrMaze[row][col]).append(" ");
                    } else {

                        maze.append(" ").append(arrMaze[row][col]).append(" ");
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
