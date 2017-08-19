package com.example.alessio.tesi;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapViewFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private EditText newLocation;
    private Button okButton;
    private Button revertButton;

    final double[] position = new double[2];
    //TODO aggiungere controllo se è acceso il gps

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        okButton =(Button) rootView.findViewById(R.id.okButton);
        revertButton =(Button) rootView.findViewById(R.id.revertButton);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        newLocation = (EditText)rootView.findViewById(R.id.newLocation);

        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        //se il gps non è attivo apro la schermata delle impostazioni gps del dispositivo
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showGPSDisabledAlertToUser();
        }

        //Listener sui bottoni aggiungi e annulla
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //prende le coordinate e le mette nel DB
                LatLng latLng=googleMap.getCameraPosition().target;
                double latidude = latLng.latitude;
                double longitude = latLng.longitude;

                Location location = new Location(newLocation.getText().toString(),latidude,longitude);
                AppDB db = new AppDB(getActivity());
                db.insertLocation(location);
                ((SessionSettingsActivity)getActivity()).updateSpinner();
                getActivity().getFragmentManager().popBackStack();
            }
        });
        revertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //torna indietro
                getActivity().getFragmentManager().popBackStack();
            }
        });

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng opPia = new LatLng(44.402880, 8.958766);


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(opPia).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        return rootView;
    }
    //Dailog per andare nelle impostazioni se il gps non è attivo
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(R.string.gps_is_off)
                .setCancelable(false)
                .setPositiveButton(R.string.go_to_sett,
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}