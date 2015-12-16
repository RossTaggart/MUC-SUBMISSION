package com.taggart.ross.gcu_weather_app;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ross on 26/10/2015.
 */
public class SearchLocation extends Activity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    LocationDBManager locationDBManager;
    EditText searchBarText;
    Button searchButton;
    Button loadButton;
    Button saveButton;
    PopupMenu locationsPopUpMenu;
    FragmentManager fmAboutDialogue;
    boolean willLocationBeSaved = false;

    /**
     * onCreate method for Activity. Launches when the Activity begins.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_location);

        //finds widgets on-screen to allow us to handle/manipulate them
        searchBarText = (EditText)findViewById(R.id.searchBarText);
        searchButton = (Button)findViewById(R.id.searchButton);
        loadButton = (Button)findViewById(R.id.loadButton);
        saveButton = (Button)findViewById(R.id.saveButton);

        //creates pop up menu based on the context of the application and where it will open
        locationsPopUpMenu = new PopupMenu(this, findViewById(R.id.loadButton));

        //sets listeners for click events
        locationsPopUpMenu.setOnMenuItemClickListener(this);
        searchButton.setOnClickListener(this);
        loadButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        //loads database manager for locations
        locationDBManager = new LocationDBManager(this, "SavedLocations.s3db", null, 1);

        try {
            //creates database if it doesn't exist
            locationDBManager.dbCreate();

            //creates list of strings (locations)
            ArrayList<String> locations = new ArrayList<>(locationDBManager.findSavedLocations());
            //adds each location in the arraylist to the menu
            for (int i=0; i < locations.size(); i++)
            {
                locationsPopUpMenu.getMenu().add(locations.get(i));
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //gets fragment - used for about dialogue
        fmAboutDialogue = this.getFragmentManager();

    }

    /**
     * Toggles if the location will be saved in the next intent (displayWeather)
     */
    private void saveLocation()
    {
        willLocationBeSaved = !willLocationBeSaved;
    }

    /**
     * Loads the locations for the menu
     */
    private void loadLocations()
    {
        locationsPopUpMenu.getMenu().clear();

        ArrayList<String> locations = new ArrayList<>(locationDBManager.findSavedLocations());
        for (int i=0; i < locations.size(); i++)
        {
            locationsPopUpMenu.getMenu().add(locations.get(i));
        }
    }

    /**
     * Creates option menu and shows it.
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * OnClick method to handle button presses. Based on buttons pressed, either the displayWeather
     * Intent is loaded with information about the location passed into it, a pop up menu with a
     * list of locations to load will be shown OR the saving of the location that is about to be
     * searched will be toggled (true/false dependant on current value).
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.searchButton:
                if (searchBarText.getText().toString() != "") {
                    Intent displayWeather = new Intent(this, DisplayWeather.class);
                    displayWeather.putExtra("location", searchBarText.getText().toString());
                    displayWeather.putExtra("willLocationBeSaved", willLocationBeSaved);
                    this.startActivity(displayWeather);
                }
                else
                {
                    Toast.makeText(this, "You haven't typed a location!", Toast.LENGTH_LONG);
                }
            case R.id.loadButton:
                loadLocations();
                locationsPopUpMenu.show();
            case R.id.saveButton:
                saveLocation();
            default:
                //code here

        }
    }

    /**
     * Deals with selecting items in the Main Menu.
     * Depending on the item selected, the application either
     * quits, shows an about dialogue, the map or the drawing to
     * canvas intent.
     * @param item
     * @return true
     */
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

    /**
     * Deals with the item that's clicked in the pop up menu.
     * Starts the displayWeather Intent and passes in the
     * name of the item in the pop up menu selected so that
     * the displayWeather Intent loads the corrects weather
     * information.
     * @param item
     * @return false
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        Intent displayWeather = new Intent(this, DisplayWeather.class);
        displayWeather.putExtra("location", item.toString());
        this.startActivity(displayWeather);
        return false;
    }


}
