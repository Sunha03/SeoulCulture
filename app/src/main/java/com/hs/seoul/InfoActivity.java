package com.hs.seoul;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class InfoActivity extends AppCompatActivity {
    Bitmap bmp;
    ImageView iv_image;
    String title = "";
    String date = "";
    String time = "";
    String place = "";
    String useFee = "";
    String inquiry = "";
    String contents = "";
    String link = "";
    String image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        iv_image = (ImageView)findViewById(R.id.iv_image);
        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        TextView tv_date = (TextView)findViewById(R.id.tv_date);
        TextView tv_time = (TextView)findViewById(R.id.tv_time);
        TextView tv_place = (TextView)findViewById(R.id.tv_place);
        TextView tv_use_fee = (TextView)findViewById(R.id.tv_use_fee);
        TextView tv_inquiry = (TextView)findViewById(R.id.tv_inquiry);
        TextView tv_contents = (TextView)findViewById(R.id.tv_contents);
        TextView tv_link = (TextView)findViewById(R.id.tv_link);

        Intent intent = getIntent();
        title = intent.getStringExtra("TITLE");
        date = intent.getStringExtra("DATE");
        time = intent.getStringExtra("TIME");
        place = intent.getStringExtra("PLACE");
        useFee = intent.getStringExtra("USEFEE");
        inquiry = intent.getStringExtra("INQUIRY");
        contents = intent.getStringExtra("CONTENTS");
        link = intent.getStringExtra("LINK");
        image = intent.getStringExtra("IMAGE");


        tv_title.setText("제목 : " + title);
        tv_date.setText("기간 : " + date);
        tv_time.setText("시간 : " + time);
        tv_place.setText("장소 : " + place);
        tv_use_fee.setText("요금 : " + useFee);
        tv_inquiry.setText("문의 : " + inquiry);
        tv_contents.setText("내용 : " + contents);
        tv_link.setText("홈페이지 : " + link);
        getImageTask task = new getImageTask();
        task.execute(image);
        iv_image.setImageBitmap(bmp);
    }

    //URL image 가져오기
    class getImageTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap b;
        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            bmp = bitmap;
            return bitmap;
        }
        protected void onPostExecute(Bitmap result) {

            iv_image.setImageBitmap(b);
        }
    }
}
