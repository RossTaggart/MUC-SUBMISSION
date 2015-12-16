package com.taggart.ross.gcu_weather_app;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ross
 */
public class aSyncXMLRetriever extends AsyncTask<String, String, String> {

    private String urlToParse;
    private String xmlFromUrl;

    public aSyncXMLRetriever(String URLToParse)
    {
        urlToParse = URLToParse;
    }

    @Override
    protected String doInBackground(String... params) {


        try {
            xmlFromUrl = sourceListingString(urlToParse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlFromUrl;
    }

    @Override
    protected void onPostExecute(String xmlFromUrl)
    {

    }

    //Downloads the XML data and holds it in a String
    private static String sourceListingString(String urlString)throws IOException
    {
        String result = "";
        InputStream anInStream = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        // Check that the connection can be opened
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try
        {
            // Open connection
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            // Check that connection is Ok
            if (response == HttpURLConnection.HTTP_OK)
            {
                // Connection is Ok so open a reader
                anInStream = httpConn.getInputStream();
                InputStreamReader in= new InputStreamReader(anInStream);
                BufferedReader bin= new BufferedReader(in);

                bin.readLine(); // Throw away the header
                // Read in the data from the XML stream
                String line = new String();
                while (( (line = bin.readLine())) != null)
                {
                    result = result + "\n" + line;
                }
            }
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");
        }

        // Return result as a string for further processing
        return result;
    }
}
