package com.mobile.Smf.model;

import com.mobile.Smf.util.PostTypeInterface;

public abstract class Post implements PostTypeInterface {

    private int postID;
    private String userName;
    private long timeStamp;
    private String localTimeStamp;
    private String universalTimeStamp;

    public abstract int getPostType();

    public Post(int postID, String userName, long timeStamp, String localTime, String universalTime){
        this.postID = postID;
        this.userName = userName;
        this.timeStamp = timeStamp;
        this.localTimeStamp = localTime;
        this.universalTimeStamp = universalTime;
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

    public long getTimeStamp() {
       return timeStamp;
   }

    public String getLocalTimeStamp() { return localTimeStamp; }

    public String getUniversalTimeStamp() {
        return universalTimeStamp;
    }

    public String getFormattedUniversalTimestamp() {
        StringBuilder sb = new StringBuilder();
        sb.append(universalTimeStamp.substring(0,4)+"-");
        sb.append(universalTimeStamp.substring(4,6)+"/");
        sb.append(universalTimeStamp.substring(6,8)+"/ - ");
        sb.append(universalTimeStamp.substring(universalTimeStamp.indexOf(8) != '0' ? 8 : 9, 10)+" h - ");
        sb.append(universalTimeStamp.substring(universalTimeStamp.indexOf(10) != '0' ? 10 : 11, 12)+" m - ");
        sb.append(universalTimeStamp.substring(universalTimeStamp.indexOf(12) != '0' ? 12 : 13, 14)+" s");
        return sb.toString();
    }

}
