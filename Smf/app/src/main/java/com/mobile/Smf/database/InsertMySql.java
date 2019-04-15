package com.mobile.Smf.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;

public class InsertMySql implements Callable<Boolean> {

    private String DB_URL, user, pass, instruction;

    public InsertMySql(String DB_URL, String user, String pass, String sqlInstruction) {

        this.DB_URL = DB_URL;
        this.user = user;
        this.pass = pass;
        this.instruction = sqlInstruction;
    }

    public Boolean call() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL, user, pass);
            System.out.println("Database connection success InsertMySql");

            Statement st = con.createStatement();

            int eval = st.executeUpdate(instruction);

            if(eval < 1)
                return false;

        } catch (ClassNotFoundException e) { e.printStackTrace(); return false;}
        catch (SQLException ex) { ex.printStackTrace(); return false; }
        return true;
    }
}