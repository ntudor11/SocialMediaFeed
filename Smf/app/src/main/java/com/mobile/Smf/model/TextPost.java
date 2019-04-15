package com.mobile.Smf.model;

import java.util.Date;

public class TextPost extends Post {

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
