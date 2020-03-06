/*  Intersection Class
    Name: Ethan Chen
    Date Completed: February 20, 2020
*/

// NOTE: the reason roadsTo is initially left empty in the constructor is because when you load from the text file,
//       each intersection has the other intersections added to roadsTo. Every intersection is added to an array
//       of intersections in DjikstrasSolver called intersections, which holds all of them. After that, there is
//       no need to ever create more, but rather pull from that list.

public class Intersection { // objects that have a location and a number (identifier). Used in TravelerNode.
    int identifier; // intersection number
    int xCoord; // x coordinate
    int yCoord; // y coordinate
    LinkedList<Intersection> roadsTo; // list of all of the intersections it connects to

    public Intersection(int identifier, int xCoord, int yCoord) { // constructor, sets each value to inputted parameter
        this.identifier = identifier;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.roadsTo = new LinkedList<Intersection>(); // left empty, to be added to when loading from file
    }

    @Override
    public String toString() { // toString to convert to string
        String string = "Intersection " + identifier;
        return string;
    }

}
