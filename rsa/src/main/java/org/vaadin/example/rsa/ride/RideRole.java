package org.vaadin.example.rsa.ride;

public enum RideRole {

    DRIVER ("This user is driving the car"),
    PASSENGER ("This user is the passenger");

    final String name;

    RideRole(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public RideRole other(){
        if (this == DRIVER){
            return PASSENGER;
        }
        return DRIVER;
    }
}
