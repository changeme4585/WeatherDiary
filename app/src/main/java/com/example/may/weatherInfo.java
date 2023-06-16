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
        EditText ed6= findViewById(R.id.ed6);

        Intent receivedIntent = getIntent();
        String temp = receivedIntent.getStringExtra("temp");
        String pressure = receivedIntent.getStringExtra("pressure");
        String humidty = receivedIntent.getStringExtra("humidty");
        String wind = receivedIntent.getStringExtra("wind");
        int direction =  Integer.valueOf(receivedIntent.getStringExtra("direction")); //풍향
        String dust = receivedIntent.getStringExtra("dust");
        int ans = 2147483537;
        int windIdx = 0;
        int []wind_degree = {0,45,90,135,180,225,270,315,360} ; //abs 함수 사용해야함
        String []wind_direction= {"북쪽","북동쪽","동쪽","남동쪽","남쪽","남서쪽","서쪽","북서쪽","북쪽"};
        for(int i =0; i<9;i++){ //근사값 알고리즘을 통해 바람의 방향을 구함
            int tmp =Math.abs(direction-wind_degree[i]);
            if (ans > tmp ){ // 편차가 더 작으면 갱신
                ans=  tmp;
                windIdx= i; // 인덱스 저장
            }
        }
        System.out.println("바람방향"+direction);
        ed1.setText("현재 기온: "+temp+"도");
        ed2.setText("현재 기압: "+pressure);
        ed3.setText("현재 습도: "+humidty);
        ed4.setText("바람세기 : "+wind);
        ed6.setText("풍향    : "+wind_direction[windIdx]);
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