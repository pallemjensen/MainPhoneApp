package com.example.mainphoneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button btnBackFromMap = findViewById(R.id.btnBackFromMap);


        btnBackFromMap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                onClickBackToDetail();
            }
        });

    }


    private void onClickBackToDetail() {
        Intent mainIntent = new Intent(this, MainActivity.class );
        startActivity(mainIntent);
    }
}
