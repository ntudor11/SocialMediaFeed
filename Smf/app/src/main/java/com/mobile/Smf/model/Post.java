package com.mobile.Smf.model;

import com.mobile.Smf.util.PostTypeInterface;

import java.util.Date;

public abstract class Post implements PostTypeInterface {

    private int postID;
    private String userName;
    private Date timeStamp;

    public abstract int getPostType();

    public Post(int postID, String userName, Date timeStamp){
        this.postID = postID;
        this.userName = userName;
        this.timeStamp = timeStamp;
    }

    public String getTimeStampAsStr(){
        return timeStamp.toString();
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

}
