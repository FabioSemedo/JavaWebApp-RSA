package org.vaadin.example.rsa.user;

/**
 * Classification to a ride provided by the other user.
 * The implementations of {{@link #valueOf(String)} and {@link #values()}
 * are automatically generated.
 * @author José Paulo Leal {@code jpleal@fc.up.pt}
 * @author Maria Pereira
 * */
public enum UserStars {

    FIVE_STARS (5 ,"Great ride"),
    FOUR_STARS(4, "Good ride"),
    THREE_STARS(3, "Average ride"),
    TWO_STARS(2, "Bad ride"),
    ONE_STARS(1, "Lousy ride");

    private final int stars;
    private final String description;

    UserStars(int stars, String description) {
        this.stars = stars;
        this.description = description;
    }

    ///Returns number of stars as an integer
    public int getStars(){
        return stars;
    }

    ///Returns description of stars as a String
    public String getDescription() {
        return description;
    }
}
