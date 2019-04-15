package com.mobile.Smf.model;

import android.graphics.Bitmap;

import java.util.Date;

public class PicturePost extends Post {

    private Bitmap picture;

    public PicturePost(int postID, String userName, Date timeStamp, Bitmap picture){
        super(postID,userName,timeStamp);
        this.picture = picture;
    }

}
