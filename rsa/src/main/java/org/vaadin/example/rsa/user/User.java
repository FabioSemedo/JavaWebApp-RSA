package org.vaadin.example.rsa.user;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.vaadin.example.rsa.match.PreferredMatch;
import org.vaadin.example.rsa.ride.Ride;
import org.vaadin.example.rsa.ride.RideRole;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

/**
 * A user of the Ride Sharing App.
 * An instance of this class records the user's authentication and other relevant data.
 * @author José Paulo Leal {@code jpleal@fc.up.pt}
 * @author Maria Pereira
 */


public class User implements Serializable{

    private final String nick;

    private String name;

    private final String key;

    private final HashMap<String, Car> cars;
    private PreferredMatch preferredMatch;
    private final LinkedList<Integer> rides;
    private final LinkedList<Integer> stars;


    /**
     * Creates a User instance.
     * This is the only constructor and is package private.
     * Hence, users can only be instanced in this package, using the method {@link Users#register(String, String)}
     * @param nick of user, must be unique.
     *             Nicks can have letters (upper and lowercase) and digits but not other characters.
     * @param name of user.
     */
    User(String nick, String name) {
        this.nick = nick;
        this.name = name;
        this.key = generateKey();
        this.cars = new HashMap<String, Car>();
        this.preferredMatch = PreferredMatch.BETTER;
        this.rides = new LinkedList<>();
        this.stars = new LinkedList<>();
    }


    /// Get user authentication key
    /// @return key of user
    //TODO: public for unit tests, possible security risk.
    public String getKey() {
        return key;
    }

    /// A key is generated to enable user authentication.
    /// @return key for this user
    String generateKey() {
        String id = nick + name;
        UUID uuid = UUID.nameUUIDFromBytes(id.getBytes());
        return uuid.toString();
    }

    /// Authenticates given key against the stored private key.
    /// @param key to check
    /// @return true if keys match; otherwise false
    public boolean authenticate(String key) {
        return this.key.equals(key);
    }

    /// The nick of this user: Cannot be changed as it a key.
    /// @return nick
    public String getNick() {
        return nick;
    }

    /// Get Name of user
    /// @return user's name
    public String getName() {
        return name;
    }

    /// Change user's name
    /// @param name to change
    public void setName(String name) {
        this.name = name;
    }

    /// Bind a car to this user. Can be used to change car features.
    /// @param car to add
    public void addCar(Car car) {
        cars.put(car.getPlate(), car);
    }

    /// Find a Car with given license plate
    /// @param plate of car
    /// @return car
    public Car getCar(String plate) {
        return cars.get(plate);
    }

    /// Remove binding between user and car.
    /// @param plate of car to remove from this user
    public void deleteCar(String plate) {
        cars.remove(plate);
    }



    /// Add stars to user according to a role. The registered values are used to compute an average.
    /// @param moreStars to add to this user
    /// @param role being rated, user or driver.
    public void addStars(UserStars moreStars, RideRole role) {
        if(role == RideRole.DRIVER) {
            rides.add(moreStars.getStars());
        }else{
            //RideRole.PASSENGER
            stars.add(moreStars.getStars());
        }
    }


    /**
     * Get a Collection of user's cars.
     * @return user's cars
     */
    public Collection<Car> getCars() {
        return cars.values();
    }



    /**
     * Get the total number of registered user's cars.
     * @return total number of cars.
     */
    public int getNumberCars() {
        return cars.size();
    }

    /**
     * Get the total number of rides where the user participated.
     * This includes both roles: driver and passenger.
     *
     * @return total number of rides the user has taken part in
     */
    public int getNumberRides() {
        return rides.size() + stars.size();
    }

    /**
     * Get the number of rides where the user acted as driver.
     * These are used to compute the user's driver rating.
     *
     * @return number of rides as driver
     */
    public int getNumberRidesDriver() {
        return rides.size();
    }

    /**
     * Get the number of rides where the user acted as passenger.
     * These are used to compute the user's passenger rating.
     *
     * @return number of rides as passenger
     */
    public int getNumberRidesPassager() {
        return stars.size();
    }



    /// Returns the average number of stars in given role
    /// @param role of user
    /// @return average number of stars for that role.
    public float getAverage(RideRole role) {
        int rating = 0;
        if(role == RideRole.DRIVER){
            for (int score: rides){
                rating += score;
            }

            return rides.isEmpty() ? 0 : (float)rating / rides.size();
        }else {
            //RideRole.PASSENGER
            for (int score: stars){
                rating += score;
            }

            return stars.isEmpty() ? 0 : (float)rating / stars.size();
        }
    }

    /// Current preference for sorting matches.
    /// Defaults to BETTER.
    /// @return preferred match by this user
    public PreferredMatch getPreferredMatch() {
        return (preferredMatch == null) ? PreferredMatch.BETTER : preferredMatch;
    }

    ///Change preference for sorting matches
    /// @param preferredMatch to set for this user
    public void setPreferredMatch(PreferredMatch preferredMatch) {
        this.preferredMatch = preferredMatch;
    }
}
