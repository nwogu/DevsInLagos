package com.example.android.devsinlagos;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Dev>> {

    /** URL for dev data from the github api dataset */
    private static final String DEV_REQUEST_URL =
            "https://api.github.com/search/users?q=+type:user+location:lagos";

    /** Adapter for the list of developers */
    private DevAdapter mAdapter;

    private TextView mTextView;

    private ProgressBar mProgressBar;

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Constant value for the dev loader ID.
     */
    private static final int DEV_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the display optiuons of the universal image loader here
        final DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(android.R.drawable.stat_sys_download)
                .showImageForEmptyUri(android.R.drawable.ic_dialog_alert)
                .showImageOnFail(android.R.drawable.stat_notify_error)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        //Set the configuration of the universal image loader here
        final ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        //Instanstiate the UIL here.
        ImageLoader.getInstance().init(config);

        ListView devListView = (ListView) findViewById(R.id.dev_list);
        mTextView = (TextView)findViewById(R.id.empty_dev_view);
        mProgressBar = (ProgressBar)findViewById(R.id.loading_dev_spinner) ;

        // Create a new adapter that takes an empty list of developers as input
        mAdapter = new DevAdapter(this, new ArrayList<Dev>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        devListView.setAdapter(mAdapter);
        devListView.setEmptyView(mTextView);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        if (isConnected()){loaderManager.initLoader(DEV_LOADER_ID, null, this);}
        else {
            mProgressBar.setVisibility(View.GONE);
            mTextView.setText("No Internet Connection");
        }

        devListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dev currentDeveloper = mAdapter.getItem(position);
                String currentDevUserNAme = currentDeveloper.getUserName();
                String currentDevUserUrl = currentDeveloper.getUserUrl();
                String currentDevImage = currentDeveloper.getUserImage();

                //Get the values of the current dev image url, username and github page
                //Pass it to a bundle and send to the next activity, using an intent.
                Intent profileIntent = new Intent(MainActivity.this, ProfilePageActivity.class);
                Bundle devBundle = new Bundle();
                devBundle.putString( "Bundleimage", currentDevImage);
                devBundle.putString("BundleUrl", currentDevUserUrl);
                devBundle.putString("BundleName", currentDevUserNAme);
                profileIntent.putExtras(devBundle);
                startActivity(profileIntent);
            }
        });
    }
//Helper method to check if the device is connect to the internet.
    private boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    @Override
    public Loader<ArrayList<Dev>> onCreateLoader(int id, Bundle args) {
        return new DevLoader(this, DEV_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Dev>> loader, ArrayList<Dev> data) {

        // Set empty state text to display "No developers found."
        mProgressBar.setVisibility(View.GONE);
        mTextView.setText("No Developers in Lagos");


        // Clear the adapter of previous developer data
        mAdapter.clear();

        // If there is a valid list of {@link Dev}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Dev>> loader) {

        mAdapter.clear();

    }
}
