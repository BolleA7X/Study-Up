package com.example.alessio.tesi;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
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
import com.example.alessio.tesi.Database.Trophy;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private FloatingActionButton fab;
    private boolean pomodoroMode;
    //valore della seekbar che poi andrà moltiplicato per 5
    private int timeVal;
    private int secTimer = 60;
    private TextView minuteValue;
    private TextView secondValue;
    private ImageButton startTimerButton;
    private SeekBar seekBar;
    private TextView currentSubject;
    private boolean isOn;
    String[] sessionData;
    //questa è per capire se la subj è settata o no
    public boolean go;
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

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //pomodoroMode = prefs.getBoolean("pomodoro",true);
        timeVal = prefs.getInt("timeVal",12);

        //Metti il corso nella textview
        updateTimer(false);
        minuteValue.setText(String.valueOf(timeVal*5));
        secondValue.setVisibility(View.INVISIBLE);

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

        //Eseguo solo la prima volta che eseguo l'app ----- SBLOCCO TROFEO 1
        if(prefs.getBoolean("firstTimeOpeningApp", true)){
            AppDB db = new AppDB(this);
            db.unlockTrophy(1);
            Toast t = Toast.makeText(this, "CONGRATULATIONS: TROPHY 1 UNLOCKED.",Toast.LENGTH_LONG);
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
        CharSequence text = getString(R.string.first_error_toast);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void start(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("timeVal", timeVal);
        editor.apply();
        secondValue.setVisibility(View.VISIBLE);
        isOn = true;
        if(pomodoroMode) {
            startTimerButton.setImageResource(R.drawable.only_stop1_pomodoro);
        }else{
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
                notificationGo();
                Toast toast = Toast.makeText(getApplicationContext(), "Timer concluso!", Toast.LENGTH_LONG);
                toast.show();
                //se il timer finisce salvo la sessione passando la durata totale del timer
                saveSession(timeVal*5);
                // Se concludo un timer pomodoro, incremento contatore pomodoriCompletati e lo salvo nelle Preferences
                if(pomodoroMode == true){
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    int pomodoriDiFilaCompletati = prefs.getInt("ConsecutivelyCompletedTomatoes", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("ConsecutivelyCompletedTomatoes", ++pomodoriDiFilaCompletati);

                    // Verifico quanti pomodori ho completato (di fila e non) per sbloccare i vari trofei compatibilmente con le altre condizioni ----- SBLOCCO TROFEI 2 - 3
                    if(pomodoriDiFilaCompletati == 5){
                        AppDB db = new AppDB(getApplicationContext());
                        db.unlockTrophy(3); // SBLOCCO TROFEO 3
                        Toast t = Toast.makeText(getApplicationContext(), "CONGRATULATIONS: TROPHY 3 UNLOCKED.",Toast.LENGTH_LONG);
                    }
                }
            }
        };
        cTimer.start();
    }

    //se il timer viene stoppato prima della fine salvo la sessione passando la durata totale - la durata rimasta
    private void stop(){
        secondValue.setVisibility(View.INVISIBLE);
        if(pomodoroMode) {
            startTimerButton.setImageResource(R.drawable.only_play_pomodoro);

        }else {
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
            //timeVal = seekBar.getProgress() + 1;
            timeVal = seekBar.getProgress();
            // spostarla quando è a zero?
            //per non porlo uguale a 0
            minuteValue.setText(String.valueOf(timeVal*5));
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(timeVal==0){
                dontBeLazy();
                timeVal = 1;
                seekBar.setProgress(1);
            }
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
        Calendar calendar = Calendar.getInstance();

        Session session = new Session(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),
                                      duration, (th)?1:0, (ex)?1:0, (pr)?1:0,loc/*luogo*/,currentSub/*corso*/);
        /*Session session = new Session(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,duration,
                                      Session.stringToInt(sessionData[0]),Session.stringToInt(sessionData[1]),
                                      Session.stringToInt(sessionData[2]),sessionData[4],sessionData[3]);*/
        AppDB db = new AppDB(this);
        if(session != null)
            db.insertSession(session);         //eseguo query per inserire nel db i dati

        Toast.makeText(this, "Sessione: "+String.valueOf(th)+" "+String.valueOf(ex)+" "+String.valueOf(pr), Toast.LENGTH_SHORT).show();
        updateTimer(false);
    }

    public void updateTimer(boolean tot){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String currentSub = prefs.getString("subj", null);
        pomodoroMode = prefs.getBoolean("pomodoro", true);

        if(currentSub!= null && !currentSub.isEmpty() ){
            currentSubject.setText(currentSub);
            go = true;
        }else{
            currentSubject.setText(R.string.subject_label);
            go = false;
        }

        //imposto la pomodoro mode
        //non so perché ma funziona al contrario
        if(pomodoroMode) {
            timeVal = 5;
            minuteValue.setText(R.string.pomodoro_value);
            secondValue.setText(R.string.seconds_value);
            seekBar.setEnabled(false);
            startTimerButton.setImageResource(R.drawable.only_play_pomodoro);

        }else if(!pomodoroMode){
            if(tot){
                timeVal = 12;
            }
            minuteValue.setText(String.valueOf(timeVal*5));
            seekBar.setProgress(timeVal);
            secondValue.setText(R.string.seconds_value);
            seekBar.setEnabled(true);
            startTimerButton.setImageResource(R.drawable.only_play);
        }
    }

    //TODO non si cancella la notifica
    public void notificationGo(){
        NotificationCompat.Builder mBuilder = (android.support.v7.app.NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icona_notifica)
                        .setContentTitle(getString(R.string.notification_title_label))
                        .setContentText(getString(R.string.notification_text_label));

        // Quando tocca la notifica rientro nella main
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Questa parte costruisce la notifica con vibrazione, suono eccetera
        mBuilder.setVibrate(new long[] { 500, 500});
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setLights(Color.RED, 3000, 3000);
        mBuilder.setAutoCancel(true);
        int mNotificationId =0;
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }
    private void dontBeLazy(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.lazy_text_label)
                .setTitle(R.string.lazy_title_label)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}