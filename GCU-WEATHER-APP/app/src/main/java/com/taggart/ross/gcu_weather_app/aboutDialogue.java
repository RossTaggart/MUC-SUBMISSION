package com.taggart.ross.gcu_weather_app;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by Ross on 29/11/2015.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class aboutDialogue extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(getActivity());
        aboutDialog.setMessage("This app allows you to search for the weather in a location of your" +
                " choosing in the UK and allow you to view a map of previously searched locations.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id){

                    }
                });

        aboutDialog.setTitle("About");
        aboutDialog.setIcon(R.drawable.ic_menu_action_about);

        return aboutDialog.create();
    }
}
