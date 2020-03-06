/*  Linked List Class (all of the necessary parts, at least)
    Name: Ethan Chen
    Date Completed: February 20, 2020
*/

import java.util.Iterator;

public class LinkedList<Item> implements Iterable<Item> { // A data structure that links nodes to one another
    Node<Item> origin;

    public void add(Node node) { // adds a node to the end of the linked list
        if (origin != null) { // if a node already exists there
            Node currentNode = origin;
            while (currentNode.next != null) {
                currentNode = currentNode.next; // traverse the list until at the end
            }
            currentNode.setNext(node); // set the end node's next node to the new node
        } else {
            origin = node; // if no node exists, set the new node as the origin
        }
    }

    public void add(Item item) { // same as the other add, just takes the item as the parameter instead of the node itself
        Node node = new Node(item); // creates the node given the item
        if (origin != null) { // same as above
            Node currentNode = origin;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.setNext(node);
        } else {
            origin = node;
        }
    }

    @Override
    public Iterator<Item> iterator() { // iterator to traverse through linked list
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<Item> {

        Node<Item> currNode = new Node<Item>(origin); // takes the value of the top item
        Item data = currNode.data;

        public boolean hasNext() {
            if(currNode != null) { // has a next object as long as currNode is not null because currNode is set to null when there are no more in the list
                return true;
            }
            return false;
        }

        public void remove() {

        }

        public Item next() {
            data = currNode.data; // stores the value of the data of the node
            if(currNode.next != null) {
                currNode = currNode.next; // moves to the next node if it exists
            } else {
                currNode = null; // sets it to null if there is no next / reached end of linked list
            }
            return data; // returns the data
        }
    }
}
