package org.vaadin.example.rsa.quad;

import java.util.Set;

/**
 * Abstract class common to all classes implementing the trie structure.
 * Defines methods required by those classes and provides general methods for checking overlaps and computing distances.
 * This class corresponds to the Component in the Composite design pattern.
 *
 * @author Fábio Semedo
 * @author José Paulo Leal {@code jpleal@fc.up.pt}
 * @param <T> type that extends interface HasPoint
 */
public abstract class Trie<T extends HasPoint> implements Element<T>{
    ///Bottom right corner x coordinate.
    protected final double bottomRightX;
    ///Bottom right corner Y coordinate.
    protected final double bottomRightY;
    ///Top left corner X coordinate.
    protected final double topLeftX;
    ///Top left corner Y coordinate.
    protected final double topLeftY;

    static int capacity = 20;

    /**
     * Quadrants of NodeTries.
     * Names are from the compass points NE, NW, SE, SW
     * Notable implicitly defined methods:
     * @code Trie.Quadrant[] values()
     * @code Trie.Quadrant valueOf(String name)
     */
    enum Quadrant{
        ///NorthEast, upper right corner.
        NE,
        ///NorthWest, upper left corner.
        NW,
        ///SouthEast, Bottom right corner.
        SE,
        ///SouthWest, Bottom left corner.
        SW
    }

    /** Create an instance from the top left and right bottom points' coordinates.
     *
     * @param topLeftX x coordinate of top left corner.
     * @param topLeftY y coordinate of top left corner.
     * @param bottomRightX x coordinate of bottom right corner.
     * @param bottomRightY y coordinate of bottom right corner.
     */
    protected Trie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY){
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
    }

    /**
     * Euclidean distance between two 2D points
     *
     * @param x1 x coordinate of first point
     * @param y1 y coordinate of first point
     * @param x2 x coordinate of second point
     * @param y2 y coordinate of second point
     *
     * @return distance between given points
     */
    public static double getDistance(double x1, double y1, double x2, double y2) {
        // c = sqrt(a*a + b*b)  Pythagoras
        x1 -= x2;
        y1 -= y2;
        return Math.sqrt(x1*x1 + y1*y1);
    }

    /// Get capacity of a bucket
    public static int getCapacity(){
        return Trie.capacity;
    }

    /**
     * Set capacity of a bucket
     @param capacity bucket capacity
     */
    public static void setCapacity(int capacity) {
        Trie.capacity = capacity;
    }

    /**
        Description of Trie capacity and area.
        <br> Trie capacity: num
        <br> Area:
        <br>Top = (x1,y1);
        <br>Bottom = (x2,y2)
        @return String description of Trie
     */
    @Override
    public String toString(){
        return "Trie capacity: "+Trie.capacity+"\nArea:\nTop=("+topLeftX+","+topLeftY+")\nBottom=("+bottomRightX+","+bottomRightY+")";
    }

    //Default visibility == package private
    /**
     * Find a recorded point with the same coordinates of given point
     * @param point  with requested 2D coordinates
     * @return recorded point, if found; null otherwise
     */
    abstract T find(T point);

    /**
     * Insert given point
     * @param
     * point to be inserted
     * @return
     * changed parent node
     */
    abstract Trie<T> insert(T point);

    /**
     * Insert given point, replacing existing points in same location
     * @param
     * point point to be inserted
     * @return
     * changed parent node
     */
    abstract Trie<T> insertReplace(T point);

    /**
     * Collect all points in this node and its descendants in given set
     * @param
     * points - set of {@link HasPoint} for collecting points
     */
    abstract void collectAll(Set<T> points);

    /**
     * Collect points at a distance smaller or equal to radius from (x,y) and place them in given list
     * @param
     * x coordinate of point
     * @param
     * y coordinate of point
     * @param
     * radius from given point
     * @param
     * points set for collecting points
     */
    abstract void collectNear(double x, double y, double radius, Set<T> points);

    /**
     * Delete given point
     * @param
     * point to delete
     */
    abstract void delete(T point);

    /**
     * Check if overlaps with given circle
     * @param
     * x centre coordinate of circle
     * @param
     * y centre coordinate of circle
     * @param radius of circle
     * @return true if overlaps and false otherwise
     */
    boolean overlaps(double x, double y, double radius){
        double closestX = limit(x, topLeftX, bottomRightX);
        double closestY = limit(y, bottomRightY,topLeftY);

        return getDistance(x, y, closestX, closestY) <= radius;
    }

    private static double limit(double value, double min, double max){
        return Math.max(min, Math.min(max, value));
    }
}