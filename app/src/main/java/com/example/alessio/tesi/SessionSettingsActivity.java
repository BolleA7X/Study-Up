package com.example.alessio.tesi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;


import com.example.alessio.tesi.Database.AppDB;

import java.io.Serializable;

import static com.example.alessio.tesi.R.layout.session_settings_activity;

public class SessionSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button endedConfig;
    private Button addSubjectButton;
    private Button addLocationButton;
    private Spinner subjectsSpinner, locationSpinner;
    private CheckBox theory, exercises, project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_settings_activity);

        endedConfig = (Button) findViewById(R.id.endedConfig);
        endedConfig.setOnClickListener(this);
        addSubjectButton = (Button)findViewById(R.id.addSubjectButton);
        addSubjectButton.setOnClickListener(this);
        addLocationButton = (Button)findViewById(R.id.addLocationButton);
        addLocationButton.setOnClickListener(this);
        theory = (CheckBox)findViewById(R.id.theoryCheckBox);
        exercises = (CheckBox)findViewById(R.id.exercisesCheckBox);
        project = (CheckBox)findViewById(R.id.projectCheckBox);
    }

    @Override
    protected void onResume() {
        subjectsSpinner = (Spinner)findViewById(R.id.subjectsSpinner);
        locationSpinner = (Spinner)findViewById(R.id.locationSpinner);

        AppDB db = new AppDB(this);

        ArrayAdapter<String> subjects = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,db.getSubjects());
        if(subjects != null) {
            subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectsSpinner.setAdapter(subjects);
        }

        ArrayAdapter<String> locations = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,db.getLocations());
        if(locations != null) {
            locations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(locations);
        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            return true;
        }
        else if (id==R.id.menu_trophies){
            Fragment newFragment = new TrophiesFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.endedConfig:
                Intent intent = new Intent();
                String locationName = locationSpinner.getSelectedItem().toString();
                if(locationName == null)
                    locationName = "";

                String[] dataToSend = {Boolean.toString(theory.isChecked()),Boolean.toString(exercises.isChecked()),
                        Boolean.toString(project.isChecked()),subjectsSpinner.getSelectedItem().toString(),locationName};
                intent.putExtra("currentSessionData",dataToSend);
                setResult(1,intent);
                finish();   //back to main activity
                break;
            case R.id.addSubjectButton:
                openDialog();
                break;
            case R.id.addLocationButton:
                FragmentManager fm = getFragmentManager();
                setLocationFragment dialogFragment = new setLocationFragment ();
                dialogFragment.show(fm, "Sample Fragment");
                break;
        }
    }

    public void openDialog() {
        FragmentManager fm = getFragmentManager();
        setSubjectFragment dialogFragment = new setSubjectFragment ();
        dialogFragment.show(fm, "Sample Fragment");
    }
}
