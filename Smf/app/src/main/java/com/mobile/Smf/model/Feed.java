/*
* Singleton
* Manages feed_as_list data, fetches data from the data interface and serves it to the UI.
* */

package com.mobile.Smf.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.mobile.Smf.database.DataInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feed {

    private static Feed feedSingleton;
    private DataInterface datainterface;
    private List<Post> feed_as_list;

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
        feed_as_list.add(new TextPost(1, "testUser1",new Date(10000000),"Lorem Ipsum"));
        feed_as_list.add(new PicturePost(4, "testUser4",new Date(40000000),createImage(200,200, Color.BLUE)));
        feed_as_list.add(new TextPost(2, "testUser2",new Date(20000000),"Lorem Ipsum2"));
        feed_as_list.add(new PicturePost(5, "testUser5",new Date(50000000),createImage(200,200, Color.GREEN)));
        feed_as_list.add(new TextPost(3, "testUser3",new Date(30000000),"Lorem Ipsum3"));
        feed_as_list.add(new PicturePost(6, "testUser6",new Date(60000000),createImage(200,200, Color.RED)));
        feed_as_list.add(new TextPost(7, "testUser7",new Date(70000000),"Lorem Ipsum4"));
        feed_as_list.add(new PicturePost(8, "testUser8",new Date(80000000),createImage(200,200, Color.YELLOW)));
        feed_as_list.add(new TextPost(9, "testUser9",new Date(90000000),"Lorem Ipsum5"));
        feed_as_list.add(new PicturePost(10, "testUser10",new Date(100000000),createImage(200,200, Color.CYAN)));
        feed_as_list.add(new TextPost(11, "testUser11",new Date(110000000),"Lorem Ipsum6"));
        feed_as_list.add(new PicturePost(12, "testUser12",new Date(120000000),createImage(200,200, Color.BLACK)));

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
