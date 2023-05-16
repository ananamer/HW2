package com.example.hw2;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Comparable<Player> {

    private Float locationLatitude;
    private Float locationLongitude;
    private String locationName;
    private int coins;
    private int distance;

    public Player() {
        this.coins = 0;
        this.distance = 0;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
    public int getCoins() {
        return coins;
    }

    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Float getLocationLatitude() {
        return locationLatitude;
    }
    public void setLocationLatitude(Float locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Float getLocationLongitude() {
        return locationLongitude;
    }
    public void setLocationLongitude(Float locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationName() {
        return locationName;
    }
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public int compareTo(Player comparePlayer)
    {
        int compareDistance = ((Player)comparePlayer).getDistance();
        return compareDistance - this.distance;
    }
}
