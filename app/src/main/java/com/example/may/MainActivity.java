package com.example.may;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sqlDB;
    todayWeather weather;
    BackThread tread;
    Cursor cursor;
    Cursor cursor1;
    private ActivityResultLauncher<Intent> mStartForResult;
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
        mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // 버튼을 누르고 돌아온 경우 처리할 작업 수행
                        // 예를 들어, 화면 원래대로 돌리는 작업 등을 수행
                        // 이 예제에서는 아무 작업도 수행하지 않습니다.
                    }
                });

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
        }
        else {
            //DB에 오늘 호출한 데이터가 있으면 새로운 값을 insert 하지 않음
            tread = new BackThread(this.getApplicationContext());
            tread.start();
        }
        Button but = findViewById(R.id.button); //날씨 정보를 보여주는 버튼
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),weatherInfo.class);
                intent.putExtra("temp", cursor1.getString(1));
                intent.putExtra("pressure", cursor1.getString(4));
                intent.putExtra("humidty", cursor1.getString(5));
                intent.putExtra("wind", cursor1.getString(6));
                intent.putExtra("direction", cursor1.getString(7));
                intent.putExtra("dust", cursor1.getString(8));
                mStartForResult.launch(intent);
            }
        });

        Button but1 = findViewById(R.id.button1); //오늘 챙겨야 할 준비물을 알려주는 버튼
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),weatherAlert.class);
                intent.putExtra("date", cursor1.getString(0));
                intent.putExtra("temp", cursor1.getString(1));
                intent.putExtra("maxTemp", cursor1.getString(2));
                intent.putExtra("minTemp", cursor1.getString(3));
                intent.putExtra("pressure", cursor1.getString(4));
                intent.putExtra("humidty", cursor1.getString(5));
                intent.putExtra("wind", cursor1.getString(6));
                intent.putExtra("dust", cursor1.getString(8));
                mStartForResult.launch(intent);
            }
        });

        Button but2 = findViewById(R.id.button2);
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(getApplicationContext(),"날씨 갱신완료",Toast.LENGTH_LONG);
                myToast.show();
            }
        });

        Button but3 = findViewById(R.id.button3);
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),pastInfo.class);
                mStartForResult.launch(intent);
            }
        });
    }

}