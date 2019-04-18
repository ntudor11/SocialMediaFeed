/*
* Singleton
* Manages feed_as_list data, fetches data from the data interface and serves it to the UI.
* */

package com.mobile.Smf.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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
        feed_as_list.add(new TextPost(2, "testUser2",new Date(20000000),"Lorem Ipsum2"));
        feed_as_list.add(new TextPost(3, "testUser3",new Date(30000000),"Lorem Ipsum3"));

        Bitmap test_img = createImage(50,50,50);
        feed_as_list.add(new PicturePost(4, "testUser4",new Date(40000000),test_img));
    }

    /**
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
