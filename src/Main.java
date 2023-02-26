import java.util.*;
import java.io.*;

//isang file muna lahat kasi di nagana github sa comlabs

public class Main {
    public static void main(String[] args) {

        List<Object> fileOut = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        fileOut = readFile();

        int size = (int) fileOut.get(0);
        char[][] arrMaze = (char[][]) fileOut.get(1);

        Cell[][] cellVisited = new Cell[size][size];


        for (int i = 0; i < cellVisited.length; i++) {
            for (int j = 0; j < cellVisited.length; j++) {
                cellVisited[i][j] = new Cell(i, j);
            }
        }

        searchForGoal(arrMaze, cellVisited);


    }

    public static List<Object> readFile() {

        List<Object> objectList = new ArrayList<>();

        char[][] arrMaze = null;
        int size = -1;
        int ctr = -1;

        File file = new File("maze//maze.txt");

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

    public static void searchForGoal(char[][] arrMaze, Cell[][] cellVisited) {
        int[] start;
        int[] goal;
        int numOfExploredStates = 0;
        double cost = 0d;
        Cell currCell;
        Cell nextCell = null;
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

        //make priority queue
        PriorityQueue<Cell> frontierPQ = new PriorityQueue<>(Comparator.comparing(Cell::getHeurActCost));

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


            if(displayMaze[currCell.getPosX()][currCell.getPosY()] != 'G') {
                displayMaze[currCell.getPosX()][currCell.getPosY()] = '.';
            }

            //let c be the total cost up to s
            cost = currCell.getActualCost();

            //if it is end then return solution
            if (arrMaze[currCell.getPosX()][currCell.getPosY()] == 'G') {
                goalCell = currCell;
                break;
            } else if(arrMaze[currCell.getPosX()][currCell.getPosY()] != 'G') {
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
                        double heurActCost =  cost+1+heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        System.out.println(heurActCost);

                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev

                            if (nextCell.getActualCost() > cost+1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost+1);
                                nextCell.setHeurActCost(heurActCost);
                            }
                        } else {
                            nextCell.setActualCost(cost+1);
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
                        double heurActCost =  cost+1+heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        System.out.println(heurActCost);

                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev

                            if (nextCell.getActualCost() > cost+1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost+1);
                                nextCell.setHeurActCost(heurActCost);
                            }
                        } else {
                            nextCell.setActualCost(cost+1);
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
                        double heurActCost =  cost+1+heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        System.out.println(heurActCost);

                        //Update frontier with s' and priority c + Cost(s,a) + h(s')
                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev

                            if (nextCell.getActualCost() > cost+1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost+1);
                                nextCell.setHeurActCost(heurActCost);
                            }
                        } else {
                            nextCell.setActualCost(cost+1);
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
                        double heurActCost =  cost+1+heuristicFunc(nextCell.getPosY(), goal[1], nextCell.getPosX(), goal[0]);

                        if (frontierPQ.contains(nextCell)) {
                            //check if next cell has a lower heuristic cost/cost now than the prev

                            if (nextCell.getActualCost() > cost+1 || nextCell.getHeurActCost() > heurActCost) {
                                nextCell.setActualCost(cost+1);
                                nextCell.setHeurActCost(heurActCost);
                            }
                        } else {
                            nextCell.setActualCost(cost+1);
                            nextCell.setHeurActCost(heurActCost);
                            frontierPQ.add(nextCell);
                            nextCell.setPrev(currCell);
                        }

                    }


                }

            }

            for (int i = 0; i < arrMaze.length; i++) {
                for (int j = 0; j < arrMaze.length; j++) {
                    System.out.print(displayMaze[i][j]);
                }
                System.out.println();
            }

            System.out.println("======");

        }

        //printing the most optimal path in x
        if (goalCell != null) {
            System.out.println("Solution found!");
            System.out.println(goalCell.getPosX() + " " + goalCell.getPosY());

            while (goalCell.getPreviousCell() != null) {
                displayMaze[goalCell.getPreviousCell().getPosX()][goalCell.getPreviousCell().getPosY()] = 'x';
                goalCell = cellVisited[goalCell.getPreviousCell().getPosX()][goalCell.getPreviousCell().getPosY()];
            }

            for (int i = 0; i < arrMaze.length; i++) {
                for (int j = 0; j < arrMaze.length; j++) {
                    System.out.print(displayMaze[i][j]);
                }
                System.out.println();
            }
        } else {
            System.out.println("Solution not found.");
        }

        System.out.println("Number of explored states: " + numOfExploredStates);

        System.out.println("Legends: ");
        System.out.println(". - visited locations");
        System.out.println("x - final path");

    }

    public static int heuristicFunc(int xPos, int xGoal, int yPos, int yGoal) {
        //manhattan distance
        return Math.abs(xPos - xGoal)+Math.abs(yPos-yGoal);
    }

}