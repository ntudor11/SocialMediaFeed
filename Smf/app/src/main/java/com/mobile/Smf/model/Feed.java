/*
* Singleton
* Manages feed data, fetches data from the data interface and serves it to the UI.
* */

package com.mobile.Smf.model;

import com.mobile.Smf.database.DataInterface;

import java.util.List;

public class Feed {

    private Feed feedSingleton;
    private DataInterface datainterface;
    private List<Post> feed;

    private Feed(){

    }

    public Feed getFeedSingleton(){
        if (feedSingleton == null) feedSingleton = new Feed();
        return feedSingleton;
    }

    public List<Post> getFeedAsList(){
        return feed;
    }

}
