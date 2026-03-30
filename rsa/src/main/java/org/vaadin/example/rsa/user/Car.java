package org.vaadin.example.rsa.user;



import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * A car used in rides, with a license plate (that can be used as key) a make, model and color.
 *  @author José Paulo Leal {@code jpleal@fc.up.pt}
 *  @author Maria Pereira
 */

public class Car implements Serializable {

    private String plate;
    private String make;
    private String model;
    private String color;


    /**
     * Create a car with given features
     *
     * @param plate - license of car
     * @param make - of car
     * @param model - of car
     * @param color - of car
     */
    public Car(String plate, String make, String model, String color) {
        this.plate = plate;
        this.make = make;
        this.model = model;
        this.color = color;
    }


    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlate() {
        return plate;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }


}
