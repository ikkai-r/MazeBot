import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// Main class
public class Main {

    // main driver method
    public static void main(String[] args) throws Exception
    {

        char [][] arr = new char[0][];
        int ctr = -1;
        int size;

        // File path is passed as parameter
        File file = new File("maze\\maze.txt");

        // Creating an object of BufferedReader class
        BufferedReader br = new BufferedReader(new FileReader(file));

        // Declaring a string variable
        String st;

        // Condition holds true till
        // there is character in a string

        while ((st = br.readLine()) != null) {

            if(ctr == -1) {
                size = Integer.parseInt(st);
                arr = new char[size][size];
            } else {
                arr[ctr] = st.toCharArray();
            }
            ctr++;
        }

    }


}