package com.mobile.Smf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.ResultSet;

import static android.content.Context.MODE_PRIVATE;
import static android.icu.text.MessagePattern.ArgType.SELECT;

public class SqLite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SmfSqLite";


    private static SQLiteDatabase mydatabase;

    private static SqLite sqLite;

    private SqLite(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mydatabase = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        onCreate(mydatabase);
    }

    public static  SqLite getSqLite(Context context) {
        if(sqLite == null)
            sqLite = new SqLite(context);
        return sqLite;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        setUpSchemas(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Cursor res = db.rawQuery("select * from Profile_info ", null);
        try {
            //and other tables if
            db.execSQL(String.format("DROP TABLE IF EXISTS %s", "Profile_info"));
        } catch(SQLException e) {e.printStackTrace();}
        onCreate(db);
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id",res.getInt(1));
        contentValues.put("Name",res.getString(2));
        contentValues.put("Email",res.getString(3));
        contentValues.put("Age",res.getInt(4));
        contentValues.put("Country",res.getString(5));
        long eval = db.insert("Profile_info", null, contentValues);
        if(eval == -1)
            throw new RuntimeException("Exception while updating SqLite");

    }


    private void setUpSchemas(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE IF EXISTS Profile_info");
            db.execSQL("CREATE TABLE IF NOT EXISTS Profile_info (Id int, Name VARCHAR(100) NOT NULL, " +
                    "Email VARCHAR(100) NOT NULL, Age int, Country VARCHAR(100) );");
        } catch(SQLException e) {e.printStackTrace();}
    }


    //Interface

    public boolean syncProfileInfoFromMySql(MySql my, String name, String email, int age, String country) {
        ResultSet rs = (ResultSet) my.getResultSetFromEmail("getFromEmail", email);
        long eval = -1;
        try {
            rs.first();
            System.out.println("TEST"+rs.getString(3));
        if(!rs.getString(3).equals(email))
            return false;

            ContentValues contentValues = new ContentValues();
            contentValues.put("Id", rs.getInt(1));
            contentValues.put("Name", name);
            contentValues.put("Email", email);
            contentValues.put("Age", age);
            //contentValues.put("Country", country);
            eval = mydatabase.insert("Profile_info", null, contentValues);
        } catch(java.sql.SQLException e) {e.printStackTrace(); System.out.println(e.getMessage());}
        if(eval != -1)
            return true;
        else
            return false;
    }

    public String getProfileInfo(String email) {
        String profileInfo = "Error";
        try {
            Cursor res = mydatabase.rawQuery(String.format("SELECT * FROM Profile_info WHERE Email = '%s';", email), null);
            res.moveToFirst();
            System.out.println("HERE \n");
            System.out.println(res.getInt(0));
            System.out.println(res.getString(1));
            System.out.println(res.getString(2));
            System.out.println(res.getInt(3));
            System.out.println(res.getString(4));
            profileInfo = "ID: " + res.getInt(0) + " Name: " + res.getString(1) +
                    " Email: " + res.getString(2) + " Age " + res.getInt(3) +
                    " Country: " + res.getString(4);

        } catch (SQLException e) {e.printStackTrace();}
        return profileInfo;
    }

}


