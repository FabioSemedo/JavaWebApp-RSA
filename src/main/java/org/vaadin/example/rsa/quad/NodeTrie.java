package org.vaadin.example.rsa.quad;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Trie with 4 sub tries with equal dimensions covering all its area.
 * This class corresponds to the Container in the Composite design pattern.
    @author Fábio Semedo
 @author José Paulo Leal {@code jpleal@fc.up.pt}
 * @param <T>
 */

public class NodeTrie<T extends HasPoint> extends Trie<T>{
    ///Sub-tries of this trie, indexed by quadrant.
    final Map< Trie.Quadrant, Trie<T>> tries;

    /**
     * Create a node in given rectangle.
     *
     * @param topLeftX  of rectangle.
     * @param topLeftY  of rectangle.
     * @param bottomRightX  of rectangle.
     * @param bottomRightY  of rectangle.
     */
    public NodeTrie(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
        super(topLeftX, topLeftY, bottomRightX, bottomRightY);
        tries = new HashMap<>();

        double centerX = (topLeftX + bottomRightX)/2;
        double centerY = (topLeftY + bottomRightY)/2;

        var nw = new LeafTrie<T>(topLeftX, topLeftY, centerX, centerY);
        tries.put(Quadrant.NW, nw);

        var ne = new LeafTrie<T>(centerX, topLeftY, bottomRightX, centerY);
        tries.put(Quadrant.NE, ne);

        var sw = new LeafTrie<T>(topLeftX, centerY, centerX, bottomRightY);
        tries.put(Quadrant.SW, sw);

        var se = new LeafTrie<T>(centerX, centerY, bottomRightX, bottomRightY);
        tries.put(Quadrant.SE, se);
    }

    /**
     * Quadrant of a point in this node.
     * @param point - to compute quadrant.
     * @return quadrant
     */
    Trie.Quadrant quadrantOf(T point) {
        boolean north = point.y() - this.bottomRightY <= 0.5 * (this.topLeftY - this.bottomRightY);
        boolean west = point.x() - this.topLeftX <= 0.5 * (this.bottomRightX - this.topLeftX);

        if(west){       //West
            if(north){  //North
                return Quadrant.NW;
            }else{      //South
                return Quadrant.SW;
            }
        }else{          //East
            if(north){  //North
                return Quadrant.SW;
            }else{      //South
                return Quadrant.SE;
            }
        }
    }

    T find(T point) {
        return tries.get(quadrantOf(point)).find(point);
    }

    Trie<T> insert(T point) {
        return tries.get(quadrantOf(point)).insert(point);
    }

    Trie<T> insertReplace(T point) {
        return tries.get(quadrantOf(point)).insertReplace(point);
    }

    void delete(T point) {
        tries.get(quadrantOf(point)).delete(point);
    }

    void collectNear(double x, double y, double radius, Set<T> points) {
        for(Trie<T> trie : tries.values()) {
            if(trie.overlaps(x, y, radius)) {
                trie.collectNear(x,y,radius,points);
            }
        }
    }

    void collectAll(Set<T> points) {
        for(Trie<T> trie : tries.values()) {
            trie.collectAll(points);
        }
    }

    /**
     * Description of NodeTrie Tries and Points inside those Tries.
     * <br> Trie HashMap: {@code (quadrants, tries)}  <br> Set of Points: {@code points}
     * @return String describing the NodeTrie
     */
    @Override
    public String toString(){
        Set<T> points = new HashSet<>();
        this.collectAll(points);

        return tries.toString() + "\n" + points;
    }

    /**
     * A collection of tries that descend from this node
     * @return collection of tries
     */
    Collection<Trie<T>> getTries(){
        return new HashSet<>(tries.values());
    }

    public void accept(Visitor<T> visitor) {
        visitor.visit(this);
    }
}
