package com.example.mainphoneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.mainphoneapp.Model.BEFriend;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivityAllFriends extends AppCompatActivity{

    private GoogleMap m_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allfriends_map);

        Button backFromMapAllFriends = findViewById(R.id.btnBackFromMapFromAllFriends);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.mapAllFriends)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                m_map = googleMap;

                ArrayList<BEFriend> friendsToShowOnMap = (ArrayList<BEFriend>) getIntent().getSerializableExtra("friends");
                for (BEFriend friendToShow : friendsToShowOnMap)
                {
                    LatLng friendPosition = new LatLng(friendToShow.getLocation().getLatitudeE6(),friendToShow.getLocation().getLongitudeE6());
                    MarkerOptions pos = new MarkerOptions().position(friendPosition).title(friendToShow.getName() + " lives here");
                    m_map.addMarker(pos);
                }
            }
        });

        backFromMapAllFriends.setOnClickListener(new View.OnClickListener() {
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
}
