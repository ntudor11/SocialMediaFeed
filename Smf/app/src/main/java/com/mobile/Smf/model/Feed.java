/*
* Singleton
* Manages feed_as_list data, fetches data from the data interface and serves it to the UI.
* */

package com.mobile.Smf.model;

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

    private Feed(){
        feed_as_list = new ArrayList<>();
        // fillFeed();
         createSomeTestPosts();
    }

    public static Feed getFeedSingleton(){
        if (feedSingleton == null) feedSingleton = new Feed();
        return feedSingleton;
    }

    public List<Post> getFeedAsList(){
        return feed_as_list;
    }

    /*
    * Intended for debugging
    * todo rework
    * */
    public void fillFeed(){
        feed_as_list = datainterface.getAllPosts();
    }

    private void createSomeTestPosts(){
        feed_as_list.add(new TextPost(1, "testUser1",new Date(100000000),loremIpsum));
        feed_as_list.add(new PicturePost(4, "testUser4",new Date(400000000),createImage(1000,1000, Color.BLUE)));
        feed_as_list.add(new TextPost(2, "testUser2",new Date(200000000),loremIpsum));
        feed_as_list.add(new PicturePost(5, "testUser5",new Date(500000000),createImage(1000,1000, Color.GREEN)));
        feed_as_list.add(new TextPost(3, "testUser3",new Date(300000000),loremIpsum));
        feed_as_list.add(new PicturePost(6, "testUser6",new Date(600000000),createImage(1000,1000, Color.RED)));
        feed_as_list.add(new TextPost(7, "testUser7",new Date(700000000),loremIpsum));
        feed_as_list.add(new PicturePost(8, "testUser8",new Date(800000000),createImage(1000,1000, Color.YELLOW)));
        feed_as_list.add(new TextPost(9, "testUser9",new Date(900000000),loremIpsum));
        feed_as_list.add(new PicturePost(10, "testUser10",new Date(1000000000),createImage(1000,1000, Color.CYAN)));
        feed_as_list.add(new TextPost(11, "testUser11",new Date(1100000000),loremIpsum));
        feed_as_list.add(new PicturePost(12, "testUser12",new Date(1200000000),createImage(1000,1000, Color.BLACK)));

    }

    /**
     * todo put this somewhere else
     * from: https://gist.github.com/catehstn/6fc1a9ab7388a1655175
     * A one color image.
     * @param width
     * @param height
     * @param color
     * @return A one color image with the given width and height.
     */
    public static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

}
