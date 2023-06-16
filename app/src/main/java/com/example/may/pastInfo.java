package com.example.may;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class pastInfo extends AppCompatActivity {
    List<String> info = new ArrayList<>();
    EditText pastText;
    EditText pastSearch;
    String todayDate ="";
    String errorMsg ="good"; //만약 잘못된 api 호출이면 오류 메시지를 이 변수에 저장
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_info);
        pastSearch = findViewById(R.id.pastSearch);
        Button but5 = findViewById(R.id.button5);
        but5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayDate = String.valueOf(pastSearch.getText());
                getPastData thread = new getPastData();
                thread.start();
                try {
                    thread.join(); // 스레드 실행을 기다림
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pastText = findViewById(R.id.pastText);
                if(errorMsg.equals("good")){
                    String answer= "";
                    answer+=todayDate.substring(0,4)+"년 "+todayDate.substring(4,6)+"월 "+todayDate.substring(6)+"일 날씨\n";
                    answer+="평균기온: "+info.get(0)+"\n";
                    answer+="최저기온: "+info.get(1)+"\n";
                    answer+="최고기온: "+info.get(2)+"\n";
                    answer+="평균풍속: "+info.get(3)+"\n";
                    answer+="일일강수량: "+info.get(4)+"\n";

                    pastText.setText(answer);
                }else{
                    pastText.setText("입력형식을 지켜주세요\n예): 2023년5월12일을 검색할 경우\n 20230512를 입력");
                }
            }
        });


        Button but6 = findViewById(R.id.button6);
        but6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
    private class getPastData extends Thread{
        public void run() {

            String urls = "http://apis.data.go.kr/1360000/AsosDalyInfoService/getWthrDataList?serviceKey=L6A5ySgk1qe%2Bc1RG%2B%2B%2FmPYUrKt35dAEddE4yF1EcdPbRpTneEf2dZwGcm3qsqwndRPJm1dW9slzSoFdM7u6yDw%3D%3D&numOfRows=10&pageNo=1&dataType=JSON&dataCd=ASOS&dateCd=DAY&";
            urls+="startDt="+todayDate+"&endDt="+todayDate;
            urls+="&stnIds=143";
            //System.out.println(urls);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().
                    url(urls).
                    get().addHeader("accept","application/json").
                    build();
            try {
                Response res = client.newCall(request).execute();
                ResponseBody body = res.body();

                JSONObject json = new JSONObject(body.string());
                String response = json.getString("response");
                JSONObject json1 = new JSONObject(response);
                String header = json1.getString("header");
                JSONObject json2 = new JSONObject(header);
                String resultCode = json2.getString("resultCode");
                if(resultCode.equals("00")){  //정상적인 api 호출
                    errorMsg="good";
                    JSONObject json4 = new JSONObject(response);
                    String getBody = json4.getString("body");
                    JSONObject json5 = new JSONObject(getBody);
                    String items = json5.getString("items");
                    JSONObject json6 = new JSONObject(items);
                    String item = json6.getString("item");
                    item= item.substring(1,item.length()-1);

                    JSONObject json7 = new JSONObject(item);
                    info.add(json7.getString("avgTa")); //평균기온 저장;
                    info.add(json7.getString("minTa")); //최저기온
                    info.add(json7.getString("maxTa")); //최고기온;
                    info.add(json7.getString("avgWs")); //평균풍속;
                    if(json7.getString("sumRn").equals("")){
                        info.add("없음");
                    }else {
                        info.add(json7.getString("sumRn")); //일일 강수량
                    }
                }else{ /// api 호출 오류
                    JSONObject json3 = new JSONObject(header);
                    errorMsg = json3.getString("resultMsg"); //오류 메시지를 저장
                }

            } catch (IOException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }catch (JSONException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
    }
}