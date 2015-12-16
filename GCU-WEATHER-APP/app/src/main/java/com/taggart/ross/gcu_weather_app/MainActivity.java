package com.taggart.ross.gcu_weather_app;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends Activity implements View.OnClickListener {

    Button startButton;
    FragmentManager fmAboutDialogue;
    Switch muteSoundSwitch;
    SharedPreferences sharedPrefs;

    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = getSharedPreferences("MY_SHARED_PREFS", MODE_PRIVATE);

        startButton = (Button)findViewById(R.id.startBtn);
        startButton.setOnClickListener(this);

        muteSoundSwitch = (Switch)findViewById(R.id.muteSoundSwitch);
        muteSoundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    //save prefs for sound to be muted
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("mute", true);
                    editor.apply();

                } else
                {
                    //save prefs for sound to not be muted
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("mute", false);
                    editor.apply();
                }
            }
        });

        fmAboutDialogue = this.getFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

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

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.startBtn)
        {
            Intent searchLocation = new Intent(this, SearchLocation.class);
            this.startActivity(searchLocation);
        }

    }
}
