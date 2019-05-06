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

import com.example.mainphoneapp.DB.DataAccessFactoryFirestore;
import com.example.mainphoneapp.DB.DataAccessFactorySql;
import com.example.mainphoneapp.Model.BEFriend;
import com.example.mainphoneapp.DB.IDataAccess;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainPhoneApp";

    ListView listViewFriends; // listview to show friends

    IDataAccess mData; // make instance of the IDataAccess interface with sql factory to use in this class
    IDataAccess mDataFirestore; // make instance of the IDataAccess interface with firestore factory to use in this class
    private Object Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Main Phone App");
        setContentView(R.layout.activity_main);

        DataAccessFactorySql.init(this);
        mData = DataAccessFactorySql.getInstance();

        DataAccessFactoryFirestore.init(this);
        mDataFirestore = DataAccessFactoryFirestore.getInstance();

        listViewFriends = findViewById(R.id.ListViewFriends);

        checkPermissions();
        fillList();

//        listViewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent x = new Intent(MainActivity.this, DetailActivity.class);
//
//                x.putExtra("id", mData.getAll().get(position).getId());
//                startActivity(x);
//            }
//        });
    }


    // Creates the menu bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Fills the list with db items, with the getAll();
    void fillList(){
        ArrayAdapter<BEFriend> arrayAdapter =
                new ArrayAdapter<BEFriend>(this,
                        android.R.layout.simple_list_item_1,
                        mDataFirestore.getAll() );
        Log.d(TAG, "count of friend objects from FireStoreGetAll sent : ============== " + mDataFirestore.getAll().size());
                listViewFriends.setAdapter(arrayAdapter);
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
        }
        return true;
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
