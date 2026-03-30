package org.vaadin.example.rsa.match;

public enum PreferredMatch {

    BETTER("Prefer to ride with better users (higher average stars; this is the default)."),
    CHEAPER("Prefer cheaper rides (if you are a passenger)"),
    CLOSER("Prefer to ride with nearby users");

    private final String name;
    private PreferredMatch(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
