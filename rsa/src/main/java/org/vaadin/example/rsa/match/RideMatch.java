package org.vaadin.example.rsa.match;

import  org.vaadin.example.rsa.ride.Ride;
import  org.vaadin.example.rsa.ride.RideRole;
import  org.vaadin.example.rsa.user.Car;



public class RideMatch {
    private static long nextId = 1;
    private final Ride right;
    private final Ride left;
    private final long id;



    public RideMatch(Ride left, Ride right) {
        this.right = right;
        this.left = left;
        id = nextId++;

    }

    public long getId()
    {
        return id;
    }

    public Ride getRide( RideRole role) {
        if (role == right.getRideRole()) {
            return right;
        }
        return left;
    }

    public Ride getLeft() {
        return left;
    }
    public Ride getRight() {
        return right;
    }

    public String getName(RideRole role){

        if (role == right.getRideRole()) {
            return right.getUser().getName();
        }
        return left.getUser().getName();
    }

    public float getStars(RideRole role){

        if (role == right.getRideRole()) {

            return right.getUser().getAverage(role);
        }
        return left.getUser().getAverage(role);


    }

    public Location getWhere(RideRole role){

        if (role == right.getRideRole()) {
            return right.getCurrent();
        }
        return left.getCurrent();

    }

    public Car getCar(){
        if (right.isDriver())
        {
            return right.getUser().getCar(right.getPlate());
        }
        return left.getUser().getCar(left.getPlate());
    }

    public float getCost(){
        if (right.isDriver())
        {
            return right.getCost();
        }
        return left.getCost();
    }

}
