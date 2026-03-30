package org.vaadin.example.rsa.quad;

/**
 * The Element interface, part of the abstract layer of the Visitor design pattern.
 * In this case, the elements is parameterized by HasPoint and defines a method to accept a Visitor.
 * This type must be added to the Component (Trie) of the Composite to ensure that all types of the structure implement it.
 *
 * @author José Paulo Leal
 * {@code jpleal@fc.up.pt}
 */
public interface Element<T extends HasPoint>
{
    /**
     * Accept a visitor to operate on a node of the composite structure
     * @param visitor to the node
     */
    public void accept(Visitor<T> visitor);
}
