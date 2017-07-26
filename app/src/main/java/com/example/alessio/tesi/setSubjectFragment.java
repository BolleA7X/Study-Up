package com.example.alessio.tesi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Course;

//classe che crea il dialog ed è chiamata in SessionSettingsActivity in openDialog()
public class setSubjectFragment extends DialogFragment  {

    private EditText subject;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view = inflater.inflate(R.layout.set_subject_fragment, null));
        subject = (EditText)view.findViewById(R.id.newSub);
        builder.setMessage(R.string.add_subject_label)
               .setPositiveButton(R.string.add_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Course course = new Course(subject.getText().toString());
                        AppDB db = new AppDB(getActivity());
                        db.insertSubject(course);
                    }
                })
               .setNegativeButton(R.string.abort_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // bottone annulla
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}