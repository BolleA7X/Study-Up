package com.example.alessio.tesi;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alessio.tesi.Database.AppDB;
import com.example.alessio.tesi.Database.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private EditText newLocation;
    private Button okButton;
    private Button revertButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        okButton =(Button) rootView.findViewById(R.id.okButton);
        revertButton =(Button) rootView.findViewById(R.id.revertButton);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        newLocation = (EditText)rootView.findViewById(R.id.newLocation);

        //Listener sui bottoni aggiungi e annulla
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //prende le coordinate e le mette nel DB
                double[] position = getCoordinates();
                Location location = new Location(newLocation.getText().toString(),position[0],position[1]);
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
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }
    //Non funziona ancora
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
                    String text1= Double.toString(position[0]);
                    String text2= Double.toString(position[1]);

                    Toast toast = Toast.makeText(getActivity(),text1+ text2, Toast.LENGTH_SHORT);
                    toast.show();
                }

                //metodi belli.
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