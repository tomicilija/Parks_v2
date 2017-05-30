package com.example;

import java.util.UUID;

public class Lokacija {
    String id;
    String name;
    private double mLat, mLong; //GPS
    String idUser; //idUser
    String fileName;
    long date;
    public static final String NODATA="_NA";

    public Lokacija(String name, double mLat, double mLong, String idUser, String fileName, long date) {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.name = name;
        this.mLat = mLat;
        this.mLong = mLong;
        this.idUser = idUser;
        this.fileName = fileName;
        this.date = date;
    }


    @Override
    public String toString() {
        return "Lokacija{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", x=" + mLat +
                ", mLong=" + mLong +
                ", vzdevek='" + idUser + '\'' +
                ", fileName='" + fileName + '\'' +
                ", date=" + date +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return mLat;
    }

    public void setLatitude(double mLat) {
        this.mLat = mLat;
    }

    public double getLongitude() {
        return mLong;
    }

    public void setLongitude(double mLong) {
        this.mLong = mLong;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean hasImage() {
        if (fileName==null) return false;
        else if (fileName.equals(NODATA)) return false;
        return true;
    }
}
