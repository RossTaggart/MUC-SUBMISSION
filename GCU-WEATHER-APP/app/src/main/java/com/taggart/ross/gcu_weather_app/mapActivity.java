package com.taggart.ross.gcu_weather_app;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ross on 29/11/2015.
 */
public class mapActivity extends FragmentActivity
{
    List<MapData> mapDataList;
    private Marker[] mapDataMarkerList = new Marker[30];
    private GoogleMap mapLocations;
    private float markerColour = 120.0f;

    private LatLng latLngAyr = new LatLng(55.864237, -4.251806);

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.map_view);

        mapDataList = new ArrayList<MapData>();
        LocationDBManager locationDBManager = new LocationDBManager(this, "SavedLocations.s3db", null, 1);

        try
        {
            locationDBManager.dbCreate();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        mapDataList = locationDBManager.allMapData();
        SetUpMap();
        AddMarkers();
    }

    private void SetUpMap()
    {
        mapLocations = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mapLocations != null)
        {
            mapLocations.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngAyr, 8));
            mapLocations.setMyLocationEnabled(true);
            mapLocations.getUiSettings().setCompassEnabled(true);
            mapLocations.getUiSettings().setMyLocationButtonEnabled(false);
            mapLocations.getUiSettings().setRotateGesturesEnabled(true);
        }
    }

    private void AddMarkers()
    {
        MarkerOptions marker;
        MapData mapData;
        String mrkTitle;
        String mrkText;

        /* for all of the marker options in the list */
        for (int i=0; i < mapDataList.size(); i++)
        {
            mapData = mapDataList.get(i);
            mrkTitle = mapData.getLocation();
            mrkText = "Marker for " + mapData.getLocation();
            marker = SetMarker(mrkTitle, mrkText, new LatLng(mapData.getLatitude(), mapData.getLongitude()), markerColour, true);
            mapDataMarkerList[i] = mapLocations.addMarker(marker);
        }
    }

    private MarkerOptions SetMarker(String title, String snippet, LatLng position, float markerColour, boolean centreAnchor)
    {
        float anchorX;
        float anchorY;

        /* on condition anchor is to be centered */
        if (centreAnchor)
        {
            anchorX = 0.5f;
            anchorY = 0.5f;
        }
        else
        {
            anchorX = 0.5f;
            anchorY = 1.0f;
        }

        /* create marker from input variables */
        MarkerOptions marker = new MarkerOptions().title(title).snippet(snippet).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_blue)).anchor(anchorX, anchorY).position(position);

        return marker;
    }
}
