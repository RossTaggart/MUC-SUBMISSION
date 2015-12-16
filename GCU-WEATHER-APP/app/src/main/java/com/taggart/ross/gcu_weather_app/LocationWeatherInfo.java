package com.taggart.ross.gcu_weather_app;

/**
 * Created by Ross on 27/10/2015.
 */
public class LocationWeatherInfo {

    private String locationName;
    private double latitude;
    private double longitude;
    private String weatherType;
    private int temperatureCelsius;
    private int temperatureFahrenheit;
    private String windDirection;
    private int windSpeed;
    private int humidity;
    private int pressure;
    private String pressureDescription;
    private String visibility;

    public LocationWeatherInfo(String locationName, double Latitude, double Longitude, String WeatherType, int TempCels, int TempFahr, String WindDirection, int WindSpeed,
                               int Humidity, int Pressure, String PressureDesc, String Visibility)
    {
        this.locationName = locationName;
        latitude = Latitude;
        longitude = Longitude;
        weatherType = WeatherType;
        temperatureCelsius = TempCels;
        temperatureFahrenheit = TempFahr;
        windDirection = WindDirection;
        windSpeed = WindSpeed;
        humidity = Humidity;
        pressure = Pressure;
        pressureDescription = PressureDesc;
        visibility = Visibility;
    }

    public LocationWeatherInfo()
    {
        weatherType = "";
        temperatureCelsius = 0;
        temperatureFahrenheit = 0;
        windDirection = "";
        windSpeed = 0;
        humidity = 0;
        pressure = 0;
        pressureDescription = "";
        visibility = "";
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public int getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(int temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public int getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }

    public void setTemperatureFahrenheit(int temperatureFahrenheit) {
        this.temperatureFahrenheit = temperatureFahrenheit;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public String getPressureDescription() {
        return pressureDescription;
    }

    public void setPressureDescription(String pressureDescription) {
        this.pressureDescription = pressureDescription;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
