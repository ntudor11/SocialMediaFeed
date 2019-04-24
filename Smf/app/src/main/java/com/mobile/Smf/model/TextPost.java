package com.mobile.Smf.model;

import java.util.Date;

public class TextPost extends Post {

    private static final int postType = 0; // was 1
    @Override
    public int getPostType(){return postType;}

    private String text;

    public TextPost(int postID, String userName, Date timeStamp, String text){
        super(postID,userName,timeStamp);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
