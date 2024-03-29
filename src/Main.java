import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        MazeBot mazeBot = new MazeBot();
        Scanner scanner = new Scanner(System.in);

        List<Object> fileOut;
        String choice;

        fileOut = mazeBot.readFile();

        int size = (int) fileOut.get(0);
        char[][] arrMaze = (char[][]) fileOut.get(1);

        Cell[][] cellVisited = new Cell[size][size];

        for (int i = 0; i < cellVisited.length; i++) {
            for (int j = 0; j < cellVisited.length; j++) {
                cellVisited[i][j] = new Cell(i, j);
            }
        }

        System.out.println("Enter: \n1: Timed animation for the paths. \n2: Prompt input before next search step:");
        choice = scanner.nextLine();

        mazeBot.commenceSearch(choice, arrMaze, cellVisited, mazeBot);

    }

}