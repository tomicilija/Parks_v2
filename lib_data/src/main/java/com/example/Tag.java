package com.example;

/**
 * Created by crepinsek on 24/02/17.
 */

public class Tag {
    private String ime;

    public String idTag() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public Tag(String ime) {
        this.ime = ime;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "ime='" + ime + '\'' +
                '}';
    }
}
