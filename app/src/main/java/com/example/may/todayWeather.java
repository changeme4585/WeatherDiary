package com.example.may;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//현재 날씨를 저장하는 데이터베이스
public class todayWeather extends SQLiteOpenHelper {

    public todayWeather(Context context) {
        super(context,"current_Info",null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists  " +
                "current_Info (" +              //테이블 이름
                "todayDate CHAR(100) PRIMARY KEY," +  //오늘 날짜
                "temperature CHAR(100) , " +    //온도 //기본키로 쓰고 싶은 컬럼은 뒤에  PRIMARY KEY를 붙혀주면 됨
                "MAX_temperature CHAR(100)," +  //최도온도
                "MIN_temperature CHAR(100)," +  //최소온도
                "pressure CHAR(100)," +         //기압
                "humidity CHAR(100)," +         //습도
                "wind_speed CHAR(100),"+    //풍속
                "wind_degree CHAR(100),"+    //바람 위치
                "dust CHAR(100)"+    //미세먼지
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS current_Info");
    }
}
