/*  Min Priority Queue Class
    Name: Ethan Chen
    Date Completed: March 3, 2020
*/

import edu.princeton.cs.algs4.StdOut;

public class MinPQ { // a heap-based Min priority queue for TravelerNodes
    TravelerNode[] heapPriorityQueue; // implementation as an array
    int backIndex; // last index or back of queue

    public MinPQ() {
        this.backIndex = 0;
        this.heapPriorityQueue = new TravelerNode[2]; // initial size of 2 (room for 1 object)
    }

    public void enqueue(TravelerNode node) { // adds a node to the back of the queue, at backIndex
        backIndex++; // increments backIndex to an empty spot
        if (backIndex >= heapPriorityQueue.length-1) { // if it is too large, resize the array
            resize(true);
        }
        addNode(node); // second part of enqueue
    }

    private void addNode(TravelerNode node) { // actually puts the node on the priority queue
        int initialIndex = backIndex; // index of the object you are putting in

        for(int x = 1; x<backIndex; x++) { // checking for duplicates (PART 1 OF REMOVING DUPLICATES)
            if(heapPriorityQueue[x].currentIntersection.identifier == node.currentIntersection.identifier) {
                if(node.compareTo(heapPriorityQueue[x]) == -1) { // duplicate has greater priority than new node
                    removeCopy(x); // NOTE: this reduces backIndex twice to account for removing 1 and to reduce to the last object in the min priority queue
                    x = backIndex; // ends for loop - should never be more than 1 duplicate
                    backIndex++; // adds back to an empty spot
                    initialIndex = backIndex;
                   // StdOut.println("lower " + node.currentIntersection.identifier + ": " + node.distanceFromStart); // for testing
                } else { // if the duplicate has smaller priority than new node, discard the new node
                    backIndex--; // reduce back to index of last object
                    return;
                }
            }
        }

        heapPriorityQueue[backIndex] = node; // put it at backIndex on the priorityQueue
            while (initialIndex != 1 && node.compareTo(heapPriorityQueue[initialIndex/2]) == -1) { // as long as the objects above it in the heap are greater, move it up the heap
                switchIndices(initialIndex, initialIndex/2);
                initialIndex /= 2;
            }
    }

    //PART 2 OF REMOVING DUPLICATES
    private void removeCopy(int indexOfRemoval) { // removes the copy
        heapPriorityQueue[indexOfRemoval] = heapPriorityQueue[backIndex-1]; // puts the last object at the location of the one it is swapping with
        heapPriorityQueue[backIndex-1] = null; // removes the very last object
        backIndex--;
        backIndex--;

        if(indexOfRemoval<=backIndex) { // after around 3 hours of debugging, I finally realized I needed to put this here in order to get correct outputs. This took forever to find.
            while (indexOfRemoval != 1 && heapPriorityQueue[indexOfRemoval].compareTo(heapPriorityQueue[indexOfRemoval / 2]) == -1) { // as long as the objects above it in the heap are greater, move it up the heap
                switchIndices(indexOfRemoval, indexOfRemoval / 2);
                indexOfRemoval /= 2;
            }
        }

        reworkPQ(indexOfRemoval); // moves node in new location to appropriate position (down the heap, if necessary)
    }

    public TravelerNode dequeue() { // set the last object to the top
        TravelerNode item = heapPriorityQueue[1];
        heapPriorityQueue[1] = heapPriorityQueue[backIndex];
        heapPriorityQueue[backIndex] = null;
        backIndex--;
        if (backIndex * 3 < heapPriorityQueue.length-1) { // resize if small, for space efficiency
            resize(false);
        }
        reworkPQ(1); // have the node on top (formerly at the bottom) make its way down the heap
        return item;
    }

    //SINK METHOD, used when dequeuing, or removing copy
    private void reworkPQ(int indexOfMoving) { // generalized indexOfMoving instead of 1 so I can use this method to sink from any index (important for removing copy)
        int currIndex = indexOfMoving; // start index
        int nextIndex1 = currIndex*2; // left location in heap
        int nextIndex2 = currIndex*2+1; // right location in heap
        boolean continueDown = true;


        if(backIndex == 0) { // empty priority queue
            return;
        }

        while (continueDown) {
           if(nextIndex2 <= backIndex) { // both left and right exist
               if((heapPriorityQueue[nextIndex1].compareTo(heapPriorityQueue[nextIndex2]) == -1 || heapPriorityQueue[nextIndex1].compareTo(heapPriorityQueue[nextIndex2]) == 0) && heapPriorityQueue[nextIndex1].compareTo(heapPriorityQueue[currIndex]) == -1) { // if left is less than right or left equals right and left is greater than curr node
                   switchIndices(nextIndex1, currIndex); // move current node down and left
                   currIndex = nextIndex1;
                   nextIndex1 = currIndex * 2;
                   nextIndex2 = currIndex * 2 + 1;
               } else if(heapPriorityQueue[nextIndex1].compareTo(heapPriorityQueue[nextIndex2]) == 1 && heapPriorityQueue[nextIndex2].compareTo(heapPriorityQueue[currIndex]) == -1) {
                   switchIndices(nextIndex2, currIndex); // move current node down and right
                   currIndex = nextIndex2;
                   nextIndex1 = currIndex * 2;
                   nextIndex2 = currIndex * 2 + 1;
               } else { // case that both left and right are smaller than current node, can't move down anymore, stop
                   continueDown = false;
               }
           } else if(nextIndex1 <= backIndex && heapPriorityQueue[nextIndex1].compareTo(heapPriorityQueue[currIndex]) == -1) { // left exists and is greater than current node, right does not
               switchIndices(nextIndex1, currIndex); // move current node down and left
               currIndex = nextIndex1;
               nextIndex1 = currIndex * 2;
               nextIndex2 = currIndex * 2 + 1;
           } else { // neither left nor right exists, stop
               continueDown = false;
           }
        }
    }

    private void resize(boolean isUpsize) { // adjusts the size of the array, copying over values to a new array of a different size
        if (isUpsize) {
            TravelerNode[] newHPQ = new TravelerNode[heapPriorityQueue.length * 2];
            for (int x = 0; x < heapPriorityQueue.length; x++) {
                newHPQ[x] = heapPriorityQueue[x];
            }
            heapPriorityQueue = newHPQ;
        } else {
            TravelerNode[] newHPQ = new TravelerNode[heapPriorityQueue.length / 2];
            for (int x = 0; x < newHPQ.length; x++) {
                newHPQ[x] = heapPriorityQueue[x];
            }
            heapPriorityQueue = newHPQ;
        }
    }

    private void switchIndices(int a, int b) { // switches the values of two indices
        TravelerNode temp = heapPriorityQueue[a];
        heapPriorityQueue[a] = heapPriorityQueue[b];
        heapPriorityQueue[b] = temp;
    }

    public boolean isEmpty() { // checks if the priority queue has any objects in it
        if (heapPriorityQueue[1] == null) {
            return true;
        }
        return false;
    }

    public int size() {
        return backIndex;
    } // length of priority queue

    public static void main(String[] args) { // tester for the methods
        MinPQ pq = new MinPQ();
        TravelerNode node1 = new TravelerNode(new Intersection(1, 0, 0), new TravelerNode(new Intersection(11, 0, 5), null));
        TravelerNode node2 = new TravelerNode(new Intersection(2, 0, 0), new TravelerNode(new Intersection(12, 0, 10), null));
        TravelerNode node3 = new TravelerNode(new Intersection(3, 0, 0), new TravelerNode(new Intersection(13, 0, 3), null));
        TravelerNode node4 = new TravelerNode(new Intersection(4, 0, 0), new TravelerNode(new Intersection(14, 0, 5), null));
        TravelerNode node5 = new TravelerNode(new Intersection(5, 0, 0), new TravelerNode(new Intersection(15, 0, 4), null));
        TravelerNode node6 = new TravelerNode(new Intersection(6, 0, 0), new TravelerNode(new Intersection(16, 0, 8), null));
        TravelerNode node7 = new TravelerNode(new Intersection(7, 0, 0), new TravelerNode(new Intersection(17, 0, 9), null));
        TravelerNode node8 = new TravelerNode(new Intersection(8, 0, 0), new TravelerNode(new Intersection(18, 0, 7), null));
        TravelerNode node9 = new TravelerNode(new Intersection(9, 0, 0), new TravelerNode(new Intersection(19, 0, 2), null));
        TravelerNode node10 = new TravelerNode(new Intersection(10, 0, 0), new TravelerNode(new Intersection(20, 0, 6), null));
        TravelerNode node11 = new TravelerNode(new Intersection(1, 0, 0), new TravelerNode(new Intersection(21, 0, 1), null));
        pq.enqueue(node1);
        pq.enqueue(node2);
        pq.enqueue(node3);
        pq.enqueue(node4);
        pq.enqueue(node5);
        pq.enqueue(node6);
        pq.enqueue(node7);
        pq.enqueue(node8);
        pq.enqueue(node9);
        pq.enqueue(node10);
        pq.enqueue(node11);
        pq.dequeue();
        pq.dequeue();
        pq.dequeue();
        pq.dequeue();
        pq.dequeue();
        pq.dequeue();
        pq.dequeue();
        pq.dequeue();
        pq.dequeue();
        pq.dequeue();
    }

    // obsolete methods
    private void reworkPQ2(int indexOfMoving) { // my initial try at reworkPQ, unsuccessful, didn't account for certain cases
        int currIndex = indexOfMoving;
        int nextIndex1 = currIndex*2;
        int nextIndex2 = currIndex*2+1;
        boolean continueDown = true;


        if(backIndex == 0) {
            return;
        }

        while (continueDown) {
            if(nextIndex1 <= backIndex || nextIndex2<= backIndex){
                if (nextIndex2 <= backIndex) {
                    if (heapPriorityQueue[nextIndex1].compareTo(heapPriorityQueue[nextIndex2]) == -1 && heapPriorityQueue[currIndex].compareTo(heapPriorityQueue[nextIndex1]) == -1) {
                        switchIndices(nextIndex1, currIndex);
                        currIndex = nextIndex1;
                        nextIndex1 = currIndex * 2;
                        nextIndex2 = currIndex * 2 + 1;
                    } else {
                        switchIndices(nextIndex2, currIndex);
                        currIndex = nextIndex2;
                        nextIndex1 = currIndex * 2;
                        nextIndex2 = currIndex * 2 + 1;
                    }
                } else {
                    switchIndices(nextIndex1, currIndex);
                    currIndex = nextIndex1;
                    nextIndex1 = currIndex * 2;
                    nextIndex2 = currIndex * 2 + 1;
                }

                if (nextIndex1 <= backIndex) {
                    if (nextIndex2 <= backIndex) {
                        if (heapPriorityQueue[currIndex].compareTo(heapPriorityQueue[nextIndex1]) == -1 && heapPriorityQueue[currIndex].compareTo(heapPriorityQueue[nextIndex2]) == -1) {
                            continueDown = false;
                        }
                    } else {
                        if (heapPriorityQueue[currIndex].compareTo(heapPriorityQueue[nextIndex1]) != 1) {
                            continueDown = false;
                        }
                    }
                } else {
                    continueDown = false;
                }
            } else {
                continueDown = false;
            }
        }
    }

}
