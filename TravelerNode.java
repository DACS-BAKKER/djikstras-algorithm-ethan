/*  TravelerNode Class
    Name: Ethan Chen
    Date Completed: March 4, 2020
*/

public class TravelerNode implements Comparable<TravelerNode> { // Intersection, where it came from, and a priority distance
    Intersection currentIntersection;
    TravelerNode lastNode;
    double distanceFromStart; // distance from the starting node via the path it has travelled so far
    double priorityDistance; // what is used to compute

    public TravelerNode(Intersection intersection, TravelerNode lastNode) { // constructor for regular Djikstra's
        this.currentIntersection = intersection;
        this.lastNode = lastNode;
        if(lastNode != null) {
            this.distanceFromStart = lastNode.distanceFromStart + addedDistance(); // adds distance from last node to last node's distance from start
         //   roundToDecimal(); // I took this out because it makes the distances recorded less accurate
        } else {
            this.distanceFromStart = 0; // if it has no last node, it is the starting node
        }
        priorityDistance = distanceFromStart; // these values are equal without the optimization - purely Djikstra's
    }

    // difference - this one needs to know the ending intersection in order to calculate how far each intersection is from it
    public TravelerNode(Intersection intersection, TravelerNode lastNode, Intersection endIntersection) { // constructor for Ethan's optimized Djikstra's
        this.currentIntersection = intersection;
        this.lastNode = lastNode;
        if(lastNode != null) {
            this.priorityDistance = lastNode.distanceFromStart + addedDistance() + ethanOptimization(endIntersection); // adds on optimization
            this.distanceFromStart = lastNode.distanceFromStart + addedDistance(); // as shown, priorityDistance and distanceFromStart are different values
        //    roundToDecimal();
        } else {
            this.distanceFromStart = 0; // start node
            priorityDistance = distanceFromStart;
        }
    }

    // creates a copy of a traveler's node, necessary in the getSolution method in DjikstrasSolver. If you do not copy
    // the solution node it ends up changing it, so instead you must make a copy.
    public TravelerNode copy() {
       TravelerNode copy = new TravelerNode(this.currentIntersection, this.lastNode);
       copy.distanceFromStart = this.distanceFromStart;
       copy.priorityDistance = this.priorityDistance;
       return copy;
    }

    private double addedDistance() { // pythagorean distance between current intersection and last intersection
        Intersection lastIntersection = lastNode.currentIntersection;
        int xDiff = currentIntersection.xCoord - lastIntersection.xCoord;
        int yDiff = currentIntersection.yCoord - lastIntersection.yCoord;
        double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        return distance;
    }

    private double ethanOptimization(Intersection endIntersection) { // half the pythagorean distance from the current intersection to the end
        int xDiff = currentIntersection.xCoord - endIntersection.xCoord;
        int yDiff = currentIntersection.yCoord - endIntersection.yCoord;
        double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        distance/=2;
        return distance;
    }

    private void roundToDecimal() { // not used, but rounds values to the nearest decimal place
        double multByTen = distanceFromStart * 10;
        int roundedInt = (int) multByTen;
        distanceFromStart = roundedInt / 10.0;
    }


    @Override
    // used to compare TravelerNodes, important for min priority queue
    public int compareTo(TravelerNode o) {
        if(priorityDistance > o.priorityDistance) {
            return 1;
        } else if(priorityDistance < o.priorityDistance) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    // String output used in runner
    public String toString() {
       if(lastNode != null) {
           String string = "Intersection " + currentIntersection.identifier + "\n";
           string += "Distance traveled: " + distanceFromStart;
           return string;
       } else {
           String string = "START: Intersection " + currentIntersection.identifier;
           return string;
       }
    }
}
