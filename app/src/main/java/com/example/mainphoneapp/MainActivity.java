package com.example.mainphoneapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mainphoneapp.DB.DataAccessFactory;
import com.example.mainphoneapp.Model.BEFriend;
import com.example.mainphoneapp.DB.IDataAccess;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainPhoneApp";

    ListView listViewFriends; // listview to show friends

    IDataAccess mData; // make instance of the IDataAccess interface to use in this class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Main Phone App");
        setContentView(R.layout.activity_main);

        DataAccessFactory.init(this);
        mData = DataAccessFactory.getInstance();

        listViewFriends = findViewById(R.id.ListViewFriends);


        fillList();

        listViewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent x = new Intent(MainActivity.this, DetailActivity.class);

                x.putExtra("id", mData.getAll().get(position).getId());
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

    //Fills the list with db items, with the getAll();
    void fillList(){
        ArrayAdapter<BEFriend> arrayAdapter =
                new ArrayAdapter<BEFriend>(this,
                        android.R.layout.simple_list_item_1,
                        mData.getAll() );
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
}
