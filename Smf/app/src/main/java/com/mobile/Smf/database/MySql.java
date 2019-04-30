package com.mobile.Smf.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mobile.Smf.model.PicturePost;
import com.mobile.Smf.model.Post;
import com.mobile.Smf.model.TextPost;
import com.mobile.Smf.model.User;
import com.mobile.Smf.util.Timestamp;

import java.io.ByteArrayOutputStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import android.util.Log;


public class MySql {
    //DB instance
    private static MySql singleton = null;
    //MySql connect values
    private static final String DB = "smf";
    private static final String user = "godtUserName";
    private static final String pass = "godtPassword";
    private static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + DB;

    //Thread related
    private static ExecutorService service;


    private MySql() {
        service = Executors.newCachedThreadPool();
    }

    public static MySql getMySql() {
        if (singleton == null) {
            singleton = new MySql();
        }
        return singleton;
    }


    //Interface


    public boolean checkIfValidLogin(String userName, String password) {

        boolean returnVal = false;
        try {
        Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass, String.format(Locale.getDefault(),
                "SELECT userID, userName, password FROM Users WHERE userName = '%s' AND password = '%s';",
                        userName, password)));

            ResultSet rs = f.get();

            rs.first();
            if (rs.getInt("userID") > 0 && rs.getString("password").equals(password) && rs.getString("userName").equals(userName))
                returnVal = true;

        } catch (SQLException exc) { exc.printStackTrace(); }
        catch (InterruptedException e) {e.printStackTrace(); }
        catch (ExecutionException ex) {ex.printStackTrace(); }

        return returnVal;
    }


    public boolean checkIfValidNewUser(String userName, String email) {

        Boolean returnVal = false;

        try {
        Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass,
                String.format("SELECT COUNT(*) FROM Users WHERE username = '%s' OR email = '%s';", userName, email)));

            ResultSet rs = f.get();

            rs.first();
            Log.d("ValidNewUser-result",""+rs.getInt(1));
            rs.first();
            if (rs.getInt(1) == 0)
                returnVal = true;

        } catch (SQLException exc) { exc.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        catch (ExecutionException ex) { ex.printStackTrace(); }

        return returnVal;
    }

    public User getUser(String userName) {
        User returnVal = null;

        try {
            Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass, String.format(Locale.getDefault(),
                    "SELECT * FROM Users WHERE userName = '%s';", userName)));

            ResultSet rs = f.get();

            rs.first();
            returnVal = new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6));
            //Todo sync this data to sqlite

        } catch (InterruptedException e) { e.printStackTrace(); }
        catch (ExecutionException ex) { ex.printStackTrace(); }
        catch (SQLException exc) { exc.printStackTrace(); }

    return returnVal;
    }

    public User addNewUser(String userName, String password, String email, String country, int birthYear) {

        User returnVal = null;

        int id = -1;
        try {
            Future<Boolean> f = service.submit(new InsertMySql(DB_URL, user, pass, String.format(Locale.getDefault(),
                    "INSERT INTO Users (userName, password, email, countryID, birthYear) " +
                            "VALUES ('%s','%s','%s', " +
                            "(SELECT countryID FROM Countries WHERE countryName = '%s'), %d);",
                            userName, password, email, country, birthYear)));


            if ((boolean) f.get()) {
                //will refactor this to a sql function later on
                Future<ResultSet> fx = service.submit(new queryMySql(DB_URL, user, pass,
                        String.format(Locale.getDefault(), "SELECT userID FROM Users WHERE email = '%s' AND userName = '%s';", email, userName)));
                ResultSet rs = fx.get();
                rs.first();
                id = rs.getRow();
            }

        } catch (InterruptedException e) { e.printStackTrace(); }
          catch (ExecutionException ex) { ex.printStackTrace(); }
          catch (SQLException exc) { exc.printStackTrace(); }

        if (id > 0)
            returnVal = new User(id, userName, password, email, country, birthYear);

        return returnVal;
    }

    // DEPRECATED
    public User getLoggedInUser(int userID) {
        ResultSet rs;
        User newUser = null;
        try {
            Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass, String.format(Locale.getDefault(),
                    "SELECT * FROM User WHERE userID = %d;", userID)));

            rs = f.get();

            rs.first();
            newUser = new User(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getInt(6));

        } catch (InterruptedException e) {e.printStackTrace(); }
          catch (ExecutionException ex) {ex.printStackTrace();}
          catch (SQLException exc) {exc.printStackTrace(); }

        return newUser;
    }


    public boolean uploadTextPost(int userID, String postText, long timestamp, String localTime, String universalTime) {
        boolean successfulTransaction = false;
        try{

            String arg1 = String.format(Locale.getDefault(),"SELECT insertTextPost (%d,0,%d,'%s','%s','%s');"
                    ,userID, timestamp, postText,localTime,universalTime);


            Future<Boolean> fPosts = service.submit(new TransactionInsertMySql(null,DB_URL, user, pass,arg1));

            successfulTransaction = fPosts.get();

        }
        catch(RuntimeException e) {System.out.println(e.getMessage()); e.printStackTrace();}
        catch(ExecutionException ex) {ex.printStackTrace(); System.out.println("1");}
        catch(InterruptedException exc) {exc.printStackTrace();System.out.println("2");}

        return successfulTransaction;
    }


    public boolean uploadPicturePost(int userID, Bitmap picture, long timestamp, String localTime, String universalTime) {

        boolean successfulTransaction = false;
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            // if better picture quality is needed play with quality parameter
            picture.compress(Bitmap.CompressFormat.JPEG,50,stream);

            byte[] pic = stream.toByteArray();

            String arg1 = String.format(Locale.getDefault(),"SELECT insertPicturePost (%d,1,%d,?,'%s','%s');"
                    ,userID, timestamp,localTime,universalTime);

            Future<Boolean> fPosts = service.submit(new TransactionInsertMySql(pic,DB_URL, user, pass,arg1));

            successfulTransaction = fPosts.get();


        }
        catch(RuntimeException e) {System.out.println(e.getMessage()); e.printStackTrace();}
        catch(ExecutionException ex) {ex.printStackTrace();}
        catch(InterruptedException exc) {exc.printStackTrace();}

        return successfulTransaction;
    }


    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> returnList = new ArrayList<>();

        try {
            String arg = "SELECT p.postType, p.postID, u.userName, p.tStamp, p.universalTimeStamps, p.localTimeStamps, t.postText, pic.picture " +
                    "FROM Posts p INNER JOIN Users u ON p.userID = u.userID LEFT JOIN TextPosts t ON p.postID = t.postID " +
                    "LEFT JOIN PicturePosts pic ON p.postID = pic.postID ORDER BY p.tStamp DESC;";

            Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass,arg));
            ResultSet rs = f.get();

            rs.first();

            while(!rs.isAfterLast()) {
                int eval = rs.getInt(1);
                if(eval == 0) {
                    returnList.add(new TextPost(rs.getInt(2), rs.getString(3),rs.getLong(4),rs.getString(7), rs.getString(6),rs.getString(5)));
                } else if (eval == 1) {
                    byte[] bytePic = rs.getBytes(8);
                    Bitmap pic = BitmapFactory.decodeByteArray(bytePic, 0, bytePic.length);
                    returnList.add(new PicturePost(rs.getInt(2), rs.getString(3),rs.getLong(4),pic, rs.getString(6),rs.getString(5)));
                }

                rs.next();
            }

        } catch(ExecutionException e) {e.printStackTrace();}
        catch(InterruptedException ex) {ex.printStackTrace();}
        catch (SQLException exc) {exc.printStackTrace(); }

        return returnList;
    }

    public ArrayList<Post> getInitialPosts() {
        //Timestamp t = new Timestamp();
        ArrayList<Post> returnList = new ArrayList<>();

        try {
            String arg = "SELECT p.postType, p.postID, u.userName, p.tStamp, p.universalTimeStamps, p.localTimeStamps, t.postText, pic.picture" +
                    " FROM Posts p INNER JOIN Users u ON p.userID = u.userID LEFT JOIN TextPosts t ON p.postID = t.postID " +
                    "LEFT JOIN PicturePosts pic ON p.postID = pic.postID ORDER BY p.tStamp DESC LIMIT 10;";

            Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass, arg));
            ResultSet rs = f.get();

            rs.first();
            if(!rs.next())
                return returnList;
            rs.first();

            while(!rs.isAfterLast()) {
                int eval = rs.getInt(1);
                if(eval == 0) {
                    returnList.add(new TextPost(rs.getInt(2), rs.getString(3),rs.getLong(4),rs.getString(7), rs.getString(6),rs.getString(5)));
                } else if (eval == 1) {
                    byte[] bytePic = rs.getBytes(8);
                    System.out.println(Arrays.toString(bytePic));
                    Bitmap pic = BitmapFactory.decodeByteArray(bytePic, 0, bytePic.length);
                    returnList.add(new PicturePost(rs.getInt(2), rs.getString(3),rs.getLong(4),pic, rs.getString(6),rs.getString(5)));
                }

                rs.next();
            }

        }catch(ExecutionException e) {e.printStackTrace();}
        catch(InterruptedException ex) {ex.printStackTrace();}
        catch (SQLException exc) {exc.printStackTrace(); }

    return returnList;

    }

    public ArrayList<Post> getSpecificNumberOfNewerPosts(int numberOfPosts, long timestamp) {

        ArrayList<Post> returnList = new ArrayList<>();

        try {
            String arg =  String.format(Locale.getDefault(),"SELECT p.postType, p.postID, u.userName, p.tStamp, p.universalTimeStamps, p.localTimeStamps, t.postText, pic.picture" +
                    " FROM Posts p INNER JOIN Users u ON p.userID = u.userID LEFT JOIN TextPosts t ON p.postID = t.postID " +
                    "LEFT JOIN PicturePosts pic ON p.postID = pic.postID WHERE p.tStamp > %d ORDER BY p.tStamp DESC LIMIT %d;",timestamp, numberOfPosts);

            Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass, arg));
            ResultSet rs = f.get();
            Log.d("SpecificNumfNewerPosts",""+rs);


            rs.first();
            if(rs.getRow() == 0)
                return returnList;

            while(!rs.isAfterLast()) {
                int eval = rs.getInt(1);
                if(eval == 0) {
                    returnList.add(new TextPost(rs.getInt(2), rs.getString(3),rs.getLong(4),rs.getString(7), rs.getString(6),rs.getString(5)));
                } else if (eval == 1) {
                    byte[] bytePic = rs.getBytes(8);
                    Bitmap pic = BitmapFactory.decodeByteArray(bytePic, 0, bytePic.length);
                    returnList.add(new PicturePost(rs.getInt(2), rs.getString(3),rs.getLong(4),pic, rs.getString(6),rs.getString(5)));
                }

                rs.next();
            }

        }catch(ExecutionException e) {e.printStackTrace();}
        catch(InterruptedException ex) {ex.printStackTrace();}
        catch (SQLException exc) {exc.printStackTrace(); }

        return returnList;
    }

    public ArrayList<Post> getSpecificNumberOfLowerPosts(int numberOfPosts, int oldestPostId) {

        ArrayList<Post> returnList = new ArrayList<>();

        try {
            //String arg =  String.format(Locale.getDefault(),"SELECT p.postType, p.postID, u.userName, p.tStamp, p.universalTimeStamps, p.localTimeStamps, t.postText, pic.picture" +
                    //"FROM Posts p INNER JOIN Users u ON p.userID = u.userID LEFT JOIN TextPosts t ON p.postID = t.postID " +
                    //"LEFT JOIN PicturePosts pic ON p.postID = pic.postID WHERE p.tStamp < %d ORDER BY p.tStamp DESC LIMIT %d;",t.getSystemTime(), numberOfPosts);

            String arg = String.format(Locale.getDefault(),
            "SELECT p.postType, p.postID, u.userName, p.tStamp, p.universalTimeStamps, p.localTimeStamps, t.postText, pic.picture " +
                    "FROM Posts p INNER JOIN Users u ON p.userID = u.userID LEFT JOIN TextPosts t ON p.postID = t.postID " +
                    "LEFT JOIN PicturePosts pic ON p.postID = pic.postID WHERE p.tStamp < (SELECT tStamp FROM Posts WHERE postID = %d) ORDER BY p.tStamp DESC LIMIT %d;",
                    oldestPostId,numberOfPosts);

            Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass, arg));
            ResultSet rs = f.get();

            rs.first();

            while(!rs.isAfterLast()) {
                int eval = rs.getInt(1);
                if(eval == 0) {
                    returnList.add(new TextPost(rs.getInt(2), rs.getString(3),rs.getLong(4),rs.getString(7), rs.getString(6),rs.getString(5)));
                } else if (eval == 1) {
                    byte[] bytePic = rs.getBytes(8);
                    Bitmap pic = BitmapFactory.decodeByteArray(bytePic, 0, bytePic.length);
                    returnList.add(new PicturePost(rs.getInt(2), rs.getString(3),rs.getLong(4),pic, rs.getString(6),rs.getString(5)));
                }

                rs.next();
            }

        }catch(ExecutionException e) {e.printStackTrace();}
        catch(InterruptedException ex) {ex.printStackTrace();}
        catch (SQLException exc) {exc.printStackTrace(); }

        return returnList;
    }

}


