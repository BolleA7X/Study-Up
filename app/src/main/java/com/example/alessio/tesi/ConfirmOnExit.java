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
import android.widget.CheckBox;

public class ConfirmOnExit  extends DialogFragment {
    private CheckBox ch;
    private SharedPreferences prefs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;
        builder.setView(view = inflater.inflate(R.layout.confirm_exit_dialog, null));

        ch = (CheckBox)view.findViewById(R.id.checkExit);

        builder.setMessage(R.string.confirm_message)
                .setTitle(R.string.confirm_title)
                .setPositiveButton(R.string.go_on_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity)getActivity()).stop();
                        if(ch.isChecked()){
                            //Shared che non farà più aprire il dialog
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("ConfirmOnExit", false);
                            editor.apply();
                        }
                    }
                })
                .setNegativeButton(R.string.go_on_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        setRetainInstance(true);
        return builder.create();
    }
}
