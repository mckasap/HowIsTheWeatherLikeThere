package org.kasapbasi.howistheweatherlikethere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ///Photo by Todd Trapani on Unsplash
    TextView tv;
    EditText et;
    public class GetJson extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url= new URL(strings[0]);

                HttpURLConnection con= (HttpURLConnection) url.openConnection();
                InputStream is= con.getInputStream();
                InputStreamReader reader= new InputStreamReader(is);
                StringBuilder sb= new StringBuilder("");
                int a=reader.read();
                while(a!=-1){
                    sb.append((char) a);
                    a=reader.read();
                }

                return  sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public void myClick(View v){
        String str= "";
        GetJson gj= new GetJson();
        String City = et.getText().toString();
        StringBuilder sb= new StringBuilder("");
//api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}
        try {
            str= gj.execute("https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=1d8e22761ea89c1ff5f3229a813eee9c").get();
            JSONObject all= new JSONObject(str);
            String w= all.getString("weather");
            String main=all.getString("main");
            JSONObject mw=new JSONObject(main);
            JSONArray jarr= new JSONArray(w);
            for(int i=0;i<jarr.length();i++){
                JSONObject joPart=jarr.getJSONObject(i);
                sb.append("Main :").append(joPart.getString("main")).append("\n");
                sb.append("Desc.:").append(joPart.getString("description")).append("\n");
                Log.d("main",joPart.getString("main"));
                Log.d("description",joPart.getString("description"));
            }
            sb.append("Temp :").append((Double.parseDouble(mw.getString("temp"))-273.15)+" C").append("\n");
            sb.append("Pres.:").append(mw.getString("pressure") +"hPa").append("\n");
            sb.append("Humm.:").append(mw.getString("humidity")+"%");
            Log.d("temp", (Double.parseDouble(mw.getString("temp"))-273.15)+" C");
            Log.d("pressure",mw.getString("pressure") +"hPa");
            Log.d("humidity",mw.getString("humidity")+"%");
            tv.setText(sb.toString());
            // Log.d("HAVA_JSON",w);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.tvResult);
        et = (EditText) findViewById(R.id.editText);


    }
}
