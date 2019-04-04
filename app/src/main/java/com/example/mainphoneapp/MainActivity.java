package com.example.mainphoneapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mainphoneapp.DB.DataAccessFactory;
import com.example.mainphoneapp.Model.BEFriend;
import com.example.mainphoneapp.Model.Friends;
import com.example.mainphoneapp.DB.IDataAccess;

import java.lang.reflect.Array;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainPhoneApp";

    //Friends m_friends;

    ListView listViewFriends;

    List<BEFriend>[] ListOfFriends;

    IDataAccess mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Main Phone App");
       // m_friends = new Friends();

        DataAccessFactory.init(this);
        mData = DataAccessFactory.getInstance();

        listViewFriends = findViewById(R.id.ListViewFriends);



        //ListActivity[] friends;

        //ListOfFriends = mData.getAll();

        fillList();

        //ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, friends);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /*@Override
    public void onListItemClick(ListView parent, View v, int position, long id){
        Intent detailIntent = new Intent(this, DetailActivity.class );
        Log.d(TAG, "We start the detail activity here");
        BEFriend friend = m_friends.getAll().get(position);
        addData(detailIntent, friend);
        startActivity(detailIntent);
        Log.d(TAG, "Detail activity is now running");
    }*/

    private void addData(Intent detailIntent, BEFriend friend){
        detailIntent.putExtra("friend", friend);
    }

    void fillList(){
        ArrayAdapter<BEFriend> arrayAdapter =
                new ArrayAdapter<BEFriend>(this,
                        android.R.layout.simple_list_item_1,
                        mData.getAll() );
                listViewFriends.setAdapter(arrayAdapter);

    }
}
