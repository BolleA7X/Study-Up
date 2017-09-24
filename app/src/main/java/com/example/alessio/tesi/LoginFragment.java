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
import android.os.AsyncTask;
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

import com.example.alessio.tesi.Database.AppDB;

import org.json.JSONObject;

public class LoginFragment  extends DialogFragment {
    private EditText logText,logPwd;
    private Button loginButton,registrationButton;
    private Fragment frg;
    String username;
    SharedPreferences prefs;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        frg = this;
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
                prepareRequest("registration.php");
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareRequest("login.php");
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

    //metodo unico per preparare la richiesta e il json con i dati in output
    private void prepareRequest(String filename) {
        username = logText.getText().toString();
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
                    //2) preparo la richiesta e la invio
                    if(filename.equals("registration.php")) {
                        Registration reg = new Registration(new RequestHandler(json, filename));
                        reg.execute();
                    }
                    else if(filename.equals("login.php")) {
                        Login log = new Login(new RequestHandler(json, filename));
                        log.execute();
                    }
                    //per sicurezza
                    else
                        System.exit(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.noConnection),Toast.LENGTH_LONG).show();
    }

    //classe per eseguire in background la richiesta per registrarsi
    private class Registration extends AsyncTask<Void,Void,Void> {
        private RequestHandler rh;
        private JSONObject response;

        Registration(RequestHandler rh) {
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
            AppDB db = new AppDB(frg.getActivity());

            try {
                if (response != null)
                    message = response.getString("message");
                else
                    message = "error";
                //3) interpretazione della risposta
                //se risposta positiva
                if (message.equals("ok")) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(frg.getActivity());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("logged", true);
                    editor.putString("loggedAs", username);
                    editor.commit();
                    db.insertUser(username);
                    getActivity().getFragmentManager().beginTransaction().remove(frg).commit();
                }
                //se username già preso
                else if (message.equals("taken"))
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.usrnameTaken), Toast.LENGTH_LONG).show();
                //se la query fallisce per motivi misteriosi
                else if (message.equals("error"))
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.unknownErr), Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    //classe per eseguire in background la richiesta per loggarsi
    private class Login extends AsyncTask<Void,Void,Void> {
        private RequestHandler rh;
        private JSONObject response;

        Login(RequestHandler rh) {
            this.rh = rh;
        }

        @Override
        protected Void doInBackground(Void... params) {
            response = rh.makeRequest();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            AppDB db = new AppDB(frg.getActivity());
            String message;
            try {
                if (response != null)
                    message = response.getString("message");
                else
                    message = "error";
                //3) interpretazione della risposta
                //se risposta positiva
                if (message.equals("ok")) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(frg.getActivity());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("logged", true).commit();
                    editor.putString("loggedAs", username).commit();
                    if(!db.searchUser(username)) {
                        db.insertUser(username);
                        Log.d("nuovo utente","inserito in login");
                    }
                    getActivity().getFragmentManager().beginTransaction().remove(frg).commit();
                }
                //se login fallito
                else if (message.equals("wrong"))
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.loginFailed), Toast.LENGTH_LONG).show();
                //se la query fallisce per motivi misteriosi
                else if (message.equals("error"))
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.unknownErr), Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
