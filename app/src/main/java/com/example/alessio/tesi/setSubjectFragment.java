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

//classe che crea il dialog ed è chiamata in SessionSettingsActivity in openDialog()
public class setSubjectFragment extends DialogFragment  {

    private EditText subject;
    //override per far apparire subito la nuova materia/location nello spinner
    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        ((SessionSettingsActivity)getActivity()).updateSpinner();
        // PROVA: setRetainInstance(false);
    }

    //TODO terminare errore setRetainInstance(true) rotazione modalità landscape
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view = inflater.inflate(R.layout.set_subject_fragment, null));
        subject = (EditText)view.findViewById(R.id.newSub);
        builder.setMessage(R.string.add_subject_label)
               .setPositiveButton(R.string.add_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String subj = subject.getText().toString();
                        if(subj!= null && !subj.isEmpty()){
                            Course course = new Course(subject.getText().toString());
                            AppDB db = new AppDB(getActivity());
                            db.insertSubject(course);
                            // SBLOCCO TROFEO 10
                            if(db.getTrophies()[9].getUnlocked() == 0){
                                db.unlockTrophy(10);
                                Toast t = Toast.makeText(getActivity(), "CONGRATULATIONS: TROPHY 10 UNLOCKED.",Toast.LENGTH_LONG);
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
                        // PROVA: setRetainInstance(false);
                    }
                });
        setRetainInstance(true);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        // PROVA: else if(getDialog() != null && getRetainInstance() == false)getDialog().dismiss();
        super.onDestroyView();
    }

}