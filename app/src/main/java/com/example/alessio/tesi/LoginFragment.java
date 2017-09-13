package com.example.alessio.tesi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Course;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.JsonParser;

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;
import static java.lang.System.in;

public class LoginFragment  extends DialogFragment {
    private EditText logText,logPwd;
    private Button loginButton,registrationButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Fragment frg = this;
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater.
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view = inflater.inflate(R.layout.login_fragment, null));

        logText = (EditText)view.findViewById(R.id.newLog);
        logPwd = (EditText)view.findViewById(R.id.newPass);
        registrationButton = (Button)view.findViewById(R.id.registrationButton);
        loginButton = (Button)view.findViewById(R.id.loginButton);

        builder.setMessage(R.string.login_textview_title);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = logText.getText().toString();
                String password = logPwd.getText().toString();
                //INVIO DATI AL SERVER
                //invio username e password al server che mi risponde dicendomi se c'è stato un errore o è andata bene
                //vedo se sono connesso
                if(isConnected()) {
                    if(!username.equals("") && !password.equals("")) {
                        try {
                            //1) costruisco il json
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.accumulate("username", username);
                            jsonObject.accumulate("password", password);
                            String json = jsonObject.toString();
                            Log.d("JSON", json);
                            //2) preparo la richiesta
                            RequestHandler rh = new RequestHandler(json,"registration.php");
                            JsonReader reader = rh.getResponse();
                            String name;
                            if(reader != null)
                                name = reader.nextName();
                            else
                                name = "error";
                            if (name.equals("message")) {
                                String message = reader.nextString();
                                //se risposta positiva
                                if (message.equals("ok")) {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("logged", true);
                                    editor.putString("loggedAs", username);
                                    getActivity().getFragmentManager().beginTransaction().remove(frg).commit();
                                }
                                //se username già preso
                                else if (message.equals("taken"))
                                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.usrnameTaken), Toast.LENGTH_SHORT).show();
                                    //se la query fallisce per motivi misteriosi
                                else if (message.equals("error"))
                                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.unknownErr), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.noConnection),Toast.LENGTH_SHORT).show();
            }
        });

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

        setRetainInstance(true);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
