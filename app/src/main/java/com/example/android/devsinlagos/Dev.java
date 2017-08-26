package com.example.android.devsinlagos;

/**
 * Created by TEST on 8/23/2017.
 */
// Public Class to hold Developer information.
public class Dev {
    // Global Variable to hold User Name.
    private String mUserNAme;
    // Global Variable to hold User Profile Image.
    private String mUserImage;
    // Global Variable to hold User Github url.
    private String mUserUrl;

    //Construct to take in dev parameters.
    public Dev(String userNAme, String userImage, String userUrl) {
        mUserNAme = userNAme;
        mUserImage = userImage;
        mUserUrl = userUrl;
    }
    //Method to get user name
    public String getUserName(){
        return mUserNAme;
    }
    //Method to get user image
    public String getUserImage(){
        return mUserImage;
    }

    //Method to get user github url
    public String getUserUrl(){
        return mUserUrl;
    }
}
