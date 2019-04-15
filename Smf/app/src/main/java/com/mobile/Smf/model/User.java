package com.mobile.Smf.model;

public class User {

    private int id;
    private String userName;
    private String password;
    private String email;
    private String country;
    private int birthYear;

    public User(int id, String userName, String password, String email, String country, int birthYear){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.country = country;
        this.birthYear = birthYear;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
