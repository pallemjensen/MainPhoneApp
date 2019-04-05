package com.example.mainphoneapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    Button deleteButton;

    ListView listViewFriends;

    IDataAccess mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Main Phone App");
        setContentView(R.layout.activity_main);

        DataAccessFactory.init(this);
        mData = DataAccessFactory.getInstance();

        listViewFriends = findViewById(R.id.ListViewFriends);
        deleteButton = findViewById(R.id.btnDelete);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    void fillList(){
        ArrayAdapter<BEFriend> arrayAdapter =
                new ArrayAdapter<BEFriend>(this,
                        android.R.layout.simple_list_item_1,
                        mData.getAll() );
                listViewFriends.setAdapter(arrayAdapter);

    }

    void onClickDeleteAll() {
        mData.deleteAll();
        fillList();
    }
}
