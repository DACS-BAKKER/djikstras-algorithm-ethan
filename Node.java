/*  Node Class
    Name: Ethan Chen
    Date Completed: February 20, 2020
*/

public class Node<Item> { // a generic Node class, holding some piece of data and a pointer to another node
    public Item data;
    public Node next;

    public Node(Item data) {
        this.data = data;
    } // constructor given a piece of data

    public Node(Node n) { // constructor that effectively copies a node
        this.data = (Item) n.data;
        this.next = n.next;
    }

    public void setNext(Node n) {
        next = n;
    } // setter for the next node

    @Override
    public String toString() {
        return String.valueOf(data);
    } // toString to convert to String
}