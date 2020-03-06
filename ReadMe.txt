Name: Ethan Chen
Project: Djikstra's Map-Routing Algorithm
Date Completed: March 5, 2020

Testing Files/Running Files:
 Runner - Test Client for the DjikstrasSolver, gives processing (worked on w/ Kevin), final route, and time taken
 DjikstrasUSGraphics - Graphical interface to find the fastest route between two points clicked on by user

Project Files:
 Intersection - Stores an intersection number, a coordinate, and a Linked List of all of the intersections it connects to via roads. It is used in TravelerNode.
 TravelerNode - Holds an intersection, that intersections distance from the start (priority), and the node it came from
 DjikstrasSolver - Takes a starting number and ending, loads values from usa.txt, and finds the fastest route using Djikstra's Algorithm. It computes upon construction of an object, and uses TravelerNodes and the Min Priority Queue.
 MinPQ - A heap-based priority queue implemented with an array, places objects in it in such a way that the next object you dequeue is always larger

Extra Files:
 LinkedList - A generic Linked List, recoded for the sake of practice
 Stack - A generic Stack, recoded for the sake of practice
 Node - A node, able to store any value and a pointer to a next node
 mapUSA1.jpg - the map picture I used in DjikstrasUSGraphics
 mapUSA2.png - the map given, that I attempted to use
 practice.txt - a test set to test the DjikstrasSolver
 usa.txt - a set of all of the (relevant) intersections in the US and what each is connected to


Analysis: (More on Paper)
This Djikstra's Algorithm is able to compute a path across the United States (from the top left corner of Washington to
the bottom right of Florida, as tested in DjikstrasUSGraphics) in ~1.2 second. Considering this method checks every path
until it finds the solution, it effectively spans to 3/4 of all of the intersections, computing and reaching more than
60000 objects. This is fairly effective, but with my optimization I can lower it to ~1.1 second. This is not a lot of time,
but for a computer it is certainly significant.

As well, Djikstra's Algorithm, as it is a weighted breadth-first search, will always find the shortest path from point
A to point B, by checking every path from point A, increasing in distance, until it reaches point B.


Notes:
My additional touch: On top of creating the general Djikstra's algorithm, I also added an optimization that makes the
algorithm a bit faster and more similar to the A* algorithm. By giving each node access to the end node, it can add
on a value to its priority equal to half its distance from the end point (didn't want to do the entire distance so the
distance to start still has more weight). This allows for those points that are moving closer to the end point to be
higher up on the priority queue than those moving away, even if they have the same distance from the start.

Here I will briefly describe some of the issues I had while coding this:

    Code - The most difficult part of this project was surely the Min-Priority Queue, as using Djikstra's algorithm was
            not much different from the breadth-first search in the 8-Puzzle or in Cannibals-And-Missionaries. Understanding
            how to add objects was simple enough, but removing them and implementing the rework method was more difficult.
            Overall, though, the code was simple enough, but I continuously got stuck on many small errors in my code and
            special scenarios such as if the left node in the heap exists but the right does not. Besides the Min-Priority
            Queue, one of the most difficult parts of this project was simply debugging methods because of the size of the
            usa.txt file, and using a smaller test set was very helpful with this. Lastly, at first it took several minutes
            to find a path between a start and end that required more than 10 steps, but by disallowing circling back to
            nodes that have already been taken off the queue, it greatly reduces the number of items on the queue at any
            given time. This addition is the boolean[] visited.

    Graphics - The most difficult graphical problem was converting the coordinates given for each intersection in the
            text file to the Canvas's coordinates. After experimenting, I figured that the coordinates roughly fit a
            10000x5000 box, so I resized each coordinate to fit into the map, which after some trial and error turned out
            to be 640x906. I also found out that the coordinates were given with the bottom left as (0, 0) and increasing y
            as you go up, so I had to invert the y coordinate. Using mapUSA2 initially, the coordinates were wider than
            the picture at the top and narrower below, so I had to design a way to try and reshape the coordinates based
            on their location (as commented out in my readjustX and readjustY methods). It did not fit because the picture
            did not use latitude lines as a horizontal but rather curved as in most pictures. I tried taking points above the
            center line and decreasing the x-value and taking lines below the center line and increasing the x-value, but
            I ended up creating an upside down V in the middle of the US without any points. I tried and almost got the
            edges to fit, but it severely misplaced the rest of the points, so instead I found a picture on the internet
            that used latitude lines as horizontals and uploaded it into my project.

    One Last Note - For the Graphics, if you do not quit out of the file before reopening it, it requires you to drag out the
            corner of the window to see the graphics. I do not know why this is the case, but at the least, the title bar
            should be visible and should allow you to still see the graphics.
