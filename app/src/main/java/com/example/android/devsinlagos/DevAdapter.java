package com.example.android.devsinlagos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by TEST on 8/23/2017.
 */

public class DevAdapter extends ArrayAdapter<Dev> {


//Constryctor to take in context and an arraylist
    public DevAdapter (Activity context, ArrayList<Dev> devs){
        super(context,0, devs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            //Inflate the layout
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.dev_layout, parent, false);
        }

        Dev currentDev = getItem(position);
        //Find the image voew and cast it
        ImageView currentUserImage = (ImageView)listItemView.findViewById(R.id.profile_pics);
        TextView currentUsername = (TextView)listItemView.findViewById(R.id.user_name);
        currentUsername.setText(currentDev.getUserName());
        // show The Image in a ImageView using universal image loader library
        ImageLoader.getInstance().displayImage(currentDev.getUserImage(), currentUserImage);


        return listItemView;

    }

}
