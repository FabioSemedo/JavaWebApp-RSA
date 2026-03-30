package org.vaadin.example.rsa.ride;

import org.vaadin.example.rsa.match.PreferredMatch;
import org.vaadin.example.rsa.match.RideMatch;
import org.vaadin.example.rsa.user.User;
import org.vaadin.example.rsa.match.Location;
import org.vaadin.example.rsa.quad.HasPoint;



import java.util.Comparator;

import static org.vaadin.example.rsa.quad.Trie.getDistance;


public class Ride implements HasPoint, RideMatchSorter {
    private static Integer nextId = 1;
    private User user;
    private Location from;
    private Location to;
    private Location current;
    private String plate;
    private float cost;
    private Integer id;
    private RideMatch match;




    public Ride(User user, Location from, Location to, String plate, float cost) {

        this.user = user;
        this.from = from;
        this.to = to;
        this.plate = plate; //se a placa for null, então é um passenger
        this.cost = cost;
        current = from;
        id = nextId++;
        match = null;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public long getId(){
        return id;
    }

    public boolean isDriver(){

        return plate != null;

    }

    public boolean isPassenger(){

        return plate == null;

    }

    public RideRole getRideRole(){

            if (isDriver()){ return RideRole.DRIVER;}
            return RideRole.PASSENGER;
    }

    public Location getCurrent() {
        return current;
    }

    public void setCurrent(Location current) {
        this.current = current;
    }


    //NAO SEI COMO GERIR
    public RideMatch getMatch(){

        return match;
    }

    //NAO SEI OQ EU FIZ AQUI
    public void setMatch(RideMatch match){
        this.match = match;
    }

    public double x(){
       return current.x();
    }

    public double y(){
        return current.y();
    }

    public boolean isMatched()
    {
        return match != null;
    }

    //TODO TERMINAR ISSO

    public Comparator<RideMatch> getComparator() {

        PreferredMatch preferredMatch = user.getPreferredMatch();
        if (preferredMatch == null){
            preferredMatch = PreferredMatch.BETTER;
        }
        RideRole thisRole = getRideRole();
        RideRole otherRole = getRideRole().other();
        //final RideRole thisRole = getRideRole();
        //final RideRole otherRole = getRideRole().other();

        switch(preferredMatch){

            case BETTER:
                return new Comparator<RideMatch>() {
                    public int compare(RideMatch ride1, RideMatch ride2) {
                            if (ride1.getStars(otherRole) > ride2.getStars(otherRole)){
                                return -1;
                            }
                            else {
                                if (ride1.getStars(otherRole) < ride2.getStars(otherRole)){
                                    return 1;
                                }
                                else{

                                    if (ride1.getId() == ride2.getId()){
                                        return 0;
                                    }
                                    return Long.compare(ride1.getId(), ride2.getId());

                                }
                            }
                    }
                };


            case CLOSER:
                return new Comparator<RideMatch>() {
                    public int compare(RideMatch ride1, RideMatch ride2) {
                        double distanceRide1 = getDistance(getCurrent().x(), getCurrent().y(), ride1.getWhere(otherRole).x(), ride1.getWhere(otherRole).y());
                        double distanceRide2 = getDistance(getCurrent().x(), getCurrent().y(), ride2.getWhere(otherRole).x(), ride2.getWhere(otherRole).y());
                        if (distanceRide1 > distanceRide2  ){
                            return 1;
                        }
                        else {
                            if ( distanceRide1 < distanceRide2 ){
                                return -1;
                            }
                            else{
                                if (ride1.getId() == ride2.getId()){
                                    return 0;
                                }
                                return Long.compare(ride1.getId(), ride2.getId());

                            }
                        }
                    }
                };

            case CHEAPER:
                return new Comparator<RideMatch>() {
                    public int compare(RideMatch ride1, RideMatch ride2) {
                        if (ride1.getCost() > ride2.getCost()){
                            return 1;
                        }
                        else {
                            if (ride1.getCost() < ride2.getCost()){
                                return -1;
                            }
                            else{
                                if (ride1.getId() == ride2.getId()){
                                    return 0;
                                }
                                return Long.compare(ride1.getId(), ride2.getId());

                            }
                        }
                    }
                };




        }

        return null;
    }
}
