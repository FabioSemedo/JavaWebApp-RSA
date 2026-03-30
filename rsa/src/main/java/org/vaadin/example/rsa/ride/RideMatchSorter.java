package org.vaadin.example.rsa.ride;
import org.vaadin.example.rsa.match.RideMatch;

import java.util.Comparator;

public interface RideMatchSorter {

    Comparator<RideMatch> getComparator();
}
