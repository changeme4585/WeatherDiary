package com.example.may;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class dustThread  extends Thread{
    public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request1 = new Request.Builder().
                url("https://api.openweathermap.org/data/2.5/air_pollution?lat=35.83900501545771&lon=128.7610299483572&appid=944b4ec7c3a10a1bbb4a432d14e6f979").
                get().addHeader("accept","application/json").
                build();

        try {
            Response response1 = client.newCall(request1).execute();
            ResponseBody b = response1.body();
            JSONObject js = new JSONObject(b.string());
            String dust = js.getString("list");
            JSONObject js1 = new JSONObject(dust.substring(1, dust.length()-1));
            String dust1 = js1.getString("components");


            JSONObject js2 = new JSONObject(dust1);
            System.out.println("미세먼지: "+js2.getString("pm2_5"));
            System.out.println("초미세먼지: "+js2.getString("pm10"));
        } catch (Exception e) {
            System.out.println("먼지에러: "+e);
        }

    }
}
