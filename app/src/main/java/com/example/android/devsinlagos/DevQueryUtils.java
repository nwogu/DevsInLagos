package com.example.android.devsinlagos;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.android.devsinlagos.MainActivity.LOG_TAG;

/**
 * Created by TEST on 8/23/2017.
 */

public final class DevQueryUtils {

    /**
     * Create a private constructor.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name DevQueryUtils (and an object instance of DevQueryUtils is not needed).
     */

    private DevQueryUtils() {
    }



    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Http Response Code:" + responseCode);
            }
        }catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Threw an io exception", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Dev} objects that has been built up from
     * parsing a JSON response.
     */
    public  static ArrayList<Dev> extractFeatureFromJson(String devJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(devJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding developers to
       ArrayList<Dev> developers = new ArrayList<>();

        // Try to parse the JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject EarthquakeJson = new JSONObject(devJSON);

            JSONArray items = EarthquakeJson.getJSONArray("items");

            for (int i = 0; i < items.length() ; i++){

                JSONObject devObject = items.getJSONObject(i);

                String username = devObject.getString("login");

                String image = devObject.getString("avatar_url");

                String url = devObject.getString("html_url");

                Dev developer = new Dev( username, image, url);

                developers.add(developer);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("DevQueryUtils", "Problem parsing the developer JSON results", e);
        }

        // Return the list of developers
        return developers;
    }

    /**
     * Query the github API and return a list of {@link Dev} objects.
     */
    public static ArrayList<Dev> fetchDeveloperData(String requestUrl) {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of Developers
        ArrayList<Dev> devs = extractFeatureFromJson(jsonResponse);

        // Return the list of devs
        return devs;

    }

}
