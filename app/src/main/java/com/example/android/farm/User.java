package com.example.android.farm;

/**
 * Created by Tasos on 19-Dec-17.
 */

public class User {

    public String name;
    public String surname;
    public String address;
    public String Location;
    public String date_of_birth;
    public String user_id;

    public User(String name, String surname, String address, String location, String date_of_birth, String user_id) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        Location = location;
        this.date_of_birth = date_of_birth;
        this.user_id = user_id;
    }

    public User(String name) {
        this.name = name;
    }

    public User() {

    }

    public User(String name, String surname, String address,String date_of_birth) {
        this.name=name;
        this.surname=surname;
        this.address=address;
        this.date_of_birth=date_of_birth;

    }

    public User(String name, String surname, String address, String date_of_birth, String user_id) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.date_of_birth = date_of_birth;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", Location='" + Location + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}