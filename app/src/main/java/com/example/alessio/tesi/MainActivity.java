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
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private FloatingActionButton fab;
    private boolean pomodoroMode;
    private int stop = R.drawable.only_stop1_pomodoro_seeds;
    private int start = R.drawable.only_play_pomodoro_seeds;
    private int timeVal;   //valore della seekbar che poi andrà moltiplicato per 5
    private int secTimer = 60;
    private TextView minuteValue;
    private TextView secondValue;
    private ImageButton startTimerButton;
    private SeekBar seekBar;
    private TextView currentSubject;
    private boolean isOn;
    String[] sessionData;
    public boolean go;    //questa è per capire se la subj è settata o no
    CountDownTimer cTimer = null;
    long lastPress;
    private Trophy[] trophies;   //per avere i trofei a disposizione in tutti i metodi
    private FragmentManager fragmentManager;


    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        Fragment fragment;
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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread tr;
        //controlla se l'app è stata già aperta
        tr = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pr = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                boolean logged = pr.getBoolean("logged",false);

                //  If the activity has never started before...
                if (!logged) {
                    //  Launch app intro
                    final Intent i = new Intent(getApplicationContext(), IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            //startActivity(i);
                            i.putExtra("Opening","");
                            startActivityForResult(i,0);
                        }
                    });
                    SharedPreferences.Editor e = pr.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });
        // Start the thread
        tr.start();
        // get references to widgets
        startTimerButton = (ImageButton)findViewById(R.id.startTimerButton);
        minuteValue = (TextView)findViewById(R.id.minuteValue);
        secondValue = (TextView)findViewById(R.id.secondValue);
        seekBar = (SeekBar)findViewById(R.id.setTimerSeekBar);
        currentSubject = (TextView)findViewById(R.id.currentSubjectLabel);

        // set listeners
        startTimerButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(seekBarListener);

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
                intent.putExtra("dataToPass","");
                startActivityForResult(intent,0);
            }
        });

        //Ottengo la data di oggi
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AppDB db = new AppDB(this);
        trophies = db.getTrophies();

        SharedPreferences.Editor editor = prefs.edit();

        // SBLOCCO TROFEO 13
        //Controllo che siano passati 7 giorni dall'ultimo utilizzo
        //ottengo l'ultimo utilizzo
        int lDay = prefs.getInt("lastDay",calendar.get(Calendar.DAY_OF_MONTH));
        int lMonth = prefs.getInt("lastMonth",calendar.get(Calendar.MONTH));
        int lYear = prefs.getInt("lastYear",calendar.get(Calendar.YEAR));
        Calendar lastUse = Calendar.getInstance();
        lastUse.set(lYear,lMonth,lDay);
        //calcolo il numero di giorni passati
        long difference = (TimeUnit.MILLISECONDS.toDays(Math.abs(calendar.getTimeInMillis() - lastUse.getTimeInMillis())));
        if(difference >= 7 && trophies[12].getUnlocked() == 0) {
            db.unlockTrophy(13);
            Toast t = Toast.makeText(this, this.getResources().getString(R.string.unlockTrophy)+"13",Toast.LENGTH_LONG);
            t.show();
        }
        //se il giorno è cambiato azzero il contatore di pomodori giornalieri
        if(difference > 0)
            editor.putInt("dailyTomatoes",0);

        //Quando apro l'app aggiorno la data di ultimo utilizzo
        editor.putInt("lastDay",calendar.get(Calendar.DAY_OF_MONTH));
        editor.putInt("lastMonth",calendar.get(Calendar.MONTH));
        editor.putInt("lastYear",calendar.get(Calendar.YEAR));
        editor.apply();
        // SBLOCCO TROFEO 20 (PLATINO)
        //per ora l'ho messo qua, ovvero quando viene aperta l'app fa il controllo. In teoria andrebbe fatto ogni volta che
        //viene sbloccato un trofeo
        if(trophies[19].getUnlocked() == 0) {
            if(db.platinum()) {
                db.unlockTrophy(20);
                Toast t = Toast.makeText(this,this.getResources().getString(R.string.unlockTrophy)+"20",Toast.LENGTH_LONG);
                t.show();
            }
        }
    }
    //Controlla i risultati delle activity, i valori ritornati
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(data != null ) {
            //Per i risultati da SessionSettings
            if(data.hasExtra("currentSessionData")){
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
            }else if(data.hasExtra("Opening")){ //per i risultati da intro
                String first = data.getStringExtra("Opening");
                if(first.equals("firstOpening")){
                    firstOpening();
                }
            }
        }

    }

    @Override
    protected void onPause() {
        //TODO fare thread?
        if (isOn) {
            stop();
        }
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {

            Toast backpressToast =Toast.makeText(getBaseContext(), R.string.back_confirm, Toast.LENGTH_LONG);
            long currentTime = System.currentTimeMillis();
            if(currentTime - lastPress > 5000){
                backpressToast.show();
                lastPress = currentTime;
            } else {
                if (backpressToast != null) backpressToast.cancel();
                super.onBackPressed();
            }
        } else {
            getFragmentManager().popBackStack();
        }

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
                    DialogStop();
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
        CharSequence text = this.getResources().getString(R.string.noSessionData);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void start(){
        final AppDB db = new AppDB(this);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("timeVal", timeVal);
        // se non sono in modalità pomodoro, azzero il contatore dei pomodori consecutivi completati (utile a sblocco trofeo 3)
        if(!pomodoroMode){
            editor.putInt("ConsecutivelyCompletedTomatoes", 0);
        }
        // SBLOCCO TROFEO 12
        if(pomodoroMode && trophies[11].getUnlocked() == 0) {
            db.unlockTrophy(12);
            trophies[11].setUnlocked(1);
            Toast t = Toast.makeText(this,this.getResources().getString(R.string.unlockTrophy)+"12",Toast.LENGTH_LONG);
            t.show();
        }
        editor.apply();
        secondValue.setVisibility(View.VISIBLE);
        isOn = true;
        startTimerButton.setImageResource(stop);
        secTimer = 60;
        int mytime;
        mytime = (timeVal*5)*60*1000; //secondi


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
                Toast toast = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.endedTimer), Toast.LENGTH_LONG);
                toast.show();
                //se il timer finisce salvo la sessione passando la durata totale del timer
                saveSession(timeVal*5);
                // Apro il database (per fare delle get per i trofei)
                // Se concludo un timer pomodoro, incremento contatore consecutivelyCompletedTomatoes e lo salvo nelle Preferences
                if(pomodoroMode) {
                    int dailyTomatoes = prefs.getInt("dailyTomatoes",0);
                    ++dailyTomatoes;
                    editor.putInt("dailyTomatoes",dailyTomatoes);
                    // SBLOCCO TROFEO 2
                    if(trophies[1].getUnlocked() == 0){
                        db.unlockTrophy(2);
                        trophies[1].setUnlocked(1);
                        Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"2",Toast.LENGTH_LONG);
                        t.show();
                    }
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    int consecutivelyCompletedTomatoes = prefs.getInt("ConsecutivelyCompletedTomatoes", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("ConsecutivelyCompletedTomatoes", ++consecutivelyCompletedTomatoes);
                    editor.apply();

                    // SBLOCCO TROFEO 3
                    if(consecutivelyCompletedTomatoes == 5 && trophies[2].getUnlocked() == 0){
                        db.unlockTrophy(3);
                        trophies[2].setUnlocked(1);
                        Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"3",Toast.LENGTH_LONG);
                        t.show();
                    }

                    // SBLOCCO TROFEO 9
                    if(dailyTomatoes == 10 && trophies[8].getUnlocked() == 0) {
                        db.unlockTrophy(9);
                        trophies[8].setUnlocked(1);
                        Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"9",Toast.LENGTH_LONG);
                        t.show();
                    }
                }

                // SBLOCCO TROFEI 4 - 5 - 6
                int totalStudyTime = db.getTotalTime(prefs.getString("loggedAs",""));
                if(totalStudyTime >= 600 && totalStudyTime < 1500 && trophies[3].getUnlocked() == 0){
                    db.unlockTrophy(4);
                    trophies[3].setUnlocked(1);
                    Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"4",Toast.LENGTH_LONG);
                    t.show();
                }
                else if(totalStudyTime >= 1500 && totalStudyTime < 3000 && trophies[4].getUnlocked() == 0){
                    db.unlockTrophy(5);
                    trophies[4].setUnlocked(1);
                    Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"5",Toast.LENGTH_LONG);
                    t.show();
                }
                else if(totalStudyTime >= 3000 && trophies[5].getUnlocked() == 0){
                    db.unlockTrophy(6);
                    trophies[5].setUnlocked(1);
                    Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"6",Toast.LENGTH_LONG);
                    t.show();
                }

                // SBLOCCO TROFEI 18 - 19
                if(trophies[17].getUnlocked() == 0 && timeVal*5 >= 120) {
                    db.unlockTrophy(18);
                    trophies[17].setUnlocked(1);
                    Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"18",Toast.LENGTH_LONG);
                    t.show();
                }
                if(trophies[18].getUnlocked() == 0 && timeVal*5 >= 180) {
                    db.unlockTrophy(19);
                    trophies[18].setUnlocked(1);
                    Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"19",Toast.LENGTH_LONG);
                    t.show();
                }

                // SBLOCCO TROFEO 17
                if (trophies[16].getUnlocked() == 0 && db.getLocationsCount() == 5){
                    db.unlockTrophy(17);
                    trophies[16].setUnlocked(1);
                    Toast t = Toast.makeText(getApplicationContext(), getApplication().getResources().getString(R.string.unlockTrophy)+"17",Toast.LENGTH_LONG);
                    t.show();
                }
            }
        };
        cTimer.start();
    }
    //Per il dialog di conferma
    private void DialogStop(){

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean confirm = prefs.getBoolean("ConfirmOnExit",true);
        if(confirm){
            ConfirmOnExit ConfirmOnExitFragment = new ConfirmOnExit ();
            FragmentManager fSm = getFragmentManager();
            ConfirmOnExitFragment.setCancelable(false);
            ConfirmOnExitFragment.show(fSm, "Confirm Fragment");
        }else{
            stop();
        }
    }
    //se il timer viene stoppato prima della fine salvo la sessione passando la durata totale - la durata rimasta
    public void stop(){
        secondValue.setVisibility(View.INVISIBLE);
        startTimerButton.setImageResource(start);
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
        String loc = prefs.getString("loc", "");
        Calendar calendar = Calendar.getInstance();

        Session session = new Session(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),
                                      duration, (th)?1:0, (ex)?1:0, (pr)?1:0,loc/*luogo*/,currentSub/*corso*/);

        AppDB db = new AppDB(this);
        if(session != null)
            db.insertSession(session,prefs.getString("loggedAs",""));         //eseguo query per inserire nel db i dati

        // SBLOCCO TROFEI 7 - 8
        if(trophies[6].getUnlocked() == 0 || trophies[7].getUnlocked() == 0) {
            int differentCourses = db.differentCourses(calendar,prefs.getString("loggedAs",""));
            if(differentCourses == 2) {
                db.unlockTrophy(7);
                trophies[6].setUnlocked(1);
                Toast t = Toast.makeText(this,this.getResources().getString(R.string.unlockTrophy)+"7",Toast.LENGTH_LONG);
                t.show();
            }
            else if(differentCourses > 2) {
                db.unlockTrophy(8);
                trophies[7].setUnlocked(1);
                Toast t = Toast.makeText(this,this.getResources().getString(R.string.unlockTrophy)+"8",Toast.LENGTH_LONG);
                t.show();
            }
        }

        // SBLOCCO TROFEO 15
        if(trophies[14].getUnlocked() == 0) {
            if(db.oneMonth()) {
                db.unlockTrophy(15);
                trophies[14].setUnlocked(1);
                Toast t = Toast.makeText(this,this.getResources().getString(R.string.unlockTrophy)+"15",Toast.LENGTH_LONG);
                t.show();
            }
        }

        // SBLOCCO TROFEO 16
        if(trophies[15].getUnlocked() == 0) {
            if(db.numberOfSessions() >= 42) {
                db.unlockTrophy(16);
                trophies[15].setUnlocked(1);
                Toast t = Toast.makeText(this,this.getResources().getString(R.string.unlockTrophy)+"16",Toast.LENGTH_LONG);
                t.show();
            }
        }

        updateTimer(false);
    }

    public void updateTimer(boolean reset){
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
        if(pomodoroMode) {
            timeVal = 5;
            minuteValue.setText(R.string.pomodoro_value);
            secondValue.setText(R.string.seconds_value);
            seekBar.setEnabled(false);
            stop = R.drawable.only_stop1_pomodoro_seeds;
            start = R.drawable.only_play_pomodoro_seeds;
            startTimerButton.setImageResource(start);
        }else{
            if(reset){
                timeVal = 12;
            }
            minuteValue.setText(String.valueOf(timeVal*5));
            seekBar.setProgress(timeVal);
            secondValue.setText(R.string.seconds_value);
            seekBar.setEnabled(true);
            stop = R.drawable.only_stop1;
            start = R.drawable.only_play;
            startTimerButton.setImageResource(start);
        }
    }

    public void notificationGo(){
        int notifyIcon = R.drawable.icona_notifica_custom;
        int notifiyText = R.string.notification_title_label_custom;
        if(pomodoroMode){
            notifyIcon = R.drawable.icona_notifica_pomodoro;
            notifiyText = R.string.notification_title_label_pomodoro;
        }
        NotificationCompat.Builder mBuilder = (android.support.v7.app.NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                        .setSmallIcon(notifyIcon)
                        .setContentTitle(getString(notifiyText))
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
        mBuilder.setVibrate(new long[] {500, 500});
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setLights(Color.RED, 3000, 3000);
        mBuilder.setAutoCancel(true);
        int mNotificationId = 0;
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
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void firstOpening(){
        LoginFragment loginFragment = new LoginFragment();
        fragmentManager = getFragmentManager();
        loginFragment.setCancelable(false);
        loginFragment.show(fragmentManager, "Sample Fragment");
    }
}