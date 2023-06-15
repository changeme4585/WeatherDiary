package com.example.may;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sqlDB;

    todayWeather weather;
    Cursor cursor;
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
        //weather.onUpgrade(sqlDB,1,1); //테이블 초기화 코드
        weather.onCreate(sqlDB);

        cursor = sqlDB.rawQuery("select * from current_Info;", null);
        System.out.println("row 개수"+cursor.getCount());
        while(cursor.moveToNext()){
            System.out.println("풍속"+cursor.getString(5));
            System.out.println("풍향"+cursor.getString(6));
        }

    }
}