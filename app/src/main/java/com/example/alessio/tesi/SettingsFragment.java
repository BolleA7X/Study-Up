package com.example.alessio.tesi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.alessio.tesi.Database.AppDB;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragment {

    ListPreference deleteCourse, deleteLocation, deleteAll;
    CheckBoxPreference checkPomodoro;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        //ottengo il riferimento ai ListPreference
        deleteCourse = new ListPreference(getActivity());
        deleteLocation = new ListPreference(getActivity());
        deleteAll = new ListPreference(getActivity());
        checkPomodoro = new CheckBoxPreference(getActivity());

        deleteCourse = (ListPreference)findPreference("delete_course_preference");
        deleteLocation = (ListPreference)findPreference("delete_location_preference");
        deleteAll = (ListPreference)findPreference("delete_all_preference");
        checkPomodoro = (CheckBoxPreference)findPreference("use_pomodoro_mode");

        checkPomodoro.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean bo =checkPomodoro.isChecked();
                String text = Boolean.toString(bo);
                Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
                toast.show();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("pomodoro", bo);
                editor.apply();
                ((MainActivity)getActivity()).updateSubjText();
                return true;
            }
        });

        //valori di base del pulsante che permette di eliminare tutti i dati
        CharSequence[] deleteAllEntries = {"No","Si"};
        CharSequence[] deleteAllEntriesValues = {"false","true"};

        //setto questi valori
        deleteAll.setEntries(deleteAllEntries);
        deleteAll.setEntryValues(deleteAllEntriesValues);
    }
    /*  if(checkPomodoro.isChecked()){
                    ((MainActivity)getActivity()).updateSubjText();
                }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater,container,savedInstanceState);
        //ho usato un metodo deprecato ma non so quale altro usare-Ale
        //Ho trovato questo rigiro che pare funzionare - Luca
        int color = ContextCompat.getColor(getActivity(), android.R.color.white);
        view.setBackgroundColor(color);

        //listener per vedere se l'utente preme su "si" nel pulsante per eliminare tutto
        deleteAll.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //query al db per eliminare i dati e reimposto il valore standard del pulsante su "no"
                //altrimenti elimina i dati in loop
                AppDB appDB = new AppDB(getActivity());
                appDB.deleteAll();
                //Cancella le sharedPref
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("subj");
                editor.remove("th");
                editor.remove("ex");
                editor.remove("pr");
                editor.commit();
                //Chiama il metodo in Main che aggiorna la TextView
                ((MainActivity)getActivity()).updateSubjText();

                deleteAll.setDefaultValue("false");
                Toast.makeText(getActivity(),"Tutti i dati sono stati eliminati",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return view;
    }
}
