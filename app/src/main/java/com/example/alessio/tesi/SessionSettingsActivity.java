package com.example.alessio.tesi;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import static com.example.alessio.tesi.R.layout.session_settings_activity;

public class SessionSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Button endedConfig;
    private Button addSubjectButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(session_settings_activity);

        endedConfig = (Button) findViewById(R.id.endedConfig);
        endedConfig.setOnClickListener(this);
        addSubjectButton = (Button)findViewById(R.id.addSubjectButton);
        addSubjectButton.setOnClickListener(this);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.endedConfig:
                finish();
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
