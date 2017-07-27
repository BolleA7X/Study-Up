package com.example.alessio.tesi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.example.alessio.tesi.Database.AppDB;

import static com.example.alessio.tesi.R.layout.session_settings_activity;

public class SessionSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button endedConfig;
    private Button addSubjectButton;
    private Spinner subjectsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_settings_activity);

        endedConfig = (Button) findViewById(R.id.endedConfig);
        endedConfig.setOnClickListener(this);
        addSubjectButton = (Button)findViewById(R.id.addSubjectButton);
        addSubjectButton.setOnClickListener(this);
        subjectsSpinner = (Spinner)findViewById(R.id.subjectsSpinner);

        AppDB db = new AppDB(this);
        ArrayAdapter<String> subjects = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,db.getSubjects());
        if(subjects != null) {
            subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectsSpinner.setAdapter(subjects);
        }
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
                finish();   //back to main activity
                break;
            case R.id.addSubjectButton:
                openDialog();
                break;
        }
    }

    public void openDialog() {
        FragmentManager fm = getFragmentManager();
        setSubjectFragment dialogFragment = new setSubjectFragment ();
        dialogFragment.show(fm, "Sample Fragment");
    }
}
