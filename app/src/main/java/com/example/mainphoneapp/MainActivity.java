package com.example.mainphoneapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mainphoneapp.Model.BEFriend;
import com.example.mainphoneapp.Model.DataAccessFactory;
import com.example.mainphoneapp.Model.Friends;
import com.example.mainphoneapp.Model.IDataAccess;

public class MainActivity extends ListActivity {

    public static String TAG = "MainPhoneApp";

    Friends m_friends;

    IDataAccess mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Main Phone App");
        m_friends = new Friends();

        DataAccessFactory.init(this);
        mData = DataAccessFactory.getInstance();

        String[] friends;

        friends = m_friends.getNames();

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, friends);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id){
        Intent detailIntent = new Intent(this, DetailActivity.class );
        Log.d(TAG, "We start the detail activity here");
        BEFriend friend = m_friends.getAll().get(position);
        addData(detailIntent, friend);
        startActivity(detailIntent);
        Log.d(TAG, "Detail activity is now running");
    }

    private void addData(Intent detailIntent, BEFriend friend){
        detailIntent.putExtra("friend", friend);
    }
}
