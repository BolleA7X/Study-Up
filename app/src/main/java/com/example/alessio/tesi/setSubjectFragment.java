package com.example.alessio.tesi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Course;

public class setSubjectFragment extends DialogFragment  {

    private EditText subject;
    private SharedPreferences prefs;

    //override per far apparire subito la nuova materia/location nello spinner
    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        ((SessionSettingsActivity)getActivity()).updateSpinner();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view = inflater.inflate(R.layout.set_subject_fragment, null));
        subject = (EditText)view.findViewById(R.id.newSub);
        builder.setMessage(R.string.add_subject_label)
               .setPositiveButton(R.string.add_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {String subj = subject.getText().toString();
                        if(!subj.isEmpty()){
                            Course course = new Course(subject.getText().toString());
                            AppDB db = new AppDB(getActivity());
                            db.insertSubject(course,prefs.getString("loggedAs",""));
                            // SBLOCCO TROFEO 10
                            if(db.getTrophies()[9].getUnlocked() == 0){
                                db.unlockTrophy(10);
                                Toast t = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.unlockTrophy)+ " " + getResources().getString(R.string.trophy_title_10),Toast.LENGTH_LONG);
                                t.show();
                            }
                        }else{
                            Toast toast = Toast.makeText(getActivity(), R.string.empty_insert_error_toast, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                })
               .setNegativeButton(R.string.abort_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        setRetainInstance(true);
        return builder.create();
    }

}