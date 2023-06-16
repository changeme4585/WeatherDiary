package com.example.may;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class weatherInfo extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);
        EditText ed1= findViewById(R.id.ed1);
        EditText ed2= findViewById(R.id.ed2);
        EditText ed3= findViewById(R.id.ed3);
        EditText ed4= findViewById(R.id.ed4);
        EditText ed5= findViewById(R.id.ed5);
        Intent receivedIntent = getIntent();
        String temp = receivedIntent.getStringExtra("temp");  //괄호안에는 키값
        String pressure = receivedIntent.getStringExtra("pressure");
        String humidty = receivedIntent.getStringExtra("humidty");
        String wind = receivedIntent.getStringExtra("wind");
        String dust = receivedIntent.getStringExtra("dust");

        ed1.setText("현재 기온: "+temp+"도");
        ed2.setText("현재 기압: "+pressure);
        ed3.setText("현재 습도: "+humidty);
        ed4.setText("바람세기 : "+wind);
        ed5.setText("미세먼지 : "+dust);
        Button but = findViewById(R.id.button4);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}