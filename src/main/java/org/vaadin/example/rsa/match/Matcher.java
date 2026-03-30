package org.vaadin.example.rsa.match;


import org.vaadin.example.rsa.RideSharingAppException;
import org.vaadin.example.rsa.quad.PointQuadtree;
import org.vaadin.example.rsa.ride.Ride;

import org.vaadin.example.rsa.user.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Matcher implements Serializable {

    private Map<Long, RideMatch> matches;
    private PointQuadtree<Ride> qtree;
    private Map <Long, Ride> rides;
    private static Location topLeft = new Location(0, 100);
    private static Location bottomRight = new Location(100, 0);
    private static double radius;

    public Matcher(){

        this.matches = new HashMap<>();
        this.rides = new HashMap<>();
        this.qtree = new PointQuadtree<Ride>(topLeft.x(), topLeft.y(), bottomRight.x(), bottomRight.y());
    }

    public long addRide(User user, Location from, Location to, String plate, float cost){
        Ride ride = new Ride(user, from, to, plate, cost);
        rides.put(ride.getId(), ride);
        qtree.insert(ride); //inserir ride em na regiao
        return ride.getId();

    }

    public static Location getTopLeft(){
        return topLeft;

    }

    public static void setTopLeft(Location topLeft){
        Matcher.topLeft = topLeft;
    }

    public static Location getBottomRight(){

        return bottomRight;
    }

    public static void setBottomRight(Location bottomRight){
        Matcher.bottomRight = bottomRight;
    }

    public static double getRadius(){
        return radius;
    }


    public static void setRadius(double radius){
        Matcher.radius = radius;
    }



    public SortedSet<RideMatch> updateRide(long rideId, Location current){

        Ride ride = rides.get(rideId);

        //atualizar a nova localização de ride em qtree
        qtree.delete(ride);
        ride.setCurrent(current);
        qtree.insert(ride);



        if(!ride.isMatched()){
            Set<Ride> nears =  qtree.findNear(ride.x(), ride.y(), radius);
            Set<Ride> possible = nears.stream().filter(r -> !r.getRideRole().equals(ride.getRideRole())).collect(Collectors.toSet());

            SortedSet<RideMatch> possibleMatchers = new TreeSet<>(ride.getComparator());
            for (Ride r : possible) {
                RideMatch match = new RideMatch(r, ride);
                possibleMatchers.add(match);

            }
            return possibleMatchers;
        }
        return null;

    }


    public void acceptMatch(long rideId, long matchId){
        Ride right = rides.get(rideId);
        Ride left = rides.get(matchId);
        RideMatch match = new RideMatch(right, left);
        matches.put(match.getId(), match);

    }


    public void concludeRide(long rideId, UserStars stars){
        Ride ride = rides.get(rideId);
        User user = ride.getUser();



        RideMatch match = matches.get(ride.getId());
        User otherUser = match.getRide(ride.getRideRole().other()).getUser();
        otherUser.addStars(stars, ride.getRideRole().other());
        rides.remove(rideId);

        }

}



