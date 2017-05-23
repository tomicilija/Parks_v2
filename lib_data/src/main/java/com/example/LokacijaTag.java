package com.example;

import java.util.UUID;

/**
 * Created by crepinsek on 24/02/17.
 */

public class LokacijaTag {
    String idLokacija;
    String tagID;
    long datum;
    String idUser;
    String id;

    public LokacijaTag(String idLokacija, String tagID, long datum, String idUser) {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.idLokacija = idLokacija;
        this.tagID = tagID;
        this.datum = datum;
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "LokacijaTag{" +
                "idLokacija='" + idLokacija + '\'' +
                ", tagID='" + tagID + '\'' +
                ", datum=" + datum +
                ", idUser='" + idUser + '\'' +
                '}';
    }
}
