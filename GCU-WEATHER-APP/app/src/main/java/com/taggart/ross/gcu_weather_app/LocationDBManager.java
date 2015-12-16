package com.taggart.ross.gcu_weather_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by rla and extended by Ross Taggart
 */
public class LocationDBManager extends SQLiteOpenHelper {

    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/com.taggart.ross.gcu_weather_app/databases/";
    private static final String DB_NAME = "SavedLocations.s3db";
    private static final String TBL_SAVEDLOCATIONS = "SavedLocations";

    public static final String COL_LOCATION = "LocationName";
    public static final String COL_LATITUDE = "Latitude";
    public static final String COL_LONGITUDE = "Longitude";

    private final Context appContext;

    /**
     * Constructor for the LocationDBManager class.
     * @param context Current App Context
     * @param name Name of database
     * @param factory SQL Cursor Factory
     * @param version Version of database
     */
    public LocationDBManager(Context context, String name,
                             SQLiteDatabase.CursorFactory factory, int version)
    {
        super (context, name, factory, version);
        this.appContext = context;
    }

    /**
     * onCreate for class - called after constructed.
     * @param db SQLiteDatabase being created.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_SAVEDLOCATIONS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TBL_SAVEDLOCATIONS + "("
                + COL_LOCATION + " TEXT UNIQUE," + COL_LATITUDE + " DOUBLE," + COL_LONGITUDE + " DOUBLE" +
                ")";
        db.execSQL(CREATE_SAVEDLOCATIONS_TABLE);

    }

    /**
     * onUpgrade method used to upgrade database if needed.
     * @param db Database in use
     * @param oldVersion Version number of old database
     * @param newVersion Version number of new database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_SAVEDLOCATIONS);
            onCreate(db);
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * @throws IOException
     */
    public void dbCreate() throws IOException {

        boolean dbExist = dbCheck();

        if(!dbExist){
            //By calling this method an empty database will be created into the default system path
            //of your application so we can overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDBFromAssets();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if exists, false if it doesn't
     */
    private boolean dbCheck(){

        SQLiteDatabase db = null;

        try{
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);

        }catch(SQLiteException e){

            Log.e("SQLHelper", "Database not Found!");

        }

        if(db != null){

            db.close();

        }

        return db != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * @throws IOException
     */
    private void copyDBFromAssets() throws IOException{

        InputStream dbInput = null;
        OutputStream dbOutput = null;
        String dbFileName = DB_PATH + DB_NAME;

        try {

            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
            //transfer bytes from the dbInput to the dbOutput
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer)) > 0) {
                dbOutput.write(buffer, 0, length);
            }

            //Close the streams
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        } catch (IOException e)
        {
            throw new Error("Problems copying DB!");
        }
    }

    /**
     * Adds a location the user wishes to save to the database.
     * @param location Location the user wishes to save.
     * @param locationLat Latitude of location.
     * @param locationLong Longitude of location.
     */
    public void addSavedLocation(String location, double locationLat, double locationLong) {


        ContentValues values = new ContentValues();
        values.put(COL_LOCATION, location);
        values.put(COL_LATITUDE, locationLat);
        values.put(COL_LONGITUDE, locationLong);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TBL_SAVEDLOCATIONS, null, values);
        db.close();
    }

    /**
     * Finds all of the locations saved in the database, grabs the names of said locations and adds
     * them to an ArrayList.
     * @return ArrayList of all location names in the database.
     */
    public ArrayList<String> findSavedLocations()
    {
        String query = "Select " + COL_LOCATION + " FROM " + TBL_SAVEDLOCATIONS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        int index = 0;
        ArrayList<String> locations = new ArrayList<>();

        if (cursor.moveToFirst())
        {
            while (cursor.isAfterLast() == false)
            {
                String location = cursor.getString(cursor.getColumnIndex(COL_LOCATION));
                locations.add(index, location);
                index++;
                cursor.moveToNext();
            }
        }

        return locations;
    }

    /**
     * Removes a location from the database.
     * Used more for development purposes.
     * @param location Location to be removed
     * @return If the removal was successful, true if yes, false if not.
     */
    public boolean removeSavedLocation(String location) {

        boolean result = false;

        String query = "Select * FROM " + TBL_SAVEDLOCATIONS + " WHERE " + COL_LOCATION + " =  \"" + location + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String tempLocation = location;

        if (cursor.moveToFirst()) {
            db.delete(TBL_SAVEDLOCATIONS, COL_LOCATION + " = ?",
                    new String[]{location});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    /**
     * Pulls ALL data from the database, storing each unique location in an object of type MapData.
     * Adds each MapData object to a List.
     * @return List<MapData> of all records in the database.
     */
    public List<MapData> allMapData()
    {
        String query = "Select * FROM " + TBL_SAVEDLOCATIONS;
        List<MapData> MapDataList = new ArrayList<MapData>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast()==false) {
                MapData MapDataEntry = new MapData();
                MapDataEntry.setLocation(cursor.getString(0));
                MapDataEntry.setLatitude(Float.parseFloat(cursor.getString(1)));
                MapDataEntry.setLongitude(Float.parseFloat(cursor.getString(2)));
                MapDataList.add(MapDataEntry);
                cursor.moveToNext();
            }
        } else {
            MapDataList.add(null);
        }
        db.close();
        return MapDataList;
    }
}
