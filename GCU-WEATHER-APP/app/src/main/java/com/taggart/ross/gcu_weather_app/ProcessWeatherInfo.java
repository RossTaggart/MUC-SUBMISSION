package com.taggart.ross.gcu_weather_app;

import android.util.Log;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by Ross on 06/11/2015.
 */
public class ProcessWeatherInfo {

    private String weatherType;
    private int temperatureCelsius;
    private int temperatureFahrenheit;
    private String windDirection;
    private int windSpeed;
    private int humidity;
    private int pressure;
    private String pressureDescription;
    private String visibility;

    public String getWeatherType()
    {
        return weatherType;
    }

    public int getTemperatureCelsius()
    {
        return temperatureCelsius;
    }

    public int getTemperatureFahrenheit()
    {
        return temperatureFahrenheit;
    }

    public String getWindDirection()
    {
        return windDirection;
    }

    public int getWindSpeed()
    {
        return windSpeed;
    }

    public int getHumidity()
    {
        return humidity;
    }

    public int getPressure()
    {
        return pressure;
    }

    public String getPressureDescription()
    {
        return pressureDescription;
    }

    public String getVisibility()
    {
        return visibility;
    }

    ProcessWeatherInfo(String title, String description)
    {
        try {

            processWeatherType(title);
            processTempCelsius(title);
            processTempFahrenheit(title);
            processWindDirection(description);
            processWindSpeed(description);
            processHumidity(description);
            processPressureAndPressureDescription(description);
            processVisibility(description);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void processWeatherType(String title)
    {
        Pattern pattern = Pattern.compile("(GMT:) (.+) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(title);

        String match = "";

        while (matcher.find())
        {
            Log.e("matcher ex", "found: " + matcher.group(1) + " " + matcher.group(2));
            match = matcher.group(1) + matcher.group(2);
            Log.e("matcher ex", "test if write to string, should make sense " + match);
        }

        String tempWethType = match.replace("GMT:", "");
        int indexOfComma = tempWethType.indexOf(",");
        weatherType = tempWethType.substring(0, indexOfComma);
        Log.e("process weather type", "Weather Type is " + weatherType);
    }

    private void processTempCelsius(String title)
    {
        int charIndex = title.indexOf("°");
        String tempTempCels = title.substring(charIndex - 2, charIndex);
        if (tempTempCels.contains(" "))
        {
            String tempTempCels2 = tempTempCels.replace(" ", "");
            tempTempCels =tempTempCels2;
        }
        temperatureCelsius = Integer.parseInt(tempTempCels);
        Log.e("process temp celsius", "Temp Celsius is " + temperatureCelsius);
    }

    private void processTempFahrenheit(String title)
    {
        int firstCharIndex = title.indexOf("°");
        int secondCharIndex = title.indexOf("°", firstCharIndex+1);
        String tempFahr = title.substring(secondCharIndex-2, secondCharIndex);
        if (tempFahr.contains(" "))
        {
            tempFahr.replace(" ", "");
        }
        temperatureFahrenheit = Integer.parseInt(tempFahr);
        Log.e("process temp fahrenheit", "Temp Fahrenheit is " + temperatureFahrenheit);
    }

    private void processWindDirection(String description)
    {
        Pattern pattern = Pattern.compile("(Wind Direction: *) (.+) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(description);

        String match = "";

        while (matcher.find())
        {
            Log.e("matcher ex", "found: " + matcher.group(1) + " " + matcher.group(2));
            match = matcher.group(1) + matcher.group(2);
            Log.e("matcher ex", "test if write to string, should make sense " + match);
        }

        int indexOfComma = match.indexOf(",");
        int indexOfColon = match.indexOf(":");
        String tempWindDirec = match.substring(indexOfColon+1, indexOfComma);

        windDirection = tempWindDirec;

        Log.e("process wind direction", "Wind Direction is " + windDirection);
    }

    private void processWindSpeed(String description)
    {
        Pattern pattern = Pattern.compile("(Wind Speed: *) (.+) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(description);

        String match = "";

        while (matcher.find())
        {
            Log.e("matcher ex", "found: " + matcher.group(1) + " " + matcher.group(2));
            match = matcher.group(1) + matcher.group(2);
            Log.e("matcher ex", "test if write to string, should make sense " + match);
        }

        int indexOfComma = match.indexOf(",");
        int indexOfColon = match.indexOf(":");

        String tempWindSpeed = match.substring(indexOfColon + 1, indexOfComma);

        String processedWindSpeedString = tempWindSpeed.replace("mph", "");
        windSpeed = Integer.parseInt(processedWindSpeedString);

        Log.e("process wind speed", "wind speed is " + windSpeed);
    }

    private void processHumidity(String description)
    {
        Pattern pattern = Pattern.compile("(Humidity: *) (.+) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(description);

        String match = "";

        while (matcher.find())
        {
            Log.e("matcher ex", "found: " + matcher.group(1) + " " + matcher.group(2));
            match = matcher.group(1) + matcher.group(2);
            Log.e("matcher ex", "test if write to string, should make sense " + match);
        }

        int indexOfComma = match.indexOf(",");
        int indexOfColon = match.indexOf(":");

        String tempHumidity = match.substring(indexOfColon + 1, indexOfComma);
        String processedHumidityString = tempHumidity.replace("%", "");

        humidity = Integer.parseInt(processedHumidityString);
        Log.e("Process Humidity", "Humidity is " + humidity);
    }

    private void processPressureAndPressureDescription(String description)
    {
        Pattern pattern = Pattern.compile("(Pressure: *) (.+) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(description);

        String match = "";

        while (matcher.find())
        {
            Log.e("matcher ex", "found: " + matcher.group(1) + " " + matcher.group(2));
            match = matcher.group(1) + matcher.group(2);
            Log.e("matcher ex", "test if write to string, should make sense " + match);
        }

        int indexOfComma = match.indexOf(",");
        int indexOfColon = match.indexOf(":");

        String tempPressure = match.substring(indexOfColon + 1, indexOfComma);
        String processedPressureString = tempPressure.replace("mb", "");

        String tempPressureDescription = match.substring(indexOfComma+2);

        indexOfComma = tempPressureDescription.indexOf(",");

        pressureDescription = tempPressureDescription.substring(0, indexOfComma);
        pressure = Integer.parseInt(processedPressureString);
        Log.e("Process Pressure", "Pressure is " + pressure);
        Log.e("Process Pressure Desc", "Pressure Description is " + pressureDescription);
    }

    private void processVisibility(String description)
    {
        int indexOf = description.indexOf("Visibility");
        String tempVisibility = description.substring(indexOf);
        visibility = tempVisibility.replace("Visibility: ", "");

        Log.e("Process Visibility", "Visibility is " + visibility);
    }
}
