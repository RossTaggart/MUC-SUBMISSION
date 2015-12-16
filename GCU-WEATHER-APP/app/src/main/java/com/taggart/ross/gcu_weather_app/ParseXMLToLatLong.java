package com.taggart.ross.gcu_weather_app;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Ross on 01/11/2015.
 */
public class ParseXMLToLatLong {

    double locationLatitude = 0.0;
    double locationLongitude = 0.0;

    double arrayLat[] = new double[5];
    double arrayLong[] = new double[5];

    int index = 0;

    ParseXMLToLatLong(String dataToParse)
    {
        parseData(dataToParse);
    }

    //Method that handles the parsing of the XML data.
    //Takes in the string of data, pulls out the latitude and longitude,
    //and saves data appropriately
    private void parseData(String dataToParse)
    {
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {

                // Found a start tag
                if(eventType == XmlPullParser.START_TAG)
                {
                    //Check for which tag we have
                    if (xpp.getName().equalsIgnoreCase("geometry"))
                    {
                        //focuses parser on the <geometry> tags
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("lat"))
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.e("MyTag","temp latitude is " + temp);
                            //locationLatitude = Double.parseDouble(temp);
                            arrayLat[index] = Double.parseDouble(temp);
                        }
                        else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("lng"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "temp longitude is " + temp);
                                //locationLongitude = Double.parseDouble(temp);
                                arrayLong[index] = Double.parseDouble(temp);
                            }

                }
                else if (eventType == XmlPullParser.END_TAG)
                {
                    if(xpp.getName().equalsIgnoreCase("location")) {
                        Log.e("Parsing", "parse finished");//finished

                        index++;

                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");

        locationLatitude = arrayLat[0];
        locationLongitude = arrayLong[0];

        Log.e("Lat", "lat is " + locationLatitude);
        Log.e("Long", "long is " + locationLongitude);

    }
}
