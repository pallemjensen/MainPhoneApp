package com.example.mainphoneapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mainphoneapp.Model.BEFriend;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainPhoneApp";
    ListView listViewFriends; // listview to show friends
    private FirebaseFirestore fireDb = FirebaseFirestore.getInstance();
    private CollectionReference friendsColRef = fireDb.collection("Friends");
    private List<BEFriend> listOfFriends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Main Phone App");
        setContentView(R.layout.activity_main);

        listViewFriends = findViewById(R.id.ListViewFriends);
        checkPermissions();

        fillList();

        listViewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent x = new Intent(MainActivity.this, DetailActivity.class);

                x.putExtra("id", listOfFriends.get(position).getId());
                Log.d(TAG,  "" + listOfFriends.get(position).getId());
                startActivity(x);
            }
        });
    }

    // Creates the menu bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Fills the listFromGetAll with db items, with the getAll();

    public void fillList(){
        friendsColRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            BEFriend beFriend = documentSnapshot.toObject(BEFriend.class);
                            String friendId = documentSnapshot.getId();
                            beFriend.setId(friendId);
                            listOfFriends.add(beFriend);
                        }
                        ArrayAdapter<BEFriend> arrayAdapter =
                                new ArrayAdapter<>(getApplicationContext(),
                                        android.R.layout.simple_list_item_1,
                                        listOfFriends
                                );
                        arrayAdapter.notifyDataSetChanged();
                        listViewFriends.setAdapter(arrayAdapter);
                    }
                });
    }

    //Go to detail, create empty friend template
    void detailAddFriend() {
            Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);

            startActivity(detailIntent);
    }


    //Method to see which item that has been selected in the menu bar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.newFriendMain:
                Toast.makeText(this, "Creating new friend....", Toast.LENGTH_SHORT)
                        .show();
                    detailAddFriend();
                break;
            case R.id.showFriendsOnMap:
                Toast.makeText(this, "Plotting friends on map....", Toast.LENGTH_SHORT)
                .show();
                showFriendsOnMap();
                break;
            case R.id.showFriendsAsList:
                Toast.makeText(this, "Showing friends on list....", Toast.LENGTH_SHORT)
                        .show();
                showFriendsAsList();
                break;
        }
        return true;
    }

    private void showFriendsAsList() {
        //fillList();
    }

    private void showFriendsOnMap() {
        Intent friendsToMap = new Intent(MainActivity.this, MapActivityAllFriends.class);

        friendsToMap.putExtra("friends", (Serializable) listOfFriends);
        startActivity(friendsToMap);
    }

    // Asks the user for permission to the different mobile applications
    private void checkPermissions() {
        ArrayList<String> permissions = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CAMERA);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CALL_PHONE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.SEND_SMS);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissions.size() > 0)
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
    }
}
