package com.example.a1530630.learningapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a1530630.learningapplication.Database.SQLiteManage;
import com.example.a1530630.learningapplication.Models.AudioAndImages;
import com.example.a1530630.learningapplication.Models.Modules;
import com.example.a1530630.learningapplication.Models.User;

import java.util.ArrayList;

public class LessonsViewAdapter extends ArrayAdapter<AudioAndImages> {
    private Context mcontext;
    int mresources;
    SQLiteManage db;
    Bitmap bitmap;
    byte[] images;
    ImageView imgview;
    int count;
    TextView imgNum;

    public LessonsViewAdapter(Context context, int resources, ArrayList<AudioAndImages> objects)
    {
        super(context,resources,objects);
        mcontext = context;
        mresources = resources;
    }
    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        db = new SQLiteManage(mcontext);
        count++;
        byte[] images = getItem(position).getByteImg();

        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.lessons_view_adapter, parent, false);

        imgview = (ImageView)convertView.findViewById(R.id.imageView2);
        bitmap = BitmapFactory.decodeByteArray(images,0,images.length);
        imgview.setImageBitmap(bitmap);

        imgNum = (TextView)convertView.findViewById(R.id.ImgFile);
        imgNum.setText("Image number: "+ (position+1));

        return convertView;
    }


}
