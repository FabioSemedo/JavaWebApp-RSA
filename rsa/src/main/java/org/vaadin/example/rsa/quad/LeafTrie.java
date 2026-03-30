package org.vaadin.example.rsa.quad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * A Trie that has no descendants.
 * This class corresponds to the Leaf in the Composite design pattern.
 * @author Fabio Semedo
 * @author José Paulo Leal {@code jpleal@fc.up.pt}
 */

public class LeafTrie<T extends  HasPoint> extends Trie<T> {
    /**
        LeafTries store their points in a {@link ArrayList}
      */
    private final ArrayList<T> list;

    /**
     * Create a leaf in given rectangle.
     *
     * @param topLeftX - of rectangle.
     * @param topLeftY - of rectangle.
     * @param bottomRightX - of rectangle.
     * @param bottomRightY - of rectangle.
     */
    public LeafTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
        super(topLeftX, topLeftY, bottomRightX, bottomRightY);
        list = new ArrayList<>();
    }

    T find(T point) {
        for (T p: list){
            if(p.x()== point.x() && p.y()== point.y()){
                return p;
            }
        }
        return null;
    }

    Trie<T> insert(T point) {
        list.add(point);
        return this;
    }

    Trie<T> insertReplace(T point) {
        for (int i = list.size()-1; i >= 0; i--) {
            var p = list.get(i);
            if(point.x() == p.x() && point.y() == p.y()){
                list.set(i, point);
                break;
            }
        }
        return this;
    }
    
    void delete(T point) {
        for (int i = 0; i< list.size(); i++) {
            var p = list.get(i);
            if(p.x() == point.x() && p.y() == point.y()){
                list.set(i, list.get(list.size() - 1));
                list.remove(list.size() - 1);
            }
        }
    }
    
    void collectNear(double x, double y, double radius, Set<T> points) {
        for(T p: list){
            if(getDistance(p.x(), p.y(), x, y) <= radius) {
                points.add(p);
            }
        }
    }
    
    void collectAll(Set<T> points) {
        points.addAll(list);
    }

    /**
     * @return The points in this Trie.
     */
    @Override
    public String toString(){
        return list.toString();
    }

    public void accept(Visitor<T> visitor){
        visitor.visit(this);
    }

    /**
     * A collection of points currently in this leaf
     * @return collection of points
     */
    Collection<T> getPoints(){
        return list;
    }
}
