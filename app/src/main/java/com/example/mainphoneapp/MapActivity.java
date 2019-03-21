package com.example.mainphoneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    private static String LOGTAG = "MAP_LOG";

    private final LatLng EASV = new LatLng(55.488230, 8.446936);
    private final LatLng PALLE_HOME = new LatLng(55.511104, 8.410175);

    MarkerOptions easv_marker;
    MarkerOptions  palleHome_marker;

    private GoogleMap m_map;

    Spinner m_zoomLevelView;

    TextView m_txtDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button btnBackFromMap = findViewById(R.id.btnBackFromMap);
        m_txtDistance = this.findViewById(R.id.txtDistance);

        Log.d(LOGTAG, "getting the map async");
        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(LOGTAG, "The map is ready - adding markers");
                m_map = googleMap;
                m_map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (marker.getPosition().equals(easv_marker.getPosition()))
                            Toast.makeText(MapActivity.this, "EASV hit!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MapActivity.this, "EASV NOT hit!", Toast.LENGTH_LONG).show();
                        return false;
                    }
                });

                palleHome_marker = new MarkerOptions().position(PALLE_HOME).title("Palle lives here");
                easv_marker = new MarkerOptions().position(EASV).title("EASV is HERE!");

                m_map.addMarker(easv_marker);
                m_map.addMarker(palleHome_marker);


                m_zoomLevelView = findViewById(R.id.spinnerZoomLevel);

                String distance = String.valueOf(distance(55.488230,55.511104,8.446936,8.410175,0,0));

                m_txtDistance.setText("Distance is " + distance + " meters.");
                setupZoomLevel();
            }
        });

        btnBackFromMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickBackToDetail();
            }
        });
    }

    private void onClickBackToDetail() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }


    public void onClickZoom(View v) {
        int level = Integer.parseInt(m_zoomLevelView.getSelectedItem().toString());
        CameraUpdate viewPoint = CameraUpdateFactory.newLatLngZoom(PALLE_HOME, level);
        // zoomlevel 0..21, where 0 is the world and 21 is single street
        Log.d(LOGTAG, "Will zoom to easv to level " + level);
        m_map.animateCamera(viewPoint);

    }

    void setupZoomLevel() {

        // Create an ArrayAdapter using the string array and a default m_zoomLevelView layout
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.zoomlevels,
                        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        m_zoomLevelView.setAdapter(adapter);
    }


        private String distance(double lat1, double lat2, double lon1,
                                double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        String temp = String.valueOf(Math.sqrt(distance));
        String[] parts = temp.split("[.]");
        String distanceRounded = parts[0];

        return distanceRounded;
    }

}
