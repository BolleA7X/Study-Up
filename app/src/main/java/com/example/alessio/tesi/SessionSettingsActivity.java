package com.example.alessio.tesi;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.example.alessio.tesi.Database.AppDB;

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

        /*SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        theory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                editor.putBoolean("checkTh", theory.isChecked());
                editor.apply();
            }
        });
        exercises.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                editor.putBoolean("checkEx", exercises.isChecked());
                editor.apply();
            }
        });
        project.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                editor.putBoolean("checkPr", project.isChecked());
                editor.apply();
            }
        });


        sharedPreferences.getBoolean("checkTh", false);
        sharedPreferences.getBoolean("checkEx", false);
        sharedPreferences.getBoolean("checkPr", false);
        editor.apply();*/
    }

    @Override
    protected void onResume() {
        updateSpinner();
        /*SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedPreferences.getBoolean("checkTh", false);
        sharedPreferences.getBoolean("checkEx", false);
        sharedPreferences.getBoolean("checkPr", false);*/
        super.onResume();
    }
    @Override
    protected void onPause() {
       /* SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("checkTh", theory.isChecked());
        editor.putBoolean("checkEx", exercises.isChecked());
        editor.putBoolean("checkPr", project.isChecked());
        editor.commit();*/
       super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            return true;
        }
        else if (id==R.id.menu_trophies){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //qua io prendo le informazioni da mandare nella MainActivity (vedi codice in MainActivity)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //bottone OK
            case R.id.endedConfig:
                Intent resultIntent = new Intent();
                String locationName = null;
                if(locationSpinner != null && locationSpinner.getSelectedItem() !=null ) {
                    locationName = (String)locationSpinner.getSelectedItem();
                }

                String subjName = null;
                if(subjectsSpinner != null && subjectsSpinner.getSelectedItem() !=null ) {
                    subjName = (String)subjectsSpinner.getSelectedItem();
                }
                //costruisce la stringa da restituire in MainActivity
                String[] dataToSend = {Boolean.toString(theory.isChecked()),Boolean.toString(exercises.isChecked()),
                        Boolean.toString(project.isChecked()),subjName,locationName};
                resultIntent.putExtra("currentSessionData",dataToSend);
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
                break;
            //Bottone aggiunta materia
            case R.id.addSubjectButton:
                setSubjectFragment subjDialogFragment = new setSubjectFragment ();
                openDialog(subjDialogFragment);
                break;
            //Bottone aggiunta location
            case R.id.addLocationButton:
                setLocationFragment locDialogfragment = new setLocationFragment ();
                openDialog(locDialogfragment);
                break;

        }
    }
    //Funzione che apre i dialog passando un DialogFragment
    public void openDialog(DialogFragment dialogFragment) {
        FragmentManager fm = getFragmentManager();
        dialogFragment.show(fm, "Sample Fragment");
    }

    public void updateSpinner(){
        subjectsSpinner = (Spinner)findViewById(R.id.subjectsSpinner);
        locationSpinner = (Spinner)findViewById(R.id.locationSpinner);

        AppDB db = new AppDB(this);

        //eseguo la query tramite il metodo getSubjects() per ottenere l'ArrayAdapter contenete le info sui corsi
        ArrayAdapter<String> subjects = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,db.getSubjects());
        if(subjects != null) {
            //se l'array è valido lo associo allo spinner.
            subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectsSpinner.setAdapter(subjects);
            subjects.notifyDataSetChanged();
        }
        //come sopra ma con i posti
        ArrayAdapter<String> locations = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,db.getLocations());
        if(locations != null) {
            locations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(locations);
            locations.notifyDataSetChanged();
        }
    }

}
