/*
* Handles serving data from MySQL server and local SQLite, as well as receiving and storing data.
*/

package com.mobile.Smf.database;


import com.mobile.Smf.model.Post;
import com.mobile.Smf.model.User;
import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class DataInterface {

    MySql mySql;
    SqLite sqLite;

    public DataInterface(Context context){

        mySql = MySql.getMySql();
        sqLite = SqLite.getSqLite(context);
    }

    /*
    * checks if the inputted userName and password match, use on the login screen
    * @param userName
    * @param password
    * @return boolean true if valid login, false if invalid
    * */
    public boolean checkIfValidLogin(String userName, String password){
        return true;
    }

    /*
    * check if the inputted userName and email are unique and can be created
    * @param userName unique username
    * @param email unique email
    * @return boolean true if both unique, false otherwise
    * */
    public boolean checkIfValidNewUser(String userName, String email){
        return true;
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
    public User addNewUser(String userName, String password, String email, String country, int birthYear){
        User newUser = new User(1,"test user","test","test@test.test","test land",2019);
        return newUser;
    }

    /*
    * get the currently logged in user
    * @return User currently logged in user
    * */
    public User getLoggedInUser(){
        User currentUser = new User(1,"test user","test","test@test.test","test land",2019);
        return currentUser;
    }

    /*
    * returns all posts, intended for development
    * @return List<Post> list of all posts
    * */
    public List<Post> getAllPosts(){
        List<Post> returnList = new ArrayList<>();
        return returnList;
    }

    /*
    * returns the specified number of new posts
    * @param numberOfPosts int number of new posts to get
    * @return List<Post> returns a list of posts to be prepended
    * */
    public List<Post> getSpecificNumberOfNewerPosts(int numberOfPosts){
        List<Post> returnList = new ArrayList<>();
        return returnList;
    }

    /*
    * returns the specified number of older and not loaded posts
    * @param numberOfPosts int number of posts to get
    * @return List<Post> returns a list of posts to be appended
    * */
    public List<Post> getSpecificNumberOfLowerPosts(int numberOfPosts){
        List<Post> returnList = new ArrayList<>();
        return returnList;
    }

    /*
    * uploads a new TextPost to MySql server.
    * @param username String the user who posted the text
    * @param text String the text of the post
    * @return boolean true if successful upload, false if upload fails
    * */
    public boolean uploadTextPost(String username, String text){
        return true;
    }

    /*
     * uploads a new PicturePost to MySql server.
     * @param username String the user who posted the text
     * @param picture the picture of the post as a Bitmap
     * @return boolean true if successful upload, false if upload fails
     * */
    public boolean uploadPicturePost(String username, Bitmap picture){
        return true;
    }
}
