package com.taggart.ross.gcu_weather_app;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Ross on 01/11/2015.
 */
public class ParseXMLToGeoname
{
    int geoname = 0000000;

    ParseXMLToGeoname(String dataToParse)
    {
        parseData(dataToParse);
    }

    //Method that handles the parsing of the XML data.
    //Takes in the string of data, pulls out the geoname,
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
                    if (xpp.getName().equalsIgnoreCase("geonameId"))
                    {
                        //focuses parser on the <geometry> tags
                        String temp = xpp.nextText();
                        Log.e("Geocode", "geocode is " + temp);
                        geoname = Integer.parseInt(temp);
                    }
                }
                else if (eventType == XmlPullParser.END_TAG)
                {
                    if(xpp.getName().equalsIgnoreCase("geonameId")) {
                        Log.e("Parsing geoname", "parse finished");//finished

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

    }
}
