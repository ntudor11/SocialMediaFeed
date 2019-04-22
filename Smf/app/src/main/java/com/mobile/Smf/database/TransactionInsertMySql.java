package com.mobile.Smf.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class TransactionInsertMySql implements Callable<Boolean> {

    private String DB_URL, user, pass;
    private Connection con;
    private String[] instructions;
    private String[] input;
    private boolean isSuccess;

    public TransactionInsertMySql(String ... params) {
        input = params;
        this.DB_URL = params[0];
        this.user = params[1];
        this.pass = params[2];
        instructions = new String[params.length-3];
        for(int i = 3; i < params.length-3; i++ ) {
            instructions[i-3] = params[i];
        }
    }

    public Boolean call() {

        PreparedStatement[] statments = new PreparedStatement[instructions.length];
        for(int i = 0; i < statments.length; i++) {
            statments[i] = null;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, user, pass);
            con.setAutoCommit(false);

            Savepoint save1 = con.setSavepoint();

            for(int i = 0; i < statments.length;i++) {
                statments[i] = con.prepareStatement(instructions[i]);
                statments[i].executeUpdate();
            }

            con.commit();

        }catch (ClassNotFoundException e) { e.printStackTrace();  isSuccess = false;}
        catch (SQLException ex) {
            ex.printStackTrace();
            try {
                con.rollback();
            } catch(SQLException e){
                isSuccess = false;
                throw new RuntimeException("Failed to rollback changes " +
                    "in TransactionInsertMySql with arguments:"+ Arrays.toString(input)+ "\n");
                    }
        }
        finally {
                try {
                    for (int i = 0; i < statments.length; i++) {
                        if (statments[i] != null)
                            statments[i].close();
                    }
                    if(con != null)
                        con.close();

                    return isSuccess;

                } catch (SQLException e) {e.printStackTrace(); return isSuccess;}
        }
    }
}
