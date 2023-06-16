package com.example.may;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class weatherAlert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_alert);
        EditText editText =findViewById(R.id.editText);
        Intent receivedIntent = getIntent();
        String date = receivedIntent.getStringExtra("date");
        int  temp = (int)Math.round(Double.valueOf(receivedIntent.getStringExtra("temp")));
        int maxTemp = (int)Math.round(Double.valueOf(receivedIntent.getStringExtra("maxTemp")));
        int minTemp = (int)Math.round(Double.valueOf(receivedIntent.getStringExtra("minTemp")));
        String pressure = receivedIntent.getStringExtra("pressure");
        int humidty = Integer.valueOf(receivedIntent.getStringExtra("humidty"));
        int wind = (int)Math.round(Double.valueOf(receivedIntent.getStringExtra("wind")));
        int dust = (int)Math.round(Double.valueOf(receivedIntent.getStringExtra("dust")));
        String answer= "준비물:\n\n\n";
        if (temp>26){
            answer+="날씨가 더워요. 실외활동을 자제하세요\n";
        }
        if (temp<0){
            answer+="날씨가 추워요. 따뜻하게 입으세요\n";
        }
        if(maxTemp-minTemp>8){
            answer+="일교차가 커요. 감기 조심하세요\n";
        }
        System.out.println(date);
        if(humidty <=40){
            answer+="습도가 낮아 빨래하기 좋아요\n";
        }
        if(humidty >65){
            answer+="습도가 높아 불쾌지수가 높아요\n";
        }
        if (wind>1){
            answer+="바람이 조금강해요\n";
        }
        if (wind>3){
            answer+="바람이 많이 강해요. 헤어스프레이를 사용하세요\n";
        }
        if (dust<=30){
            answer+="미세먼지가 좋아요. 산책하기 좋은날이에요.\n";
        }
        else if(30<dust&&dust<70){
            answer+="미세먼지가 나빠요. 마스크를 준비하세요.\n";
        }
        else{
            answer+="미세먼지가 최악이에요. 외출을 자제하세요.\n";
        }

            editText.setText(answer);
        Button but = findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}