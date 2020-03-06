/*  Djikstras Graphics Class
    Name: Ethan Chen
    Date Completed: March 5, 2020
*/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DjikstrasUSGraphics extends Canvas { // Graphical User Interface

    private static BufferedImage US;
    private static Intersection[] solutionIntersections = new Intersection[1000];
    private static DjikstrasSolver maprouting;
    private static DjikstrasSolver maproutingDefault;
    private static int starting;
    private static int ending;
    private static int xClicked;
    private static int yClicked;
    private static int xCoordAdjusted;
    private static int yCoordAdjusted;
    private static int startingXClicked;
    private static int startingYClicked;
    private static int endingXClicked;
    private static int endingYClicked;
    private static int numClicks = 0;
    private static boolean initialOpen;

    public static void main(String[] args) throws IOException {
        initialOpen = true;
        US = ImageIO.read(new File("src/mapUSA2.png"));
        US = resize(US, 1000, 600);

        try {
            maproutingDefault = new DjikstrasSolver(0, 0, new File("src/usa.txt"), false); // used as default to get intersections list for initial state
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Djikstras Route-Finder"); // setup of frame
        Canvas canvas = new DjikstrasUSGraphics();
        canvas.setSize(1000, 700);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { // click on screen

                initialOpen = false;
                numClicks++;
                Graphics gc = canvas.getGraphics();
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // clear

                if (numClicks == 4) {
                    numClicks = 0;
                }

                xClicked = e.getX();
                yClicked = e.getY();
                xCoordAdjusted = (int) (((xClicked - 40) / 906.0) * 10000);
                yCoordAdjusted = (int) (((625 - yClicked) / 640.0) * 5000); // grabbing where clicked and switching it into coordinate system of intersections from text file
                canvas.repaint(); // repaint the canvas

                return;
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void paint(Graphics g) {
        g.drawImage(US, 0, 50, null);
        if (initialOpen) { // initial opening, default state
            g.drawString("Click on a point to select start", 15, 20);
            for (Intersection intersection : maproutingDefault.intersections) {
                if (intersection != null) {
                    int x = readjustX(intersection.xCoord, intersection.yCoord);
                    int y = readjustY(intersection.yCoord, intersection.xCoord);
                    g.fillOval(40 + x, 625 - y, 5, 5); // trial and error to get numbers
                }
            }
        } else {

            if (numClicks == 1) { // first click, selects starting location

                for (Intersection intersection : maproutingDefault.intersections) {
                    if (intersection != null) {
                        int x = readjustX(intersection.xCoord, intersection.yCoord);
                        int y = readjustY(intersection.yCoord, intersection.xCoord);
                        g.fillOval(40 + x, 625 - y, 5, 5);
                    }
                }

                boolean isIntersection = false;
                for (Intersection inter : maproutingDefault.intersections) { // checks to see if click matches any intersections
                    if (Math.abs(inter.xCoord - xCoordAdjusted) < 30 && Math.abs(inter.yCoord - yCoordAdjusted) < 30) { // margin of error
                        starting = inter.identifier;
                        startingXClicked = xClicked;
                        startingYClicked = yClicked;
                        g.setColor(Color.BLUE);
                        g.fillOval(startingXClicked, startingYClicked, 10, 10);
                        g.setColor(Color.BLACK);
                        g.drawString("Click on a point to select end", 15, 20);
                        return;
                    }
                }
                if (!isIntersection) { // doesn't match any intersections, try again
                    numClicks--;
                    for (Intersection intersection : maproutingDefault.intersections) {
                        if (intersection != null) {
                            int x = readjustX(intersection.xCoord, intersection.yCoord);
                            int y = readjustY(intersection.yCoord, intersection.xCoord);
                            g.fillOval(40 + x, 625 - y, 5, 5);
                            g.drawString("Click on a point to select start", 15, 20);
                        }
                    }
                    return;
                }

            } else if (numClicks == 2) { // second click, select end. Pretty much the same code as above
                g.setColor(Color.BLUE);
                g.fillOval(startingXClicked, startingYClicked, 10, 10);
                g.setColor(Color.BLACK);

                boolean isIntersection = false;
                for (Intersection inter : maproutingDefault.intersections) {
                    if (Math.abs(inter.xCoord - xCoordAdjusted) < 15 && Math.abs(inter.yCoord - yCoordAdjusted) < 15) {
                        ending = inter.identifier;
                        endingXClicked = xClicked;
                        endingYClicked = yClicked;
                        g.setColor(Color.RED);
                        g.fillOval(xClicked, yClicked, 10, 10);
                        g.setColor(Color.BLACK);
                        g.drawString("Click anywhere to start Djikstra's Algorithm", 15, 20);
                        return;
                    }
                }
                if (!isIntersection) { // if it misses an intersection
                    numClicks--;
                    for (Intersection intersection : maproutingDefault.intersections) {
                        if (intersection != null) {
                            int x = readjustX(intersection.xCoord, intersection.yCoord);
                            int y = readjustY(intersection.yCoord, intersection.xCoord);
                            g.fillOval(40 + x, 625 - y, 5, 5);
                        }
                    }
                    g.setColor(Color.BLUE);
                    g.fillOval(startingXClicked, startingYClicked, 10, 10);
                    g.setColor(Color.BLACK);
                    g.drawString("Click on a point to select end", 15, 20);
                    return;
                }

            } else if(numClicks == 3){ // Click to start, creates the djikstras solver and draws each point on the path between start and end

                g.setColor(Color.BLUE);
                g.fillOval(startingXClicked, startingYClicked, 10, 10);
                g.setColor(Color.RED);
                g.fillOval(endingXClicked, endingYClicked, 10, 10);

                try {
                    Stopwatch stop = new Stopwatch();
                    maprouting = new DjikstrasSolver(starting, ending, new File("src/usa.txt"), false);
                    StdOut.println("Time to find route: " + stop.elapsedTime() + " seconds");
                } catch (IOException d) {
                    d.printStackTrace();
                }
                g.setColor(Color.MAGENTA);
                for (TravelerNode node : maprouting.getSolution()) {
                    Intersection intersection = node.currentIntersection;
                    if (intersection != null) {
                        int x = readjustX(intersection.xCoord, intersection.yCoord);
                        int y = readjustY(intersection.yCoord, intersection.xCoord);
                        g.fillOval(40 + x, 625 - y, 5, 5);
                    }
                }
                g.setColor(Color.BLACK);

                g.drawString("Click anywhere to reset", 15, 20);
                g.drawString("This route is roughly " + (int) maprouting.totalDistance/3 + " miles long", 15, 40);
                // distance from topeka-tampa=1078 miles, 3538 units on map, accounting for direct line error, roughly 3 units to 1 mile
                return;

            } else { // resets everything
               g.drawString("Click on a point to select start", 15, 20);
                for (Intersection intersection : maproutingDefault.intersections) {
                    if (intersection != null) {
                        int x = readjustX(intersection.xCoord, intersection.yCoord);
                        int y = readjustY(intersection.yCoord, intersection.xCoord);
                        g.fillOval(40 + x, 625 - y, 5, 5);
                    }
                }
            }
        }
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) { // resizes images - taken from StackOverflow
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public int readjustX(int xCoordinate, int yCoordinate) { // takes the xCoordinate of intersection and fixes it to resized coordinate system
      /*  double ySqueeze = yCoordinate / 5000.0;
        int yFit = (int) (ySqueeze * 600);*/

        double xSqueeze = xCoordinate / 10000.0; // trial and error to get numbers
        int xFit = (int) (xSqueeze * 906);

       /* if(yFit > 230) {
            if(xFit > 322) {
                xFit -= (yFit - 200) / 7;
            } else {
                xFit += (yFit - 200) / 7;
            }
        } else {
            if(xFit > 322) {
                xFit += (200 - yFit) / 7;
            } else {
                xFit -= (200 - yFit) / 7;
            }
        }*/


        return xFit;
    }

    public int readjustY(int yCoordinate, int xCoordinate) { // takes the yCoordinate of intersection and fixes it to resized coordinate system
       /* double xSqueeze = xCoordinate / 10000.0;
        int xFit = (int) (xSqueeze * 1000);*/

        double ySqueeze = yCoordinate / 5000.0; // trial and error to get numbers
        int yFit = (int) (ySqueeze * 640);

       /* if(xFit < 322) {
            yFit += (322-xFit)/10;
        } else {
            yFit += (xFit-322)/10;
        }*/
        return yFit;
    }
}