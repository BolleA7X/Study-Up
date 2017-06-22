package com.example.alessio.tesi;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /************Prova Timer************/
    private TextView timerValue;
    private ImageButton imageB;

    private boolean isOn;
    CountDownTimer cTimer = null;
    private SharedPreferences prefs;

    //Menu
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get references to widgets
        imageB = (ImageButton)findViewById(R.id.startTimerButton);
        timerValue =(TextView)findViewById(R.id.timerValue);

        // set listeners
        imageB.setOnClickListener(this);

        // get preferences
        prefs = getSharedPreferences("Prefs", MODE_PRIVATE);


        //floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.setButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SessionSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor edit = prefs.edit();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startTimerButton:
                if (isOn) {
                    stop();
                }
                else {
                    start();
                }
                break;
        }
    }
    private void start(){
        isOn = true;
        imageB.setImageResource(R.drawable.only_stop);

        //start timer function
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                String s = String.valueOf( millisUntilFinished / 1000);
                timerValue.setText(s);
                //here you can have your logic to set text to edittext
            }
            public void onFinish() {
                timerValue.setText("OK!");
            }
        };
        cTimer.start();

    }
    private void stop(){
        imageB.setImageResource(R.drawable.only_play);
        isOn = false;
        if(cTimer!=null)
            cTimer.cancel();

    }

}
