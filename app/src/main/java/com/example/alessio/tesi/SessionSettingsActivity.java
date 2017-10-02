package com.example.alessio.tesi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alessio.tesi.Database.AppDB;

import static com.example.alessio.tesi.R.layout.session_settings_activity;

public class SessionSettingsActivity extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences prefs;
    private Button endedConfig;
    private Button addSubjectButton;
    private Button addLocationButton;
    private Spinner subjectsSpinner, locationSpinner;
    private CheckBox theory, exercises, project;
    private ImageButton viewLocationButton;
    private int locSpinPosition;
    private int subjSpinPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(session_settings_activity);

        endedConfig = (Button) findViewById(R.id.endedConfig);
        endedConfig.setOnClickListener(this);
        addSubjectButton = (Button)findViewById(R.id.addSubjectButton);
        addSubjectButton.setOnClickListener(this);
        addLocationButton = (Button)findViewById(R.id.addLocationButton);
        addLocationButton.setOnClickListener(this);
        theory = (CheckBox)findViewById(R.id.theoryCheckBox);
        exercises = (CheckBox)findViewById(R.id.exercisesCheckBox);
        project = (CheckBox)findViewById(R.id.projectCheckBox);
        viewLocationButton = (ImageButton)findViewById(R.id.viewLocationButton);
        subjectsSpinner = (Spinner)findViewById(R.id.subjectsSpinner);
        locationSpinner = (Spinner)findViewById(R.id.locationSpinner);

        //Shared per i checkbox
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean th = prefs.getBoolean("th", false);
        Boolean ex = prefs.getBoolean("ex", false);
        Boolean pr = prefs.getBoolean("pr", false);

        theory.setChecked(th);
        exercises.setChecked(ex);
        project.setChecked(pr);
        updateSpinner();
    }

    @Override
    protected void onResume() {
        updateSpinner();
        super.onResume();
    }

    @Override
    protected void onPause() {
       super.onPause();
    }

    @Override
    protected void onStop(){
        //salva gli Shared per i checkbox e le posizioni degli spinner
        Boolean th = theory.isChecked();
        Boolean ex = exercises.isChecked();
        Boolean pr = project.isChecked();
        locSpinPosition = locationSpinner.getSelectedItemPosition();
        subjSpinPosition = subjectsSpinner.getSelectedItemPosition();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("th", th);
        editor.putBoolean("ex", ex);
        editor.putBoolean("pr", pr);
        editor.putInt("locSpinPosition",locSpinPosition);
        editor.putInt("subjSpinPosition",subjSpinPosition);

        editor.apply();
        super.onStop();
    }

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
                FragmentManager fSm = getFragmentManager();
                subjDialogFragment.setCancelable(false);
                subjDialogFragment.show(fSm, "Sample Fragment");
                break;
            //Bottone aggiunta location
            case R.id.addLocationButton:
                MapViewFragment mapFragment = new MapViewFragment();
                FragmentManager fLm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fLm.beginTransaction();
                fragmentTransaction.add(R.id.sessionSettingsActivity, mapFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.viewLocationButton:
                getLocation();
                break;
        }
    }

    public void updateSpinner(){
        AppDB db = new AppDB(this);
        //Prende dalle Shared l'ultima posizione dello spinner
        locSpinPosition = prefs.getInt("locSpinPosition",-1);
        subjSpinPosition = prefs.getInt("subjSpinPosition",-1);

        //eseguo la query tramite il metodo getSubjects() per ottenere l'ArrayAdapter contenete le info sui corsi
        ArrayAdapter<String> subjects = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,db.getSubjects(prefs.getString("loggedAs","")));
        if(subjects != null) {
            //se l'array è valido lo associo allo spinner.
            subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectsSpinner.setAdapter(subjects);
            if(subjSpinPosition != -1){
                subjectsSpinner.setSelection(subjSpinPosition);
            }
            subjects.notifyDataSetChanged();
        }
        //come sopra ma con i posti
        ArrayAdapter<String> locations = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,db.getLocations());
        if(locations != null) {
            locations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(locations);
            if(locSpinPosition != -1){
                locationSpinner.setSelection(locSpinPosition);
            }
            locations.notifyDataSetChanged();
        }
    }

    public void getLocation(){
        String loc=null;
        try {
            loc = locationSpinner.getSelectedItem().toString();
        }
        catch (Exception exc) {

        }

        if(loc !=null && !loc.isEmpty()){
            //Lancia il fragment della mappa
            FragmentManager fM;
            FragmentTransaction fT;
            MyLocationsFragment MyLocFrag = new MyLocationsFragment();
            Bundle bundle = new Bundle();
            //passo al fragment il nome della location
            bundle.putString("LocName", loc);
            MyLocFrag.setArguments(bundle);
            fM = getFragmentManager();
            fT =fM.beginTransaction();
            fT.add(R.id.sessionSettingsActivity, MyLocFrag);
            fT.addToBackStack(null);
            fT.commit();
        }else{
            //se non ci sono stringhe dà errore
            Toast toast = Toast.makeText(getApplicationContext(), R.string.no_location_label, Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
