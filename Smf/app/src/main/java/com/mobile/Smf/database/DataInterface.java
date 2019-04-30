/*
* Handles serving data from MySQL server and local SQLite, as well as receiving and storing data.
*/

package com.mobile.Smf.database;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import com.mobile.Smf.model.Post;
import com.mobile.Smf.model.User;
import com.mobile.Smf.util.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


public class DataInterface {

    private MySql mySql;
    private SqLite sqLite;
    private User user;
    private Context context; //not sure about this
    private ArrayList<Post> postsInUse;
    private List<Post> newerPosts;
    private List<Post> olderPosts;

    // Background synchronization control elements
    private AtomicBoolean backgroundSync;
    private AtomicBoolean scrollFlag;
    private ReentrantLock inUse = new ReentrantLock();
    private ReentrantLock newP = new ReentrantLock();
    private ReentrantLock oldP = new ReentrantLock();

    //Network related
    private ConnectivityManager connectivityManager;
    private  NetworkInfo activeNetworkInfo;

    private static DataInterface dataInterface;

    private DataInterface(Context context){

        this.context = context;
        mySql = MySql.getMySql();
        sqLite = SqLite.getSqLite(context);
        getUserFromCookie();
        setUpNetworkFields();
        getInitialPosts();
        backgroundSync = new AtomicBoolean(true);
        scrollFlag = new AtomicBoolean(false);

        startBackGroundSync();
    }

    public static DataInterface getDataInterface(Context context) {
        if(dataInterface == null)
            dataInterface = new DataInterface(context);
        return dataInterface;
    }

    private void getInitialPosts() {
        if(isConnected()) {
            postsInUse = mySql.getInitialPosts();

        } else {
            //todo check sqlite for posts or throw exception
        }
    }

    public ArrayList<Post> getFirstTenPosts() {
        return postsInUse;
    }

    private void getUserFromCookie() {
        user = sqLite.getLoggedInUser();
    }

    /*
    * checks if the inputted userName and password match, use on the login screen
    * @param userName
    * @param password
    * @return boolean true if valid login, false if invalid
    * */
    public boolean checkIfValidLogin(String userName, String password){
        boolean returnVal = false;
        sqLite.setUpSchemas();
            if(mySql.checkIfValidLogin(userName, password)) {
                User u = mySql.getUser(userName);
                if(sqLite.syncProfileInfoFromMySql(u)){
                    returnVal = true;
                }
            }
        return returnVal;
    }

    /*
    * check if the inputted userName and email are unique and can be created
    * @param userName unique username
    * @param email unique email
    * @return boolean true if both unique, false otherwise
    * */
    public boolean checkIfValidNewUser(String userName, String email){
        return mySql.checkIfValidNewUser(userName,email);
    }

    /*
    * input the new user into the MySQL server and return the created user
    * @param userName str username
    * @param password str password
    * @param email str password
    * @param country str country
    * @param birthYear int year
    * @return newly created user
    * */
    public boolean addNewUser(String userName, String password, String email, String country, int birthYear){
       boolean returnVal = false;

        User user = mySql.addNewUser(userName,password,email,country,birthYear);
        //Log.d("addNewUser",""+user);
        if(user != null)
            if(sqLite.syncProfileInfoFromMySql(user))
                returnVal = true;

        return returnVal;
    }

    /*
    * get the currently logged in user
    * @return User currently logged in user
    * */

    public User getLoggedInUser(){
        user = sqLite.getLoggedInUser();
        return user ;
    }

    private void updateLoggedInUser(){
        user = sqLite.getLoggedInUser();
    }

    /*
    * returns all posts, intended for development
    * @return List<Post> list of all posts
    * */
    public List<Post> getAllPosts(){
        return mySql.getAllPosts();
    }

    /*
    * returns the specified number of new posts
    * @param numberOfPosts int number of new posts to get
    * @return List<Post> returns a list of posts to be prepended
    * */
    public List<Post> getSpecificNumberOfNewerPosts(int numberOfPosts, long timestamp){
        return mySql.getSpecificNumberOfNewerPosts(numberOfPosts, timestamp);
    }

    /*
    * returns the specified number of older and not loaded posts
    * @param numberOfPosts int number of posts to get
    * @return List<Post> returns a list of posts to be appended
    * */
    public List<Post> getSpecificNumberOfLowerPosts(int numberOfPosts, int oldestPostID){
        return mySql.getSpecificNumberOfLowerPosts(numberOfPosts, oldestPostID);
    }

    /*
    * uploads a new TextPost to MySql server.
    * @param username String the user who posted the text
    * @param text String the text of the post
    * @return boolean true if successful upload, false if upload fails
    * */
    public boolean uploadTextPost(String text){
        Timestamp t = new Timestamp();
        Log.d("uploadTextPost", "UserName"+user.getUserName()+" userID "+user.getId());
        return mySql.uploadTextPost(user.getId(), text, t.getSystemTime(), t.getLocalTime(), t.getUniversalTime());
    }

    /*
    * USE THIS METHOD TO UPDATE VIEW WITH OLDER POSTS
    * */
    public List<Post> getUpdatedListOlder() {

        if(olderPosts.size() == 0)
            return null;

        oldP.lock();
        int eval = olderPosts.size() < 10 ? olderPosts.size() : 10;
        List<Post> olderSliced = olderPosts.subList(0,eval);
        olderPosts = olderPosts.subList(eval,olderPosts.size());
        oldP.unlock();
        scrollFlag.compareAndSet(false,true);

        return olderSliced;
    }

    /*
     * USE THIS METHOD TO UPDATE VIEW WITH NEWER POSTS
     * */
    public List<Post> getUpdatedListNewer() {

        if(newerPosts.size() == 0)
            return null;

        newP.lock();
        int eval = newerPosts.size() < 5 ? newerPosts.size() : 5;
        List<Post> newerSliced = newerPosts.subList(0, eval);
        newerPosts = newerPosts.subList(eval,newerPosts.size());
        newP.unlock();

        return newerSliced;
    }


    /*
     * uploads a new PicturePost to MySql server.
     * @param username String the user who posted the text
     * @param icon_photos the icon_photos of the post as a Bitmap
     * @return boolean true if successful upload, false if upload fails
     * */
    public boolean uploadPicturePost(Bitmap picture){

        Timestamp t = new Timestamp();
        return mySql.uploadPicturePost(user.getId(), picture, t.getSystemTime(),t.getLocalTime(),t.getUniversalTime());

    }

    /*
    * Should log the user out of the system, by deleteing all local data associated with the user
    * @return boolean true if succesfully deleted all local user data, false otherwise
    * */
    public boolean logCurrentUserOut(){

        killBackgroundSync();
        user = null;
        return sqLite.dropAllTables();
    }

    public void killBackgroundSync() {
        backgroundSync.getAndSet(false);
    }

    public void setScrollFlag() {
        scrollFlag.getAndSet(true);
        System.out.println("SETTING FLAG!!!");
    }



    private void startBackGroundSync() {

        // read values into sqLite when local list gets to long --> do this tomorrow
        // maybe adjustments should be made to how many we read in to begin with and save in sqlite
        if(isConnected()) {

            olderPosts = mySql.getSpecificNumberOfLowerPosts(10, postsInUse.get(postsInUse.size() - 1).getPostID());
            newerPosts = mySql.getSpecificNumberOfNewerPosts(1,postsInUse.get(0).getTimeStamp());

        }

        Thread backgroundThread = new Thread(() -> {


            while(backgroundSync.get()) {
                //System.out.println("postInUse: "+postsInUse.size());

                if(isConnected()) {
                    if (newerPosts.size() < 20) {
                        newP.lock();
                        newerPosts.addAll(mySql.getSpecificNumberOfNewerPosts(1,newerPosts.size() != 0 ? newerPosts.get(newerPosts.size()-1).getTimeStamp() : postsInUse.get(0).getTimeStamp()));
                        //System.out.println("newerPosts size: "+newerPosts.size());
                        newP.unlock();

                    }

                    if (scrollFlag.get()) {
                        oldP.lock();
                        olderPosts.addAll(mySql.getSpecificNumberOfLowerPosts(20, postsInUse.get(postsInUse.size() - 1).getPostID()));
                        //System.out.println("olderPosts size: "+olderPosts.size());
                        oldP.unlock();
                        if (olderPosts.size() > 50) {
                            oldP.lock();
                            List<Post> toSqLite = olderPosts.subList(30, olderPosts.size());
                            olderPosts = olderPosts.subList(0, 30);
                            oldP.unlock();
                            scrollFlag.compareAndSet(true, false);
                            sqLite.addToPosts(toSqLite);

                        }
                    }
                }

                try {
                    Thread.sleep(5000);
                } catch(InterruptedException e) {e.printStackTrace();}

                if(newerPosts.size() > 25) {
                    newP.lock();
                    ArrayList<Post> toSqLite = (ArrayList<Post>) newerPosts.subList(0,10);
                    newerPosts = (ArrayList<Post>) newerPosts.subList(10,newerPosts.size());
                    newP.unlock();
                    sqLite.addToPosts(toSqLite);
                }
            }
        });

        backgroundThread.start();

    }

    private void setUpNetworkFields() {

        if(connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        }
        if(activeNetworkInfo == null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
    }

    public boolean isConnected() {
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void checkSqLiteTables(){
        System.out.println("checkSqLiteTables");
        sqLite.checkTables();
    }

} //end class
