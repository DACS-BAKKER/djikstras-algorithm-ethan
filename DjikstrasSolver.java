/*  DjikstrasSolver Class
    Name: Ethan Chen
    Date Completed: March 5, 2020
*/

import edu.princeton.cs.algs4.StdOut;
import java.io.*;
import java.util.Iterator;
import java.util.StringTokenizer;

public class DjikstrasSolver { // Finds the shortest path between a start and end, given a file in the described form
    MinPQ queue;
    int start;
    TravelerNode startNode = null;
    int end;
    TravelerNode solutionNode;
    Intersection endIntersection;
    double totalDistance;
    public Intersection[] intersections; // list of all intersections from text file
    private boolean[] visited; // list of booleans, each index value represents if the corresponding intersection (by identifier) has already been visited

    public DjikstrasSolver(int start, int end, File file, boolean optimize) throws IOException { // constructor, solves upon creation of instance
        loadFile(file);
        visited = new boolean[intersections.length];
        this.start = start;
        this.end = end;

        if (!optimize) { // not optimized
            startNode = new TravelerNode(intersections[start], null);
            solutionNode = solveDjikstras(false);
        } else { // optimized
            endIntersection = intersections[end];
            startNode = new TravelerNode(intersections[start], null, endIntersection); // needs to know end intersection to compute distance
            solutionNode = solveDjikstras(true);
        }
    }

    public TravelerNode solveDjikstras(boolean optimize) { // effectively the same as any breadth-first search
        queue = new MinPQ();
        queue.enqueue(startNode); // put on the start node
        TravelerNode currNode;
        while (!queue.isEmpty()) {
            currNode = queue.dequeue(); // take off the front of the queue
           // StdOut.println("process " + currNode.currentIntersection.identifier + ": " + currNode.distanceFromStart); // used for testing
            visited[currNode.currentIntersection.identifier] = true; // once it is taken off, set visited value to true, so you can never circle back
            if (currNode.currentIntersection.identifier == end) { // if it is solution
                return currNode;
            }
            for (Intersection inter : currNode.currentIntersection.roadsTo) { // goes through each of the next possible intersections
                if (visited[inter.identifier] == false) { // if it hasn't been visited before
                    TravelerNode nextNode;
                    if (!optimize) {
                        nextNode = new TravelerNode(inter, currNode); // not optimized
                    } else {
                        nextNode = new TravelerNode(inter, currNode, endIntersection); // optimized
                    }
                    queue.enqueue(nextNode); // add the next intersection to the min priority queue
                }
            }
        }
        return null;
    }

    public Iterable<TravelerNode> getSolution() { // gives an iterable method that gives each TravelerNode along the solution path

        totalDistance = solutionNode.distanceFromStart;
        TravelerNode traversalNode = solutionNode.copy();
        Stack<TravelerNode> stack = new Stack<TravelerNode>();
        stack.push(traversalNode);

        while (traversalNode.lastNode != null) { // adds the solution path (which is backwards) to stack to put in correct order
            traversalNode = traversalNode.lastNode;
            stack.push(traversalNode);
        }

        // iterator class
        class RoadIterator implements Iterator<TravelerNode> {

            @Override
            public boolean hasNext() {
                if (!stack.isEmpty()) { // still another step as long as the stack is not empty
                    return true;
                }
                return false;
            }


            @Override
            public TravelerNode next() {
                return stack.pop();
            } // pop off stack to attain order

        }

        // iterable class
        class RoadIterable implements Iterable<TravelerNode> {
            @Override
            public Iterator<TravelerNode> iterator() {
                return new RoadIterator();
            }
        }

        return new RoadIterable();
    }

    public void printSolution() { // prints out each route taken
        for (TravelerNode intersection : getSolution()) {
            StdOut.println(intersection);
            StdOut.println();
        }
    }

    public void loadFile(File file) throws IOException { // reads from text file and fills Intersection[] intersections
        BufferedReader buf = new BufferedReader(new FileReader(file));
        String line = buf.readLine();
        StringTokenizer tok = new StringTokenizer(line, " ");
        int totalIntersections = Integer.valueOf(tok.nextToken()); // first two lines
        int totalRoads = Integer.valueOf(tok.nextToken());
        intersections = new Intersection[totalIntersections];

        // next totalIntersections lines after are all intersections, split as "(number) (xcoord) (ycoord)"
        for (int x = 0; x < totalIntersections; x++) {
            line = buf.readLine();
            StringTokenizer tok2 = new StringTokenizer(line, " ");
            int identifier = Integer.valueOf(tok2.nextToken());
            int xCoord = Integer.valueOf(tok2.nextToken());
            int yCoord = Integer.valueOf(tok2.nextToken());
            Intersection nextIntersection = new Intersection(identifier, xCoord, yCoord); // creates new intersection from data
            intersections[identifier] = nextIntersection; // adds it to array of intersections
            // notice how the identifier also equates to the index in the intersections array
        }

        // at this point you have a completely full array of intersections, but each has an empty roadsTo
        // next totalRoads lines after are all connections, split as "(intersection1) (intersection2)"
        for (int x = 0; x < totalRoads; x++) {
            line = buf.readLine();
            StringTokenizer tok3 = new StringTokenizer(line, " ");
            int int1 = Integer.valueOf(tok3.nextToken());
            int int2 = Integer.valueOf(tok3.nextToken());
            intersections[int1].roadsTo.add(intersections[int2]); // adds to the linked list of connections between roads
            intersections[int2].roadsTo.add(intersections[int1]);
        }
    }


}
