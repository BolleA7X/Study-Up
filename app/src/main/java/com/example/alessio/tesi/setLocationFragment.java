package com.example.alessio.tesi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Location;

//classe che crea il dialog ed è chiamata in SessionSettingsActivity in openDialog()
public class setLocationFragment extends DialogFragment  {

    private EditText newLocation;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view = inflater.inflate(R.layout.set_location_fragment, null));
        newLocation = (EditText)view.findViewById(R.id.newLoc);
        builder.setMessage(R.string.add_location_label)
                .setPositiveButton(R.string.add_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //ottengo le coordinate con getCoordinates() (vedi sotto) e le inserisco nel db
                        double[] position = getCoordinates();
                        Location location = new Location(newLocation.getText().toString(),position[0],position[1]);
                        AppDB db = new AppDB(getActivity());
                        db.insertLocation(location);
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

    //questo metodo serve per ottenere le coordinate tramite gps del dispositivo
    //non so se funziona, non l'ho ancora testato
    private double[] getCoordinates() {
        final double[] position = new double[2];
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        //se il gps non è attivo apro la schermata delle impostazioni gps del dispositivo
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        else {
            //altrimenti prelevo le coordinate e le inserisco in un array temporaneo che ritorna al chiamante
            LocationListener ll = new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    position[0] = location.getLatitude();
                    position[1] = location.getLongitude();
                }

                //metodi belli
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
        }
        return position;
    }
}