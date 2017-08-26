package com.example.android.devsinlagos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import static android.R.id.message;
import static java.security.AccessController.getContext;

public class ProfilePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_design_profile_screen_xml_ui_design);
        Bundle bundle = getIntent().getExtras();
        final String username = bundle.getString("BundleName");
        String userimage = bundle.getString("Bundleimage");
        final String userurl = bundle.getString("BundleUrl");

        TextView developername = (TextView)findViewById(R.id.user_profile_name);
        developername.setText(username);
        Button visit_button = (Button)findViewById(R.id.visit_button);
        Button share_button = (Button)findViewById(R.id.share_button);
        ImageView imageButton = (ImageView) findViewById(R.id.user_profile_photo);

        ImageLoader.getInstance().displayImage(userimage, imageButton);
        Log.v("nnn", "this is" + userimage);

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer: @" + username
                + " \nProfile: " + userurl);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        visit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri devUri = Uri.parse(userurl);

                // Create a new intent to view the developer URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, devUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });

    }
}
