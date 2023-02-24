import java.util.*;
import java.io.*;

//isang file muna lahat kasi di nagana github sa comlabs

public class Main {
    public static void main(String[]args) {

        Scanner scanner = new Scanner(System.in);
        char[][] arrMaze = readFile();

        for(int i = 0; i < arrMaze.length; i++) {
            System.out.println(arrMaze[i]);
        }

        searchForGoal(arrMaze);


    }

    public static char[][] readFile() {

        char[][] arrMaze = null;
        int size;
        int ctr = -1;

        File file = new File("maze//maze.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
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

        return arrMaze;
    }

    public static int[] findStartOrGoal(char id, char[][] arrMaze) {
        int[] pos = new int[2];

        for (int i = 0; i < arrMaze.length; i++) {
            for(int j = 0; j < arrMaze.length; j++) {

                if (arrMaze[i][j] == id) {
                    pos[0] = i;
                    pos[1] = j;
                }

            }
        }


        return pos;
    }

    public static void searchForGoal(char[][] arrMaze) {
        int[] start;
        int[] goal;
        int cost = 0;
        int[] currPos = new int[2];

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
        PriorityQueue<Integer> frontierPQ = new PriorityQueue<>();
        List<int[]> exploredCells = new ArrayList<>();

        //add start priority to the queue
        frontierPQ.add(cost+heuristicFunc(start[1], goal[1], start[0], goal[0]));

        //set current pos to start
        currPos = start;

        //while queue is not empty
        while(!frontierPQ.isEmpty()) {

            //set current pos to the element that will pop

            //remove s with smallest priority p
            //from the frontier

            frontierPQ.poll();

            //add s to explored
            exploredCells.add(currPos);

            //for each action in Action(s)
            //check up, down, left, right, if valid
            //and if no wall

            //check up
            if((currPos[0]-1 <= arrMaze.length-1) &&
                    (arrMaze[currPos[0]-1][currPos[1]] != '#')) {

                // If s' already explored then continue
                currPos[0]--;

                if(exploredCells.contains(currPos)) {
                    continue;
                } else {
                    //Update frontier with s' and priority c + Cost(s,a) + h(s')
                    frontierPQ.add(cost+heuristicFunc(currPos[1], goal[1], currPos[0], goal[0]));
                }


            }

            //check down
            else if((currPos[0]+1 <= arrMaze.length-1) &&
                    (arrMaze[currPos[0]+1][currPos[1]] != '#')) {

                // If s' already explored then continue
                currPos[0]++;

                if(exploredCells.contains(currPos)) {
                    continue;
                } else {
                    //Update frontier with s' and priority c + Cost(s,a) + h(s')
                    frontierPQ.add(cost+heuristicFunc(currPos[1], goal[1], currPos[0], goal[0]));
                }

            }

//check left
            else if((currPos[1]-1 <= arrMaze.length-1) &&
                    (arrMaze[currPos[0]][currPos[1]-1] != '#')) {
                // If s' already explored then continue
                currPos[1]--;

                if(exploredCells.contains(currPos)) {
                    continue;
                } else {
                    //Update frontier with s' and priority c + Cost(s,a) + h(s')
                    frontierPQ.add(cost+heuristicFunc(currPos[1], goal[1], currPos[0], goal[0]));
                }

            }

            //check right
            else if((currPos[1]+1 <= arrMaze.length-1) &&
                    (arrMaze[currPos[0]][currPos[1]+1] != '#')) {
                // If s' already explored then continue
                currPos[1]++;

                if(exploredCells.contains(currPos)) {
                    continue;
                } else {
                    //Update frontier with s' and priority c + Cost(s,a) + h(s')
                    frontierPQ.add(cost+heuristicFunc(currPos[1], goal[1], currPos[0], goal[0]));
                }
            }

            System.out.println(frontierPQ);
        }





    }

    public static int heuristicFunc(int xPos, int xGoal, int yPos, int yGoal) {
        //manhattan distance
        return Math.abs(xPos - xGoal)+Math.abs(yPos-yGoal);
    }

}