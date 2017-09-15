package com.example.alessio.tesi;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragment {

    ListPreference deleteCourse, deleteLocation, deleteAll, syncUp, logout;
    CheckBoxPreference checkPomodoro;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Fragment frg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        frg = this;

        AppDB db = new AppDB(getActivity());

        //ottengo il riferimento ai ListPreference
        deleteCourse = new ListPreference(getActivity());
        deleteLocation = new ListPreference(getActivity());
        deleteAll = new ListPreference(getActivity());
        syncUp = new ListPreference(getActivity());
        logout = new ListPreference(getActivity());
        checkPomodoro = new CheckBoxPreference(getActivity());

        deleteCourse = (ListPreference)findPreference("delete_course_preference");
        deleteLocation = (ListPreference)findPreference("delete_location_preference");
        deleteAll = (ListPreference)findPreference("delete_all_preference");
        syncUp = (ListPreference)findPreference("syncUp_button");
        logout = (ListPreference)findPreference("logout_button");
        checkPomodoro = (CheckBoxPreference)findPreference("use_pomodoro_mode");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs.edit();

       checkPomodoro.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean pomodoroChecked = checkPomodoro.isChecked();
                // Passo !pomodoroChecked (cioè il suo opposto) perché il suo valore quando chiamo updatePomodoro() è ancora
                // quello vecchio (cioè quello prima di cliccarlo), perché si aggiorna dopo il 'return true' qua sotto.
                updatePomodoro(!pomodoroChecked);
                return true;
            }
        });

        //valori di base del pulsante che permette di eliminare tutti i dati
        CharSequence[] deleteAllEntries = {"No","Si"};
        CharSequence[] deleteAllEntriesValues = {"false","true"};

        //valori base degli altri due: prelevati dal db
        ArrayList<String> entries1 = db.getSubjects();
        entries1.add(0,getActivity().getResources().getString(R.string.none));
        ArrayList<String> entries2 = db.getLocations();
        entries2.add(0,getActivity().getResources().getString(R.string.none));
        CharSequence[] deleteCourseEntries = entries1.toArray(new CharSequence[entries1.size()]);
        CharSequence[] deleteLocationEntries = entries2.toArray(new CharSequence[entries2.size()]);

        //setto questi valori
        deleteAll.setEntries(deleteAllEntries);
        deleteAll.setEntryValues(deleteAllEntriesValues);
        deleteAll.setDefaultValue("false");

        deleteCourse.setEntries(deleteCourseEntries);
        deleteCourse.setEntryValues(deleteCourseEntries);
        deleteCourse.setDefaultValue(getActivity().getResources().getString(R.string.none));

        deleteLocation.setEntries(deleteLocationEntries);
        deleteLocation.setEntryValues(deleteLocationEntries);
        deleteLocation.setDefaultValue(getActivity().getResources().getString(R.string.none));

        syncUp.setEntries(deleteAllEntries);
        syncUp.setEntryValues(deleteAllEntriesValues);
        syncUp.setDefaultValue(getActivity().getResources().getString(R.string.none));

        logout.setEntries(deleteAllEntries);
        logout.setEntryValues(deleteAllEntriesValues);
        logout.setDefaultValue(getActivity().getResources().getString(R.string.none));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater,container,savedInstanceState);

        int color = ContextCompat.getColor(getActivity(), android.R.color.white);
        view.setBackgroundColor(color);

        //listener per vedere se l'utente preme su "si" nel pulsante per eliminare tutto
        deleteAll.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //query al db per eliminare i dati e reimposto il valore standard del pulsante su "no"
                //altrimenti elimina i dati in loop
                deleteAll.setDefaultValue("false");
                if(newValue.toString().equals("true")) {
                    AppDB appDB = new AppDB(getActivity());
                    appDB.deleteAll();
                    //Cancella le sharedPref
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("subj");
                    editor.remove("th");
                    editor.remove("ex");
                    editor.remove("pr");
                    editor.apply();
                    //Chiama il metodo in Main che aggiorna la TextView
                    ((MainActivity)getActivity()).updateTimer(true);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.allDataDeleted), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        //listener per vedere quale corso è stato selezionato per l'eliminazione
        deleteCourse.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                deleteCourse.setDefaultValue(getActivity().getResources().getString(R.string.none));
                String newVal = newValue.toString();
                if(!newVal.equals(getActivity().getResources().getString(R.string.none))) {
                    AppDB db = new AppDB(getActivity());
                    db.deleteCourse(newVal);
                    Toast.makeText(getActivity(),newVal+" "+getActivity().getResources().getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                    if(db.getTrophies()[10].getUnlocked() == 0){
                        db.unlockTrophy(11);
                        Toast t = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.unlockTrophy)+"11",Toast.LENGTH_LONG);
                        t.show();
                    }
                }
                return false;
            }
        });

        //listener per vedere quale posto è stato selezionato per l'eliminazione
        deleteLocation.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                deleteLocation.setDefaultValue(getActivity().getResources().getString(R.string.none));
                String newVal = newValue.toString();
                if(!newVal.equals(getActivity().getResources().getString(R.string.none))) {
                    AppDB db = new AppDB(getActivity());
                    db.deleteLocation(newVal);
                    Toast.makeText(getActivity(),newVal+" "+getActivity().getResources().getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        //listener per vedere se è stato premuto il comando di sincronizzazione (upload)
        syncUp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().equals("true"))
                    prepareRequest();
                return false;
            }
        });

        //listener per vedere se è stato premuto il tasto di logout
        logout.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().equals("true")) {
                    logout.setDefaultValue("false");
                    editor.putBoolean("logged", false);
                    editor.putString("loggedAs", "");
                    editor.commit();
                    //riavvio l'app
                    Intent intent = frg.getActivity().getPackageManager().getLaunchIntentForPackage(frg.getActivity().getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return false;
            }
        });

        return view;
    }

    private void updatePomodoro(boolean pomodoroChecked){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("pomodoro", pomodoroChecked);
        editor.apply();
        ((MainActivity)getActivity()).updateTimer(true);
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    //metodo per preparare la richiesta e il json con i dati in output
    private void prepareRequest() {
        //INVIO DATI AL SERVER
        //invio username e password al server che mi risponde dicendomi se c'è stato un errore o è andata bene
        //vedo se sono connesso
        if(isConnected()) {
            //1) ottengo le sessioni dal db
            AppDB db = new AppDB(getActivity());
            ArrayList<Session> data = db.getAllSessions(prefs.getInt("lastIndex",0));
            try {
                //2) costruisco il json
                JSONObject mainJsonObject = new JSONObject();
                JSONArray sessionData = new JSONArray();
                mainJsonObject.accumulate("user", prefs.getString("loggedAs",""));
                for(Session s: data) {
                    JSONObject otherJsonObject = new JSONObject();
                    otherJsonObject.accumulate("id",s.getId());
                    otherJsonObject.accumulate("duration",s.getDuration());
                    otherJsonObject.accumulate("year",s.getYear());
                    otherJsonObject.accumulate("month",s.getMonth());
                    otherJsonObject.accumulate("day",s.getDay());
                    otherJsonObject.accumulate("th",s.getTheory());
                    otherJsonObject.accumulate("ex",s.getExercise());
                    otherJsonObject.accumulate("pr",s.getProject());
                    otherJsonObject.accumulate("course",s.getCourse_name());
                    sessionData.put(otherJsonObject);
                }
                mainJsonObject.accumulate("data",sessionData);
                String json = mainJsonObject.toString();
                Log.d("json",json);
                //3) preparo la richiesta e la invio
                SettingsFragment.Upload up = new SettingsFragment.Upload(new RequestHandler(json, "syncUp.php"));
                up.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.noConnection),Toast.LENGTH_LONG).show();
    }

    //classe per eseguire in background la richiesta per sincronizzare i dati (upload)
    private class Upload extends AsyncTask<Void,Void,Void> {
        private RequestHandler rh;
        private JSONObject response;

        Upload(RequestHandler rh) {
            this.rh = rh;
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = rh.makeRequest();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            String message;
            try {
                if (response != null)
                    message = response.getString("message");
                else
                    message = "fatal_error";
                //3) interpretazione della risposta
                //se risposta positiva
                if(message.equals("ok"))
                    editor.putInt("lastIndex",response.getInt("index")).commit();
                else if(message.equals("alreadySynced"))
                    Toast.makeText(frg.getActivity(),frg.getActivity().getResources().getString(R.string.alreadySynced),Toast.LENGTH_LONG).show();
                else if(message.equals("error")) {
                    editor.putInt("lastIndex",response.getInt("index")).commit();
                    Toast.makeText(frg.getActivity(),frg.getActivity().getResources().getString(R.string.unknownErr),Toast.LENGTH_LONG).show();
                }
                else if(message.equals("fatal_error"))
                    Toast.makeText(frg.getActivity(),frg.getActivity().getResources().getString(R.string.unknownErr),Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
