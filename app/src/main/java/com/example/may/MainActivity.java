package com.example.may;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sqlDB;

    todayWeather weather;
    Cursor cursor;
    Cursor cursor1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        dustThread threadDust = new dustThread();
//        threadDust.start();

        weather = new todayWeather(this.getApplicationContext());
        sqlDB = weather.getWritableDatabase();
       //weather.onUpgrade(sqlDB,1,1); //테이블 초기화 코드
        weather.onCreate(sqlDB);

        cursor = sqlDB.rawQuery("select * from current_Info;", null);
        //System.out.println("row 개수"+cursor.getCount());




//        while(cursor.moveToNext()){
//            //System.out.println("DB시간"+cursor.getString(0));
//            //System.out.println("풍속"+cursor.getString(5));
//            //System.out.println("풍향"+cursor.getString(6));
//        }


        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = dateFormat.format(date);
        String spliDate[] = getTime.split("-");
        String currentDate="" ;
        for(int i = 0; i<spliDate.length;i++){
            currentDate+=spliDate[i];  //현재 시간
        }
        String recentDate = "";
        cursor1 = sqlDB.rawQuery("SELECT * FROM current_Info ORDER BY todayDate DESC LIMIT 1", null);
        if(cursor1.moveToLast()) { ///가장 최근값 (오늘 DB값을 넣음)
            recentDate= cursor1.getString(0);
        }

        if(recentDate.equals(currentDate)) {
            System.out.println("이미 있음");
            System.out.println("미세먼지"+cursor1.getString(8));
        }
        else {
            //DB에 오늘 호출한 데이터가 있으면 새로운 값을 insert 하지 않음
            System.out.println("DB 갱신");
            BackThread tread = new BackThread(this.getApplicationContext());
            tread.start();
            //System.out.println("미세먼지"+cursor1.getString(8));
        }
        Button but = findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),weatherInfo.class);
                intent.putExtra("temp", cursor1.getString(1));
                intent.putExtra("pressure", cursor1.getString(4));
                intent.putExtra("humidty", cursor1.getString(5));
                intent.putExtra("wind", cursor1.getString(6));
                intent.putExtra("dust", cursor1.getString(8));
                startActivity(intent);
            }
        });
    }
}