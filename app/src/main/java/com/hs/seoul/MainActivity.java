package com.hs.seoul;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    private ListView mListView = null;
    ListAdapter listAdapter;
    String[] arrayJSON = new String[3];
    ArrayList<ItemEvent> eventList;
    String result = "";
    String title = "";
    String date = "";
    String time = "";
    String place = "";
    String useFee = "";
    String inquiry = "";
    String contents = "";
    String link = "";
    String strImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int nCount = 0;
        eventList = new ArrayList<ItemEvent>();

        getJSON task = new getJSON();
        try {
            result = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mListView = (ListView)findViewById(R.id.listView);
        listAdapter = new ListAdapter(this, eventList);
        mListView.setAdapter(listAdapter);

    }

    //JSON 가져오기
    class getJSON extends AsyncTask<String, Integer, String > {
        private String str, receiveMsg;
        private final String key = "677a73544d61686e393068754a6255";            //API KEY
        int getEventNum = 100;                                                      //가져올 행사 개수

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL("http://openapi.seoul.go.kr:8088/" + key + "/json/SearchConcertDetailService/1/" + getEventNum);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                if(con.getResponseCode() == con.HTTP_OK) {
                    InputStreamReader inputStream = new InputStreamReader(con.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStream);
                    StringBuffer strBuffer = new StringBuffer();
                    while((str = bufferedReader.readLine()) != null) {
                        strBuffer.append(str);
                    }

                    bufferedReader.close();
                    con.disconnect();

                    receiveMsg = strBuffer.toString().trim();
                    Log.e("superdroid", "ReceiveMsg : " + receiveMsg);

                    eventListJSONParser(receiveMsg);

                }
                else {
                    Log.e("superdroid", con.getResponseCode() + "ERROR!");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(String.valueOf(result));
            ListAdapter adapter = new ListAdapter(MainActivity.this, eventList);

            mListView.setAdapter(adapter);
        }
    }

    //필요한 데이터 가져오기
    public String[] eventListJSONParser(String jsonString) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject SearchConcert = jsonObject.getJSONObject("SearchConcertDetailService");
            JSONArray jsonArray = SearchConcert.getJSONArray("row");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jObject = jsonArray.getJSONObject(i);
                ItemEvent event = new ItemEvent();

                title = jObject.optString("TITLE");
                date = jObject.optString("STRTDATE") + " ~ " + jObject.optString("END_DATE");
                time = jObject.optString("TIME");
                place = jObject.optString("PLACE");
                useFee = jObject.optString("USE_FEE");
                inquiry = jObject.optString("INQUIRY");
                contents = jObject.optString("CONTENTS");
                strImage = jObject.optString("MAIN_IMG");
                link = jObject.optString("ORG_LINK");
                event.title = title;
                event.date = date;
                event.time = time;
                event.place = place;
                event.useFee = useFee;
                event.inquiry = inquiry;
                event.contents = contents;
                event.link = link;
                event.image = strImage;

                eventList.add(event);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayJSON;
    }
}
