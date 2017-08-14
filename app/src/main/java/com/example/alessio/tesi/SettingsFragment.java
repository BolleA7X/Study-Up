package com.example.alessio.tesi;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alessio.tesi.Database.AppDB;

public class SettingsFragment extends PreferenceFragment {

    ListPreference deleteCourse, deleteLocation, deleteAll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        //ottengo il riferimento ai ListPreference
        deleteCourse = new ListPreference(getActivity());
        deleteLocation = new ListPreference(getActivity());
        deleteAll = new ListPreference(getActivity());

        deleteCourse = (ListPreference)findPreference("delete_course_preference");
        deleteLocation = (ListPreference)findPreference("delete_location_preference");
        deleteAll = (ListPreference)findPreference("delete_all_preference");

        //valori di base del pulsante che permette di eliminare tutti i dati
        CharSequence[] deleteAllEntries = {"No","Si"};
        CharSequence[] deleteAllEntriesValues = {"false","true"};

        //setto questi valori
        deleteAll.setEntries(deleteAllEntries);
        deleteAll.setEntryValues(deleteAllEntriesValues);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater,container,savedInstanceState);
        //ho usato un metodo deprecato ma non so quale altro usare
        view.setBackgroundColor(getResources().getColor(android.R.color.white));

        //listener per vedere se l'utente preme su "si" nel pulsante per eliminare tutto
        deleteAll.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //query al db per eliminare i dati e reimposto il valore standard del pulsante su "no"
                //altrimenti elimina i dati in loop
                AppDB appDB = new AppDB(getActivity());
                appDB.deleteAll();
                deleteAll.setDefaultValue("false");
                Toast.makeText(getActivity(),"Tutti i dati sono stati eliminati",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return view;
    }
}
