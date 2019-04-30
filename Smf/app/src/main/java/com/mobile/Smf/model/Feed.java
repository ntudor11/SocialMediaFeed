/*
* Singleton
* Manages feed_as_list data, fetches data from the data interface and serves it to the UI.
* */

package com.mobile.Smf.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;

import com.mobile.Smf.database.DataInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feed {

    private static Feed feedSingleton;
    private DataInterface datainterface;
    private List<Post> feed_as_list;

    private String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sollicitudin, lectus in euismod pharetra, mauris felis suscipit quam, vel malesuada turpis enim quis tellus. Praesent maximus fermentum imperdiet. Aenean efficitur sem lorem, quis imperdiet sem hendrerit malesuada. Vivamus porta gravida nibh, sed aliquet eros finibus a. Integer ullamcorper leo dictum ipsum tempor fermentum. Donec in sem vitae magna tristique venenatis. Vivamus in mauris magna. Integer finibus, ipsum at porttitor maximus, sapien tortor interdum sapien, sit amet dictum lacus orci in libero. ";

    private Feed(Context context){
        datainterface = DataInterface.getDataInterface(context);
        feed_as_list = new ArrayList<>();
         fillFeed();
         //createSomeTestPosts();
    }

    public static Feed getFeedSingleton(Context context){
        if (feedSingleton == null) feedSingleton = new Feed(context);
        return feedSingleton;
    }

    public List<Post> getFeedAsList(){
        return feed_as_list;
    }

    public void fillFeed(){
        feed_as_list = datainterface.getFirstTenPosts();
    }

    public void updateWithNewerPosts(){
        feed_as_list = datainterface.getUpdatedListNewer();
    }

    public void updateWithOlderPosts(){
        feed_as_list = datainterface.getUpdatedListOlder();
    }



}
