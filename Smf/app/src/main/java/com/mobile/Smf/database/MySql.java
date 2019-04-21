package com.mobile.Smf.database;


import com.mobile.Smf.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


public class MySql {
    //DB instance
    private static MySql singleton = null;
    //MySql connect values
    private static final String DB = "Mobile";
    private static final String user = "thkh_mobile1";
    private static final String pass = "Thd14061985";
    private static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + DB;

    //Thread related
    private static ExecutorService service;
    private AtomicInteger oldestBound = new AtomicInteger();
    private AtomicInteger newestBound = new AtomicInteger();



    private MySql() {
        service = Executors.newCachedThreadPool();

     /*
     Future<Boolean> f = service.submit(new UpdateMySql(DB_URL,user,pass,
             "CREATE TABLE IF NOT EXISTS User (User_id int NOT NULL AUTO_INCREMENT primary key," +
                     "User_name VARCHAR(100) NOT NULL, Email VARCHAR(100) NOT NULL, Age int NOT NULL," +
                     "Country VARCHAR(100) NOT NULL, UNIQUE (Email));"));
     boolean returnVal = false;

     try {
         returnVal = f.get();
     } catch (InterruptedException e)  {e.printStackTrace(); }
       catch (ExecutionException ex) {ex.printStackTrace(); }
     if(!returnVal)
         throw new RuntimeException("User table not created");
         */
    }

    public static MySql getMySql() {
        if (singleton == null) {
            singleton = new MySql();
        }
        return singleton;
    }


    //Interface


    public boolean checkIfValidLogin(String userName, String password) {
        Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass, String.format("SELECT id, userName, password FROM User " +
                "WHERE userName = %s AND password = %s;", userName, password)));
        ResultSet res = null;
        try {
            res = f.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
        try {
            if (res.getInt("id") > 0 || (res.getString("password").equals(password)
                    || res.getString("userName").equals(userName)))
                return true;

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return false;
    }

    public boolean checkIfValidNewUser(String userName, String email) {
        Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass,
                String.format("SELECT COUNT (*) FROM User WHERE username = '%s' OR email = '%s';", userName, email)));

        ResultSet res = null;
        try {
            res = f.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        }
        try {
            res.first();
            if (res.getRow() != 0)
                return false;

        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return true;
    }

    public User addNewUser(String userName, String password, String email, String country, int birthYear) {

        boolean res = false;
        ResultSet rs = null;
        int id = -1;
        try {
            Future<Boolean> f = service.submit(new InsertMySql(DB_URL, user, pass, String.format(Locale.getDefault(), "INSERT INTO User (userName, password," +
                    "email, country, birthYear) VALUES (%s,%s,%s,%s,%d);", userName, password, email, country, birthYear)));

            res = f.get();

            if (res) {
                Future<ResultSet> fx = service.submit(new queryMySql(DB_URL, user, pass,
                        String.format("SELECT id FROM User WHERE email = '%s';", email)));
                rs = fx.get();
                rs.first();
                id = rs.getRow();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        if (id <= 0)
            return new User(id, userName, password, email, country, birthYear);
        else
            return null;
    }


    public User getLoggedInUser(String userName, String password) {
        ResultSet rs = null;
        User newUser = null;
        try {
            Future<ResultSet> f = service.submit(new queryMySql(DB_URL, user, pass, String.format(Locale.getDefault(),
                    "SELECT * FROM User WHERE userName = '%s';", userName)));

            rs = f.get();

            rs.first();
            newUser = new User(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getInt(6));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return newUser;
    }
/*  //Will be done as callable thread
    public boolean uploadTextPost(String username, String text, long timestamp) {

        try{
            Future<Boolean> f = service.submit(new InsertMySql(DB_URL, user, pass,String.format(Locale.getDefault(),
                    "INSERT INTO Post (userID, timePosted) VALUES (, %d);"
                    ,username, text)));
        }
    }
*/
}


