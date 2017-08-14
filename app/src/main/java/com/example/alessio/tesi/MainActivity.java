package com.example.alessio.tesi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Session;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private Boolean pomodoroMode;
    private FloatingActionButton fab;
    private int timeVal=90;
    private long mytime;
    private TextView timerValue;
    private ImageButton imageB;
    private SeekBar seekBar;
    private TextView currentSubject;
    private boolean isOn;
    String[] sessionData;

    CountDownTimer cTimer = null;

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
        Fragment fragment;
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;

        switch(id){
            case R.id.menu_trophies:
                fragment = new TrophiesFragment();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainActivity, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.menu_settings:
                fragment = new SettingsFragment();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainActivity, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.menu_results:
                Intent intent = new Intent(this,ResultsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_calendar:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this,R.xml.preference,false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        pomodoroMode = prefs.getBoolean("use_pomodoro_mode",false);

        // get references to widgets
        imageB = (ImageButton)findViewById(R.id.startTimerButton);
        timerValue = (TextView)findViewById(R.id.timerValue);
        seekBar = (SeekBar)findViewById(R.id.setTimerSeekBar);
        currentSubject = (TextView)findViewById(R.id.currentSubjectLabel);

        // set listeners
        imageB.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(seekBarListener);

        timerValue.setText(String.valueOf(timeVal));

        //floating button
        fab = (FloatingActionButton) findViewById(R.id.setButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SessionSettingsActivity.class);
                /*
                putExtra mette informazione extra nell'intent. Una volta che viene aperta la nuova activity è possibile usare
                l'intent per passare informazioni tra le due activity. Il metodo startActivityForResult serve proprio per fare in
                modo che la seconda activity possa dare risultati e passarli all prima
                 */
                intent.putExtra("dataToPass","");
                startActivityForResult(intent,0);
            }
        });
        //Metti il corso nella textivew
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String currentSub = sharedPref.getString("subj",null);

        if(currentSub!= null && !currentSub.isEmpty() ){
            currentSubject.setText(currentSub);
        }

        //imposto la pomodoro mode
        if(pomodoroMode) {
            timeVal = 25;
            timerValue.setText("25");
            seekBar.setEnabled(false);
        }
    }

    //questo metodo viene chiamato quando la seconda activity viene chiusa e passa i risultati a questa tramite la putExtra()
    //nell'intent. Questi dati vengono prelevati dall'intent tramite getStringArrayExtra() e messi in "sessionData".
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(data != null) {
            sessionData = data.getStringArrayExtra("currentSessionData");
            String subj = sessionData[3];
            String locat = sessionData[4];
            if(subj != null)
                currentSubject.setText(subj);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop(){
        String subjName = currentSubject.getText().toString();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("subj", subjName);
        editor.apply();
        super.onStop();
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
                    if(sessionData != null)
                        start();
                    else
                        FirstErrorToast();
                }
                break;
        }
    }
    //Funzione che manda il toast se non ho ancora messo sessione e corso
    private void FirstErrorToast(){
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.FirstErrorToast);;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    private void start(){
        isOn = true;
        imageB.setImageResource(R.drawable.only_stop1);
        mytime = timeVal*1000;
        seekBar.setEnabled(false);
        fab.setClickable(false);
        fab.setVisibility(View.INVISIBLE);

        //start timer function
        cTimer = new CountDownTimer(mytime, 1000) {
            public void onTick(long millisUntilFinished) {
                String s = String.valueOf( millisUntilFinished / 1000);
                timerValue.setText(s);
            }
            public void onFinish() {
                timerValue.setText("OK!");
                //se il timer finisce salvo la sessione passando la durata totale del timer
                saveSession(timeVal);
            }
        };
        cTimer.start();
    }

    //se il timer viene stoppato prima della fine salvo la sessione passando la durata totale - la durata rimasta
    private void stop(){
        saveSession(timeVal - Integer.parseInt(timerValue.getText().toString()));
    }

    //*****************************************************
    // Event handler for the SeekBar
    //*****************************************************
    private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            timeVal = seekBar.getProgress();
            timerValue.setText(String.valueOf(timeVal));
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    //salva la sessione
    private void saveSession(int duration) {
        //creo una variabile Session e ci salvo i dati necessari, ovvero giorno mese e anno (dal calendario interno del sistema),
        //la durata passata come parametro (vedi sopra), i dati sulla sessione (corso,luogo,teoria,esercizi,progetto)
        Session session = new Session(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,duration,
                                      Session.stringToInt(sessionData[0]),Session.stringToInt(sessionData[1]),
                                      Session.stringToInt(sessionData[2]),sessionData[4]/*luogo*/,sessionData[3]/*corso*/);
        AppDB db = new AppDB(this);
        if(session != null)
            db.insertSession(session);                  //eseguo query per inserire nel db i dati.

        //resetto il timer per poterlo riutilizzare (lavoro di luca spostato in questo metodo)
        imageB.setImageResource(R.drawable.only_play);
        isOn = false;
        if(!pomodoroMode) {
            seekBar.setEnabled(true);
            timerValue.setText(String.valueOf(timeVal));
        }

        fab.setClickable(true);
        fab.setVisibility(View.VISIBLE);

        if(cTimer!=null){
            cTimer.cancel();
        }
    }

}