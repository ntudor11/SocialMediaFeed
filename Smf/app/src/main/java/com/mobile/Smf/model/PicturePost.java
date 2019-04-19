package com.mobile.Smf.model;

import android.graphics.Bitmap;

import com.mobile.Smf.util.PostTypeInterface;

import java.util.Date;

public class PicturePost extends Post implements PostTypeInterface {

    private static final int postType = 2;
    @Override
    public int getPostType(){return postType;}

    private Bitmap picture;

    public PicturePost(int postID, String userName, Date timeStamp, Bitmap picture){
        super(postID,userName,timeStamp);
        this.picture = picture;
    }

    public Bitmap getPicture(){
        return picture;
    }

}
