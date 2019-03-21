package com.example.mainphoneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    private static String LOGTAG = "MAP_LOG";
    private GoogleMap m_map;
    Spinner m_zoomLevelView;


    private final LatLng EASV = new LatLng(55.488230, 8.446936);
    MarkerOptions easv_marker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);


                //Button back from map
                Button btnBackFromMap = findViewById(R.id.btnBackFromMap);


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
        });
    }
