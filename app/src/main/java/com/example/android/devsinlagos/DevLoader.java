package com.example.android.devsinlagos;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by TEST on 8/23/2017.
 */

public class DevLoader extends AsyncTaskLoader<ArrayList<Dev>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = DevLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link DevLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */

    public DevLoader(Context context, String url) {
        super(context);

        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Dev> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of developers.
        ArrayList<Dev> developers = DevQueryUtils.fetchDeveloperData(mUrl);
        return developers;
    }
}
