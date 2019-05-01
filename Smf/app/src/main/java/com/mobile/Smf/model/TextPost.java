package com.mobile.Smf.model;

public class TextPost extends Post {

    private static final int postType = 0;

    private String text;

    public TextPost(int postID, String userName, long timeStamp, String text, String localTime, String universalTime){
        super(postID,userName,timeStamp,localTime, universalTime);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getPostType(){return postType;}

}
