package com.example.mainphoneapp;

import java.io.Serializable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;


import com.example.mainphoneapp.Model.BEFriend;

public class DetailActivity extends AppCompatActivity {
     String TAG = MainActivity.TAG;

     EditText etName;
     EditText etPhone;
     CheckBox cbFav;

     @Override
        protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_detail);
         Log.d(TAG, " Detail activity is running");

         etName = findViewById(R.id.etName);
         etPhone = findViewById(R.id.etPhone);
         cbFav = findViewById(R.id.cbFavorite);

         setGui();
     }

     private void setGui(){
         BEFriend friend = (BEFriend) getIntent().getSerializableExtra("friend");
         etName.setText(friend.getName());
         etPhone.setText(friend.getPhone());
         cbFav.setChecked(friend.isFavorite());
     }
}