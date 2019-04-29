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
import com.example.mainphoneapp.Model.BEFriend;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.mainphoneapp.helper.distance;

public class MapActivity extends AppCompatActivity {

    private static String LOGTAG = "MAP_LOG";

    private final LatLng EASV = new LatLng(55.488230, 8.446936);

    MarkerOptions easv_marker;
    MarkerOptions home;

    private GoogleMap m_map;

    Spinner m_zoomLevelView;
    BEFriend friend;
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
                friend = (BEFriend) getIntent().getSerializableExtra("friend");

                final LatLng homeMarker = new LatLng(friend.getLat(),friend.getLon());
                home = new MarkerOptions().position(homeMarker).title(friend.getName() + " lives here");
                easv_marker = new MarkerOptions().position(EASV).title("EASV is HERE!");

                m_map.addMarker(easv_marker);
                m_map.addMarker(home);


                m_zoomLevelView = findViewById(R.id.spinnerZoomLevel);

                String distance = String.valueOf(distance(55.488230,friend.getLat(),8.446936,friend.getLon(),0,0));

                m_txtDistance.setText("Distance to EASV is " + distance + " meters.");
                setupZoomLevel();
            }
        });

        btnBackFromMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickBackToMain();
            }
        });
    }

    // Method to send the user back to the main activity
    private void onClickBackToMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    // method to zoom to the level of zoom selected
    public void onClickZoom(View v) {
        int level = Integer.parseInt(m_zoomLevelView.getSelectedItem().toString());
        CameraUpdate viewPoint = CameraUpdateFactory.newLatLngZoom(new LatLng(friend.getLat(),friend.getLon()), level);
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
}
