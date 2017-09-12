package com.example.alessio.tesi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Course;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

public class LoginFragment  extends DialogFragment {
    private EditText LogText;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view = inflater.inflate(R.layout.login_fragment, null));
        LogText = (EditText)view.findViewById(R.id.newLog);
        builder.setMessage(R.string.login_textview_title)
                .setPositiveButton(R.string.add_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String subj = LogText.getText().toString();
                        /*TODO finire recaptcha api*/
                        /*
                        SafetyNet.getClient(getActivity()).verifyWithRecaptcha("6LfAaTAUAAAAAEY6tcbxGIgsi7lcs_oio70_uJ0w")
                                .addOnSuccessListener(this, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                        if (!result.getTokenResult().isEmpty()) {
                                            handleSiteVerify(result.getTokenResult());
                                        }
                                    }
                                });
                                .addOnFailureListener(this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e instanceof ApiException) {
                                        ApiException apiException = (ApiException) e;
                                        Log.d(TAG, "Error message: " +
                                                CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                                    } else {
                                        Log.d(TAG, "Unknown type of error: " + e.getMessage());
                                    }
                                }
                        });
                       */

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

}
