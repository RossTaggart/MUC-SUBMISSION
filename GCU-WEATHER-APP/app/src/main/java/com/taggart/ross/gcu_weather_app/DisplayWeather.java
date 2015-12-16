package com.taggart.ross.gcu_weather_app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Ross on 26/10/2015.
 */
public class DisplayWeather extends Activity implements View.OnClickListener {

    SharedPreferences sharedPrefs;
    TextView locationText;
    TextView weatherInfoText;
    Button backButton;
    String location = "";
    FragmentManager fmAboutDialogue;
    SoundPool soundPool;
    LocationWeatherInfo locationWeatherInfo;
    int soundID;
    boolean willLocationBeSaved;
    boolean isSoundMuted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_weather_output);

        //finds widgets in Activity
        locationText = (TextView)findViewById(R.id.locationTextView);
        weatherInfoText = (TextView)findViewById(R.id.weatherInfoTextView);
        backButton = (Button)findViewById(R.id.backButton);

        //gives button OnClickListener
        backButton.setOnClickListener(this);

        //gets the shared preferences and loads our preferences
        sharedPrefs = getSharedPreferences("MY_SHARED_PREFS", MODE_PRIVATE);
        loadSavedPreferences();

        //gets the weather of our specified location
        getLocationWeather();

        //gets fragment for our menu
        fmAboutDialogue = this.getFragmentManager();

        //sets hardware buttons to control volume of sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //creates sound pool based on API version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }

        //sets OnLoadCompleteListener for soundpool
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
            {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    //plays sound if the sound isn't muted
                    if (!isSoundMuted) {
                        playSounds();
                    }

                }
        });
        //loads in sounds
        loadSounds();


    }

    //plays the sound relating to the weather of the searched location
    private void playSounds() {

        soundPool.play(soundID, 1.0f, 1.0f, 1, 1, 0);

    }

    //creates sound pool for newer API's
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool()
    {
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
    }

    //creates sound pool for older API's
    @SuppressWarnings("deprecation")
    private void createOldSoundPool()
    {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    }

    //loads sounds specific to the weather types
    private void loadSounds()
    {
        if (locationWeatherInfo.getWeatherType().contains("Rain") || locationWeatherInfo.getWeatherType().contains("Showers"))
        {
            Log.e("loadSounds()", "Rain/Showers");
            soundID = soundPool.load(this, R.raw.rain2, 1);

        }
        else if (locationWeatherInfo.getWeatherType().contains("Cloud") || locationWeatherInfo.getWeatherType().contains("Sunny") ||
                locationWeatherInfo.getWeatherType().contains(("Clear")))
        {
            Log.e("loadSounds()", "Cloud/Sunny/Clear");
            soundID = soundPool.load(this, R.raw.birds, 1);

        }
        else if (locationWeatherInfo.getWeatherType().contains("Thunder") || locationWeatherInfo.getWeatherType().contains("Lightning") ||
                locationWeatherInfo.getWeatherType().contains("Storm"))
        {
            Log.e("loadSounds()", "Thunder/Lightning/Storm");
            soundID = soundPool.load(this, R.raw.thunder, 1);

        }
    }

    //loads our saved preferences
    private void loadSavedPreferences()
    {
        isSoundMuted = sharedPrefs.getBoolean("mute", false);
    }

    //gets our weather location
    private void getLocationWeather()
    {
        //fetches the data added to the intent
        Bundle data = getIntent().getExtras();
        if (data != null)
        {
            location = data.getString("location");
            willLocationBeSaved = data.getBoolean("willLocationBeSaved", false);
        }

        //defines the URL we will use to grab our latitude and longitude
        String urlToRetrieve = "https://maps.googleapis.com/maps/api/geocode/xml?address=" + location + "&components=country:UK&region=uk&key=AIzaSyANJvD02eYXK6l4bAJRCnZvKs6YQX5baxk";

        //creates an XMLRetriever object
        aSyncXMLRetriever aSyncXMLRetrieverGoogle = new aSyncXMLRetriever(urlToRetrieve);
        String xmlFromUrl = "";

        //tries to execute the ASyncTask and grabs the output from the task
        //to save into the xmlFromUrl String
        try {
            xmlFromUrl = aSyncXMLRetrieverGoogle.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //parses our XML data
        ParseXMLToLatLong parseXMLToLatLong = new ParseXMLToLatLong(xmlFromUrl);

        //grabs the latitude and longitude from the ParseXMLToLatLong object
        double locationLat = parseXMLToLatLong.locationLatitude;
        double locationLong = parseXMLToLatLong.locationLongitude;

        //checks to see if the location is going to be saved and, if so, calls the
        //saveLocation() method.
        if (willLocationBeSaved)
        {
            saveLocation(location, locationLat, locationLong);
        }

        //creates our URL that we use to query the Geonames website based on our latitude and longitude
        String urlGeonames = "http://api.geonames.org/findNearbyPlaceName?lat=" + locationLat + "&lng=" + locationLong + "&username=rosstaggart&style=full";

        //creates another aSyncXMLRetriever to grab the raw XML data from the Geonames site
        aSyncXMLRetriever aSyncXMLRetrieverGeonames = new aSyncXMLRetriever(urlGeonames);
        String xmlFromUrlGeonames = "";

        //tries to execute the ASyncTask and save the XML data in the xmlFromUrlGeonames String
        try {
            xmlFromUrlGeonames = aSyncXMLRetrieverGeonames.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Creates a ParseXMLToGeoname object to grab our geoname
        ParseXMLToGeoname parseXMLToGeoname = new ParseXMLToGeoname(xmlFromUrlGeonames);
        //grabs geoname
        int locationGeoname = parseXMLToGeoname.geoname;

        //Defines our URL to grab the BBC Weather RSS feed for our location
        String urlBBCRSSFeed = "http://open.live.bbc.co.uk/weather/feeds/en/" + locationGeoname + "/observations.rss";
        //Creates an ASyncXMLRetriever object to grab our RSS feed
        aSyncXMLRetriever aSyncXMLRetrieverRSSFeed = new aSyncXMLRetriever(urlBBCRSSFeed);
        String xmlFromUrlRSSFeed = "";

        //tries to execute the ASyncTask and save the data in the xmlFromUrlRSSFeed String
        try {
            xmlFromUrlRSSFeed = aSyncXMLRetrieverRSSFeed.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Creates a ParseXMLToLocationWeatherInfo object to parse the data from the RSS Feed
        ParseXMLToLocationWeatherInfo parseXMLToLocationWeatherInfo = new ParseXMLToLocationWeatherInfo(xmlFromUrlRSSFeed);
        //Creates a ProcessWeatherInfo object to take the data from the RSS feed and process it further
        ProcessWeatherInfo processWeatherInfo = new ProcessWeatherInfo(parseXMLToLocationWeatherInfo.getTitle(), parseXMLToLocationWeatherInfo.getDescription());

        //Creates our locationWeatherInfo object so we can store the weather information about the
        //location neatly
        locationWeatherInfo = new LocationWeatherInfo(location, locationLat, locationLong, processWeatherInfo.getWeatherType(),
                processWeatherInfo.getTemperatureCelsius(), processWeatherInfo.getTemperatureFahrenheit(), processWeatherInfo.getWindDirection(),
                processWeatherInfo.getWindSpeed(), processWeatherInfo.getHumidity(), processWeatherInfo.getPressure(),
                processWeatherInfo.getPressureDescription(), processWeatherInfo.getVisibility());

        //calls the displayWeather() method to show our weather information on screen
        displayWeather(locationWeatherInfo);

        //log statements for Debug
        Log.e("OnCreate", "xmlfromUrl is " + xmlFromUrl);
        Log.e("Checking quit of aSync", "status is" + aSyncXMLRetrieverGoogle.isCancelled());
        Log.e("Checking XML from geo", "xmlFromUrlGeonames is " + xmlFromUrlGeonames);
        Log.e("checking geoname", "locationGeoname is " + locationGeoname);
        Log.e("checking rss xml", "xmlFromUrlRSSFeed is " + xmlFromUrlRSSFeed);
    }

    //saves our location in the location database
    private void saveLocation(String location, double locationLat, double locationLong)
    {
        LocationDBManager locationDBManager = new LocationDBManager(this, "SavedLocations.s3db", null, 1);

        try {
            locationDBManager.dbCreate();

            locationDBManager.addSavedLocation(location, locationLat, locationLong);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //displays the weather information about our location
    private void displayWeather(LocationWeatherInfo locationWeatherInfo)
    {
        locationText.setText(locationWeatherInfo.getLocationName());

        weatherInfoText.setText("Latitude: " + locationWeatherInfo.getLatitude() +
                "\nLongitude: " + locationWeatherInfo.getLongitude() +
                "\n\nThe Weather Description is: " + locationWeatherInfo.getWeatherType() +
                "\nThe Temperature is: " + locationWeatherInfo.getTemperatureCelsius() + "°C ("
                + locationWeatherInfo.getTemperatureFahrenheit() + "°F)" +
                "\nThe Wind Direction is: " + locationWeatherInfo.getWindDirection() +
                "\nThe Wind Speed is: " + locationWeatherInfo.getWindSpeed() + "mph." + "" +
                "\nThe humidity is: " + locationWeatherInfo.getHumidity() + "%" + "" +
                "\nThe Pressure is: " + locationWeatherInfo.getPressure() + "mb" + "" +
                "\nThe Pressure is described as: " + locationWeatherInfo.getPressureDescription() +
                "\nThe Visibility is: " + locationWeatherInfo.getVisibility());
    }

    //onclick method to handle button interaction
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.backButton)
        {
            Intent searchLocation = new Intent(this, SearchLocation.class);
            this.startActivity(searchLocation);
        }
    }

    //creates our options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //handles interaction with our menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handles item selection
        switch (item.getItemId()){
            case R.id.Quit:
                finish();
                return true;
            case R.id.About:
                DialogFragment aboutDlg = new aboutDialogue();
                aboutDlg.show(fmAboutDialogue, "about_Dlg");
                return true;
            case R.id.Map:
                Intent Map = new Intent(this, mapActivity.class);
                this.startActivity(Map);
                return true;
            case R.id.canvasDraw:
                Intent drawToCanvas = new Intent(this, drawToCanvas.class);
                this.startActivity(drawToCanvas);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
