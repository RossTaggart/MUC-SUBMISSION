package com.taggart.ross.gcu_weather_app;

/**
 * Created by Ross on 29/11/2015.
 */
import java.io.Serializable;

public class MapData implements Serializable{

// *********************************************
// Declare variables etc.
// *********************************************

    private int entryID;
    private String Location;
    private float Latitude;
    private float Longitude;

    private static final long serialVersionUID = 0L;

// *********************************************
// Declare getters and setters etc.
// *********************************************


    public int getEntryID() {
        return entryID;
    }

    public void setEntryID(int entryID) {
        this.entryID = entryID;
    }

    public String getLocation()
    {
        return Location;
    }

    public void setLocation (String location)
    {
        this.Location = location;
    }

    public float getLatitude()
    {
        return Latitude;
    }

    public void setLatitude(float Lat)
    {
        this.Latitude = Lat;
    }

    public float getLongitude()
    {
        return Longitude;
    }

    public void setLongitude(float fLongitude)
    {
        this.Longitude = fLongitude;
    }

    @Override
    public String toString() {
        String mapData;
        mapData = "LocationWeatherInfo [entryID=" + entryID;
        mapData = ", Location=" + Location;
        mapData = ", Latitude=" + Latitude;
        mapData = ", Longitude=" + Longitude +"]";
        return mapData;
    }

}
