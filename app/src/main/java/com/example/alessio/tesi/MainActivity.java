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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Session;
import com.example.alessio.tesi.Database.Trophy;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private FloatingActionButton fab;
    private Boolean pomodoroMode;
    //valore della seekbar che poi andrà moltiplicato per 5
    private int timeVal=12;

    private int secTimer=60;
    private TextView minuteValue;
    private TextView secondValue;
    private ImageButton startTimerButton;
    private SeekBar seekBar;
    private TextView currentSubject;
    private boolean isOn;
    String[] sessionData;
    //questa è per capire se la subj è settata o no
    public Boolean go;
    CountDownTimer cTimer = null;

    //Contatori per sblocco trofei

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
        Intent intent;

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
                intent = new Intent(this,ResultsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_calendar:
                intent = new Intent(this,CalendarActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get references to widgets
        startTimerButton = (ImageButton)findViewById(R.id.startTimerButton);
        minuteValue = (TextView)findViewById(R.id.minuteValue);
        secondValue = (TextView)findViewById(R.id.secondValue);
        seekBar = (SeekBar)findViewById(R.id.setTimerSeekBar);
        currentSubject = (TextView)findViewById(R.id.currentSubjectLabel);

        // set listeners
        startTimerButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(seekBarListener);

        minuteValue.setText(String.valueOf(timeVal*5));
        secondValue.setVisibility(View.INVISIBLE);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        pomodoroMode = prefs.getBoolean("pomodoro",false);
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
        //Metti il corso nella textview
        updateSubjText();

        //Eseguo solo la prima volta che eseguo l'app
        if(prefs.getBoolean("firstTimeOpeningApp", true) == true){
            AppDB db = new AppDB(this);
            Trophy[] trophyData = db.getTrophies();
            trophyData[0].setUnlocked(1);
            Toast t = Toast.makeText(this, "CONGRATULATIONS: TROPHY UNLOCKED.",Toast.LENGTH_LONG);
            t.show();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTimeOpeningApp", false);
            editor.apply();
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
            if(subj != null) {
                currentSubject.setText(subj);
                go = true;
            }

            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("subj", subj);
            editor.putString("loc", locat);
            editor.apply();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop(){
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
                } else {
                    if(go){
                        start();
                    }else{
                        FirstErrorToast();
                    }
                }
                break;
        }
    }
    //Funzione che manda il toast se non ho ancora messo sessione e corso
    private void FirstErrorToast(){
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.first_error_toast);;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    //TODO notifiche
    private void start(){
        secondValue.setVisibility(View.VISIBLE);
        isOn = true;
        if(!pomodoroMode) {
            startTimerButton.setImageResource(R.drawable.only_stop1_pomodoro);

        }else if (pomodoroMode){
            startTimerButton.setImageResource(R.drawable.only_stop1);
        }

        secTimer = 60;
        int mytime = (timeVal*5)*60*1000; //secondi
        seekBar.setEnabled(false);
        fab.setClickable(false);
        fab.setVisibility(View.INVISIBLE);
        //start timer function
        cTimer = new CountDownTimer(mytime, 1000) {
            public void onTick(long millisUntilFinished) {
                updateViews(millisUntilFinished);
            }
            public void onFinish() {
                Toast toast = Toast.makeText(getApplicationContext(), "Timer concluso!", Toast.LENGTH_LONG);
                toast.show();
                //se il timer finisce salvo la sessione passando la durata totale del timer
                saveSession(timeVal*5);
            }
        };
        cTimer.start();
    }

    //se il timer viene stoppato prima della fine salvo la sessione passando la durata totale - la durata rimasta
    private void stop(){
        secondValue.setVisibility(View.INVISIBLE);
        if(!pomodoroMode) {
            startTimerButton.setImageResource(R.drawable.only_play_pomodoro);

        }else if (pomodoroMode){
            startTimerButton.setImageResource(R.drawable.only_play);
        }
        isOn = false;
        fab.setClickable(true);
        fab.setVisibility(View.VISIBLE);

        if(cTimer!=null){
            cTimer.cancel();
        }
        int saveInt = timeVal*5 - Integer.parseInt(minuteValue.getText().toString());
        saveSession(saveInt);
    }
    //Funzione per aggiornare le TextView per il timer
    private void updateViews(long millisUntilFinished){
        if(secTimer == 60){
            String min = String.valueOf( millisUntilFinished / 60000);
            minuteValue.setText(min);
        }

        secTimer = secTimer - 1;
        secondValue.setText(String.valueOf(secTimer));

        if(secTimer == 0){
            secTimer = 60;
        }
    }
    // Event handler for the SeekBar
    private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            timeVal = seekBar.getProgress();
            //per non porlo uguale a 0
            if(timeVal==0){
                timeVal =1;
            }
            minuteValue.setText(String.valueOf(timeVal*5));
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    //salva la sessione
    private void saveSession(int duration) {
        //creo una variabile Session e ci salvo i dati necessari, ovvero giorno mese e anno (dal calendario interno del sistema),
        //la durata passata come parametro (vedi sopra), i dati sulla sessione (corso,luogo,teoria,esercizi,progetto)
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String currentSub = prefs.getString("subj",null);
        Boolean th = prefs.getBoolean("th", false);
        Boolean ex = prefs.getBoolean("ex", false);
        Boolean pr = prefs.getBoolean("pr", false);
        String loc = prefs.getString("loc", "Nessun dato");

        Session session = new Session(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH, duration,
                (th)?1:0, (ex)?1:0, (pr)?1:0,loc/*luogo*/,currentSub/*corso*/);
        /*Session session = new Session(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,duration,
                                      Session.stringToInt(sessionData[0]),Session.stringToInt(sessionData[1]),
                                      Session.stringToInt(sessionData[2]),sessionData[4],sessionData[3]);*/
        AppDB db = new AppDB(this);
        if(session != null)
            db.insertSession(session);         //eseguo query per inserire nel db i dati

        Toast.makeText(this, "Sessione: "+String.valueOf(th)+" "+String.valueOf(ex)+" "+String.valueOf(pr), Toast.LENGTH_SHORT).show();
        updateSubjText();
    }

    public void updateSubjText(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String currentSub = prefs.getString("subj",null);
        pomodoroMode = prefs.getBoolean("pomodoro",true);

        if(currentSub!= null && !currentSub.isEmpty() ){
            currentSubject.setText(currentSub);
            go = true;
        }else{
            currentSubject.setText(R.string.subject_label);
            go = false;
        }

        //imposto la pomodoro mode
        //non so perché ma funziona al contrario
        if(!pomodoroMode) {
            timeVal = 5;
            minuteValue.setText("25");
            seekBar.setEnabled(false);
            startTimerButton.setImageResource(R.drawable.only_play_pomodoro);

        }else if (pomodoroMode){
            timeVal = 12;
            minuteValue.setText(String.valueOf(timeVal*5));
            seekBar.setEnabled(true);
            startTimerButton.setImageResource(R.drawable.only_play);
        }
    }
}