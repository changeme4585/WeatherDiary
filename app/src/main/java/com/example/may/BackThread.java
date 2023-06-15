package com.example.may;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BackThread extends Thread{
    private Context context;

    public BackThread(Context context) {
        this.context = context;
    }
    SQLiteDatabase sqlDB;
    todayWeather weather;

    //
    public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url("https://api.openweathermap.org/data/2.5/weather?lat=35.83900501545771&lon=128.7610299483572&appid=944b4ec7c3a10a1bbb4a432d14e6f979").
                get().addHeader("accept","application/json").
                build();

        Request request1 = new Request.Builder().
                url("https://api.openweathermap.org/data/2.5/air_pollution?lat=35.83900501545771&lon=128.7610299483572&appid=944b4ec7c3a10a1bbb4a432d14e6f979").
                get().addHeader("accept","application/json").
                build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();


            JSONObject json = new JSONObject(body.string());
            String tempInfo = json.getString("main");

            JSONObject json1 = new JSONObject(tempInfo);
            String temp = String.valueOf(Double.valueOf(json1.getString("temp"))-273.0).substring(0,5);  //현재 온도
            //String feels_like = String.valueOf(Double.valueOf(json1.getString("feels_like"))-273).substring(0,5); //체감온도
            String temp_min = String.valueOf(Double.valueOf(json1.getString("temp_min"))-273).substring(0,5); //최대 온도
            String temp_max = String.valueOf(Double.valueOf(json1.getString("temp_max"))-273).substring(0,5); //최소 온도
            String pressure = json1.getString("pressure");
            String humidity = json1.getString("humidity");
            String windInfo = json.getString("wind");
            JSONObject json2 = new JSONObject(windInfo);


            Response responseDust = client.newCall(request1).execute();
            ResponseBody b = responseDust.body();
            JSONObject js = new JSONObject(b.string());
            String dust = js.getString("list");
            JSONObject js1 = new JSONObject(dust.substring(1, dust.length()-1));
            String dust1 = js1.getString("components");
            JSONObject js2 = new JSONObject(dust1);
            String getDust = js2.getString("pm2_5");




            Context applicationContext = context.getApplicationContext();
            weather = new todayWeather(applicationContext);
            sqlDB = weather.getWritableDatabase();
            //weather.onUpgrade(sqlDB,1,1); //테이블 초기화 코드
            weather.onCreate(sqlDB);


            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String getTime = dateFormat.format(date);
            String spliDate[] = getTime.split("-");
            String sumDate="" ;
            for(int i = 0; i<spliDate.length;i++){
                sumDate+=spliDate[i];
            }

             try {
            sqlDB.execSQL("insert into " + "current_Info" +
                    "(todayDate,temperature,MAX_temperature,MIN_temperature,pressure,humidity,wind_speed,wind_degree,dust) " +
                    "values ('" + sumDate + "','" + temp + "', '" + temp_min + "','" + temp_max + "','"+pressure+"','"+humidity+"','" + json2.getString("speed")+"','" + json2.getString("deg")+"','" + getDust+"');");
        }catch (Exception e) {
            System.out.println(e);
        }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);

        }
    }

}
