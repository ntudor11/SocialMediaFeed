/*
* Handles serving data from MySQL server and local SQLite, as well as recieving and storing data.
*/

package com.mobile.Smf;

import com.mobile.Smf.model.Post;
import com.mobile.Smf.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataInterface {

    public DataInterface(){

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
        User newUser = new User();
        return newUser;
    }

    /*
    * get the currently logged in user
    * @return User currently logged in user
    * */
    public User getLoggedInUser(){
        User currentUser = new User();
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
    public List<Post> getSpecificNumberNewerOfPosts(int numberOfPosts){
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
}
