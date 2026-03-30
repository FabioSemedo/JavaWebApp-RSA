package org.vaadin.example.rsa.quad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

/**
 * This class follows the Facade design pattern
 * and <b>presents a single access point to manage QuadTrees</b>.
 * It provides methods for inserting, deleting and finding elements implementing {@link HasPoint}.
 * This class corresponds to the Client in the Composite design pattern used in this package.
 * @author Fábio Semedo
 * @author José Paulo Leal {@code jpleal@fc.up.pt}
 * @param <T>
 */
public class PointQuadtree<T extends HasPoint> implements Iterable<T> {
    /// Root of this QuadTree.
    Trie<T> top;

    /**
     * Iterator over points stored in the internal node structure.
     * It traverses the tree depth first, using coroutine with threads,
     * and collects all points in no particular order.
     */
    public class PointIterator implements Iterator<T>, Runnable, Visitor<T> {
        private final Stack<Trie<T>> stack;  // Stack to manage tree traversal
        private final List<T> points;  // List to store the points we find
        private int index;  // Current index for iteration

        public PointIterator() {
            stack = new Stack<>();
            points = new ArrayList<>();
            index = 0;

            // Initially, push the root to the stack
            stack.push(top);
            traverse();
        }

        @Override
        public boolean hasNext() {
            return index < points.size();  // Check if we have more points
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return points.get(index++);  // Return the next point in the list
        }

        @Override
        public void run() {
            traverse();  // Start traversal in a separate thread
        }

        @Override
        public void visit(LeafTrie<T> leaf) {
            // Add all points from this leaf to the points list
            points.addAll(leaf.getPoints());
        }

        @Override
        public void visit(NodeTrie<T> node) {
            // Push children of this node onto the stack to visit them later
            for (Trie<T> subTrie : node.getTries()) {
                stack.push(subTrie);
            }
        }

        private void traverse() {
            while (!stack.isEmpty()) {
                Trie<T> current = stack.pop();

                if (current instanceof LeafTrie) {
                    visit((LeafTrie<T>) current);
                } else if (current instanceof NodeTrie) {
                    visit((NodeTrie<T>) current);
                }
            }
        }

    }

    /**
     * Create a QuadTree for points in a rectangle with given top left and bottom right corners.
     * This is typically used for a region in the Cartesian plane,
     * as used in mathematics, and can also be used for geographic coordinates.
     * <p>The new Trie starts as a LeafTrie
     *
     * @param topLeftX x coordinate of top left corner
     * @param topLeftY y coordinate of top left corner
     * @param bottomRightX x coordinate of bottom right corner
     * @param bottomRightY y coordinate of bottom right corner
     */
    public PointQuadtree(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
        this.top =new LeafTrie<>(topLeftX, topLeftY, bottomRightX, bottomRightY);
    }

    /**
     * Find a recorded point with the same coordinates of given point
         @param point with requested coordinates
         @return recorded point, if found; null otherwise
     */
    public T find(T point) {
        return top.find(point);
    }

    /**
     * Returns a set of points at a distance smaller or equal to radius from point with given coordinates.
     * @param x - coordinate of point
     * @param y - coordinate of point
     * @param radius - from given point
     * @return set of instances of type {@link HasPoint}
     */
    public Set<T> findNear(double x, double y, double radius){
        Set<T> set = new HashSet<>();
        top.collectNear(x,y,radius,set);
        return set;
    }

    /// Check if a given point is within Trie bounds.
    private void validate(T point) throws PointOutOfBoundException{
        boolean valid = top.topLeftX<= point.x() && point.x() <= top.bottomRightX
                && top.bottomRightY <= point.y() && point.y() <= top.topLeftY;
        if(!valid){
            throw new PointOutOfBoundException("Given point: ("+point.x()+","+point.y()+") is outside Trie bounds. [("+top.topLeftX+","+top.topLeftY+");("+top.bottomRightX+","+ top.bottomRightY+")]");
        }
    }

    /**
        Insert given point in the QuadTree
        @param point to be inserted
     */
    public void insert(T point) throws PointOutOfBoundException{
        validate(point);
        top.insert(point);
    }

    /**
     * Insert point, replacing existing point in the same position
     * @param point point to be inserted
     */
    public void insertReplace(T point) throws PointOutOfBoundException
    {
        validate(point);
        top.insertReplace(point);
    }
    /**
     * Delete given point from QuadTree, if it exists there
     * @param point to be deleted
     */
    public void delete(T point){
        top.delete(point);
    }

    /**
     * A set containing all points in the QuadTree
     * @return set of instances of type {@link HasPoint}
     */
    public Set<T> getAll(){
        Set<T> set = new HashSet<>();
        top.collectAll(set);
        return set;
    }

    /**
     * @return an iterator over the points stored in the QuadTree
     */
    @Override
    public Iterator<T> iterator(){
        return new PointIterator();
    }

}
