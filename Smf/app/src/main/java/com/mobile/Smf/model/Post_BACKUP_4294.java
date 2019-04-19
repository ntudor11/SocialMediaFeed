package com.mobile.Smf.model;

import java.util.Date;

public class Post {

<<<<<<< HEAD
    int id;

    public Post(){
=======
    private int postID;
    private String userName;
    private Date timeStamp;

    public Post(int postID, String userName, Date timeStamp){
        this.postID = postID;
        this.userName = userName;
        this.timeStamp = timeStamp;
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
>>>>>>> f5ad993bb1b1f7a9c12fe367a5bac75d66ebcabb

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

}
