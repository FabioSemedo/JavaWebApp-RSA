package org.vaadin.example.rsa;

import org.vaadin.example.rsa.match.RideMatch;
import org.vaadin.example.rsa.ride.RideRole;
import org.vaadin.example.rsa.user.UserStars;
import org.vaadin.example.rsa.user.Users;
import org.vaadin.example.rsa.user.User;
import org.vaadin.example.rsa.match.Matcher;
import org.vaadin.example.rsa.match.PreferredMatch;
import org.vaadin.example.rsa.match.Location;
import java.io.File;
import java.util.Set;

public class Manager {
    private static Manager manager;
    public static final File USERS_FILE = new File("users.ser");
    private Users users;
    private Matcher matcher;


    private Manager() {
        try {
            users = Users.getInstance();
        } catch (RideSharingAppException e) {
            e.printStackTrace();
        }
        matcher = new Matcher();

    }

    public static Manager getInstance()
            throws RideSharingAppException{

        if (manager == null){
            manager = new Manager();
        }
        return manager;

    }

    void reset(){
        manager = null;
    }

    public User register(String nick,
                         String name)
            throws RideSharingAppException{

        return users.register(nick, name);

    }

    public PreferredMatch getPreferredMatch(String nick,
                                            String key)
            throws RideSharingAppException{

        return users.getUser(nick).getPreferredMatch();

    }

    public void setPreferredMatch(String nick,
                                  String key,
                                  PreferredMatch preferred)
            throws RideSharingAppException{

        users.getUser(nick).setPreferredMatch(preferred);

    }

    public long addRide(String nick,
                        String key,
                        Location from,
                        Location to,
                        String plate,
                        float cost)
            throws RideSharingAppException{

        if(users.authenticate(nick, key)) {
            return matcher.addRide(users.getUser(nick), from, to, plate, cost);
        }
        throw new RideSharingAppException("User not authenticated");
    }

    public Set<RideMatch> updateRide(long rideId,
                                     Location current){

        return matcher.updateRide(rideId, current);

    }

    public void acceptMatch(long rideId,
                            long matchId){

        matcher.acceptMatch(rideId, matchId);
    }

    public void concludeRide(long rideId,
                             UserStars classification){

        matcher.concludeRide(rideId, classification);
    }

    public double getAverage(String nick,
                             RideRole role)
            throws RideSharingAppException{

        return users.getUser(nick).getAverage(role);

    }





}
