package com.example.alessio.tesi;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.alessio.tesi.Database.AppDB;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MyLocationsFragment extends Fragment{

    MapView mMapView;
    private GoogleMap googleMap;
    private ImageButton closeButton;
    private Marker mark;
    private double[] coord;
    private String LocName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_locations_map, container, false);
        //sessionSettingsLayout.getForeground().setAlpha(220);
        LocName = getArguments().getString("LocName");
        closeButton =(ImageButton) rootView.findViewById(R.id.closeButton);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        /*database*/
        AppDB appDB = new AppDB(getActivity());
        coord = appDB.getCoordinates(LocName);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

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
                //googleMap.setMyLocationEnabled(true);
                // For dropping a marker at a point on the Map
                //LatLng opPia = new LatLng(44.402880, 8.958766);
                LatLng myLoc = new LatLng(coord[0], coord[1]);

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLoc).zoom(12).build();
                mark = googleMap.addMarker(new MarkerOptions().position(myLoc)
                        .title(LocName));
                mark.setTag(0);
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        return rootView;
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
