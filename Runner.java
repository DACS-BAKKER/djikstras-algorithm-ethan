/*  TravelerNode Class
    Name: Ethan Chen
    Date Completed: March 1, 2020
*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.io.File;
import java.io.IOException;

public class Runner { // test client for Djikstra's Solver

    public static void main(String[] args) throws IOException {
        File file = new File("src/usa.txt");
       // File file = new File("src/practice.txt");
        StdOut.println("Welcome to the Djikstra's Algorithm Map-Routing Solver");
        StdOut.println("Please type in your starting intersection number between 0 and 87574");
        int start =  StdIn.readInt();
        StdOut.println("Please type in your ending intersection number between 0 and 87574");
        int end = StdIn.readInt();
        StdOut.println("Would you like to use Ethan's optimization? 1) yes; 2) no");
        int opt = StdIn.readInt();

        Stopwatch stop = new Stopwatch(); // for timing purposes
        DjikstrasSolver maprouting;
        if(opt == 1) {
            maprouting = new DjikstrasSolver(start, end, file, true);
        } else {
            maprouting = new DjikstrasSolver(start, end, file, false);
        }
        double elapsed = stop.elapsedTime();
        maprouting.printSolution();
        StdOut.println(elapsed + " seconds have elapsed");
    }

}
