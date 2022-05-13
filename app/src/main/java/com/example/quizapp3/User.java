package com.example.quizapp3;

public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String city;
    public String country;
    public String history;
    public String language;
    public String literature;
    public String geography;
    public Integer totalPoints;

    public User() {
    }

    public User (String firstName, String lastName, String email,String city, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.country = country;
        this.history = "1";
        this.language = "1";
        this.literature = "1";
        this.geography = "1";
        this.totalPoints = 0;
    }
}
