/*  Stack Class
    Name: Ethan Chen
    Date Completed: February 20, 2020
*/

public class Stack<Item> { // A first-in last-out data structure

    Node<Item> top;

    public Stack() {

    }

    public boolean isEmpty() { // checks if empty
        if (top == null) { // if there is no top node it does not exist
            return true;
        } else {
            return false;
        }
    }

    public void push(Item item) { // adds an item to a stack
        Node newNode = new Node(item);

        if (!isEmpty()) { // if there are existing nodes
            Node tempNode = top; // save the value of the old top node, set the new node as top, and point to the old top
            top = newNode;
            top.setNext(tempNode);
        } else { // if it is empty, set the new node as the top
            top = newNode;
        }
    }

    public Item pop() { // removes an item from the stack
        Node temp;

        if(!isEmpty()) { // if the top exists
            if(top.next != null) { // if there is more than one object, take off top and set second as top
                temp = top;
                top = top.next;
                return (Item) temp.data;
            } else { // only one object, take off top and set null as top
                temp = top;
                top = null;
                return (Item) temp.data;
            }
        } else {
            return null; // not possible to pop a nonexistent object
        }
    }

}

