package org.vaadin.example.session;

import org.vaadin.example.rsa.Manager;
import org.vaadin.example.rsa.RideSharingAppException;
import org.vaadin.example.rsa.match.Location;
import org.vaadin.example.rsa.ride.Ride;
import org.vaadin.example.rsa.user.Car;
import org.vaadin.example.rsa.user.User;
import org.vaadin.example.rsa.user.Users;

public class UserPassageiro {


        private final Users users;
        private final Manager manager;
        public UserPassageiro() throws RideSharingAppException {
            this.users = Users.getInstance();
            this.manager = Manager.getInstance();
            User passageiro = users.register("CarmillaVampira", "Vanessa");
            Car car = new Car("00-AA-00", "Ford", "Focus", "Preto");
            passageiro.addCar(car);
            Car car2 = new Car("OO-22-OO", "Toyota", "Corolla", "Prata");
            passageiro.addCar(car2);
            Car car3 = new Car("11-XX-60", "Chevrolet", "Onix", "Vermelho");
            passageiro.addCar(car3);
            Ride ride = new Ride(
                    passageiro,
                    new Location(0, 0),
                    new Location(800, 200),
                    "",
                    0);
            manager.addRide(
                    passageiro.getNick(),
                    passageiro.getKey(),
                    ride.getFrom(),
                    ride.getTo(),
                    car2.getPlate(),
                    ride.getCost());

        }
    }
