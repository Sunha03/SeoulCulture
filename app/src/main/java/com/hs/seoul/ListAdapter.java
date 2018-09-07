package com.hs.seoul;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class ListAdapter extends BaseAdapter implements View.OnClickListener {
    Bitmap bmp;
    Context mContext;

    ImageView iv_image;
    TextView tv_title;
    TextView tv_date;

    LayoutInflater inflater = null;
    private ArrayList<ItemEvent> mEvents = null;
    private int nListCnt = 0;

    public ListAdapter(Context context, ArrayList<ItemEvent> events)
    {
        mEvents = events;
        nListCnt = mEvents.size();
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return nListCnt;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            final Context context = parent.getContext();
            if(inflater == null) {
                inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        convertView.setTag(position);
        convertView.setOnClickListener(this);

        iv_image = (ImageView)convertView.findViewById(R.id.iv_image);
        tv_title = (TextView)convertView.findViewById(R.id.tv_title);
        tv_date = (TextView)convertView.findViewById(R.id.tv_date);

        tv_title.setText(mEvents.get(position).title);
        tv_date.setText(mEvents.get(position).date);
        getImageTask task = new getImageTask();
        task.execute(mEvents.get(position).image);
        iv_image.setImageBitmap(bmp);


        return convertView;
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

    public void onClick(View v) {
        int position = (Integer)v.getTag();

        ItemEvent event = mEvents.get(position);

        Bundle extras = new Bundle();
        extras.putString("TITLE", event.title);
        extras.putString("DATE", event.date);
        extras.putString("TIME", event.time);
        extras.putString("PLACE", event.place);
        extras.putString("USEFEE", event.useFee);
        extras.putString("INQUIRY", event.inquiry);
        extras.putString("CONTENTS", event.contents);
        extras.putString("LINK", event.link);
        extras.putString("IMAGE", event.image);


        Intent intent = new Intent(mContext, InfoActivity.class);
        intent.putExtras(extras);
        mContext.startActivity(intent);
    }
}
