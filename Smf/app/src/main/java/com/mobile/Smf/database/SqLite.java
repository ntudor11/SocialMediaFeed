package com.mobile.Smf.database;

import com.mobile.Smf.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;



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

        Cursor res = null;
        try {
        res = db.rawQuery("select * from Profile_info ", null);

            //and other tables if
            db.execSQL(String.format("DROP TABLE IF EXISTS %s", "Profile_info"));
            //dropAllTables(db);
        } catch(SQLException e) {e.printStackTrace();}
        onCreate(db);
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",res.getInt(0));
        contentValues.put("name",res.getString(1));
        contentValues.put("password",res.getString(2));
        contentValues.put("email",res.getString(3));
        contentValues.put("country",res.getString(4));
        contentValues.put("birthYear",res.getInt(5));
        long eval = db.insert("Profile_info", null, contentValues);
        if(eval == -1)
            throw new RuntimeException("Exception while onUpgrade SqLite");

    }

//Use this to set up all
    private void setUpSchemas(SQLiteDatabase db) {
        try {
            //db.execSQL("DROP TABLE IF EXISTS Profile_info");
            db.execSQL("CREATE TABLE IF NOT EXISTS Profile_info (id int, name VARCHAR(100) NOT NULL, " +
                    "password VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL, " +
                    "country VARCHAR(100), birthYear int );");
        } catch(SQLException e) {e.printStackTrace();}
    }


    //Interface

    public boolean syncProfileInfoFromMySql(User self) {

        Cursor c = this.getProfileInfo(self.getEmail(),self.getUserName());
        if(self.getUserName().equals(c.getString(1)) && self.getEmail().equals(c.getString(3))) {

            ContentValues contentValues = new ContentValues();
            contentValues.put("id", self.getId());
            contentValues.put("name", self.getUserName());
            contentValues.put("password", self.getPassword());
            contentValues.put("email", self.getEmail());
            contentValues.put("country", self.getCountry());
            contentValues.put("birthYear", self.getBirthYear());

            long eval = mydatabase.insert("Profile_info", null, contentValues);
                if(eval != -1)
                    return true;
                else
                    return false;
        } else
            return false;

    }

    //could also be made to return a list
    public Cursor getProfileInfo(String email, String name) {
        Cursor res = null;
        try {
            res = mydatabase.rawQuery(String.format("SELECT * FROM Profile_info WHERE email = '%s' " +
                    "AND name ='%s';", email, name), null);

        } catch (SQLException e) {e.printStackTrace();}
        return res;
    }


    //Helper functions

    private boolean dropAllTables() {
        Cursor cursor = mydatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        try {
            List<String> tables = new ArrayList<>(cursor.getCount());

            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
            }

            for (String table : tables) {
                if (table.startsWith("sqlite_") || table.startsWith("android_metadata")) {
                    continue;
                }
                mydatabase.execSQL("DROP TABLE IF EXISTS " + table);
            }
            Cursor deletetest = mydatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if(deletetest == null)
                return true;
            else
                return false;

        } finally {
            cursor.close();
        }
    }

}


