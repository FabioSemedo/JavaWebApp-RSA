package org.vaadin.example.session;

import org.vaadin.example.rsa.Manager;
import org.vaadin.example.rsa.RideSharingAppException;
import org.vaadin.example.rsa.match.Location;
import org.vaadin.example.rsa.ride.Ride;
import org.vaadin.example.rsa.user.Car;
import org.vaadin.example.rsa.user.User;
import org.vaadin.example.rsa.user.Users;

public class UserMotorista {

    private final Users users;
    private final Manager manager;
    public UserMotorista() throws RideSharingAppException {
        this.users = Users.getInstance();
        this.manager = Manager.getInstance();
        User motorista = users.register("FkALburque", "Mateus");
        Car car = new Car("00-OO-00", "Fiat", "Uno", "Prata");
        motorista.addCar(car);
        Car car2 = new Car("OO-00-OO", "Puma", "Uno", "Azul");
        motorista.addCar(car2);
        Ride ride = new Ride(
                motorista,
                new Location(0, 0),
                new Location(800, 200),
                car2.getPlate(),
                3);
        manager.addRide(
                motorista.getNick(),
                motorista.getKey(),
                ride.getFrom(),
                ride.getTo(),
                car2.getPlate(),
                ride.getCost());

    }
}
