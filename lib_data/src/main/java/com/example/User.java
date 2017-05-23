package com.example;

/**
 * Created by crepinsek on 24/02/17.
 */

public class User {
    private String idUser;
    private String vzdevek;

    public User(String idUser, String vzdevek) {
        this.idUser = idUser;
        this.vzdevek = vzdevek;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getVzdevek() {
        return vzdevek;
    }

    public void setVzdevek(String vzdevek) {
        this.vzdevek = vzdevek;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser='" + idUser + '\'' +
                ", vzdevek='" + vzdevek + '\'' +
                '}';
    }
}
