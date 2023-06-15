package com.example.may;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sqlDB;

    todayWeather weather;
    Cursor cursor;
    Cursor cursor1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BackThread tread = new BackThread(this.getApplicationContext());
        tread.start();
        dustThread threadDust = new dustThread();
        threadDust.start();
        weather = new todayWeather(this.getApplicationContext());
        sqlDB = weather.getWritableDatabase();
        weather.onUpgrade(sqlDB,1,1); //테이블 초기화 코드
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
        String sumDate="" ;
        for(int i = 0; i<spliDate.length;i++){
            sumDate+=spliDate[i];
        }
        String query = "SELECT * FROM current_Info ORDER BY todayDate DESC LIMIT 1";
        cursor1 = sqlDB.rawQuery("SELECT * FROM current_Info ORDER BY todayDate DESC LIMIT 1", null);
        if(cursor1.moveToLast()) { ///가장 최근값 (오늘 DB값을 넣음)
            System.out.println("DB시간"+cursor1.getString(0));
        }
        System.out.println("현재시간"+sumDate);


    }
}